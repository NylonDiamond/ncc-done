package com.statefarm.aitp.model;

import java.util.List;

public class Child extends Client {

	private ChildID childID;
	private List<GoalID> goalList;
	private int age;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public ChildID getChildID() {
		return childID;
	}

	public void setChildID(ChildID childID) {
		this.childID = childID;
	}

	public List<GoalID> getGoalList() {
		return goalList;
	}

	public void setGoalList(List<GoalID> goalList) {
		this.goalList = goalList;
	}

}
