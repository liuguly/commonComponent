package com.jingchen.im;

import java.io.IOException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import com.jingchen.im.listeneradapter.SimpleListenerAdapter;


public class ImServiceTest {
	
	
	public static void main(String[] args) throws SmackException, IOException, XMPPException, InterruptedException {
		
		ImService4Android imService4Android = ImServiceFactory.getInstance();
		
		imService4Android.addListener(new SimpleListenerAdapter());
		
		imService4Android.loginImServer("13468602251", "123456", "jingchen");
		
		System.out.println("13468602251 登录成功！");
		
		ImService4Android imService4Android1 = ImServiceFactory.getInstance();
		
		imService4Android1.addListener(new SimpleListenerAdapter());
		
		imService4Android1.loginImServer("admin", "123", "jingchen");
		
		System.out.println("admin 登录成功！");
		
		Thread.sleep(1000);
		
		Message message = new Message();
		
		message.setBody("Hello world!");
		message.setTo("admin@jingchen/Smack");
		
		try {
			imService4Android.sendText(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		Thread.sleep(1000000);
	}
	
}
