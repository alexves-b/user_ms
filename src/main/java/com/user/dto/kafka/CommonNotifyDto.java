package com.user.dto.kafka;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author Artem Lebedev | 05/09/2023 - 11:58 <p> <p>
 * @Description <p>
 * <b>Long\UUID initiatorId</b> : Инициатор события <p>
 * <b>String text</b>: Описание события <p>
 * <b>String objId</b>: Id сущности уведомления в DB <p>
 * <b>Enum service</b>: Сервис инициатор <p>
 * <b>Timestamp timestamp</b>: Дата\Время <p>
 * <b>String type</b>: Тип события (пост, коммент, доб. в друзья, ...) <p>
 * <b>Long\UUID to</b>: Получатель <p>
 */

@Data
public class CommonNotifyDto {
	private Long initiatorId;
	private String text;
	private String objId;
	private ServiceNameEnum service;
	private Timestamp timestamp;
	private CommonNotifyTypeEnum type;
	private Long to;

	public CommonNotifyDto(String text, CommonNotifyTypeEnum type) {
		this.initiatorId = ServiceNameEnum.USERS.getId();
		this.text = text;
		this.service = ServiceNameEnum.USERS;
		this.timestamp = new Timestamp(System.currentTimeMillis());
		this.type = type;
	}

	@Override
	public String toString() {
		return "\nCommonNotifyDto" +
				"\n    initiatorId: '" + initiatorId + '\'' +
				"\n    text: '" + text + '\'' +
				"\n    objId: '" + objId + '\'' +
				"\n    service: '" + service + '\'' +
				"\n    timestamp: '" + timestamp + '\'' +
				"\n    type: '" + type + '\'' +
				"\n    to: " + to;
	}
}


