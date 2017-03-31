package com.statefarm.aitp.service;

import java.util.List;

import com.statefarm.aitp.model.Child;
import com.statefarm.aitp.model.Goal;
import com.statefarm.aitp.model.GoalID;

public abstract class ChildService extends BaseService {

	public ChildService(String fileRoot) {
		super(fileRoot);
	}

	/**
	 * Updates the progress of the goal associated with the GoalID provided.
	 * 
	 * @param goal
	 * @return the percentage complete of the goal as an int (.667 = 66)
	 */
	public abstract int updateGoalProgress(GoalID goalID, int current);
	
	/**
	 * Finds all goals sorted by percentage complete in descending order.
	 * 
	 * @param child
	 * @return list of goals
	 */
	public abstract List<Goal> findAllGoals(Child child);
	
	/**
	 * Finds the percentage completion for all goals, weighted equally.
	 * 
	 * @param child
	 * @return percentage complete
	 */
	public abstract int findAllGoalsCompletion(Child child);
	
	/**
	 * Finds the other children attempting to complete the same tasks as the provided child.
	 * To be included, a child must share at least 1 task with the provided child.
	 * 
	 * @param child
	 * @return list of children
	 */
	public abstract List<Child> findChildrenBySameTasks(Child child);

}
