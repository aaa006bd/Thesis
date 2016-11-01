package com.example.services;

import org.opencv.core.Mat;

public interface JsonPersingService {
  
	public String matToJson(Mat img);
	
	public Mat jsonToMat(String Json);
  
}
