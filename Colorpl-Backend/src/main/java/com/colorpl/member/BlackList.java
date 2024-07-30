package com.colorpl.member;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Getter
@Entity
public class BlackList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "invalid_refresh_token")
	private String invalidRefreshToken;

	public BlackList(String invalidRefreshToken) {
		this.invalidRefreshToken = invalidRefreshToken;
	}
}