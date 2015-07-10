package com.jingchen.im;

import org.jivesoftware.smack.packet.Message;

import com.jingchen.im.listeneradapter.ListenerAdapter;


public interface ImService4Android {
	
	public void loginImServer(String userName,String password,String serviceName)throws LoginServiceException;
	
	public void sendText(Message message) throws Exception;
	
	public void sendImage(String receiverJID,String imagePath,String description) throws Exception;
	
	public void sendVoice(String receiverJID,String voicePath,String description) throws Exception;
	
	public void addListener(ListenerAdapter listenerAdapter) ;
}
