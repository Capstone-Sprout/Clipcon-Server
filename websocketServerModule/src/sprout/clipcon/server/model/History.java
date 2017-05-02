package sprout.clipcon.server.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class History {
	@Getter
	@Setter
	private String groupPK;
	private Map<String, Contents> contentsMap;

	// �׷쳻�� �� Data�� �����ϴ� ����Ű��
	private static int contentsPKValue = 0;

	public History(String groupPK) {
		this.groupPK = groupPK; // XXX[delf]: ��� �ʿ����� �� �𸣰���
		contentsMap = new HashMap<String, Contents>();
	}

	/** ���ο� �����Ͱ� ���ε�Ǹ� �����丮�� add */
	public void addContents(Contents contents) {
		contents.setContentsPKName(Integer.toString(++contentsPKValue));
		contentsMap.put(contents.getContentsPKName(), contents);
	}

	/** Data�� �����ϴ� ����Ű���� ��ġ�ϴ� Contents�� return */
	public Contents getContentsByPK(String contentsPKName) {
		return contentsMap.get(contentsPKName);
	}

	public Contents putContents(Contents contents) {
//		contents.setContentsPKName(Integer.toString(++contentsPKValue));
		contentsMap.put(contents.getContentsPKName(), contents);

		return contentsMap.get(contentsPKValue);
	}

}
