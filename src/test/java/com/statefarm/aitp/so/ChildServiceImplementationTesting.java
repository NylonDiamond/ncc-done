package com.statefarm.aitp.so;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.statefarm.aitp.model.Child;
import com.statefarm.aitp.model.Goal;
import com.statefarm.aitp.model.GoalID;
import com.statefarm.aitp.persistence.Database;
import com.statefarm.aitp.persistence.XMLFileLookup;
import com.statefarm.aitp.service.ChildService;
import com.statefarm.aitp.solution.ChildServiceImpl;

public class ChildServiceImplementationTesting {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Before
	public void setUp() throws Exception {
		FileUtils.copyDirectory(FileUtils.toFile(ChildServiceImplementationTesting.class.getResource("/db")), folder.getRoot());
	}

	/**
	 * public abstract int updateGoalProgress(GoalID goalID, int current);
	 * Updates the progress of the goal associated with the GoalID provided.
	 * 
	 * @param goal
	 * @return the percentage complete of the goal as an int (.667 = 66)
	 */
	@Test
	public void testUpdateGoalProgressReturnValue() throws Exception {
		ChildService childService = new ChildServiceImpl(folder.getRoot().getAbsolutePath());

		GoalID goalID = new GoalID();
		goalID.setGoalID(1);
		// Minimal Progress Achieved
		int returnValue = childService.updateGoalProgress(goalID, 3);
		assertEquals(returnValue, 30);

		// Minimal Progress Achieved
		returnValue = childService.updateGoalProgress(goalID, 0);
		assertEquals(returnValue, 0);

		// Minimal Progress Achieved
		returnValue = childService.updateGoalProgress(goalID, 10);
		assertEquals(returnValue, 100);

		// Minimal Progress Achieved
		returnValue = childService.updateGoalProgress(goalID, 15);
		assertEquals(returnValue, 150);
	}

	@Test
	public void testUpdateGoalProgressDatabaseUpdate() throws Exception {
		File file = new File(folder.getRoot().getAbsolutePath() + XMLFileLookup.findXMLFile(Goal.class));
		ChildService childService = new ChildServiceImpl(folder.getRoot().getAbsolutePath());

		GoalID goalID = new GoalID();
		goalID.setGoalID(1);
		// Minimal Progress Achieved
		childService.updateGoalProgress(goalID, 5);
		List<Goal> goals = Database.retrieve(file);
		assertEquals(goals.get(0).getCurrent(), 5);

		// Minimal Progress Achieved
		goalID.setGoalID(2);
		childService.updateGoalProgress(goalID, 1000);
		goals = Database.retrieve(file);
		assertEquals(goals.get(1).getCurrent(), 1000);
	}

	/**
	 * public abstract List<Child> findChildrenBySameTasks(Child child); Finds
	 * the other children attempting to complete the same tasks as the provided
	 * child. To be included, a child must share at least 1 task with the
	 * provided child.
	 * 
	 * @param child
	 * @return list of children
	 */
	@Test
	public void testFindChildrenCompletingSimilarTasks() throws Exception {
		File childFile = new File(folder.getRoot().getAbsolutePath() + XMLFileLookup.findXMLFile(Child.class));
		List<Child> listChildren = Database.retrieve(childFile);
		Child child = listChildren.get(0);
		ChildService childService = new ChildServiceImpl(folder.getRoot().getAbsolutePath());
		List<Child> childrenOfSimilarTasks = childService.findChildrenBySameTasks(child);
		assertEquals(childrenOfSimilarTasks.size(), 1);
	}

	/**
	 * Finds the percentage completion for all goals, weighted equally.
	 * 
	 * @param child
	 * @return percentage complete
	 */

	@Test
	public void testFindAllPercentageCompleteOfOneGoal() throws Exception {
		File childFile = new File(folder.getRoot().getAbsolutePath() + XMLFileLookup.findXMLFile(Child.class));
		List<Child> listChildren = Database.retrieve(childFile);
		Child child = listChildren.get(1);
		ChildService childService = new ChildServiceImpl(folder.getRoot().getAbsolutePath());
		int totalPercentageComplete = childService.findAllGoalsCompletion(child);
		assertEquals(totalPercentageComplete, 20);
	}

	@Test
	public void testFindAllPercentageCompleteOfAllGoals() throws Exception {
		File childFile = new File(folder.getRoot().getAbsolutePath() + XMLFileLookup.findXMLFile(Child.class));
		List<Child> listChildren = Database.retrieve(childFile);
		Child child = listChildren.get(0);
		ChildService childService = new ChildServiceImpl(folder.getRoot().getAbsolutePath());
		int totalPercentageComplete = childService.findAllGoalsCompletion(child);
		assertEquals(totalPercentageComplete, 35);
	}

	/**
	 * public abstract List<Goal> findAllGoals(Child child); Finds all goals
	 * sorted by percentage complete in descending order.
	 * 
	 * @param child
	 * @return list of goals
	 */
	@Test
	public void testFindAllGoalsAssociatedToChildSortedByPercentageComplete() throws Exception {

		File childFile = new File(folder.getRoot().getAbsolutePath() + XMLFileLookup.findXMLFile(Child.class));
		List<Child> listChildren = Database.retrieve(childFile);
		Child child = listChildren.get(0);

		ChildService childService = new ChildServiceImpl(folder.getRoot().getAbsolutePath());
		List<Goal> listGoals = childService.findAllGoals(child);
		Goal childID_100_First_Goal = listGoals.get(0);
		// assertEquals(childID_101_First_Goal.getTaskID().toString(), 1);
		assertEquals(childID_100_First_Goal.getTarget(), 10);
		assertEquals(childID_100_First_Goal.getCurrent(), 1);

		Goal childID_101_Second_Goal = listGoals.get(1);
		// assertEquals(childID_101_Second_Goal.getTaskID().toString(), 3);
		assertEquals(childID_101_Second_Goal.getTarget(), 10);
		assertEquals(childID_101_Second_Goal.getCurrent(), 6);

	}

}
