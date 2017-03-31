package com.statefarm.aitp.model;

import java.util.List;

public class Supervisor extends Client {
	
	private int supervisorID;

	private List<ChildID> childList;
	
	private SupervisorType supervisorType;

	public int getSupervisorID() {
		return supervisorID;
	}

	public void setSupervisorID(int supervisorID) {
		this.supervisorID = supervisorID;
	}

		public List<ChildID> getChildList() {
		return childList;
	}

	public void setChildList(List<ChildID> childList) {
		this.childList = childList;
	}

	public SupervisorType getSupervisorType() {
		return supervisorType;
	}

	public void setSupervisorType(SupervisorType supervisorType) {
		this.supervisorType = supervisorType;
	}

}
