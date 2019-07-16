package org.ethan.io.myIO.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("127.0.0.1",6666);
		
		BufferedReader clientInput = new BufferedReader(new InputStreamReader(System.in));
		
		PrintWriter writer = new PrintWriter(socket.getOutputStream());
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		String input = clientInput.readLine();
		
		while(!input.equals("exit")) {
			//将客户端输入的数据发送至服务端
			writer.println(input);
			
			writer.flush();
			
			System.out.println("Response from server :" + reader.readLine());
		}
		
		System.out.println("bye!");
		writer.close();
		reader.close();
		socket.close();
	}
}
