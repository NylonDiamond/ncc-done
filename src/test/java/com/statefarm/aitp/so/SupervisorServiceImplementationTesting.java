package com.statefarm.aitp.so;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.statefarm.aitp.model.Child;
import com.statefarm.aitp.model.ChildID;
import com.statefarm.aitp.model.CountType;
import com.statefarm.aitp.model.Goal;
import com.statefarm.aitp.model.GoalID;
import com.statefarm.aitp.model.Supervisor;
import com.statefarm.aitp.model.Task;
import com.statefarm.aitp.model.TaskID;
import com.statefarm.aitp.model.TaskType;
import com.statefarm.aitp.persistence.Database;
import com.statefarm.aitp.persistence.XMLFileLookup;
import com.statefarm.aitp.service.SupervisorService;
import com.statefarm.aitp.solution.SupervisorServiceImpl;

public class SupervisorServiceImplementationTesting {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Before
	public void setUp() throws Exception {
		FileUtils.copyDirectory(FileUtils.toFile(SupervisorServiceImplementationTesting.class.getResource("/db")), folder.getRoot());
	}

	@Test
	public void testAddTask() throws Exception {
		File file = new File(folder.getRoot().getAbsolutePath() + XMLFileLookup.findXMLFile(Task.class));
		List<Task> tasks = Database.retrieve(file);
		int tasksBeforeAdd = tasks.size();
		SupervisorService supervisorService = new SupervisorServiceImpl(folder.getRoot().getAbsolutePath());

		Task task = new Task();
		TaskID taskID = new TaskID();
		taskID.setTaskID(5);
		task.setTaskID(taskID);
		task.setTaskName("Bench Press");
		task.setTaskType(TaskType.EXERCISE);
		task.setCountType(CountType.REPITITIONS);
		supervisorService.addTask(task);
		tasks = Database.retrieve(file);
		assertEquals(tasksBeforeAdd + 1, tasks.size());

	}

	@Test
	public void testUpdateTask() throws Exception {
		File file = new File(folder.getRoot().getAbsolutePath() + XMLFileLookup.findXMLFile(Task.class));
		List<Task> tasks = Database.retrieve(file);
		Task taskToUpdate = tasks.get(3);
		taskToUpdate.setTaskName("Push-Downs");
		SupervisorService supervisorService = new SupervisorServiceImpl(folder.getRoot().getAbsolutePath());
		supervisorService.updateTask(taskToUpdate);
		String updatedTaskName = "Push-Downs";
		tasks = Database.retrieve(file);
		assertTrue((tasks.get(3).getTaskName()).equalsIgnoreCase(updatedTaskName));
	}

	/**
	 * Deletes the provided Task in the DB as well as cascades the delete to all
	 * affected goals and removes affected goals from the child whom they are
	 * associated.
	 * 
	 * @param task
	 */
	@Test
	public void testDeleteTask() throws Exception {
		File file = new File(folder.getRoot().getAbsolutePath() + XMLFileLookup.findXMLFile(Task.class));
		List<Task> tasks = Database.retrieve(file);
		Task taskToDelete = tasks.get(2);
		int tasksPriorToDelete = tasks.size();
		assertTrue((taskToDelete.getTaskName().equalsIgnoreCase("Push-ups")));
		SupervisorService supervisorService = new SupervisorServiceImpl(folder.getRoot().getAbsolutePath());
		supervisorService.deleteTask(taskToDelete);
		tasks = Database.retrieve(file);
		int tasksAfterDelete = tasks.size();
		taskToDelete = tasks.get(2);
		assertFalse(tasksPriorToDelete == tasksAfterDelete);
		assertFalse((taskToDelete.getTaskName().equalsIgnoreCase("Push-ups")));
		assertTrue((taskToDelete.getTaskName().equalsIgnoreCase("Sit-ups")));

	}

	@Test
	public void testGoalsAssociatedWithDeletedTask() throws Exception {
		File taskFile = new File(folder.getRoot().getAbsolutePath() + XMLFileLookup.findXMLFile(Task.class));
		File goalFile = new File(folder.getRoot().getAbsolutePath() + XMLFileLookup.findXMLFile(Goal.class));

		List<Goal> goals = Database.retrieve(goalFile);
		int goalsPriorSize = goals.size();
		assertTrue(goalsPriorSize == 4);

		List<Task> tasks = Database.retrieve(taskFile);
		Task taskToDelete = tasks.get(1);
		SupervisorService supervisorService = new SupervisorServiceImpl(folder.getRoot().getAbsolutePath());
		supervisorService.deleteTask(taskToDelete);

		goals = Database.retrieve(goalFile);
		int goalsAfterSize = goals.size();
		assertFalse(goalsPriorSize == goalsAfterSize);
		assertTrue(goalsAfterSize == 2);
	}

	@Test
	public void testChildrenGoalDeletedAfterAssociatedTaskDeleted() throws Exception {
		File taskFile = new File(folder.getRoot().getAbsolutePath() + XMLFileLookup.findXMLFile(Task.class));
		File childFile = new File(folder.getRoot().getAbsolutePath() + XMLFileLookup.findXMLFile(Child.class));
		List<Child> children = Database.retrieve(childFile);
		Child childBeforeTaskIsDeleted = children.get(0);
		int childrenPriorSize = children.size();
		assertTrue(childrenPriorSize == 3);

		List<Task> tasks = Database.retrieve(taskFile);
		Task taskToDelete = tasks.get(1);
		SupervisorService supervisorService = new SupervisorServiceImpl(folder.getRoot().getAbsolutePath());
		supervisorService.deleteTask(taskToDelete);

		List<Child> children2 = Database.retrieve(childFile);
		Child childAfterTaskIsDeleted = children2.get(0);
		int childrenAfterSize = children2.size();
		assertTrue(childrenPriorSize == childrenAfterSize);
		assertEquals(childBeforeTaskIsDeleted.getChildID(), childAfterTaskIsDeleted.getChildID());
		assertFalse(childAfterTaskIsDeleted.getGoalList().size() == childBeforeTaskIsDeleted.getGoalList().size());
	}

	/**
	 * Adds the provided goal to the child defined on the goal. If provided goal
	 * does not exist in DB, an entry will need to be created.
	 * 
	 * @param goal
	 */
	@Test
	public void testAddGoal() throws Exception {
		File file = new File(folder.getRoot().getAbsolutePath() + XMLFileLookup.findXMLFile(Goal.class));
		List<Goal> goals = Database.retrieve(file);
		int goalsBeforeAdd = goals.size();
		SupervisorService supervisorService = new SupervisorServiceImpl(folder.getRoot().getAbsolutePath());
		Goal goal = new Goal();
		GoalID goalID = new GoalID();
		goalID.setGoalID(7176);
		goal.setGoalID(goalID);
		goal.setCurrent(0);
		goal.setTarget(100);
		TaskID taskID = new TaskID();
		taskID.setTaskID(7177);
		goal.setTaskID(taskID);
		ChildID childID = new ChildID();
		childID.setChildID(7178);
		goal.setChildID(childID);
		supervisorService.addGoal(goal);
		goals = Database.retrieve(file);
		assertEquals(goalsBeforeAdd + 1, goals.size());
	}

	@Test
	public void testAddExistingGoal() throws Exception {
		File file = new File(folder.getRoot().getAbsolutePath() + XMLFileLookup.findXMLFile(Goal.class));
		List<Goal> goals = Database.retrieve(file);
		int goalsBeforeAdd = goals.size();
		SupervisorService supervisorService = new SupervisorServiceImpl(folder.getRoot().getAbsolutePath());
		Goal goal = new Goal();
		GoalID goalID = new GoalID();
		goalID.setGoalID(1);
		goal.setGoalID(goalID);
		goal.setCurrent(0);
		goal.setTarget(100);
		TaskID taskID = new TaskID();
		taskID.setTaskID(7177);
		goal.setTaskID(taskID);
		ChildID childID = new ChildID();
		childID.setChildID(7178);
		goal.setChildID(childID);
		supervisorService.addGoal(goal);
		
		File newFile = new File(folder.getRoot().getAbsolutePath() + XMLFileLookup.findXMLFile(Goal.class));
		List<Goal> newGoals = Database.retrieve(newFile);
		assertEquals(goalsBeforeAdd, newGoals.size());
	}

	@Test
	public void testAddExistingGoalToChildWithGoalAlready() throws Exception {
		File file = new File(folder.getRoot().getAbsolutePath() + XMLFileLookup.findXMLFile(Goal.class));
		List<Goal> goals = Database.retrieve(file);
		int goalsBeforeAdd = goals.size();
		SupervisorService supervisorService = new SupervisorServiceImpl(folder.getRoot().getAbsolutePath());
		Goal goal = new Goal();
		GoalID goalID = new GoalID();
		goalID.setGoalID(1);
		goal.setGoalID(goalID);
		goal.setCurrent(0);
		goal.setTarget(100);
		TaskID taskID = new TaskID();
		taskID.setTaskID(7177);
		goal.setTaskID(taskID);
		ChildID childID = new ChildID();
		childID.setChildID(100);
		goal.setChildID(childID);
		supervisorService.addGoal(goal);
		
		goals = Database.retrieve(file);
		assertEquals(goalsBeforeAdd, goals.size());
	}

	@Test
	public void testUpdateGoal() throws Exception {
		File file = new File(folder.getRoot().getAbsolutePath() + XMLFileLookup.findXMLFile(Goal.class));
		List<Goal> goals = Database.retrieve(file);
		Goal goalBeforeUpdate = goals.get(1);
		assertTrue(goalBeforeUpdate.getTarget() == 10);
		goalBeforeUpdate.setTarget(200);
		SupervisorService supervisorService = new SupervisorServiceImpl(folder.getRoot().getAbsolutePath());
		supervisorService.updateGoal(goalBeforeUpdate);
		
		List<Goal> newGoalList = Database.retrieve(file);
		Goal goalAfterUpdate = newGoalList.get(1);
		
		
		assertTrue(goalAfterUpdate.getTarget() == 200);
		assertFalse(goalBeforeUpdate.getTarget() == 10);

		Goal newUpdate = goals.get(0);
		newUpdate.setCurrent(2);
		supervisorService.updateGoal(newUpdate);
		
		
		newGoalList = Database.retrieve(file);
		goalAfterUpdate = newGoalList.get(1);
		assertTrue(goalAfterUpdate.getCurrent() == 2);
	}

	@Test

	public void testDeleteGoalUsingSize() throws Exception {

		File file = new File(folder.getRoot().getAbsolutePath() + XMLFileLookup.findXMLFile(Goal.class));
		List<Goal> goals = Database.retrieve(file);
		int goalsBeforeDelet = goals.size();
		SupervisorService supervisorService = new SupervisorServiceImpl(folder.getRoot().getAbsolutePath());
		
		Goal deleteGoal = goals.get(0);
		supervisorService.deleteGoal(deleteGoal);
		goals = Database.retrieve(file);
		int goalsAfterDelete = goals.size();		
		assertEquals(goalsBeforeDelet -1, goalsAfterDelete);
		

	}
	
	@Test
	public void testDeleteGoalFromChild() throws Exception {
		
		File file = new File(folder.getRoot().getAbsolutePath() + XMLFileLookup.findXMLFile(Goal.class));
		List<Goal> goals = Database.retrieve(file);
		
		SupervisorService supervisorService = new SupervisorServiceImpl(folder.getRoot().getAbsolutePath());
		File file1 = new File(folder.getRoot().getAbsolutePath() + XMLFileLookup.findXMLFile(Supervisor.class));
		List<Supervisor> sups = Database.retrieve(file1);
				
		List<Child> childrenActualBeforeGoalDelete =  supervisorService.findAllChildren(sups.get(0));
		Child child = childrenActualBeforeGoalDelete.get(0);
		
		int childGoalsizeBeforeDelete = child.getGoalList().size();
		
		Goal deleteGoal = goals.get(0);
		supervisorService.deleteGoal(deleteGoal);
		
		List<Child> childrenActualAftereGoalDelete =  supervisorService.findAllChildren(sups.get(0));
		Child child1 = childrenActualAftereGoalDelete.get(0);
		int childGoalsizeAfterDelete = child1.getGoalList().size();
		
		
		assertEquals(childGoalsizeBeforeDelete -1, childGoalsizeAfterDelete);
		
	}

	@Test
	public void testFindAllChildrenForSupervisor1() throws Exception {
		File file = new File(folder.getRoot().getAbsolutePath() + XMLFileLookup.findXMLFile(Supervisor.class));
		List<Supervisor> supervisors = Database.retrieve(file);
		SupervisorService supervisorService = new SupervisorServiceImpl(folder.getRoot().getAbsolutePath());
		List<Child> childrenActual = supervisorService.findAllChildren(supervisors.get(0));
		ChildID expectedID = new ChildID();
		expectedID.setChildID(100);
		assertEquals(expectedID, childrenActual.get(0).getChildID());

	}

	@Test
	public void testFindAllChildrenForSupervior2() throws Exception {
		File file = new File(folder.getRoot().getAbsolutePath() + XMLFileLookup.findXMLFile(Supervisor.class));
		List<Supervisor> supervisors = Database.retrieve(file);
		SupervisorService supervisorService = new SupervisorServiceImpl(folder.getRoot().getAbsolutePath());
		List<Child> childrenActual = supervisorService.findAllChildren(supervisors.get(1));
		ChildID expectedID = new ChildID();
		expectedID.setChildID(101);
		assertEquals(expectedID, childrenActual.get(1).getChildID());

	}

}
