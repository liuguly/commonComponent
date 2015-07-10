package com.jingchen.im.listeneradapter;

import java.io.File;
import java.io.IOException;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;

public class SimpleListenerAdapter implements ListenerAdapter{

	@Override
	public void connected(XMPPConnection connection) {
		// TODO Auto-generated method stub
	}

	@Override
	public void authenticated(XMPPConnection connection, boolean resumed) {
		// TODO Auto-generated method stub
		System.out.println("当前用户："+connection.getUser());
	}

	@Override
	public void connectionClosed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connectionClosedOnError(Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reconnectionSuccessful() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reconnectingIn(int seconds) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reconnectionFailed(Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processPacket(Stanza packet) throws NotConnectedException {
		Message message = (Message)packet;
		
		System.out.println("接收者："+message.getTo());
		System.out.println(message.getBody());
		
	}

	@Override
	public void fileTransferRequest(FileTransferRequest request) {
		
		System.out.println(request.getMimeType());
		
		if(request.getFileSize()>0) {
			// Accept it
			IncomingFileTransfer transfer = request.accept();
			try {
				transfer.recieveFile(new File("E:\\"+request.getFileName()));
			} catch (SmackException | IOException e) {
				e.printStackTrace();
			}
		} else {
			// Reject it
			try {
				request.reject();
			} catch (NotConnectedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
