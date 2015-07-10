package com.jingchen.im.listeneradapter;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;

public interface ListenerAdapter extends ConnectionListener,StanzaListener,FileTransferListener{

}
