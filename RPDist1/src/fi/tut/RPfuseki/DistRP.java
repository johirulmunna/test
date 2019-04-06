package fi.tut.RPfuseki;

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
 * Servlet implementation class DistRP
 */
public class DistRP extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static String IPown = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DistRP() {
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
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String IPmain = request.getParameter("IPmain");
		IPown = request.getParameter("IPown");
		System.out.println("Ipown value" + IPown);
		String Passcode = request.getParameter("code");

		if (Passcode != null) {

			try {

				URIBuilder uriBuilder = new URIBuilder();
				uriBuilder.setScheme("http").setHost(IPmain).setPort(8080).setPath("/RPDiv/Test1")
						.addParameter("Passcode", Passcode).addParameter("WSIP", IPmain).addParameter("ipself", IPown);
				URI uri = uriBuilder.build();
				System.out.println(uri);
				HttpGet getMethod = new HttpGet(uri);
				HttpClient httpclient = new DefaultHttpClient();

				try {
					HttpResponse respons = httpclient.execute(getMethod);
					int code = respons.getStatusLine().getStatusCode();
					System.out.println(code);
					System.out.println("DistRP class OK ");
					ClientUpdate.CUpdate(IPown);

				} catch (IOException e) {
					// handle this IOException properly in the future
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("no code typed");
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
