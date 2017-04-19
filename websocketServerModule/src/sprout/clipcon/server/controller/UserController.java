package sprout.clipcon.server.controller;

import java.io.IOException;
import java.util.Map;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import lombok.Getter;
import sprout.clipcon.server.model.AddressBook;
import sprout.clipcon.server.model.Group;
import sprout.clipcon.server.model.User;
import sprout.clipcon.server.model.message.ChatMessageDecoder;
import sprout.clipcon.server.model.message.ChatMessageEncoder;
import sprout.clipcon.server.model.message.Message;

@Getter
@ServerEndpoint(value = "/ServerEndpoint", encoders = { ChatMessageEncoder.class }, decoders = { ChatMessageDecoder.class })
public class UserController {
	private Server server;	// ����
	private Group group;	// ���� ���� �׷�
	private User user;		// user ����
	private Session session;

	@OnOpen
	public void handleOpen(Session userSession) {
		this.session = userSession;
	}

	@OnMessage
	public void handleMessage(Message incomingMessage, Session userSession) throws IOException, EncodeException {
		String type = incomingMessage.getType();

		if (session != userSession) { // for test
			System.out.println("�̷���Ȳ�� �߻��� �� ������");
			return;
		}

		System.out.println("[Server] message received success. type: " + type);

		switch (type) {
		case "sign in":
			String email = incomingMessage.get(Message.EMAIL);
			String name = incomingMessage.get("password");
			String result = MemberAdministrator.getUserAuthentication(email, name);
			if (result.equals(MemberAdministrator.CONFIRM)) { // ����: ����
				System.out.println("if in");
				server = Server.getInstance();	// ���� ��ü �Ҵ�
				user = MemberAdministrator.getUserByEmail("����� �̸���");
				server.enterUserInLobby(user);			// ������ ����� ����
				
				User tmpUser = testCreateTmpUser();
				Message sendMsg = new Message().setType(Message.RESPONSE_SIGN_IN);
				MessageParser.AddUserInfoToMessage(sendMsg, tmpUser);
				sendMsg.add("result", result);
				System.out.println("delf: " + sendMsg);
				session.getBasicRemote().sendObject(sendMsg);
				
			} else { // ����: �ź�
				System.out.println("�źε�");
			}

			break;		

		case Message.REQUEST_SIGN_UP:
			break;

		case Message.REQUEST_CREATE_GROUP:
			group = server.createGroup("�׷� �̸�");	// �ش� �̸����� �׷� ����
			group.addUser(user.getEmail(), this);		// �� �׷쿡 ����
			break;

		case Message.REQUEST_JOIN_GROUP:
			group = server.getGroupByPrimaryKey("�׷����Ű");
			group.addUser(user.getEmail(), this);
			break;

		case Message.REQUEST_GET_ADDRESSBOOK:
			break;

		case Message.TEST_DEBUG_MODE:
			server = Server.getInstance();	// ���� ��ü �Ҵ�
			// server.testGroup.addUser(incomingMessage.get(Message.EMAIL), session);

			break;

		default:
			// ������ �������� ���� type�� ��û�� ���� �� �޽����� �״�� ������.
			System.out.println("���ܻ���");
			userSession.getBasicRemote().sendObject(incomingMessage);
			break;
		}
	}

	@OnClose
	public void handleClose(Session userSession) {
		if (userSession == null) {
			System.out.println("null");
		}
		// group.getUsers().remove(userSession);
	}

	@OnError
	public void handleError(Throwable t) {
		System.out.println("����");
	}

	private User testCreateTmpUser() {
		User user = new User("test", "12");
		
		AddressBook ab = user.getAddressBook();
		Map<String, String> users = ab.getUsers();
		users.put("em1", "n1");
		users.put("em2", "n2");
		users.put("em3", "n3");
		users.put("em5", "n5");
		
		return user;
	}
}