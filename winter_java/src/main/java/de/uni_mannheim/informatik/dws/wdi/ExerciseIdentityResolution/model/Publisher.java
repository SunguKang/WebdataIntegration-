package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model;

import java.io.Serializable;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class Publisher extends AbstractRecord<Attribute> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String publisherName;

	public Publisher(String identifier, String provenance) {
		super(identifier, provenance);
	}
	
	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}
	public String getPublisherName() {
		return publisherName;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
		@Override
		public int hashCode() {
			int result = 31 + ((publisherName == null) ? 0 : publisherName.hashCode());
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
			Publisher other = (Publisher) obj;
			if (publisherName == null) {
				if (other.publisherName != null)
					return false;
			} else if (!publisherName.equals(other.publisherName))
				return false;
			return true;
		}

		public static final Attribute PublisherName = new Attribute("PublisherName");
				
		/* (non-Javadoc)
		 * @see de.uni_mannheim.informatik.wdi.model.Record#hasValue(java.lang.Object)
		 */
		@Override
		public boolean hasValue(Attribute attribute) {
			if(attribute==PublisherName)
				return publisherName!=null;
			else
				return false;
		}
}
