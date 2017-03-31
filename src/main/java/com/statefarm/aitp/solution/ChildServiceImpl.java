package com.statefarm.aitp.solution;

import java.io.File;
import java.util.*;

import com.statefarm.aitp.model.*;
import com.statefarm.aitp.persistence.Database;
import com.statefarm.aitp.persistence.XMLFileLookup;
import com.statefarm.aitp.service.ChildService;

public class ChildServiceImpl extends ChildService {
    private String fileRoot;

    //	ChildService childService = new ChildServiceImpl(folder.getRoot().getAbsolutePath());
    public ChildServiceImpl(String fileRoot) {
        super(fileRoot);
        this.fileRoot = fileRoot;
    }

    @Override
    public int updateGoalProgress(GoalID goalID, int current) {
        List<Goal> goals = Database.retrieve(new File(fileRoot + XMLFileLookup.findXMLFile(Goal.class)));
        Goal theGoal = Goal.getGoalById(goalID, goals);
        theGoal.setCurrent(current);
        Database.overwriteAll(goals, new File(fileRoot + XMLFileLookup.findXMLFile(Goal.class)));
        return current * 10;
    }

    @Override
    public List<Goal> findAllGoals(Child child) {
        List<Goal> allGoals = Database.retrieve(new File(fileRoot + XMLFileLookup.findXMLFile(Goal.class)));
        List<GoalID> goals = child.getGoalList();
        List<Goal> childsGoals = new ArrayList<Goal>();
        for (Goal g : allGoals) {
            for (GoalID id : goals) {
                if (id.equals(g.getGoalID())) {
                    childsGoals.add(g);
                }
            }
        }

        boolean running = true;
        while(running) {
            running  = false;
            for (int x = 0; x < childsGoals.size() - 1; x++) {
                if (childsGoals.get(x).getCurrent() > childsGoals.get(x + 1).getCurrent()) {
                    int temp = childsGoals.get(x).getCurrent();
                    childsGoals.get(x).setCurrent(childsGoals.get(x + 1).getCurrent());
                    childsGoals.get(x + 1).setCurrent(temp);
                    running = true;
                }
            }
        }
//        for(Goal g: childsGoals){
//            g.setCurrent(g.getCurrent()*10);
//        }
        return childsGoals;
    }

    @Override
    public int findAllGoalsCompletion(Child child) {
        List<Goal> allGoals = Database.retrieve(new File(fileRoot + XMLFileLookup.findXMLFile(Goal.class)));
        List<Goal> childsGoals = findAllGoals(child);
        int runningTotal = 0;
        int count = 0;
        for (Goal all : allGoals) {
               for(Goal cGoal : childsGoals){
                   if(all.getGoalID().equals(cGoal.getGoalID())){
                       runningTotal += cGoal.getCurrent();
                       count++;
                   }
               }
            }
        return (runningTotal *10)/childsGoals.size();
    }


    @Override
    public List<Child> findChildrenBySameTasks(Child child) {
        List<Child> children = Database.retrieve(new File(fileRoot + XMLFileLookup.findXMLFile(Child.class)));
        List<Goal> allGoals = Database.retrieve(new File(fileRoot + XMLFileLookup.findXMLFile(Goal.class)));

        //THE FIRST STEP
        List<TaskID> childTasks = new ArrayList<TaskID>();
        for (Goal goal : allGoals) {
            for (GoalID goalID : child.getGoalList()) {
                if (goal.getGoalID().equals(goalID)) {
                    childTasks.add(goal.getTaskID());
                    break;
                }
            }
        }

        //Find the children with similar tasks. use a set to make sure the same child doesn't get added twice
        Set<Child> similarChildrenForTask = new HashSet<Child>();
        for (Child c : children) {
            //Check to make sure we are not checking the child who is in question
            if (c.getChildID().equals(child.getChildID())) {
                continue;
            }

            for (TaskID taskID : childTasks) {
                for (Goal goal : this.findAllGoals(c)) {
                    if (taskID.equals(goal.getTaskID())) {
                        similarChildrenForTask.add(c);
                        break;
                    }
                }
            }

        }
        return new ArrayList<Child>(similarChildrenForTask);
    }

}