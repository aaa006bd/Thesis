package com.example.services;


import java.util.Base64;

import org.opencv.core.Mat;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class JsonPerserService implements JsonPersingService{

	@Override
	public String matToJson(Mat img) {
		JsonObject jsonObj = new JsonObject();
		
		if (img.isContinuous()) {
			
			int cols = img.cols();
			int rows = img.rows();
			int elemSize = (int) img.elemSize();
			
			float[] data = new float[cols*rows*elemSize];
			
			img.get(0, 0, data);//getting the data into float array
			
			jsonObj.addProperty("rows", img.rows());
			jsonObj.addProperty("cols", img.cols());
			jsonObj.addProperty("type", img.type());
			
			//cannot save bool data in json so encode the
			//byte array as base64 string
			
			//String encodedData = new String(Base64.getEncoder().encode(data));
			
			Gson parser = new Gson();
			String encodedData = parser.toJson(data);
			
			jsonObj.addProperty("data", encodedData);
						
			String json = new Gson().toJson(jsonObj);
			
			return json;
			
		} else {
			return "{}";
		}
		
		
	}

	@Override
	public Mat jsonToMat(String json) {
		
		JsonParser parser = new JsonParser();
		JsonObject jsonObj = parser.parse(json).getAsJsonObject();
		
		int rows = jsonObj.get("rows").getAsInt();
		int cols = jsonObj.get("cols").getAsInt();
		int type = jsonObj.get("type").getAsInt();
		
		String dataString = jsonObj.get("data").getAsString();
		float[] dataArray =  new Gson().fromJson(dataString, float[].class);
		
		Mat mat = new Mat(rows, cols, type);
		mat.put(0,0,dataArray);
		
		return mat;
	}

}
