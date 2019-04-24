package test;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server 
{
	
	public static void main (String[] args) throws IOException {
		Socket clientSocket = null;
		ServerSocket serverSocket = null;
		try
		{
		serverSocket = new ServerSocket(4000); 
		System.out.println("server started....");
		clientSocket = serverSocket.accept();
		DataOutputStream dout = new DataOutputStream(clientSocket.getOutputStream());
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String msgOut = "";
		
		msgOut = br.readLine();
		dout.writeUTF(msgOut);
		
		}
		catch(Exception e){} 
		//read & display the message
		//BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputS­tream()));
		Scanner in1 = new Scanner(clientSocket.getInputStream()); //IMP line_ scanning from the client socket
		String mes; 
		while(true){
					if (in1.hasNext())
						{
							mes=in1.nextLine();
							System.out.println("Client message :"+mes);
						}
					}
	}

}
