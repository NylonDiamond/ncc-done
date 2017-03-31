package com.statefarm.aitp.persistence;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FileUtils;
import com.statefarm.aitp.model.Child;
import com.statefarm.aitp.model.ChildID;
import com.statefarm.aitp.model.Goal;
import com.statefarm.aitp.model.GoalID;
import com.statefarm.aitp.model.Supervisor;
import com.statefarm.aitp.model.Task;
import com.statefarm.aitp.model.TaskID;
import com.thoughtworks.xstream.XStream;

public class Database {

	private static XStream xstream = new XStream();

	@SuppressWarnings("unchecked")
	public static <T> List<T> retrieve(File file) {
		xstream.alias("Child", Child.class);
		xstream.alias("ChildID", ChildID.class);
		xstream.alias("Task", Task.class);
		xstream.alias("TaskID", TaskID.class);
		xstream.alias("Goal", Goal.class);
		xstream.alias("GoalID", GoalID.class);
		xstream.alias("Supervisor", Supervisor.class);

	
		return (List<T>) xstream.fromXML(file);
	}

	public static <T> void overwriteAll(List<T> contents, File file) {
		try {
			FileUtils.write(file, xstream.toXML(contents));
		} catch (IOException e) {
			throw new DataBaseException("Error writing to file " + file.getName(), e);
		}
	}
}
