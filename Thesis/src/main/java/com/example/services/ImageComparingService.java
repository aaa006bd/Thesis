package com.example.services;

import org.opencv.core.Mat;

public interface ImageComparingService {

	  double compareHistogram(Mat imageOne,Mat imageTwo);
	
}
