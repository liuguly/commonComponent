package com.jingchen.im;

public class ImServiceFactory {
	
	
	public static ImService4Android getInstance(){
		return new ImService4AndroidImpl();
	}
	
}
