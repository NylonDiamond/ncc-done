package com.statefarm.aitp.model;

public class GoalID {

	private int goalID;

	public int getGoalID() {
		return goalID;
	}

	public void setGoalID(int goalID) {
		this.goalID = goalID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + goalID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GoalID other = (GoalID) obj;
		if (goalID != other.goalID)
			return false;
		return true;
	}

}
