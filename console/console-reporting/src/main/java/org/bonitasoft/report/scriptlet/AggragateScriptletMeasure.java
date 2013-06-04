package org.bonitasoft.report.scriptlet;

public class AggragateScriptletMeasure implements Comparable<AggragateScriptletMeasure>{

	AggragateScriptletKey key;
	Double measure;
	
	public AggragateScriptletMeasure() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public AggragateScriptletMeasure(AggragateScriptletKey key, Double measure) {
		super();
		this.key = key;
		this.measure = measure;
	}



	public AggragateScriptletKey getKey() {
		return key;
	}

	public void setKey(AggragateScriptletKey key) {
		this.key = key;
	}

	public Double getMeasure() {
		return measure;
	}

	public void setMeasure(Double measure) {
		this.measure = measure;
	}

	public int compareTo(AggragateScriptletMeasure o) {
		
		if (o == null)
			return 1;
		
		if(this.measure == null)
			 return -1;
		if(((AggragateScriptletMeasure) o).measure == null)
			 return 1;
		 
		if (this.measure.doubleValue() == ((AggragateScriptletMeasure) o).measure.doubleValue())
		        return 0;
		else if (this.measure.doubleValue() > ((AggragateScriptletMeasure) o).measure.doubleValue())
		    return 1;
		else
		    return -1;
	}	

}
