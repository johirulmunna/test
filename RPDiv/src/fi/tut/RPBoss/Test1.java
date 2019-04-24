package fi.tut.RPBoss;

import java.io.IOException;
import java.net.URI;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Servlet implementation class Test1
 */

public class Test1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String Passphrase = "RPBoss121";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Test1() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String Passcode = request.getParameter("Passcode"); //receive the passcode from client
		String DeviceIP = request.getParameter("ipself"); //recieve the IP of the client devices
		String BossIP = request.getParameter("WSIP");	//server IP
		String WScall = "ws://" + BossIP + ":8080/RPDiv/socconnect"; 

		System.out.println(Passcode);
		System.out.println(DeviceIP);
		System.out.println(BossIP);
		System.out.println(WScall);

		if (Passcode.equals(Passphrase) == true) {		//check the passcode if it "RPBoss121"

			System.out.println("passcode match");
						try {

				URIBuilder uriBuilder = new URIBuilder();
				uriBuilder.setScheme("http").setHost(DeviceIP).setPort(8080).setPath("/RPDist1/WebsocINIT") 
						.addParameter("SocketIP", WScall);
				System.out.println("URI set");

				URI uri = uriBuilder.build();
				HttpGet getMethod = new HttpGet(uri);
				HttpClient httpclient = new DefaultHttpClient();

				try {
					System.out.println("request for WebSocketINIT class initiated");
					HttpResponse respons = httpclient.execute(getMethod);
					int code = respons.getStatusLine().getStatusCode();
					System.out.println(code);

				} catch (IOException e) {
					// handle this IOException properly in the future
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			System.out.println("passcode missmatch");

		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
