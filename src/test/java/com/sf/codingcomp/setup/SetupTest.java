package com.sf.codingcomp.setup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.statefarm.aitp.model.CountType;
import com.statefarm.aitp.model.Task;
import com.statefarm.aitp.model.TaskID;
import com.statefarm.aitp.model.TaskType;
import com.statefarm.aitp.persistence.Database;
import com.statefarm.aitp.persistence.XMLFileLookup;
import com.thoughtworks.xstream.XStream;

public class SetupTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Before
	public void setUp() throws Exception {
		FileUtils.copyDirectory(FileUtils.toFile(SetupTest.class.getResource("/db")), folder.getRoot());
	}

	@Test
	public void testJUnitDependency() {
		assertTrue(true);
	}

	@Test
	public void testXStreamDependency() {
		XStream xstream = new XStream();
		assertNotNull(xstream);
	}

	@Test
	public void testReadWrite() throws Exception {
		Task task = new Task();
		TaskID taskID = new TaskID();
		taskID.setTaskID(6);
		task.setTaskID(taskID);
		task.setTaskName("jump");
		task.setCountType(CountType.REPITITIONS);
		task.setTaskType(TaskType.EXERCISE);
		File file = new File(folder.getRoot().getAbsolutePath() + XMLFileLookup.findXMLFile(Task.class));
		List<Task> tasks = Database.retrieve(file);
		tasks.add(task);
		Database.overwriteAll(tasks, file);
		tasks = Database.retrieve(file);
		assertEquals(6, tasks.size());
	}

}
