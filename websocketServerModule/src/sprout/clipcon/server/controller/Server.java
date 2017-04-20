package sprout.clipcon.server.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import sprout.clipcon.server.model.Group;

public class Server {
	private static Server uniqueInstance;
	private Map<String, Group> groups = Collections.synchronizedMap(new HashMap<String, Group>());	// ���� �� �����Ѵ� �׷�

	private Server() {
		Group testGroup = new Group("godoy");
		groups.put("godoy", testGroup);
	}

	public static Server getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new Server();
		}
		return uniqueInstance;
	}

	/**
	 * �ش� �׷쿡 ����� �߰�
	 * @param key �׷� ���� Ű
	 * @param user �׷쿡 ���� �� ����� 
	 * @return �׷��� ���� ����. �׷��� �������� ������ false, �����ϰ� ����ڰ� ���������� �׷쿡 �߰� ������ true. */
	public Group getGroupByPrimaryKey(String key) {
		Group targetGroup = groups.get(key);
		if (targetGroup != null) {
		}
		return targetGroup;
	}

	public Group createGroup() {
		String groupKey = generatePrimaryKey();
		System.out.println("�Ҵ�� �׷� Ű�� " + groupKey);
		Group newGroup = new Group(groupKey);
		groups.put(groupKey, newGroup);
		return newGroup;
	}

	private String generatePrimaryKey() {
		StringBuffer temp = new StringBuffer();
		Random rnd = new Random();
		for (int i = 0; i < 5; i++) {
			int rIndex = rnd.nextInt(3);
			switch (rIndex) {
			case 0:
				temp.append((char) ((int) (rnd.nextInt(26)) + 97));
				break;
			case 1:
				temp.append((char) ((int) (rnd.nextInt(26)) + 65));
				break;
			case 2:
				temp.append((rnd.nextInt(10)));
				break;
			}
		}
		return temp.toString();
	}

	public static void main(String[] args) {
		StringBuffer temp = new StringBuffer();
		Random rnd = new Random();
		for (int i = 0; i < 6; i++) {
			int rIndex = rnd.nextInt(3);
			switch (rIndex) {
			case 0:
				// a-z
				temp.append((char) ((int) (rnd.nextInt(26)) + 97));
				break;
			case 1:
				// A-Z
				temp.append((char) ((int) (rnd.nextInt(26)) + 65));
				break;
			case 2:
				// 0-9
				temp.append((rnd.nextInt(10)));
				break;
			}
		}
	}
}
