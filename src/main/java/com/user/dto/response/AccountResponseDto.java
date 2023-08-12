package com.user.dto.response;

import com.user.dto.secure.AccountSecureDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
public class AccountResponseDto {

	@Nullable
	private AccountSecureDto accountSecureDto;

	private Boolean isExist;
}
