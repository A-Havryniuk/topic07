package com.epam.rd.java.basic.topic07.task03;

public class Constants {
	
	public static final String SETTINGS_FILE = "app.properties";
	public static final String FIND_TEAM_BY_ID = "SELECT * FROM teams WHERE id = ?";
	public static final String UPDATE_USER = "UPDATE users SET id = ? WHERE login = '!'";
	public static final String UPDATE_TEAM = "UPDATE teams SET id = ? WHERE name = '!'";
	public static final String SELECT_USER = "SELECT * FROM users WHERE login = ?";
	public static final String SELECT_TEAM = "SELECT * FROM teams WHERE name = ?";
	public static final String INSERT_USER = "INSERT INTO users (login) VALUES (?)";
	public static final String FIND_ALL_USERS = "SELECT * from users";
	public static final String INSERT_TEAM = "INSERT INTO teams (name) VALUES (?)";
	public static final String FIND_ALL_TEAMS = "SELECT * from teams";
	public static final String GET_USERS_TEAMS = "SELECT * from users_teams";

}
