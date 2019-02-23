package com.booking.model;

import javax.annotation.Generated;
import javax.persistence.*;

@Entity
@Table(name = "USER_DETAILS")
public class User {

	@Id
	@Column(unique = true)
	private String username;
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUserName(String username) {
		this.username = username;
	}

	@Override
	public boolean equals(Object other) {
		if ((other == null) || (getClass() != other.getClass())) {
			return false;
		} else {
			return username.equals(((User) other).getUsername()) && password.equals(((User) other).getPassword());
		}
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
