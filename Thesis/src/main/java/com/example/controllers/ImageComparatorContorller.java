package com.example.controllers;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.openqa.selenium.lift.find.ImageFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.models.ImageFeatures;
import com.example.models.ImageId;
import com.example.models.ImageRepository;
import com.example.services.ImageProcessor;
import com.example.services.JsonPerserService;

import scala.annotation.meta.setter;

@Controller
public class ImageComparatorContorller {

	@Autowired
	private ImageRepository repository;
	
	@Autowired
	private ImageProcessor processor;
	
	@Autowired
	private JsonPerserService jsonParser;
	
	@GetMapping("/select")
	public String selectImageToCompare(Model model){
		
		List<ImageFeatures> allImages = repository.findAll();
		model.addAttribute("imageList", allImages);
		model.addAttribute("imageId", new ImageId());
		return "selectImage";
	}
	
	@PostMapping("/select")
	public String comapareImageAndShowResult(
			@ModelAttribute ImageId imageId,
			Model model){
		System.err.println("id: "+ imageId.getId());
		ImageFeatures selectedImage = repository.findOne(imageId.getId());
		List<ImageFeatures> allImages = repository.findAll();
		allImages.remove(selectedImage);
		
		List<ImageId>results = new ArrayList<>();
		
		Mat featuresOfSelectedImage = jsonParser.jsonToMat(selectedImage.getImageFeatures());
		
		for (ImageFeatures image : allImages) {
			
			ImageId resultTemp = new ImageId();
			resultTemp.setId(image.getId());
			resultTemp.setImageCapiton(image.getCaption());
			
			Mat featuresOfTheOtherImage = jsonParser.jsonToMat(image.getImageFeatures());
			resultTemp.setResults(processor.compareHistogram(featuresOfSelectedImage, featuresOfTheOtherImage));
			results.add(resultTemp);
		}
		model.addAttribute("imageInfo", results);
		
		return "showResult";
		
	}
}
