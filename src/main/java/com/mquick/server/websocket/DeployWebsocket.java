package com.mquick.server.websocket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;
import org.eclipse.jetty.websocket.WebSocketClientFactory;

public class DeployWebsocket {

	public DeployWebsocket() throws Exception {
		String destUri = "ws://www.risetek.com:8080/express/talk/terminal";
//		String destUri = "ws://127.0.0.1:8080/talk/terminal";
		WebSocketClientFactory factory = new WebSocketClientFactory();
		factory.start();
		
		WebSocketClient client = factory.newWebSocketClient();
		
		WebSocket.Connection connection = client.open(new URI(destUri),
				new WebSocket.OnTextMessage() {

					@Override
					public void onOpen(Connection connection) {
						try {
							connection.sendMessage("Hello Boss");
							
							InetAddress addr = InetAddress.getLocalHost();

						    // Get IP Address
							connection.sendMessage("My address is:"+addr.getHostAddress());

						    // Get hostname
							connection.sendMessage("My name is:"+addr.getHostName());
							
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onClose(int closeCode, String message) {
						System.out.println("closed ?");

						// reconnect, we should user a monitor to do this.
				    	try {
							new DeployWebsocket();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}

					@Override
					public void onMessage(String data) {
						
						System.out.println("Dashboard tell us:" + data);
						
					}
				}).get(5, TimeUnit.SECONDS);

	}
}
