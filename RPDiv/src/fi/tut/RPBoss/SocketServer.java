package fi.tut.RPBoss;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.xml.parsers.ParserConfigurationException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

@ServerEndpoint("/socconnect")
public class SocketServer {
	/**
	 * @OnOpen allows us to intercept the creation of a new session. The session
	 *         class allows us to send data to the user. In the method onOpen,
	 *         we'll let the user know that the handshake was successful.
	 */
	private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());
	private static List<String> ary = new ArrayList<String>();
	List<String> aryST1 = new ArrayList<String>();

	List<String> aryST7 = new ArrayList<String>();
	List<String> Ansary = new ArrayList<String>();
	Boolean mun = false;
	private static int count = 0;

	@OnOpen
	public void onOpen(Session session) {
		System.out.println(session.getId() + " has opened a connection");
		clients.add(session);

	}

	/**
	 * When a user sends a message to the server, this method will intercept the
	 * message and allow us to react to it. For now the message is read as a
	 * String.
	 * 
	 * @throws IOException
	 * @throws ParseException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	@SuppressWarnings("unchecked")
	@OnMessage
	public void onMessage(String message, Session session)
			throws IOException, ParseException, ParserConfigurationException, SAXException {
		// public void onMessage(String message, Session session) throws
		// IOException{

		// count++;

		System.out.println("Message from " + session.getId() + ": " + message);

		String mmsg = session.getId();
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = (JSONObject) parser.parse(message);
		String tag = (String) jsonObject.get("tag");
		String Suptype = (String) jsonObject.get("SupType");
		String OrgQry = (String) jsonObject.get("orgmsg");

		String RPIP = (String) jsonObject.get("address");

		jsonObject.put("session", mmsg);
		synchronized (clients) {

			if (tag.equals("client")) {
				String UpdateQSTR = (String) jsonObject.get("UpdateAr");
				if ((Suptype != "NSS") || (!(UpdateQSTR.contains("NSS"))) || (!(UpdateQSTR.contains("NSS.")))) {

					int tempCount = count;
					// ary[tempCount]= UpdateQSTR;
					ary.add(UpdateQSTR);
					System.out.println(count + " ary value " + ary.get(tempCount));
					mun = FuskUpdateRes.update(UpdateQSTR);
					System.out.println(mun);
					count++;
					System.out.println("next client Response numbers: " + count);
				} else {
					System.out.println("client: " + session + " provides No Support");
					count++;
					System.out.println("next client Response numbers: " + count);
				}
			} else if (tag.equals("boss")) {
				String MSG = jsonObject.toString();
				for (Session client : clients) {
					client.getBasicRemote().sendText(MSG);
					System.out.println("query sent from Socketserver");
				}
			}

			if (!(count < clients.size() - 1)) {

				System.out.println("OrgQry in SocketServer onMessage" + OrgQry);
				Ansary = FinalQry.finalq(OrgQry);
				System.out.println("Ansary value in socket server: " + Ansary);
				JSONObject jsonob1 = new JSONObject();
				jsonob1.put("tag", "finalAns");
				jsonob1.put("answer", Ansary);
				jsonob1.put("address", RPIP);
				String MSG1 = jsonob1.toString();
				for (Session C : clients) {
					if (!C.equals(session)) {
						C.getBasicRemote().sendText(MSG1);
					}
				}
				for (int a = 0; a < ary.size(); a++) {
					String DelQ = ary.get(a);
					if (!(DelQ == null)) {
						DeleteUpdate.DeleteAll(DelQ);
					} else {
						continue;
					}
				}

				count = 0;
				ary.clear();
			}
		}
	}

	// try {
	// session.getBasicRemote().sendText(message);
	// } catch (IOException ex) {
	// ex.printStackTrace();
	// }

	/**
	 * The user closes the connection.
	 * 
	 * Note: you can't send messages to the client from this method
	 */
	@OnClose
	public void onClose(Session session) {
		System.out.println("Session " + session.getId() + " has ended");
		clients.remove(session);
	}
}
