package org.ethan.io.myIO.bio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static void main(String[] args) throws Exception {
		ServerSocket server = new ServerSocket(6666);
		
		//等待客户端请求
		Socket socket = server.accept();
		
		BufferedReader serverInput = new BufferedReader(new InputStreamReader(System.in));
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		PrintWriter writer = new PrintWriter(socket.getOutputStream());
		
		String request = reader.readLine();
		System.out.print("Get request : "+ request);
		
		String response = serverInput.readLine();
		
		while(!response.equals("exit") && !request.equals(request)) {
			writer.write(response);
			
			writer.flush();
			
			request = reader.readLine();
			System.out.print("Get request : "+ request);
		}
		
		System.out.println("bye!");
		writer.close();
		reader.close();
		socket.close();
		server.close();
	}
}
