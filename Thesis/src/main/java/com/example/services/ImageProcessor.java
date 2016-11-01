package com.example.services;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;

@Service
public class ImageProcessor implements ImageProcessingService,ImageComparingService{
	
	public Mat calculateHistogram( Mat src){
		Mat dest = new Mat(src.height(),src.width(),CvType.CV_8U);
		Imgproc.cvtColor(src, dest, Imgproc.COLOR_BGR2HSV);
		List<Mat>bgr_planes = new ArrayList<>();
		Core.split(dest, bgr_planes);
		
		MatOfFloat ranges = new MatOfFloat(0,180,0,256);
		MatOfInt histSize = new MatOfInt(50,60);
		MatOfInt channels = new MatOfInt(0,1);
		
		MatOfByte calculatedHistogram = new MatOfByte();
		
		Imgproc.calcHist(bgr_planes, channels, new MatOfByte(), calculatedHistogram, histSize, ranges,false);
		
		return calculatedHistogram;
	}

	@Override
	public double compareHistogram(Mat imageOne, Mat imageTwo) {
		//this one compares the similarity using histogram
		double similarity = Imgproc.compareHist(imageOne, imageTwo, Imgproc.CV_COMP_CORREL);
		
		return similarity;
	}

}
