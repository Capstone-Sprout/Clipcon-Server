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
import sprout.clipcon.server.model.Contents;
import sprout.clipcon.server.model.Group;
import sprout.clipcon.server.model.message.MessageDecoder;
import sprout.clipcon.server.model.message.MessageEncoder;
import sprout.clipcon.server.model.message.Message;
import sprout.clipcon.server.model.message.MessageParser;

@Getter
@ServerEndpoint(value = "/ServerEndpoint", encoders = { MessageEncoder.class }, decoders = { MessageDecoder.class })
public class UserController {
	private Server server;	// ����
	private Group group;	// ���� ���� �׷�
	// private User user; // user ����
	private Session session;
	private String userName;

	@OnOpen
	public void handleOpen(Session userSession) {
		this.session = userSession;
	}

	@OnMessage
	public void handleMessage(Message incomingMessage, Session userSession) throws IOException, EncodeException {
		String type = incomingMessage.getType(); // ���� �޽����� Ÿ�� ����

		if (session != userSession) { // for test
			System.out.println("�̷���Ȳ�� �߻��� �� ������");
			return;
		}
		this.session = userSession; // ���� assign

		System.out.println("[Server] message received success. type: " + type); // �޽��� Ÿ�� Ȯ��

		Message responseMsg = null; // Ŭ���̾�Ʈ���� ���� �޽��� �ʱ�ȭ

		switch (type) {

			case Message.REQUEST_CREATE_GROUP: /* ��û Ÿ��: �׷� ���� */

			server = Server.getInstance();		// "����"�� instance �߰�
			group = server.createGroup();			// ������ �׷��� �߰��ϰ� "�� ��ü�� �Ҽӵ� �׷�"�� instance �߰�
			userName = group.addUser(session.getId(), this); 								// �׷쿡 �ڽ��� �߰�, ������� �̸��� �޾ƿ� / XXX ���� �ʿ�

			responseMsg = new Message().setType(Message.RESPONSE_CREATE_GROUP); 	// ���� �޽��� ����, ���� Ÿ���� "�׷� ���� ��û�� ���� ����"
			responseMsg.add(Message.RESULT, Message.CONFIRM);							// ���� �޽����� ���� �߰�: ���� ���
			responseMsg.add(Message.NAME, userName);											// ���� �޽����� ���� �߰�: ����� �̸�
			MessageParser.addMessageToGroup(responseMsg, group);							// ���� �޽����� ���� �߰�: �׷� ����

				break;

			case Message.REQUEST_JOIN_GROUP: /* ��û Ÿ��: �׷� ���� */

			server = Server.getInstance();
			group = server.getGroupByPrimaryKey(incomingMessage.get(Message.GROUP_PK));	// �������� "��û�� �׷�Ű�� �ش��ϴ� ��ü"�� ������

			responseMsg = new Message().setType(Message.RESPONSE_JOIN_GROUP);				// ���� �޼��� ����, ���� Ÿ���� "�׷� ���� ��û�� ���� ����"
			if (group != null) {													// �ش� �׷�Ű�� ���εǴ� �׷��� ���� ��,
				userName = group.addUser(session.getId(), this);		// �׷쿡 �ڽ��� �߰�, ������� �̸��� �޾ƿ� / XXX ���� �ʿ�
				responseMsg.add(Message.RESULT, Message.CONFIRM);	// ���� �޽����� ���� �߰�: ���� ���
				responseMsg.add(Message.NAME, userName);					// ���� �޽����� ���� �߰�: ����� �̸�
				MessageParser.addMessageToGroup(responseMsg, group);	// ���� �޽����� ���� �߰�: �׷� ����

				Message noti = new Message().setType(Message.NOTI_ADD_PARTICIPANT);	// �˸� �޽��� ����, �˸� Ÿ���� "�����ڿ� ���� ����"
				noti.add(Message.PARTICIPANT_NAME, userName);						// �˸� �޽����� ���� �߰�: ������ ����
				group.send(userName, noti);

			} else {																		// �ش� �׷�Ű�� ���εǴ� �׷��� �������� ���� ��,
				responseMsg.add(Message.RESULT, Message.REJECT);		// ���� �޽����� ���� �߰�: ���� ���
			}
				break;

			default:
			responseMsg = new Message().setType(Message.TEST_DEBUG_MODE);
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

		Message noti = new Message().setType(Message.NOTI_EXIT_PARTICIPANT);	// �˸� �޽��� ����, �˸� Ÿ���� "�����ڿ� ���� ����"
		noti.add(Message.PARTICIPANT_NAME, userName);							// �˸� �޽����� ���� �߰�: ������ ����
		try {
			group.send(userName, noti);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (EncodeException e) {
			e.printStackTrace();
		}
		group.removeUser(userName);
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

	// test main method
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