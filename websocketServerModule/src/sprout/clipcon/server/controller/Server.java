package sprout.clipcon.server.controller;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import sprout.clipcon.server.model.Group;

public class Server {
	private static Server uniqueInstance;
	/** ���� �� �����ϴ� �׷�� */
	private Map<String, Group> groups = Collections.synchronizedMap(new HashMap<String, Group>());

	private Server() {
		Group testGroup = new Group("godoy");
		groups.put("godoy", testGroup);

		new Thread(new Runnable() {
			@Override
			public void run() {
				int i = 0;
				while (true) {
					System.out.println(Thread.currentThread().getName() + " " + i++);
				}
			}
		});// .start();
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
	 * @return �׷��� ���� ����. �׷��� �����ϸ� true, �׷��� ������ false */
	public Group getGroupByPrimaryKey(String key) {
		Group targetGroup = groups.get(key);
		if (targetGroup != null) {
		}
		return targetGroup;
	}

	/**
	 * �׷� ���� �� ������ �׷� ��Ͽ� �߰� 
	 * @return ������ �׷� ��ü */
	public Group createGroup() {
		String groupKey = generatePrimaryKey(5);
		System.out.println("�Ҵ�� �׷� Ű�� " + groupKey);
		Group newGroup = new Group(groupKey);
		groups.put(groupKey, newGroup);
		return newGroup;
	}

	/**
	 * ���� �빮��, �ҹ���, ���ڷ� ȥ�յ� ���ڿ� ����.
	 * @param length ������ ���ڿ� ���� 
	 * @return ������ ���ڿ� */
	private String generatePrimaryKey(int length) {
		StringBuffer temp = new StringBuffer();
		Random rnd = new Random();
		for (int i = 0; i < length; i++) {
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

	/** the method for test and debug. */
	public static void subDirList(String source) {
		File dir = new File(source);
		File[] fileList = dir.listFiles();
		try {
			for (int i = 0; i < fileList.length; i++) {
				File file = fileList[i];
				if (file.isFile()) {
					System.out.println("���� �̸� = " + file.getPath());
				} else if (file.isDirectory()) {
					System.out.println("�� �̸� = " + file.getPath());
					subDirList(file.getCanonicalPath().toString());
				}
			}
		} catch (IOException e) {
		}
	}

	public static void main(String[] args) {
		subDirList("C:\\Users\\delf\\Desktop\\dev");
	}
}
