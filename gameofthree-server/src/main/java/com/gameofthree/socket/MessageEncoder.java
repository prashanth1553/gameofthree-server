package com.gameofthree.socket;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.gameofthree.model.Message;
import com.google.gson.Gson;

public class MessageEncoder implements Encoder.Text<Message> {

	private Gson gson = new Gson();

	@Override
	public String encode(Message message) throws EncodeException {
		String json = gson.toJson(message);
		return json;
	}

	@Override
	public void init(EndpointConfig endpointConfig) {
		// Custom initialization logic
	}

	@Override
	public void destroy() {
		// Close resources
	}
}