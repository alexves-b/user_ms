package com.user.service.impl;

import com.demo.storage.notifications.ENotificationType;
import com.demo.storage.notifications.EServiceName;
import com.demo.storage.notifications.NotificationCommonDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.client.AdminClient;
import com.user.dto.ConfirmationCode;
import com.user.dto.account.AccountDto;
import com.user.dto.account.AccountForFriends;
import com.user.dto.account.AccountStatisticRequestDto;
import com.user.dto.secure.AccountSecureDto;
import com.user.exception.EmailIsBlank;
import com.user.exception.EmailNotUnique;
import com.user.exception.UserRecoveryAnswerNotFound;
import com.user.jwt_token.JwtTokenUtils;
import com.user.kafka.KafkaProducer;
import com.user.kafka.KafkaProducerForJson;
import com.user.model.RecoveryAnswer;
import com.user.model.User;
import com.user.repository.AnwserRepository;
import com.user.repository.UserRepository;
import com.user.service.UserService;
import com.user.service.UserSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.RandomStringGenerator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final JwtTokenUtils jwtTokenUtils;
	private final KafkaProducer kafkaProducer;
	private final AdminClient adminClient;
	private final BCryptPasswordEncoder passwordEncoder;
	private final KafkaProducerForJson kafkaProducerForJson;
	private final ObjectMapper objectMapper;
	private final EmailServiceImpl emailService;
	private final AnwserRepository anwserRepository;
	private final ConfirmationCode codeString;
	
	@Override
	public AccountDto getUserByEmail(String email) {
		User user = findUserByEmail(email);
		log.info("user from repository: \n" + user);
		return new AccountDto(user);
	}

	@Override
	public AccountSecureDto createUser(AccountSecureDto accountSecureDto) {

		if (accountSecureDto.getEmail().isEmpty() || accountSecureDto.getEmail().isBlank()) {
			throw new EmailIsBlank("email is blank");
		}
		if (userRepository.existsByEmail(accountSecureDto.getEmail())) {
			String emailNotUnique = "email: " + accountSecureDto.getEmail() + " not unique!";
			log.warn(emailNotUnique);
			throw new EmailNotUnique(emailNotUnique);
		}
		User user = User.builder().email(accountSecureDto.getEmail())
				.firstName(accountSecureDto.getFirstName())
				.lastName(accountSecureDto.getLastName())
				.password(accountSecureDto.getPassword())
				.regDate(LocalDateTime.now())
				.uuidConfirmationEmail(UUID.randomUUID())
				.isConfirmed(false)
				.dateToConfirmation(LocalDateTime.now().plusDays(3))
				.roles("ROLE_USER").build();
		userRepository.save(user);
		log.info("User was created:  " + user);
		emailService.emailConfirmmationWhehRegistered(user.getEmail(), String.valueOf(user.getUuidConfirmationEmail()));
		if (userRepository.findById(user.getId()).isPresent()){
			kafkaProducerForJson.sendMessageForFriends(objectMapper.convertValue(user, AccountForFriends.class));
		}
		return new AccountSecureDto(user.getId(), user.getFirstName(),
				user.getLastName(), user.getEmail(), user.getPassword(), user.getRoles());
	}

	@Override
	@Transactional
	public User editUser(AccountDto accountDto) {
		User oldUser = userRepository.findUserByEmail(accountDto.getEmail())
				.orElseThrow(() -> new UsernameNotFoundException
						("user with email: " + accountDto.getEmail() + " not found"));
		changeUserDetails(oldUser, accountDto);
		return oldUser;
	}
	@Override
	public User editUser(AccountDto accountDto, String email) {
		    accountDto.setEmail(email);
			return editUser(accountDto);
	}
	@Override
	public List<AccountDto> searchUser(String userFullName, String offset, String limit) {
		if (userFullName.isBlank()) {
			throw new UsernameNotFoundException("пусто");
		}
		log.info("Fullname user: - " + userFullName);
		String[] fullName = userFullName.split(" ");
		String firstName;
		String lastName;
		if (fullName.length < 2) {
			firstName = fullName[0];
			lastName = fullName[0];
		} else {
			firstName = fullName[0];
			lastName = fullName[1];
		}
		Specification<User> specification = Specification
				.where(UserSpecification.findByFirstName(firstName))
				.or(UserSpecification.findByLastName(lastName));

		Page<User> userList = userRepository.findAll(specification,
				PageRequest.of(Integer.parseInt(offset), Integer.parseInt(limit)));

		ObjectMapper objectMapper = new ObjectMapper();
		return userList.stream()
				.map(user -> objectMapper.convertValue(user, AccountDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public User getUserById(Long id) {
		User user = userRepository.findById(id).orElseThrow(() ->
				new UsernameNotFoundException("user with id: " + id + " not found"));
		log.info("user with id - " + id + " was found");
		return user;
	}
	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	@Transactional
	public Long deleteUserById(Long id) {
		userRepository.findById(id).orElseThrow(() ->
				new UsernameNotFoundException("user with id: " + id + " not found"));

		String emailForDel = userRepository.findById(id).orElseThrow().getEmail();
		NotificationCommonDto delNotify = NotificationCommonDto.builder()
				.producerId(id)
				.service(EServiceName.USERS)
				.notificationType(ENotificationType.DEL_USER)
				.timestamp(new Timestamp(System.currentTimeMillis()))
				.content(String.format("Пользователь с email '%s' удален", emailForDel))
				.build();

		kafkaProducer.produceKafkaMessage("notify-topic-common", delNotify);

		log.info("user with id: " + id + " was deleted.");
		return userRepository.deleteUserById(id);
	}

	@Override
	public void markForDeleteUserAfterThirtyDaysByToken(String bearToken) {
		String email = getEmailFromBearerToken(bearToken);
		User user = findUserByEmail(email);
		user.setIsDeleted(true);
		user.setDeletionDate(LocalDateTime.now().plusDays(30));
		userRepository.save(user);

		String emailForDel = user.getEmail();

		NotificationCommonDto delNotify = NotificationCommonDto.builder()
				.producerId(user.getId())
				.service(EServiceName.USERS)
				.notificationType(ENotificationType.DEL_USER)
				.timestamp(new Timestamp(System.currentTimeMillis()))
				.content(String.format("Пользователь с email '%s' удален", emailForDel))
				.build();

		kafkaProducer.produceKafkaMessage("notify-topic-common", delNotify);
	}

	@Scheduled(cron = "0 0 0 * * ?")
	public void deleteAccountMarkedDeleteAndDelDateToday() {
		try {
			ArrayList <User> listForDeletion = userRepository.findUserByIsDeletedAndDeletionDateBeforeNow()
					.orElseThrow(() -> new RuntimeException("No user for deletion"));
			log.info("time when was deleted: - " + LocalDateTime.now());
			log.info(listForDeletion.toString());
			userRepository.deleteAll(listForDeletion);
			log.info("users was deleted!");
		} catch (Exception ex) {
			ex.printStackTrace();
			log.warn(ex.getMessage());
		}
	}

	@Override
	public String uploadAvatarToServer(String bearerToken, MultipartFile file) {
		return adminClient.getLinkForUploadAvatar(bearerToken,file).getFileName();
	}

	@Transactional
	@Override
	public void unmarkForDeleteUserAfterThirtyDaysByToken(String bearToken) {
		String email = getEmailFromBearerToken(bearToken);
		User user = findUserByEmail(email);
		user.setIsDeleted(false);
		user.setDeletionDate(null);
	}
	@Override
	public void blockUser(Long id) {
		User user = userRepository.findById(id).orElseThrow(() ->
				new UsernameNotFoundException("user with id: " + id + " not found, can't block or unblock user."));
		user.setIsBlocked(!user.getIsBlocked());
		log.info("user: " + user.getId() + "isBlocked: " + user.getIsBlocked() );
		userRepository.save(user);
	}
	@Override
	public String getEmailFromBearerToken(String bearerToken) {
		log.info(bearerToken);
		final String[] parts = bearerToken.split("\\s");
		final String jwtToken = parts[1];
		log.info(jwtTokenUtils.decodeJWTToken(jwtToken));
		ObjectMapper mapper = new ObjectMapper();
		String email = "";
		try {
			Map<String, String> obj = mapper.readValue(jwtTokenUtils.decodeJWTToken(jwtToken), Map.class);
			email = obj.get("sub");
		} catch (Exception ex) {
			log.error("problem with parsing token" + " - " + ex.getMessage());
			throw new RuntimeException("problem with parsing token");
		}
		return email;
	}
	@Override
	public Long getUserCount() {
		return userRepository.count();
	}

	@Override
	public AccountStatisticRequestDto getStatistic(AccountStatisticRequestDto accountStatisticRequestDto) {
		return new AccountStatisticRequestDto();
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static Object objectMapper(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		return mapper.convertValue(object, User.class);
	}
	@Transactional
	public AccountDto changeEmail(String email,String bearerToken) {
		String massageError = "email: " + email + " not unique!";
		String emailFromBearerToken = getEmailFromBearerToken(bearerToken);
		User user = findUserByEmail(emailFromBearerToken);
		if (userRepository.existsByEmail(email)){
			log.warn(massageError);
			throw new EmailNotUnique(massageError);
		}
		Integer code = Integer.parseInt(codeString.toString());
		emailService.confirmationForChangeEmail(user.getEmail(),email,user.getUuidConfirmationEmail(),code);
		log.info(email);
		return new AccountDto(user);
	}

	public User findUserByEmail(String email) {
		return userRepository.findUserByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException
						("user with email: " + email + " not found"));
	}

	@Transactional
	public AccountDto changePassword(String password,String bearerToken) {
		String passwordBCrypt = passwordEncoder.encode(password);
		String email = getEmailFromBearerToken(bearerToken);
		User user = findUserByEmail(email);
		user.setPassword(passwordBCrypt);
		log.info("Password for user " +user.getEmail() + " was changed");
		try {
			emailService.notifyAboutChangePassword(email);
		}catch (Exception ex) {
			log.error(ex.getMessage());
		}
		return new AccountDto(user);
	}
	public void changeUserDetails(User user, AccountDto accountDto){
           //Проверить наличие всех полей
		if (user.getEmail().equals(accountDto.getEmail())) {
			user.setEmojiStatus(accountDto.getEmojiStatus());
			user.setPhoto(accountDto.getPhoto());
			user.setAbout(accountDto.getAbout());
			user.setCity(accountDto.getCity());
			user.setCountry(accountDto.getCountry());
			user.setFirstName(accountDto.getFirstName());
			user.setLastName(accountDto.getLastName());
			user.setPhone(accountDto.getPhone());
			user.setBirthDate(accountDto.getBirthDate());
			user.setGender(accountDto.getGender());
			user.setProfileCover(accountDto.getProfileCover());
			log.info("user was edited: " + user);
		}
	}

	public Map<String, Object> getCountryList(){
		JSONObject jsonObject = new JSONObject();

		JSONArray russia = new JSONArray();
		russia.put("Москва");
		russia.put("Санкт-Петербург");
		russia.put("Воронеж");

		JSONArray belarussia = new JSONArray();
		belarussia.put("Минск");
		belarussia.put("Витебск");
		belarussia.put("Брест");

		JSONArray georgia = new JSONArray();
		georgia.put("Тбилиси");
		georgia.put("Батуми");
		georgia.put("Кутаиси");

		jsonObject.put("Россия",russia.toList());
		jsonObject.put("Беларусь",belarussia.toList());
		jsonObject.put("Грузия",georgia.toList());
		System.out.println(jsonObject.toMap());
		return jsonObject.toMap();
	}
	@Transactional
	public void addRecoveryQuestionAndConfirmEmail(String email,int numberOfQuestion, String answer) {
		User user = findUserByEmail(email);
		user.setIsConfirmed(true);
		user.setDateToConfirmation(null);
		String answerEncrypted = passwordEncoder.encode(answer);
		RecoveryAnswer recoveryAnswer = new RecoveryAnswer(numberOfQuestion,answerEncrypted,user);
		anwserRepository.save(recoveryAnswer);
		log.warn("user with email: " + user.getEmail() + " was confirmed");
	}
	public User getUserByUUid(UUID uuid) {
		User user = (userRepository.findByUuidConfirmationEmail(uuid)
				.orElseThrow(() -> new NotFoundException("user with uuid: " + uuid + " not found")));
		return user;
	}

	@Scheduled(cron = "0 0 0 * * ?")
	public void deleteNotConfirmedAccount() {
		try {
			ArrayList <User> listForDeletion = userRepository.findUserByNotConfirmedAndConfirmationDateBeforeNow()
					.orElseThrow(() -> new RuntimeException("No user for deletion without not confirmed email"));
			log.info("time when was deleted not confirmed users: - " + LocalDateTime.now());
			log.info(listForDeletion.toString());
			userRepository.deleteAll(listForDeletion);
			log.info("users was deleted!");
		} catch (Exception ex) {
			ex.printStackTrace();
			log.warn(ex.getMessage());
		}
	}

	public boolean checkConfirmationCode(Integer code) {
		int correctCode = Integer.parseInt(String.valueOf(codeString));
        return correctCode == code;
	}

	@Transactional
	public boolean checkRecoveryQuestionAndAnswer(String email, String answer, Integer numberOfQuestion) {
		User user = findUserByEmail(email);
		Long userId = user.getId();
		RecoveryAnswer recoveryAnswer = anwserRepository.findAnswerByUserId(userId).orElseThrow(() ->
				new UserRecoveryAnswerNotFound("answer with user_id: " + userId + " not found"));

		boolean compareAnswer = passwordEncoder.matches(answer,recoveryAnswer.getAnswerEncrypted());
		boolean compareQuestions = recoveryAnswer.getNumberQuestion() == numberOfQuestion;

        return compareAnswer & compareQuestions;
    }

	@Transactional
	public void setEmail(String oldEmail,String changedEmail) {
		User user = findUserByEmail(oldEmail);
		user.setEmail(changedEmail);
	}

	@Transactional
	public void sendNewPasswordForUserEmail(String email) {
		User user = findUserByEmail(email);
		String password = generateRandomSpecialCharacters(8);
		String passwordBCrypt = passwordEncoder.encode(password);

		try {
			emailService.sendMessageAboutNewPasswordByEmail(email,password);
			user.setPassword(passwordBCrypt);
			log.info("Password for user " +user.getEmail() + " was changed");
		}catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	@Transactional
	public void sendNewPasswordForNewUserEmail(String oldEmail,String emailToSend) {
		User user = findUserByEmail(oldEmail);
		String password = generateRandomSpecialCharacters(8);
		String passwordBCrypt = passwordEncoder.encode(password);

		try {
			emailService.sendMessageAboutNewPasswordByEmail(emailToSend,password);
			user.setPassword(passwordBCrypt);
			log.info("Password for user " +user.getEmail() + " was changed and send to emal: " + emailToSend);
		}catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}
	public String generateRandomSpecialCharacters(int length) {
		RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange(33, 125)
				.build();
		return pwdGenerator.generate(length);
	}
}
