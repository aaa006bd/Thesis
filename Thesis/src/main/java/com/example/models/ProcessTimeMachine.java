package com.example.models;

public class ProcessTimeMachine {
	
	public long id;
	public String imageCaption;
	public long processTimeMillis;
	public long processTimeNanos;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getImageCaption() {
		return imageCaption;
	}
	public void setImageCaption(String imageCaption) {
		this.imageCaption = imageCaption;
	}
	public long getProcessTimeMillis() {
		return processTimeMillis;
	}
	public void setProcessTimeMillis(long processTimeMillis) {
		this.processTimeMillis = processTimeMillis;
	}
	public long getProcessTimeNanos() {
		return processTimeNanos;
	}
	public void setProcessTimeNanos(long processTimeNanos) {
		this.processTimeNanos = processTimeNanos;
	}
	
	

}
