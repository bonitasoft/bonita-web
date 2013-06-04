package org.bonitasoft.report.scriptlet;

public class AggragateScriptletKey implements Comparable<AggragateScriptletKey>{

	String category;
	String serie;
	
	
	public AggragateScriptletKey() {
		super();
		// TODO Auto-generated constructor stub
	}


	public AggragateScriptletKey(String categorie, String serie) {
		super();
		this.category = categorie;
		this.serie = serie;
	}


	public String getCategory() {
		return category;
	}


	public void setCategorie(String categorie) {
		this.category = categorie;
	}


	public String getSerie() {
		return serie;
	}


	public void setSerie(String serie) {
		this.serie = serie;
	}
	
	public String toString(){
		return "{categorie:"+this.category+", serie:"+this.serie+"}";
	}
	
	public int compareTo(AggragateScriptletKey o) {
		
		if (o == null)
			return 1;
		
		if( (this.category != null || this.serie != null) &&
			(o.getCategory() == null || o.getSerie() == null)
		)
			return 1;
		
		if( (this.category == null || this.serie == null) &&
				(o.getCategory() != null || o.getSerie() != null)
			)
			return -1;
		
		
		return (this.category.equals(o.getCategory()) ? 1 : 0 )
				+  
			   (this.serie.equals(o.getSerie()) ? 1 : 0);			
	}
	
	public boolean equals(Object o) {	
		
		
		if (o == null) 	return false;
		if ( !(o instanceof AggragateScriptletKey) ) return false;
	
		AggragateScriptletKey obj = (AggragateScriptletKey) o;
		
		return this.category.equals(obj.getCategory()) && this.serie.equals(obj.getSerie());
	}
	
	public int hashCode(){
		return this.category.hashCode() + this.serie.hashCode();
	}

	
	
}
