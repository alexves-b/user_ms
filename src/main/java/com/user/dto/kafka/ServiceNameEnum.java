package com.user.dto.kafka;

import lombok.Getter;

/**
 * @author Artem Lebedev | 05/09/2023 - 12:04
 */

@Getter
public enum ServiceNameEnum {
	MESSAGES(1L),
	ADMIN(2L),
	FRIENDS(3L),
	AUTH(4L),
	USERS(5L),
	NOTIFY(6L),
	POSTS(7L);

	private final Long id;

	ServiceNameEnum(Long id){
		this.id = id;
	}
}
