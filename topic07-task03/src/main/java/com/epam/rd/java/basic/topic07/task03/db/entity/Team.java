package com.epam.rd.java.basic.topic07.task03.db.entity;

import java.util.Objects;

public class Team {

	private int id;
		
	private String name;
	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public Team(String name)
	{
		this.name = name;
	}

	public Team(int id, String name) {
		this.id = id;
		this.name = name;
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