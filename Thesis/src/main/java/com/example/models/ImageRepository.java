package com.example.models;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageFeatures , Long> {
	
	public ImageFeatures findById(long id);
	
}
