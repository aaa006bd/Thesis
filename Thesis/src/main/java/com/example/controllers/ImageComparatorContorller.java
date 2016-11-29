package com.example.controllers;

import java.io.FileWriter;
import java.io.Writer;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.models.ImageFeatures;
import com.example.models.ImageId;
import com.example.models.ImageRepository;
import com.example.models.ProcessTimeMachine;
import com.example.services.ImageProcessor;
import com.example.services.JsonPerserService;
import com.google.gson.Gson;


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
		
		List<ImageFeatures> allImages = repository.findImageDetails();
		model.addAttribute("imageList", allImages);
		model.addAttribute("imageId", new ImageId());
		return "selectImage";
	}
	
	// time measurement unit has been added
	@PostMapping("/select")
	public String comapareImageAndShowResult(
			@ModelAttribute ImageId imageId,
			Model model){
		ImageFeatures selectedImage = repository.findOne(imageId.getId());
		List<ImageFeatures> allImages = repository.findAll();
		allImages.remove(selectedImage);
		
		Mat featuresOfSelectedImage = jsonParser.jsonToMat(selectedImage.getImageFeatures());
		
		List<ImageId>results = new ArrayList<>();
		List<ProcessTimeMachine> processTime = new ArrayList<>();
		
		for (ImageFeatures image : allImages) {
			
			ImageId resultTemp = new ImageId();
			resultTemp.setId(image.getId());
			resultTemp.setImageCapiton(image.getCaption());
			
			Instant processStart = Instant.now();// start time of each process
			
			Mat featuresOfTheOtherImage = jsonParser.jsonToMat(image.getImageFeatures());
			resultTemp.setResults(processor.compareHistogram(featuresOfSelectedImage, featuresOfTheOtherImage));

			Instant processEnd = Instant.now();// end time of each process
			
			ProcessTimeMachine processTimeInfo = new ProcessTimeMachine();
			processTimeInfo.setId(image.getId());
			processTimeInfo.setImageCaption(image.getCaption());
			processTimeInfo.setProcessTimeMillis( 
					Duration.between(processStart, processEnd).toMillis());// process time in millisecond
			processTimeInfo.setProcessTimeNanos(
					Duration.between(processStart, processEnd).toNanos());// process time in nanosecond
	
			processTime.add(processTimeInfo);
			results.add(resultTemp);
		}
		
		writeJsonFeedForTime(processTime);
		
		model.addAttribute("imageInfo", results);
		model.addAttribute("imageProcessTime",processTime);
		
		return "showResult";
		
	}
	
	private void writeJsonFeedForTime(List<ProcessTimeMachine> processTimeList){
		try(Writer writer = new FileWriter("timeList.json")) {
			Gson jsonParser = new Gson();
			jsonParser.toJson(processTimeList,writer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
