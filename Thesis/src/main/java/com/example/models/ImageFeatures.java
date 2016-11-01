package com.example.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.Type;

@Entity
public class ImageFeatures {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String caption;
	@Lob
	private byte[] imageRaw;
	@Lob
	private String imageFeatures;
	
	public ImageFeatures(){
		
	}
	
	public ImageFeatures(String caption, String imageFeatures) {
		super();
		this.caption = caption;
		
		this.imageFeatures = imageFeatures;
	}



	public byte[] getImageRaw() {
		return imageRaw;
	}

	public void setImageRaw(byte[] imageRaw) {
		this.imageRaw = imageRaw;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getImageFeatures() {
		return imageFeatures;
	}
	public void setImageFeatures(String imageFeatures) {
		this.imageFeatures = imageFeatures;
	}
	
	

}
