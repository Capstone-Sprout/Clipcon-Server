package sprout.clipcon.server.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
		return user;
	}

//	public static User getUserAndGroupByMessage(Message message) {
//		User user = new User(message.get(Message.NAME));
//		Group group = new Group(message.get(Message.GROUP_PK));
//
//		// group.setName(name);
//		
//		List<String> userStringList = new ArrayList<String>();
//		JSONArray tmpArray = message.getJson().getJSONArray(Message.LIST);
//		Iterator<?> it = tmpArray.iterator();
//		while (it.hasNext()) {
//			JSONObject tmpJson = (JSONObject) it.next();
//			userStringList.add(tmpJson.getString(Message.NAME));
//		}
//		
//
//		List<User> userList = new ArrayList<User>();
//		for (String userName : userStringList) {
//			userList.add(new User(userName));
//		}
//
//		group.setUserList(userList);
//		user.setGroup(group);
//
//		return user;
//	}

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
		return addressBook;
	}

	public static Message getMessageByUser(User user) {
		Message message = new Message().setType(Message.USER_INFO); // ��ȯ�� ��ü, Ÿ���� '��������'
		message.add(Message.NAME, user.getName());// name ����
		return message;
	}
	
	public static Message getMessageByGroup(Message message, Group group) {
		message.add(Message.GROUP_PK, group.getPrimaryKey());
		List<String> userList = group.getUserList();
		System.out.println("list size: " + userList.size());

		JSONArray array = new JSONArray();
		Iterator<String> it = userList.iterator();
		while (it.hasNext()) {
			array.put(it.next());
		}
		message.add(Message.LIST, array);
		return message;
		// TODO:
	}

	public static Message appendMessageByGroup(Message message, Group group) {
		return new Message();
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

		List<String> list = new ArrayList<String>();
		list.add("em1");
		list.add("em2");
		list.add("em3");
		list.add("em4");

		Group tmpGroup = new Group();
		tmpGroup.setPrimaryKey("##");

		Message tmpMessage = new Message().setType(Message.TEST_DEBUG_MODE);

		JSONArray array = new JSONArray();
		Iterator<?> it = list.iterator();
		while (it.hasNext()) {
			array.put(it.next());
		}
		tmpMessage.add(Message.LIST, array);
		System.out.println(tmpMessage);
	}

	public List<String> createTestList() {
		List<String> list = new ArrayList<String>();
		list.add("em1");
		list.add("em2");
		list.add("em3");
		list.add("em4");
		return list;
	}

}