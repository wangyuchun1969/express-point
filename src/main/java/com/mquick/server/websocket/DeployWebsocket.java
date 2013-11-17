package com.mquick.server.websocket;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;
import org.eclipse.jetty.websocket.WebSocketClientFactory;

public class DeployWebsocket {

	public DeployWebsocket() throws Exception {
		String destUri = "ws://www.risetek.com:8080/express/talk/terminal";
		WebSocketClientFactory factory = new WebSocketClientFactory();
		factory.start();
		WebSocketClient client = factory.newWebSocketClient();
		
		WebSocket.Connection connection = client.open(new URI(destUri),
				new WebSocket.OnTextMessage() {

					@Override
					public void onOpen(Connection connection) {
					}

					@Override
					public void onClose(int closeCode, String message) {
					}

					@Override
					public void onMessage(String data) {
					}
				}).get(5, TimeUnit.SECONDS);

		connection.sendMessage("Hello Boss");
	}
}
