package com.statefarm.aitp.model;

import java.util.Arrays;
import java.util.List;

public enum SupervisorType {
	
	PARENT(Arrays.asList(AccessType.READ)), COACH(Arrays.asList(AccessType.READ));
	
	private List<AccessType> accessTypes;
	
	private SupervisorType(List<AccessType> accessTypes) {
		this.accessTypes = accessTypes;
	}

	public List<AccessType> getAccessTypes() {
		return accessTypes;
	}

}
