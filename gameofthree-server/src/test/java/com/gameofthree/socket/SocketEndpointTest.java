package com.gameofthree.socket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import java.io.IOException;
import java.util.Set;

import javax.websocket.EncodeException;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.gameofthree.model.Message;

@RunWith(MockitoJUnitRunner.class)
public class SocketEndpointTest {

	private Session session = Mockito.mock(Session.class);
	@InjectMocks
	private SocketEndpoint socketEndpoint;

	@Test
	public void testOnOpen() throws IOException, EncodeException {
		@SuppressWarnings("unchecked")
		Set<SocketEndpoint> chatEndpoints = (Set<SocketEndpoint>) ReflectionTestUtils.getField(SocketEndpoint.class,
				"chatEndpoints");
		RemoteEndpoint.Basic basic = Mockito.mock(RemoteEndpoint.Basic.class);
		Mockito.when(session.getBasicRemote()).thenReturn(basic);
		doNothing().when(basic).sendObject(any(Message.class));
		socketEndpoint.onOpen(session, "test");
		assertEquals(1, chatEndpoints.size());

		// verify max allowed users
		new SocketEndpoint().onOpen(session, "test");
		assertEquals(2, chatEndpoints.size());

		new SocketEndpoint().onOpen(session, "test");
		assertEquals(2, chatEndpoints.size()); // max allowed user is 2
		Mockito.verify(session).close(); // verify that extra play session is closed
	}
}
