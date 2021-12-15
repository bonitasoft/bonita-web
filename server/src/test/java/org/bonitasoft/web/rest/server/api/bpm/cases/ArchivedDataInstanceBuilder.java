package org.bonitasoft.web.rest.server.api.bpm.cases;

import java.io.Serializable;
import java.util.Date;

import org.bonitasoft.engine.bpm.data.ArchivedDataInstance;
import org.bonitasoft.engine.bpm.data.impl.ArchivedDataInstanceImpl;

public class ArchivedDataInstanceBuilder {

	public static ArchivedDataInstanceBuilder anArchivedDataInstance(String name) {
		return new ArchivedDataInstanceBuilder(name);
	}

	private String name;
	private String description;
	private String className;
	private Serializable value;
	private long containerId;
	private Date archivedDate;
	private long sourceObjectId;

	private ArchivedDataInstanceBuilder(String name) {
		this.name = name;
	}

	public ArchivedDataInstanceBuilder withDescription(String description) {
		this.description = description;
		return this;
	}

	public ArchivedDataInstanceBuilder withType(String className) {
		this.className = className;
		return this;
	}

	public ArchivedDataInstanceBuilder withValue(Serializable value) {
		this.value = value;
		return this;
	}

	public ArchivedDataInstanceBuilder withContainerId(long containerId) {
		this.containerId = containerId;
		return this;
	}

	public ArchivedDataInstanceBuilder withArchivedDate(Date archivedDate) {
		this.archivedDate = archivedDate;
		return this;
	}

	public ArchivedDataInstanceBuilder withSourceObjectId(long sourceObjectId) {
		this.sourceObjectId = sourceObjectId;
		return this;
	}

	public ArchivedDataInstance build() {
		var instance = new ArchivedDataInstanceImpl();
		instance.setName(name);
		instance.setClassName(className);
		instance.setContainerId(containerId);
		instance.setContainerType("PROCESS_INSTANCE");
		instance.setDescription(description);
		instance.setSourceObjectId(sourceObjectId);
		instance.setArchiveDate(archivedDate);
		instance.setValue(value);
		return instance;
	}
}