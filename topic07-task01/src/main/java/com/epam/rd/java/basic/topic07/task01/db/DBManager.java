package com.epam.rd.java.basic.topic07.task01.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Driver;
import java.util.Properties;

import com.epam.rd.java.basic.topic07.task01.Constants;
import com.epam.rd.java.basic.topic07.task01.db.entity.*;

import javax.swing.plaf.nimbus.State;

public class DBManager {

	private static DBManager instance;

	public DBManager() {

	}

	public static synchronized DBManager getInstance() {
		if(instance == null)
			instance =  new DBManager();
		return instance;
	}

	public List<User> findAllUsers() throws DBException{
		List<User> users = new ArrayList<>();
		String url = getFullURL();
		try {
			Connection connection = DriverManager.getConnection(url);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(Constants.FIND_ALL_USERS);
			while(rs.next()) {
				users.add(MapUser(rs));
			}
			connection.close();
			statement.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	public boolean insertUser(User user) throws DBException {
		String url = getFullURL();
		try {
			Connection connection = DriverManager.getConnection(url);
			Statement statement = connection.createStatement();
			statement.executeUpdate(Constants.INSERT_USER.replaceAll("[?]", "'"+user.getLogin()+"'"));
			connection.close();
			statement.close();
			return true;
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public User getUser(String login) throws DBException {
		User user = User.createUser(login);
		try(Connection connection = DriverManager.getConnection(getFullURL());
			Statement statement = connection.createStatement()) {
			String strSelect = Constants.SELECT_USER.replaceAll("[?]", "'"+login+"'");
			ResultSet rs = statement.executeQuery(strSelect);
			user.setId(rs.getInt(Fields.USER_ID));
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public Team getTeam(String name) throws DBException {
		Team team = Team.createTeam(name);
		try(Connection connection = DriverManager.getConnection(getFullURL());
		Statement statement = connection.createStatement()) {
			String strSelect = Constants.SELECT_TEAM.replaceAll("[?]", "'"+name+"'");
			ResultSet rs = statement.executeQuery(strSelect);
			team.setId(rs.getInt(Fields.TEAM_ID));
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return team;
	}

	public List<Team> findAllTeams() throws DBException {
		List<Team> teams = new ArrayList<>();
		try(Connection connection = DriverManager.getConnection(getFullURL());
		Statement statement = connection.createStatement()) {
		ResultSet rs = statement.executeQuery(Constants.FIND_ALL_TEAMS);
		while (rs.next())
		{
			teams.add(mapTeam(rs));
		}
		rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return teams;
	}

	public boolean insertTeam(Team team) throws DBException {
		try(Connection connection = DriverManager.getConnection(getFullURL());
			Statement statement = connection.createStatement()) {
			statement.executeUpdate(Constants.INSERT_TEAM.replaceAll("[?]", "'"+team.getName()+"'"));
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	private String getFullURL() {
		String url = null;
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(Constants.SETTINGS_FILE));
			url = properties.getProperty(Constants.CONNECTION_URL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return url;
	}
	private User MapUser(ResultSet rs) throws SQLException {
		User user = User.createUser(rs.getString(Fields.USER_LOGIN));
		user.setId(rs.getInt(Fields.USER_ID));
		return user;

	}
	private Team mapTeam (ResultSet rs) throws SQLException {
		Team team = Team.createTeam(rs.getString(Fields.TEAM_NAME));
		team.setId(rs.getInt(Fields.TEAM_ID));
		return team;
	}
}
