package cs455.overlay.transport;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import cs455.overlay.node.MessagingNode;

public class TCPServerThread implements Runnable {
	
	private int port;
	private ServerSocket serverSocket;
	private MessagingNode node;
	
	public TCPServerThread(int port, MessagingNode node) throws IOException {
		this.node = node;
		try {
			this.serverSocket = new ServerSocket(port);
			this.port = this.serverSocket.getLocalPort();
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}
	
	public void run() {
		System.out.println("Starting thread for node: " + node.toString() + ". Listening on port: " + getPort());
		
		while(serverSocket != null) {
			try {
				Socket socket = serverSocket.accept();
			} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
			break;
			}
		}
		
		System.out.println("Ending thread for node: " + node.toString() + ". Closing on port: " + getPort());
	}
	
	private int getPort() {
		return this.port;
	}
	
	public ServerSocket getServerSocket() {
		return this.serverSocket;
	}
}
