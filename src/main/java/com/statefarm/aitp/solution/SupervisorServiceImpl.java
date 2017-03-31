package com.statefarm.aitp.solution;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.statefarm.aitp.model.*;
import com.statefarm.aitp.persistence.Database;
import com.statefarm.aitp.persistence.XMLFileLookup;
import com.statefarm.aitp.service.SupervisorService;

public class SupervisorServiceImpl extends SupervisorService {
    private String fileRoot;

    public SupervisorServiceImpl(String fileRoot) {
        super(fileRoot);
        this.fileRoot = fileRoot;
    }

    @Override
    public void addTask(Task task) {
        File file = new File(fileRoot + XMLFileLookup.findXMLFile(Task.class));
        List<Task> tasks = Database.retrieve(file);
        tasks.add(task);
        Database.overwriteAll(tasks, file);
    }

    @Override
    public void updateTask(Task task) {
        File file = new File(fileRoot + XMLFileLookup.findXMLFile(Task.class));
        List<Task> tasks = Database.retrieve(file);
        for (Task t : tasks) {
            if (t.getTaskID().equals(task.getTaskID())) {
                t.setCountType(task.getCountType());
                t.setTaskName(task.getTaskName());
                t.setTaskType(task.getTaskType());
            }
        }
        tasks.add(task);
        Database.overwriteAll(tasks, file);
    }

    @Override
    public void deleteTask(Task task) {

        File file1 = new File(fileRoot + XMLFileLookup.findXMLFile(Task.class));
        File file2 = new File(fileRoot + XMLFileLookup.findXMLFile(Goal.class));
        File file3 = new File(fileRoot + XMLFileLookup.findXMLFile(Child.class));
        List<Task> tasks = Database.retrieve(file1);
        List<Goal> goals = Database.retrieve(file2);
        List<Child> children = Database.retrieve(file3);


        for (Child c : children) {
            List<GoalID> cGoals = c.getGoalList();
            List<GoalID> newCGoals = new ArrayList<GoalID>();
            for (Goal g : goals) {
                for (GoalID id : cGoals) {
                    if (!g.getGoalID().equals(id)) {
                        newCGoals.add(id);
                    }
                }
            }
            c.setGoalList(newCGoals);
        }
        List<Goal> newGoal = new ArrayList<Goal>();
        for (Goal g : goals) {
            if (!g.getTaskID().equals(task.getTaskID())) {
                newGoal.add(g);
            }
        }
        List<Task> newTasks = new ArrayList<Task>();
        for (Task t : tasks) {
            if (!t.getTaskID().equals(task.getTaskID())) {
                newTasks.add(t);
            }
        }
        Database.overwriteAll(newTasks, file1);
        Database.overwriteAll(newGoal, file2);
        Database.overwriteAll(children, file3);
    }

    @Override
    public void deleteGoal(Goal goal) {
        File file2 = new File(fileRoot + XMLFileLookup.findXMLFile(Goal.class));
        File file3 = new File(fileRoot + XMLFileLookup.findXMLFile(Child.class));
        List<Child> children = Database.retrieve(file3);
        List<Goal> goals = Database.retrieve(file2);
        List<Goal> newGoals = new ArrayList<Goal>();

        for (Goal g : goals) {
            if (!g.getGoalID().equals(goal.getGoalID())) {
                newGoals.add(g);
            }
        }

        for (Child c : children) {
            List<GoalID> cGoals = c.getGoalList();
            List<GoalID> newCGoals = new ArrayList<GoalID>();
            for (Goal g : newGoals) {
                for (GoalID id : cGoals) {
                    if (g.getGoalID().equals(id)) {
                        newCGoals.add(id);
                    }
                }
            }
            c.setGoalList(newCGoals);
        }


        Database.overwriteAll(newGoals, file2);
        Database.overwriteAll(children, file3);
    }

    @Override
    public void addGoal(Goal goal) {
        File file = new File(fileRoot + XMLFileLookup.findXMLFile(Goal.class));
        File file2 = new File(fileRoot + XMLFileLookup.findXMLFile(Child.class));
        List<Child> children = Database.retrieve(file2);
        List<Goal> goals = Database.retrieve(file);
        //the child to add the goal to
        ChildID childId = goal.getChildID();

        //check all goals to see if goal is in it
        boolean inGoals = false;
        for (Goal g : goals) {
            if (g.getGoalID().equals(goal.getGoalID())) {
                inGoals = true;
            }
        }
        //if not in goals .. add it to goals
        if (!inGoals) {
            goals.add(goal);
            //then add to child goals list
            List<GoalID> newChildList = new ArrayList<GoalID>();
            addGoalToChild(goal, children, childId);
        } else {
            //add to childs goal list
            List<GoalID> newChildList = new ArrayList<GoalID>();
            addGoalToChild(goal, children, childId);
        }

//        goals.add(goal);
        Database.overwriteAll(goals, file);
        Database.overwriteAll(children, file2);

    }

    private void addGoalToChild(Goal goal, List<Child> children, ChildID childId) {
        for (Child c : children) {
            if (c.getChildID().equals(childId)) {
                List<GoalID> gs = c.getGoalList();
                if (!gs.contains(goal.getGoalID())) {
                    gs.add(goal.getGoalID());
                }
                c.setGoalList(gs);
            }
        }
    }

    @Override
    public void updateGoal(Goal goal) {
        File file2 = new File(fileRoot + XMLFileLookup.findXMLFile(Goal.class));
        List<Goal> goals = Database.retrieve(file2);
//        List<Goal> newGoals = new ArrayList<Goal>();
        for (Goal g : goals) {
            if (g.getGoalID().equals(goal.getGoalID())) {
                g.setCurrent(goal.getCurrent());
                g.setChildID(goal.getChildID());
                g.setGoalID(goal.getGoalID());
                g.setTarget(goal.getTarget());
                g.setTaskID(goal.getTaskID());
            }
        }
        Database.overwriteAll(goals, file2);

    }

    @Override
    public List<Child> findAllChildren(Supervisor supervisor) {
        File file = new File(fileRoot + XMLFileLookup.findXMLFile(Supervisor.class));
        File file2 = new File(fileRoot + XMLFileLookup.findXMLFile(Child.class));
        List<Child> children = Database.retrieve(file2);
        List<Supervisor> supervisors = Database.retrieve(file);
        List<ChildID> childIDS = supervisor.getChildList();
        List<Child> childIDSNew = new ArrayList<Child>();
        for (Child c : children) {
            for (ChildID id : childIDS) {
                if (c.getChildID().equals(id)) {
                    childIDSNew.add(c);
                }
            }
        }
        return children;
    }

}
