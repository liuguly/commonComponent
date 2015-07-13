package com.jingchen.im;

import javax.naming.InitialContext;

import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.pubsub.AccessModel;
import org.jivesoftware.smackx.pubsub.ConfigureForm;
import org.jivesoftware.smackx.pubsub.Item;
import org.jivesoftware.smackx.pubsub.ItemPublishEvent;
import org.jivesoftware.smackx.pubsub.LeafNode;
import org.jivesoftware.smackx.pubsub.PubSubManager;
import org.jivesoftware.smackx.pubsub.PublishModel;
import org.jivesoftware.smackx.pubsub.listener.ItemEventListener;
import org.jivesoftware.smackx.xdata.packet.DataForm;
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
	
	@Test
	public void pubSubTest() throws NoResponseException, XMPPErrorException, NotConnectedException, InterruptedException{
		PubSubManager mgr = new PubSubManager(imService4Android.getXmpptcpConnection());
		
		LeafNode leaf = mgr.createNode("d1");
		ConfigureForm form = new ConfigureForm(DataForm.Type.submit);
		form.setAccessModel(AccessModel.open);
		form.setDeliverPayloads(false);
		form.setNotifyRetract(true);
		form.setPersistentItems(true);
		form.setPublishModel(PublishModel.open);
		form.setSubscribe(true);
		leaf.sendConfigurationForm(form);
		
		Thread.sleep(1000);
		
		
		PubSubManager mgr1 = new PubSubManager(imService4Android1.getXmpptcpConnection());
		LeafNode leafNode = mgr1.getNode("d1");
		
		leafNode.addItemEventListener(new ItemEventListener<Item>() {
			@Override
			public void handlePublishedItems(ItemPublishEvent<Item> items) {
				System.out.println(items.getItems().size());
				
			}
		});
		leafNode.subscribe("admin@jingchen/Smack");
		
		
		Thread.sleep(1000);
		leaf.send(new Item("123"));
		Thread.sleep(200000);
		
	}
	
}
