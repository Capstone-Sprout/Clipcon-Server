package sprout.clipcon.server.controller;

import java.awt.geom.GeneralPath;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import sprout.clipcon.server.model.Group;
import sprout.clipcon.server.model.User;

public class Server {
	private static Server uniqueInstance;
	private MemberAdministrator MemberAdministrator = new MemberAdministrator();
	private Map<String, Group> groups = Collections.synchronizedMap(new HashMap<String, Group>());	// ���� �� �����Ѵ� �׷�
	private Set<User> userOnLobby = Collections.synchronizedSet(new HashSet<User>());				// �׷쿡 �������� ���� ���� ���� �����
	
	public Group testGroup = new Group("##", "test group");

	private Server() {
	}

	public static Server getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new Server();
		}
		return uniqueInstance;
	}

	/**
	 * ����ڰ� ������ ���� 
	 * @param user ������ ������ ����� */
	public void enterUserInLobby(User user) {
		userOnLobby.add(user);
	}

	public void exitUSerAtLobby(User user) {
		// TODO: static?
		userOnLobby.remove(user);
	}

	// public void

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

	public Group createGroup(String name) {
		String groupKey = generatePrimaryKey();
		return groups.put(groupKey, new Group(groupKey, name));
	}

	private String generatePrimaryKey() {
		StringBuffer temp = new StringBuffer();
		Random rnd = new Random();
		for (int i = 0; i < 20; i++) {
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
