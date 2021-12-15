package org.bonitasoft.web.rest.server.api.bpm.signal;

public class BPMSignal {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static BPMSignal create(String signalName) {
		var bpmSignal = new BPMSignal();
		bpmSignal.setName(signalName);
		return bpmSignal;
	}

}
