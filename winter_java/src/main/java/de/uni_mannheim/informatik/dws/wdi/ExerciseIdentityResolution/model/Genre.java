package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model;

import java.io.Serializable;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class Genre extends AbstractRecord<Attribute> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String genreName;

	public Genre(String identifier, String provenance) {
		super(identifier, provenance);
	}
	
	public void setGenreName(String genreName) {
		this.genreName = genreName;
	}
	public String getGenreName() {
		return genreName;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
		@Override
		public int hashCode() {
			int result = 31 + ((genreName == null) ? 0 : genreName.hashCode());
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
			Genre other = (Genre) obj;
			if (genreName == null) {
				if (other.genreName != null)
					return false;
			} else if (!genreName.equals(other.genreName))
				return false;
			return true;
		}

		public static final Attribute GenreName = new Attribute("GenreName");
				
		/* (non-Javadoc)
		 * @see de.uni_mannheim.informatik.wdi.model.Record#hasValue(java.lang.Object)
		 */
		@Override
		public boolean hasValue(Attribute attribute) {
			if(attribute==GenreName)
				return genreName!=null;
			else
				return false;
		}
}
