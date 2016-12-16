package com.example.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.models.ImageFeatures;
import com.example.models.ImageRepository;
import com.example.services.ImageProcessor;
import com.example.services.JsonPersingService;

@Controller
public class BulkUploaderController {

	@Value("${project.bulkImageUploadPath}")
	private String pathString;
	
	
	@Autowired
	private ImageProcessor imageProcessorObj;
	
	@Autowired
	private JsonPersingService jsonParser;
	
	@Autowired
	private ImageRepository repository;
	
	

	@GetMapping("/bulk/read")
	public String getBulkImageNames() {
		Path directoryPath = Paths.get(pathString);

		List<File> allFilesOfFolder = new ArrayList<>();
		Comparator<File>sortByName = (File f1,File f2)->f1.getName().compareTo(f2.getName());
		int counter = 0;

		try {
			allFilesOfFolder = Files.walk(directoryPath)
					.filter(Files::isRegularFile).map(Path::toFile)
					.collect(Collectors.toList());
			Collections.sort(allFilesOfFolder,sortByName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (File file : allFilesOfFolder) {
			ImageFeatures imgFeatureObj = new ImageFeatures();
			try {
				byte[] imageInBytes = Files.readAllBytes(file.toPath());
				imgFeatureObj.setCaption(file.getName());
				imgFeatureObj.setImageRaw(imageInBytes);			
				insertImageFeature(imgFeatureObj, imageInBytes);
				
				repository.save(imgFeatureObj);
				
				System.out.println(file.getName()+" uploaded successfully");
				System.out.println("no of images uploaded : "+ (++counter));
			} catch (IOException e) {
				System.err.println(file.getName()+ " could not be uploaded");
				e.printStackTrace();
			}
			
		}

		return "bulkSuccess";
	}
	
private void insertImageFeature(ImageFeatures imgFeatureObj, byte[] imageInBytes) {
		
		Mat imageMatrix = Imgcodecs.imdecode(new MatOfByte(imageInBytes), 
				 Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
		Mat imageFeatures = imageProcessorObj.calculateHistogram(imageMatrix);
		
		String encodedImageFeatures = jsonParser.matToJson(imageFeatures);
		
		 imgFeatureObj.setImageFeatures(encodedImageFeatures);
	}
}
