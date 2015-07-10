package com.jingchen.im;

import javax.naming.InitialContext;

import org.jivesoftware.smack.packet.Message;
import org.junit.Before;
import org.junit.Test;

import com.jingchen.im.listeneradapter.SimpleListenerAdapter;

public class MsgTest {
	
	
	private ImService4Android imService4Android;
	private ImService4Android imService4Android1;
	
	@Before
	public void init() throws InterruptedException{
		imService4Android = ImServiceFactory.getInstance();
		
		imService4Android.addListener(new SimpleListenerAdapter());
		
		imService4Android.loginImServer("13468602251", "123456", "jingchen");
		
		System.out.println("13468602251 登录成功！");
		
		imService4Android1 = ImServiceFactory.getInstance();
		
		imService4Android1.addListener(new SimpleListenerAdapter());
		
		imService4Android1.loginImServer("admin", "123", "jingchen");
		
		System.out.println("admin 登录成功！");
		Thread.sleep(500);
	}
	
	@Test
	public void sendText() throws InterruptedException{
		Message message = new Message();
		
		message.setBody("Hello world!");
		message.setTo("admin@jingchen/Smack");
		
		try {
			imService4Android.sendText(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Thread.sleep(1000);
	}
	
	@Test
	public void sendFile() throws InterruptedException{
		try {
			imService4Android.sendVoice("admin@jingchen/Smack", "D:\\smack_4_1_2.zip", "一个文本");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Thread.sleep(2000);
	}
	
}
