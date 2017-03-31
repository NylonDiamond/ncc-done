package com.statefarm.aitp.service;

import java.util.List;

import com.statefarm.aitp.model.Child;
import com.statefarm.aitp.model.Goal;
import com.statefarm.aitp.model.Supervisor;
import com.statefarm.aitp.model.Task;

public abstract class SupervisorService extends BaseService {

	public SupervisorService(String fileRoot) {
		super(fileRoot);
	}

	/**
	 * Adds the provided Task to the DB.
	 * 
	 * @param task
	 */
	public abstract void addTask(Task task);

	/**
	 * Updates the provided Task in the DB.
	 * 
	 * @param task
	 */
	public abstract void updateTask(Task task);

	/**
	 * Deletes the provided Task in the DB as well as cascades the delete to all
	 * affected goals and removes affected goals from the child whom they are associated.
	 * 
	 * @param task
	 */
	public abstract void deleteTask(Task task);

	/**
	 * Deletes the provided Goal in the DB as well as cascades the delete to the
	 * affected child.
	 * 
	 * @param goal
	 */
	public abstract void deleteGoal(Goal goal);

	/**
	 * Adds the provided goal to the child defined on the goal.
	 * If provided goal does not exist in DB, an entry will need to be created.
	 * 
	 * @param goal
	 */
	public abstract void addGoal(Goal goal);
	
	/**
	 * Update the provided goal.
	 * 
	 * @param goal
	 */
	public abstract void updateGoal(Goal goal);

	/**
	 * Returns all children associated with the provided supervisor. When
	 * returned, the children will be sorted in descending order by age.
	 * 
	 * @param supervisor
	 * @return
	 */
	public abstract List<Child> findAllChildren(Supervisor supervisor);
}
