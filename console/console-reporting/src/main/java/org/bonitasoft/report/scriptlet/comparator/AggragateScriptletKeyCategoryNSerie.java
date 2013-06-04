package org.bonitasoft.report.scriptlet.comparator;

import java.util.Comparator;

import org.bonitasoft.report.scriptlet.AggragateScriptletMeasure;


public class AggragateScriptletKeyCategoryNSerie implements
Comparator<AggragateScriptletMeasure> {
	
	public int compare(AggragateScriptletMeasure o1, AggragateScriptletMeasure o2) {
		
		if (o1 == null && o2 == null)
			return 0;
		
		if (o1 != null && o2 == null)
			return 1;
		
		if (o1 == null && o2 != null)
			return -1;
		
		if (o1.getKey() != null && o2.getKey() == null)
			return 1;
		
		if (o1.getKey() == null && o2.getKey() != null)
			return -1;
		
		String s1 = o1.getKey().getCategory() + o1.getKey().getSerie();
		String s2 = o2.getKey().getCategory() + o2.getKey().getSerie();
	 
		return s1.compareTo(s2);
	}

}
