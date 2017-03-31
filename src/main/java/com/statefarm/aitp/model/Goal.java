package com.statefarm.aitp.model;

import com.statefarm.aitp.persistence.DataBaseException;

import java.util.List;

public class Goal{

    private GoalID goalID;
    private ChildID childID;
    private TaskID taskID;
    private int target;
    private int current;

    public GoalID getGoalID() {
        return goalID;
    }

    public void setGoalID(GoalID goalID) {
        this.goalID = goalID;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public TaskID getTaskID() {
        return taskID;
    }

    public void setTaskID(TaskID taskID) {
        this.taskID = taskID;
    }

    public ChildID getChildID() {
        return childID;
    }

    public void setChildID(ChildID childID) {
        this.childID = childID;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((goalID == null) ? 0 : goalID.hashCode());
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
        Goal other = (Goal) obj;
        if (goalID == null) {
            if (other.goalID != null)
                return false;
        } else if (!goalID.equals(other.goalID))
            return false;
        return true;
    }

    public static Goal getGoalById(GoalID goalId, List<Goal> goals) {
        for (Goal g : goals) {
            if (g.getGoalID().equals(goalId)) {
                return g;
            }
        }
        return null;
    }


}
