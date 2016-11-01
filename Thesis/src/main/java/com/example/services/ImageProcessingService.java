package com.example.services;

import org.opencv.core.Mat;

public interface ImageProcessingService {
 public Mat calculateHistogram(Mat src);
}
