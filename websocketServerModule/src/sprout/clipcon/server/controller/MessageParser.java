package sprout.clipcon.server.controller;

import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import sprout.clipcon.server.model.AddressBook;
import sprout.clipcon.server.model.Group;
import sprout.clipcon.server.model.User;
import sprout.clipcon.server.model.message.Message;

public class MessageParser {

	/**
	 * @param message �������� ���� Message��ü
	 * @return message �κ��� ��ȯ�� User��ü */
	public static User getUserByMessage(Message message) {
		User user = new User(); // ��ȯ�� ��ü
		user.setEmail(message.get(Message.EMAIL));	// User��ü�� email ����
		user.setName(message.get(Message.NAME));	// User��ü�� name ����

		// user�� ������ AddressBook��ü ����
		AddressBook addressBook = new AddressBook();
		Map<String, String> users = addressBook.getUsers();

		// message���� JSONObject����
		JSONObject jsonMsg = message.getJson();
		JSONArray array = jsonMsg.getJSONArray(Message.LIST);

		Iterator<?> it = array.iterator();
		while (it.hasNext()) {
			JSONObject tmpJson = (JSONObject) it.next();
			String email = tmpJson.getString(Message.EMAIL);
			String name = tmpJson.getString(Message.NAME);
			users.put(email, name);
		}
		user.setAddressBook(addressBook);
		return user;
	}

	public static AddressBook getAddressBookByMessage(Message message) {
		AddressBook addressBook = new AddressBook();
		Map<String, String> users = addressBook.getUsers();
		JSONObject jsonMsg = message.getJson();
		JSONArray array = jsonMsg.getJSONArray(Message.LIST);

		Iterator<?> it = array.iterator();
		while (it.hasNext()) {
			JSONObject tmpJson = (JSONObject) it.next();
			String email = tmpJson.getString(Message.EMAIL);
			String name = tmpJson.getString(Message.NAME);
			// User tmpUser = new User(email, tmpJson.getString(Message.NAME));
			users.put(email, name);
		}

		for (String key : users.keySet()) {
			System.out.println(key + " " + users.get(key));
		}

		return addressBook;
	}

	public static Message getMeessageByAddressBook(AddressBook addressBook) {
		Map<String, String> users = addressBook.getUsers();
		Message message = new Message().setType(Message.ADDRESS_BOOK);

		JSONArray array = new JSONArray();
		for (String key : users.keySet()) {
			JSONObject tmp = new JSONObject();
			tmp.put(Message.EMAIL, users.get(key));
			tmp.put(Message.NAME, users.get(key));
			array.put(tmp);
		}
		message.getJson().put(Message.LIST, array);
		return message;
	}

	public static Message getMessageByUser(User user) {
		Message message = new Message().setType(Message.USER_INFO); // ��ȯ�� ��ü, Ÿ���� '��������'

		message.add(Message.EMAIL, user.getEmail());	// email ����
		message.add(Message.NAME, user.getName());		// name ����

		// Json���� ��ȯ�� �ּҷ� Map
		Map<String, String> users = user.getAddressBook().getUsers();
		// �ּҷ� ���� ���� JsonArray
		JSONArray array = new JSONArray();
		// array�� �ּҷ� ���� ����
		for (String key : users.keySet()) {
			JSONObject tmp = new JSONObject();
			tmp.put(Message.EMAIL, users.get(key));
			tmp.put(Message.NAME, users.get(key));
			array.put(tmp);
		}
		// array�� message�� ����
		message.getJson().put(Message.LIST, array);

		return message;
	}

	public static Message AddUserInfoToMessage(Message message, User user) {
		message.add(Message.EMAIL, user.getEmail());	// email ����
		message.add(Message.NAME, user.getName());		// name ����

		// Json���� ��ȯ�� �ּҷ� Map
		Map<String, String> users = user.getAddressBook().getUsers();
		// �ּҷ� ���� ���� JsonArray
		JSONArray array = new JSONArray();
		// array�� �ּҷ� ���� ����
		for (String key : users.keySet()) {
			System.out.println("����� ��");
			JSONObject tmp = new JSONObject();
			tmp.put(Message.EMAIL, users.get(key));
			tmp.put(Message.NAME, users.get(key));
			array.put(tmp);
		}
		// array�� message�� ����
		message.getJson().put(Message.LIST, array);
		System.out.println(message);
		return message;
	}

	public static Message getMessageByGroup(Group group) {
		Message message = new Message().setType(Message.GROUP_INFO);
		message.add("groupkey", group.getPrimaryKey());
		message.add("groupname", group.getName());
		message.add("list", group.getUserList());

		return message;
	}

	// public static Group getGroupByMessage(Message message) {
	// Group group = new Group();
	// String key = message.get("groupkey");
	// String name = message.get("groupname");
	// group.setPrimaryKey(key);
	// group.setName(name);
	//
	// group.setUsers(message.getObject("list"));
	// return group;
	// }

	public static void main(String[] args) {

		AddressBook ab = new AddressBook();
		Map<String, String> users = ab.getUsers();

		users.put("em1", "n1");
		users.put("em2", "n2");
		users.put("em3", "n3");
		users.put("em5", "n5");

		User tmp = new User("abab", "1212");
		tmp.setAddressBook(ab);
		Message tmpMessage = getMessageByUser(tmp);
		User tmpUser = getUserByMessage(tmpMessage);
		System.out.println(tmpUser.getName());
		System.out.println(tmpUser.getEmail());

		System.out.println("�ּҷ� ���");
		Map<String, String> tmpMap = tmpUser.getAddressBook().getUsers();
		for (String key : tmpMap.keySet()) {
			System.out.println(key + " " + tmpMap.get(key));
		}
	}

}
