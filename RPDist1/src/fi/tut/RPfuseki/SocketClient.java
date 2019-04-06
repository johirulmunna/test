package fi.tut.RPfuseki;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

@ClientEndpoint
public class SocketClient {

	String DivIP = DistRP.IPown;
	// String role = null;
	String ary = null;
	Session userSession = null;
	private MessageHandler messageHandler;

	public static void ChatClientEndpoint(String SocketIP) {
		try {
			URI endpointURI = URI.create(SocketIP);
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			container.connectToServer(SocketClient.class, endpointURI);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Callback hook for Connection open events.
	 * 
	 * @param userSession
	 *            the userSession which is opened.
	 */
	@OnOpen
	public void onOpen(Session userSession) {
		this.userSession = userSession;
	}

	/**
	 * Callback hook for Connection close events.
	 * 
	 * @param userSession
	 *            the userSession which is getting closed.
	 * @param reason
	 *            the reason for connection close
	 */
	@OnClose
	public void onClose(Session userSession, CloseReason reason) {
		this.userSession = null;
	}

	/**
	 * Callback hook for Message Events. This method will be invoked when a
	 * client send a message.
	 * 
	 * @param message
	 *            The text message
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws ParseException
	 * @throws XPathExpressionException 
	 */
	@OnMessage
	public void onMessage(String message)
			throws ParserConfigurationException, SAXException, IOException, ParseException, XPathExpressionException {
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = (JSONObject) parser.parse(message);

		System.out.println(DivIP);

		String imessage = (String) jsonObject.get("tag");
		System.out.println(imessage);
		String jmessage = (String) jsonObject.get("message");
		System.out.println(jmessage);
		String ClientUpQ = (String) jsonObject.get("UpdateQ");
		System.out.println(ClientUpQ);
		if (imessage.equals("boss")) {
			System.out.println("Received msg: " + jmessage);
			ary = QueryTest.ChkQuery(jmessage);

			System.out.println("Value of ary in class SocketCleint = " + ary);

			String type = null;
			if (ary.contains("NSS")) {
				type = "NSS";
			}
			// System.out.println("Value in class SocketCleint = "+role);

			JSONObject jsonObject1 = new JSONObject();
			jsonObject1.put("UpdateAr", ary);
			jsonObject1.put("tag", "client");
			jsonObject1.put("address", DivIP);
			jsonObject1.put("orgmsg", jmessage);
			jsonObject1.put("SupType", type);

			System.out.println("jsonObejct1 value:" + jsonObject1);
			String irole = jsonObject1.toString();
			System.out.println("Irole value to be sent to Server:" + irole);
			sendMessage(irole);
		}
		// add else if condotion (for some certain tag) for updating all the
		// answers to another KB
		else if (imessage.equals("ClientUp")) {
			System.out.println("client query initiated");
			ClientUpdate.CUpdate(ClientUpQ);

		} else { // nothing happens
			System.out.println("Not a message for client");
		}
	}

	/**
	 * register message handler
	 * 
	 * @param message
	 */
	public void addMessageHandler(MessageHandler msgHandler) {
		this.messageHandler = msgHandler;
	}

	/**
	 * Send a message.
	 * 
	 * @param user
	 * @param message
	 */
	public void sendMessage(String role) {
		this.userSession.getAsyncRemote().sendText(role);
	}

	/**
	 * Message handler.
	 * 
	 * 
	 */
	public static interface MessageHandler {
		public void handleMessage(String message);
	}
}
