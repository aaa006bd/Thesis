package com.example.models;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImageRepository extends JpaRepository<ImageFeatures , Long> {
	
	public ImageFeatures findById(long id);
	
	@Query("SELECT new com.example.models.ImageFeatures(f.id,f.caption) FROM ImageFeatures f")
	public List<ImageFeatures> findImageDetails();
	
	@Query("SELECT new com.example.models.ImageFeatures(f.id,f.caption,f.imageFeatures) FROM ImageFeatures f")
	public List<ImageFeatures>findImageFeatures();
	
}
