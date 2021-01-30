package com.gameofthree.socket;

import static com.gameofthree.constants.GameConstants.GAME_OVER;
import static com.gameofthree.constants.GameConstants.GAME_STARTED;
import static com.gameofthree.constants.GameConstants.MAX_NUMBER_OF_PLAYERS_ALLOWED;
import static com.gameofthree.constants.GameConstants.PLAYER_DISCONNECTED;
import static com.gameofthree.constants.GameConstants.WELCOME_MESSAGE;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import com.gameofthree.model.Message;

@Component
@ServerEndpoint(value = "/chat/{username}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class SocketEndpoint {

	private Session session;
	private static final Set<SocketEndpoint> chatEndpoints = new HashSet<>();
	private static HashMap<String, String> users = new HashMap<>();
	private static final Random rand = new Random();
	private static final int UPPER_BOUND = 1000;

	@OnOpen
	public void onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {
		synchronized (chatEndpoints) {
			if (!chatEndpoints.contains(this)) {
				if (chatEndpoints.size() == MAX_NUMBER_OF_PLAYERS_ALLOWED) {
					session.close();
				} else {
					this.session = session;
					chatEndpoints.add(this);
					users.put(session.getId(), username);
					verifyPlayersCountAndStartGame();
				}
			}
		}
	}

	@OnMessage
	public void onMessage(Session session, Message message) throws IOException, EncodeException {
		if (GAME_OVER.equals(message.getContent())) {
			synchronized (chatEndpoints) {
				chatEndpoints.remove(this);
				broadcast(GAME_OVER);
			}
		} else {
			synchronized (chatEndpoints) {
				for (SocketEndpoint point : chatEndpoints) {
					if (!point.equals(this)) {
						sendMessage(point, message);
					}
				}
			}
		}
	}

	@OnClose
	public void onClose(Session session) throws IOException, EncodeException {
		synchronized (chatEndpoints) {
			if (chatEndpoints.contains(this)) {
				chatEndpoints.remove(this);
				broadcast(PLAYER_DISCONNECTED);
			}
		}
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		// Do error handling here
	}

	private static void broadcast(String content) {
		Message message = getMessageFromContent(content);
		chatEndpoints.forEach(endpoint -> {
			synchronized (endpoint) {
				try {
					endpoint.session.getBasicRemote().sendObject(message);
				} catch (IOException | EncodeException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void verifyPlayersCountAndStartGame() {
		if (chatEndpoints.size() == MAX_NUMBER_OF_PLAYERS_ALLOWED) {
			broadcast(GAME_STARTED);
			sendMessage(this, new Message(generateRandomNumber(), 0));
		} else {
			sendMessage(this, getMessageFromContent(WELCOME_MESSAGE));
		}
	}

	private static int generateRandomNumber() {
		return rand.nextInt(UPPER_BOUND) + 3;
	}

	public void sendMessage(SocketEndpoint socketEndpoint, Message message) {
		try {
			socketEndpoint.session.getBasicRemote().sendObject(message);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (EncodeException e) {
			e.printStackTrace();
		}
	}

	private static Message getMessageFromContent(String content) {
		return new Message(true, content);
	}
}
