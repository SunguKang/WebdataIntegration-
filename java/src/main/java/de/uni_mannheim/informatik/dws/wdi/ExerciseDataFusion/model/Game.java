package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.Fusible;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class Game extends AbstractRecord<Attribute> implements Serializable{
	
	protected String id;
	private String name;
	private String platform;
	private List<Publisher> publishers;
	private LocalDateTime publicationDate;
	private float globallySoldUnits;
	private List<Genre> genres;
	private float criticScore;
	private float userScore;
	private List<Developer> developers;
	private String summary;
	private String rating;
	private String series;
	
	public Game(String identifier, String provenance) {
		super(identifier, provenance);
		genres = new LinkedList<>();
		developers = new LinkedList<>();
		publishers = new LinkedList<>();
	}

	
	@Override
	public String getIdentifier() {
		return id;
	}

//	@Override
//	public String getProvenance() {
//		return provenance;
//	}
	
	public String getName(){
		return name;
	}
	public String getPlatform(){
		return platform;
	}
	public List<Publisher> getPublishers(){
		return publishers;
	}
	public LocalDateTime getPublicationDate(){
		return publicationDate;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setPlatform(String platform){
		this.platform = platform;
	}
	public void setPublishers(List<Publisher> publishers){
		this.publishers = publishers;
	}
	public void setPublicationDate(LocalDateTime publicationDate){
		this.publicationDate = publicationDate;
	}
	
	public float getGloballySoldUnits() {
        return globallySoldUnits;
    }
    
	public void setGloballyySoldUnits(float globallySoldUnits) {
	        this.globallySoldUnits = globallySoldUnits;
	    }    
	    
	public List<Genre> getGenres() {
	        return genres;
	    }
	    
	public void setGenres(List<Genre> genres) {
	        this.genres = genres;
	    }    
	    
	public float getCriticScore() {
	        return criticScore;
	    }
	    
	public void setCriticScore(float criticScore) {
	        this.criticScore = criticScore;
	    }    
	    
	public float getUserScore() {
	        return userScore;
	    }
	    
	public void setUserScore(float userScore) {
	        this.userScore = userScore;
	    }
	    
	public List<Developer> getDevelopers() {
	        return developers;
	    }

   public void setDevelopers(List<Developer> developers) {
        this.developers = developers;
    }    
    
	public String getSummary() {
	        return summary;
	    }
	    
	public void setSummary(String summary) {
	        this.summary = summary;
	    }    
	    
	public String getRating() {
	        return rating;
	    }
	    
	public void setRating(String rating) {
	        this.rating = rating;
	    }
	    
	public String getSeries() {
	        return series;
	    }
	    
	public void setSeries(String series) {
	        this.series = series;
	    }   	
	
	private Map<Attribute, Collection<String>> provenance = new HashMap<>();
	private Collection<String> recordProvenance;

	public void setRecordProvenance(Collection<String> provenance) {
		recordProvenance = provenance;
	}

	public Collection<String> getRecordProvenance() {
		return recordProvenance;
	}

	public void setAttributeProvenance(Attribute attribute,
			Collection<String> provenance) {
		this.provenance.put(attribute, provenance);
	}

	public Collection<String> getAttributeProvenance(String attribute) {
		return provenance.get(attribute);
	}

	public String getMergedAttributeProvenance(Attribute attribute) {
		Collection<String> prov = provenance.get(attribute);

		if (prov != null) {
			return StringUtils.join(prov, "+");
		} else {
			return "";
		}
	}

	public static final Attribute NAME= new Attribute("name");
	public static final Attribute PLATFORM = new Attribute("platform");
	public static final Attribute PUBLISHERS = new Attribute("publishers");
	public static final Attribute PUBLICATIONDATE = new Attribute("publicationDate");
	public static final Attribute GENRES = new Attribute("genres");
	public static final Attribute DEVELOPERS = new Attribute("developers");	
	
	
	@Override
	public String toString() {
		return String.format("[Game %s: %s / %s ]", getIdentifier(), getName(),
				getPlatform().toString());
	}

	@Override
	//  TODO has to be adapted
	public boolean hasValue(Attribute attribute) {
		if(attribute==NAME)
			return getName() != null && !getName().isEmpty();
		else if(attribute==PLATFORM)
			return getPlatform() != null && !getPlatform().isEmpty();
		else if(attribute==PUBLICATIONDATE)
			return getPublicationDate() != null;
		else if(attribute==PUBLISHERS)
			return getPublishers() != null && getPublishers().size() > 0;
		else if(attribute==GENRES)
			return getGenres() != null && getGenres().size() > 0;
		else if(attribute==DEVELOPERS)
			return getDevelopers() != null && getDevelopers().size() > 0;
		else
			return false;
	}

	@Override
	public int hashCode() {
		return getIdentifier().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Game){
			return this.getIdentifier().equals(((Game) obj).getIdentifier());
		}else
			return false;
	}

	
	
}
