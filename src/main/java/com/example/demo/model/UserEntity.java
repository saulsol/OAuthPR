package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class UserEntity {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	private String email;

	@Column(nullable = false)
	private String username;

	private String password;

	private String role;

	private String authProvider;

	@Builder
	public UserEntity(String username, String password, String role, String email, String authProvider) {
		this.authProvider = authProvider;
		this.email = email;
		this.username = username;
		this.password = password;
		this.role = role;
	}
}
