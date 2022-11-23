package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model;

import java.io.Serializable;
import java.util.LinkedList;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.Fusible;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import java.time.LocalDateTime;
import java.util.List;

public class Game extends AbstractRecord<Attribute> implements Serializable{ {
	
	protected String id;
	protected String provenance;
	private String name;
	private String platform;
	private List<Publisher> publishers;
	private LocalDateTime publicationDate;
	private Float globallySoldUnits;
	private List<Genre> genres;
	private Float criticScore;
	private Float userScore;
	private List<Developer> developers;
	private String summary;
	private String rating;
	private String series;
	
	public Game(String identifier, String provenance) {
		id = identifier;
		this.provenance = provenance;
		genres = new LinkedList<>();
		developers = new LinkedList<>();
		publishers = new LinkedList<>();
	}
	
	@Override
	public String getIdentifier() {
		return id;
	}

	@Override
	public String getProvenance() {
		return provenance;
	}
	
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

	public int getPublicationYear(){
		return publicationDate.getYear();
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
	
	public Float getGloballySoldUnits() {
        return globallySoldUnits;
    }
    
	public void setGloballyySoldUnits(Float globallySoldUnits) {
	        this.globallySoldUnits = globallySoldUnits;
	    }    
	    
	public List<Genre> getGenres() {
	        return genres;
	    }
	    
	public void setGenres(List<Genre> genres) {
	        this.genres = genres;
	    }    
	    
	public Float getCriticScore() {
	        return criticScore;
	    }
	    
	public void setCriticScore(Float criticScore) {
	        this.criticScore = criticScore;
	    }    
	    
	public Float getUserScore() {
	        return userScore;
	    }
	    
	public void setUserScore(Float userScore) {
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

	@Override
	public String toString() {
		return String.format("[Game %s: %s / %s ]", getIdentifier(), getName(),
				getPlatform());
	}

	@Override
	public int hashCode() {
		return getIdentifier().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Game) {
			return this.getIdentifier().equals(((Game) obj).getIdentifier());
		} else
			return false;
	}

	@Override
	public boolean hasValue(Object o) {
		if (o instanceof Game) {
			return this.getIdentifier().equals(((Game) o).getIdentifier());
		} else
			return false;
	}
}
