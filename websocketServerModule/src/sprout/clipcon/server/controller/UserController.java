package sprout.clipcon.server.controller;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import lombok.Getter;
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
	private String userName;

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
		this.session = userSession;

		System.out.println("[Server] message received success. type: " + type);

		Message responseMsg = null;
		switch (type) {

		case Message.REQUEST_CREATE_GROUP:
			server = Server.getInstance();			// ���� ��ü �Ҵ�
			group = server.createGroup();	// �ش� �̸����� �׷� ����

			responseMsg = new Message().setType(Message.RESPONSE_CREATE_GROUP);
			userName = group.addUser(session.getId(), this);
			responseMsg.add(Message.NAME, userName);
			responseMsg.add(Message.RESULT, Message.CONFIRM);
			MessageParser.getMessageByGroup(responseMsg, group);// �׷� ������ �����ϴ� Message ��ü ����
			break;

		case Message.REQUEST_JOIN_GROUP:
			server = Server.getInstance();
			group = server.getGroupByPrimaryKey("godoy");	 // �׷�Ű�� �ش��ϴ� ��ü ����
			// group = server.getGroupByPrimaryKey(incomingMessage.get("groupPK")); // �׷�Ű�� �ش��ϴ� ��ü ����
			responseMsg = new Message().setType(Message.RESPONSE_JOIN_GROUP);

			if (group != null) {
				responseMsg.add(Message.RESULT, Message.CONFIRM);
				userName = group.addUser(session.getId(), this);
				responseMsg.add(Message.NAME, userName);
				MessageParser.getMessageByGroup(responseMsg, group);// �׷� ������ �����ϴ� Message ��ü ����
			} else {
				responseMsg.add(Message.RESULT, Message.REJECT);
			}
			break;

		case Message.TEST_DEBUG_MODE:
			server = Server.getInstance();	// ���� ��ü �Ҵ�
			// server.testGroup.addUser(incomingMessage.get(Message.EMAIL), session);

			break;

		default:
			// ������ �������� ���� type�� ��û�� ���� �� �޽����� �״�� ������.
			System.out.println("���ܻ���");
			break;
		}
		System.out.println("===== Ŭ���̾�Ʈ���� ���� �޽��� =====\n" + responseMsg + "\n============================");
		sendMessage(session, responseMsg);					 // ����
	}

	@OnClose
	public void handleClose(Session userSession) {
		System.out.println("����");
		if (userSession == null) {
			System.out.println("������ null");
		}
		// group.getUsers().remove(userSession);
	}

	@OnError
	public void handleError(Throwable t) {
		System.out.println("���� �߻�");
		t.printStackTrace();
	}

	private void sendMessage(Session session, Message message) throws IOException, EncodeException {
		session.getBasicRemote().sendObject(message);
	}

	// for test and debugging
	private String createTmpGroup() {
		Group group = server.createGroup();
		if (group == null) {
			System.out.println("�׷��� ��������� ����");
		}
		return group.getPrimaryKey();
	}

	public static void main(String[] args) {
		Message tmpMessage = new Message().setJson("{\"group pk\":\"godoy\",\"message type\":\"request/join group\"}");
		try {
			new UserController().handleMessage(tmpMessage, null);
		} catch (Exception e) {
			System.out.println("���� ����");
			e.printStackTrace();
		}
	}
}