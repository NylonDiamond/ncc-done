package com.statefarm.aitp.persistence;

import java.util.HashMap;
import java.util.Map;

import com.statefarm.aitp.model.Child;
import com.statefarm.aitp.model.Goal;
import com.statefarm.aitp.model.Supervisor;
import com.statefarm.aitp.model.Task;

public class XMLFileLookup {

	private static Map<Class<?>, String> mappings = new HashMap<Class<?>, String>();

	static {
		mappings.put(Goal.class, "/goal.xml");
		mappings.put(Child.class, "/children.xml");
		mappings.put(Supervisor.class, "/supervisor.xml");
		mappings.put(Task.class, "/tasks.xml");
	}

	public static String findXMLFile(Class<?> clazz) {
		return mappings.get(clazz);
	}

}
