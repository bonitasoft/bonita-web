package org.bonitasoft.reporting.utils.widget;

public class Widget {
	
	private String id;
	private String widget;
	private String label;
	private String query;
	private String availableValues;
	private String initialValue;
	private String hasAll;
	private String hasAllValue;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWidget() {
		return widget;
	}
	public void setWidget(String widget) {
		this.widget = widget;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getAvailableValues() {
		return availableValues;
	}
	public void setAvailableValues(String availableValues) {
		this.availableValues = availableValues;
	}
	public String getInitialValue() {
		return initialValue;
	}
	public void setInitialValue(String initialValue) {
		this.initialValue = initialValue;
	}
	
	public String getHasAll() {
		return hasAll;
	}
	public void setHasAll(String hasAll) {
		this.hasAll = hasAll;
	}
	public String getHasAllValue() {
		return hasAllValue;
	}
	public void setHasAllValue(String hasAllValue) {
		this.hasAllValue = hasAllValue;
	}
	public String toString(){
		return "id:"+id+", " +
			   "widget:"+ widget +
			   ", label:"+label+ 
			   ", query:"+query+ 
			   ", availableValues:"+availableValues+ 
			   ", initialValue:"+ initialValue +
			   ", hasAll:" + hasAll +
			   ", hasAllValue:" + hasAllValue;
	}
	
	

}
