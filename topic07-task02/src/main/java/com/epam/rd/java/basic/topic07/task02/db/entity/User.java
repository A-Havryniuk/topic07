package com.epam.rd.java.basic.topic07.task02.db.entity;

import java.util.Objects;

public class User {

	private int id;

	private String login;

	public User(int i, String login) {
		this.id = 0;
		this.login = login;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getLogin() {
		return this.login;
	}
	public String toString() {
		return this.login;
	}
	public boolean equals(Object obj) {
		if(obj instanceof User)
			return Objects.equals(((User) obj).getLogin(), this.login);
		return false;
	}
	public static User createUser(String login) {
		return new User(0, login);
	}
}