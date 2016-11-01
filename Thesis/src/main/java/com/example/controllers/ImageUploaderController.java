package com.example.controllers;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.models.ImageFeatures;
import com.example.models.ImageRepository;
import com.example.services.ImageProcessor;
import com.example.services.JsonPersingService;

@Controller
public class ImageUploaderController {

	@Autowired
	private ImageRepository repository;
	
	@Autowired
	private ImageProcessor imageProcessorObj;
	
	@Autowired
	private JsonPersingService jsonParser;
	
	@RequestMapping(value="/save",method = RequestMethod.GET)
	public String getSaveForm(){
		return "saveForm";
	}
	
	
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public String save(
			@ModelAttribute("imageFeatures") ImageFeatures imgFeatureObj,
			@RequestParam("imageFile") MultipartFile imageFileObj,
			RedirectAttributes redirectAttribs
			){
		showFileParamsInConsole(imageFileObj);
		
		try {
			byte[] imageInBytes = imageFileObj.getBytes();//after so many research :v
			imgFeatureObj.setCaption(imageFileObj.getOriginalFilename());
			imgFeatureObj.setImageRaw(imageInBytes);			
			insertImageFeature(imgFeatureObj, imageInBytes);//extracts feature from image and puts it in the field
			
			redirectAttribs.addFlashAttribute("message", 
					"your file"+imageFileObj.getOriginalFilename()+" was uploaded successfully");
			
			repository.save(imgFeatureObj);
			
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttribs.addFlashAttribute("message", 
					"your file"+imageFileObj.getOriginalFilename()+" could not be uploaded");
		}						
		return "redirect:/save";
	}


	private void showFileParamsInConsole(MultipartFile imageFileObj) {
		System.out.println("file name: "+imageFileObj.getName());
		System.out.println("original file name: "+imageFileObj.getOriginalFilename());
		System.out.println("file size: "+imageFileObj.getSize());
		System.out.println("file type:"+imageFileObj.getContentType());
	}

	@GetMapping("/success")
	public String successpage(){
		return "redirect:failure";
	}

	@GetMapping("/show/{imageId}")
	public String showImageDatas(@PathVariable("imageId")Long imgId,
			Model model){
		
		ImageFeatures expectedImage = repository.findById(imgId);
		Mat featuresOfExpectedImage = jsonParser.jsonToMat(expectedImage.getImageFeatures());
		
		int height = featuresOfExpectedImage.height();
		int width = featuresOfExpectedImage.width();
		
		model.addAttribute("imageFeatures", expectedImage);
		model.addAttribute("height", height);
		model.addAttribute("width", width);
		
		System.err.println("hello show");
		System.err.println(height);
		System.err.println(width);
		
		return "showImageData";
	}
	
	private void insertImageFeature(ImageFeatures imgFeatureObj, byte[] imageInBytes) {
		
		Mat imageMatrix = Imgcodecs.imdecode(new MatOfByte(imageInBytes), 
				 Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
		Mat imageFeatures = imageProcessorObj.calculateHistogram(imageMatrix);
		
		String encodedImageFeatures = jsonParser.matToJson(imageFeatures);
		
		 imgFeatureObj.setImageFeatures(encodedImageFeatures);
	}
	
}
