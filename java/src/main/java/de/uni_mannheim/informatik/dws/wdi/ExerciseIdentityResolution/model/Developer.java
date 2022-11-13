package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model;

import java.io.Serializable;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class Developer extends AbstractRecord<Attribute> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String developerName;

	public Developer(String identifier, String provenance) {
		super(identifier, provenance);
	}
	
	public void setDeveloperName(String developerName) {
		this.developerName = developerName;
	}
	public String getDeveloperName() {
		return developerName;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
		@Override
		public int hashCode() {
			int result = 31 + ((developerName == null) ? 0 : developerName.hashCode());
			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Developer other = (Developer) obj;
			if (developerName == null) {
				if (other.developerName != null)
					return false;
			} else if (!developerName.equals(other.developerName))
				return false;
			return true;
		}

		public static final Attribute DeveloperName = new Attribute("DeveloperName");
				
		/* (non-Javadoc)
		 * @see de.uni_mannheim.informatik.wdi.model.Record#hasValue(java.lang.Object)
		 */
		@Override
		public boolean hasValue(Attribute attribute) {
			if(attribute==DeveloperName)
				return DeveloperName!=null;
			else
				return false;
		}
}
