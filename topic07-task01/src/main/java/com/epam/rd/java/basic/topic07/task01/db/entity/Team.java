package com.epam.rd.java.basic.topic07.task01.db.entity;

import java.util.Objects;

public class Team {

	private int id;
		
	private String name;

	public Team(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName(){
		return this.name;
	}
	public String toString() {
		return this.name;
	}
	public boolean equals(Object obj) {
		if(obj instanceof Team)
		 	return  Objects.equals(((Team) obj).getName(), this.name);
		return false;
	}
	public static Team createTeam(String name) {
		return new Team(0, name);
	}
}