package com.epam.rd.java.basic.topic07.task02;

import com.epam.rd.java.basic.topic07.task02.db.*;
import com.epam.rd.java.basic.topic07.task02.db.entity.*;

public class Demo {

	public static void main(String[] args) throws DBException {
		// users  ==> [ivanov petrov obama]
		// teams  ==> [teamA teamB teamC ]
		
		DBManager dbManager = DBManager.getInstance();

		User userPetrov = dbManager.getUser("petrov");
		User userIvanov = dbManager.getUser("ivanov");
		User userObama = dbManager.getUser("obama");

		Team teamA = dbManager.getTeam("teamA");
		Team teamB = dbManager.getTeam("teamB");
		Team teamC = dbManager.getTeam("teamC");

		// method setTeamsForUser must implement transaction!
		dbManager.setTeamsForUser(userIvanov, teamA);
		dbManager.setTeamsForUser(userPetrov, teamA, teamB);
		dbManager.setTeamsForUser(userObama, teamA, teamB, teamC);

		for (User user : dbManager.findAllUsers()) {
			System.out.println((dbManager.getUserTeams(user)));
		}
		// teamA
		// teamA teamB
		// teamA teamB teamC
	}

}
