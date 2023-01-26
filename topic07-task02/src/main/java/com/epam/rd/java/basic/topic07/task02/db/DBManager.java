package com.epam.rd.java.basic.topic07.task02.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.epam.rd.java.basic.topic07.task02.Constants;
import com.epam.rd.java.basic.topic07.task02.db.entity.*;

import javax.swing.plaf.nimbus.State;

public class DBManager {
	private static DBManager instance;
	List<User> users = new ArrayList<>();
	List<Team> teams = new ArrayList<>();

	public static synchronized DBManager getInstance() {
		if(instance == null)
			instance =  new DBManager();
		return instance;
	}

	public List<User> findAllUsers() throws DBException {
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
		try (Connection connection = DriverManager.getConnection(getFullURL());
			 Statement statement = connection.createStatement()) {
			String sql = (Constants.SELECT_USER.replaceAll("[?]", "'"+user.getLogin()+"'"));
			ResultSet rs = statement.executeQuery(sql);
			if(!rs.next())
			{
				String putUser = Constants.INSERT_USER.replaceAll("[?]", user.getLogin()).replaceAll("[!]", ""+user.getId());
				statement.executeUpdate(putUser);
			}
			else {
				String updateUser = Constants.UPDATE_USER.replaceAll("[?]", ""+user.getId()).replaceAll("[!]", user.getLogin());
				statement.executeUpdate(updateUser);
			}
			return true;
		} catch (SQLException ex)
		{
			ex.printStackTrace();
		}
		return false;
	}

	public boolean deleteUsers(User... users) throws DBException {
		for (User user : users) {
			if (user == null) return false;
			try (Connection con = DriverManager.getConnection(getFullURL());
				 PreparedStatement stmt = con.prepareStatement(Constants.DELETE_USER);
			) {
				stmt.setInt(1, user.getId());
				stmt.executeUpdate();
			} catch (SQLException throwables) {
				throwables.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public User getUser(String login) throws DBException {
		User user = User.createUser(login);
		try (Connection con = DriverManager.getConnection(getFullURL());
			 PreparedStatement stmt = con.prepareStatement(Constants.GET_USER);
		) {
			stmt.setString(1, login);
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				user.setId(resultSet.getInt("id"));
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		return user;
	}

	public Team getTeam(String name) throws DBException {
		Team team = Team.createTeam(name);
		try (Connection con = DriverManager.getConnection(getFullURL());
			 PreparedStatement stmt = con.prepareStatement(Constants.GET_TEAM);
		) {
			stmt.setString(1, name);
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				team.setId(resultSet.getInt("id"));
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		return team;
	}

	public List<Team> findAllTeams() throws DBException {
		String url = getFullURL();
		try {
			Connection connection = DriverManager.getConnection(url);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(Constants.FIND_ALL_TEAMS);
			while(rs.next()) {
				teams.add(mapTeam(rs));
			}
			connection.close();
			statement.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return teams;
	}

	public boolean insertTeam(Team team) throws DBException {
		String url = getFullURL();
		try (Connection connection = DriverManager.getConnection(url);
			 PreparedStatement statement = connection.prepareStatement(Constants.INSERT_TEAM, Statement.RETURN_GENERATED_KEYS);
		) {
			statement.setString(1, team.getName());
			int affectedRows = statement.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("No rows affected");
			}
			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					team.setId(generatedKeys.getInt(1));
				} else {
					throw new SQLException("Creating user failed, no ID obtained.");
				}
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return true;
	}

	public boolean setTeamsForUser(User user, Team... teams) throws DBException {
		return false;
	}

	public List<Team> getUserTeams(User user) throws DBException {
		List<Team> teamList = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(getFullURL());
			 PreparedStatement statement = connection.prepareStatement(Constants.FIND_USER_TEAMS);
		) {
			statement.setInt(1, user.getId());
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				teamList.add(mapTeamByTeamId(rs));
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return teamList;
	}

	public boolean deleteTeam(Team team) throws DBException {
		if (team == null) return false;
		try (Connection con = DriverManager.getConnection(getFullURL());
			 PreparedStatement stmt = con.prepareStatement(Constants.DELETE_TEAM);
		) {
			stmt.setInt(1, team.getId());
			stmt.executeUpdate();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean updateTeam(Team team) throws DBException {
		try (Connection con = DriverManager.getConnection(getFullURL());
			 PreparedStatement statement = con.prepareStatement(Constants.UPDATE_TEAM);
		) {
			statement.setString(1, team.getName());
			statement.setInt(2, team.getId());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	private String getFullURL() {
		String url = null;
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("app.properties"));
			url = properties.getProperty("connection.url");
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
	private Team mapTeamByTeamId(ResultSet rs) throws SQLException {
		try (Connection connection = DriverManager.getConnection(getFullURL());
			 PreparedStatement statement = connection.prepareStatement(Constants.FIND_TEAM_BY_ID);
		) {
			statement.setInt(1, rs.getInt("team_id"));
			ResultSet resultSet = statement.executeQuery();
			resultSet.next();
			return mapTeam(resultSet);
		}
	}
}
