package sprout.clipcon.server.model;

import java.util.HashMap;
import java.util.Map;

import javax.websocket.Session;

import lombok.Getter;
import sprout.clipcon.server.controller.Server;
import sprout.clipcon.server.model.message.Message;

@Getter
public class AddressBook {
	private Map<String, String> users = new HashMap<String, String>();
	// private Server server = Server.getInstance();

	public Session getSessionByEmail(String email) {
		// return users.get(email).getSession(); // ��� �ڵ�
		// TODO: �κ񿡼� ã�Ƽ� ���� �������ֱ�
		return null;
	}

	public void addAddressByMessage(Message message) {
		String email = message.get(Message.EMAIL);
		users.put(message.get(Message.EMAIL), message.get(Message.NAME));
	}
	
	public void deleteAddress(Message message) {
		users.remove(message.get(Message.EMAIL));
	}
}
