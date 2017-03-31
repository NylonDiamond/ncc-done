package com.statefarm.aitp.model;

public class Task {
	
	private TaskID taskID;
	
	private String taskName;
	
	private TaskType taskType;
	
	private CountType countType;

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public TaskType getTaskType() {
		return taskType;
	}

	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}

	public CountType getCountType() {
		return countType;
	}

	public void setCountType(CountType countType) {
		this.countType = countType;
	}

	public TaskID getTaskID() {
		return taskID;
	}

	public void setTaskID(TaskID taskID) {
		this.taskID = taskID;
	}

	

}
