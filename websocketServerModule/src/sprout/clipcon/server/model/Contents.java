package sprout.clipcon.server.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Contents {
	public static String TYPE_STRING = "STRING";
	public static String TYPE_IMAGE = "IMAGE";
	public static String TYPE_FILE = "FILE";

	private String contentsType;
	private long contentsSize;

	// �׷쳻�� �� Data�� �����ϴ� ����Ű��
	private static int contentsPKValue = 0;
	public String contentsPKName;

	private String uploadUserName;
	private String uploadTime;

	// String Type: String��, File Type: FileOriginName
	private String contentsValue;
	
	public Contents(String type, String userEmail, String time, long size) {
		this();
		this.contentsType = type;
		this.uploadUserName = userEmail;
		this.uploadTime = time;
		this.contentsSize = size;
	}

	/** ���� �� ����Ű���� �Ҵ��Ѵ�. */
	public Contents() {
		this.contentsPKName = Integer.toString(++contentsPKValue);
		System.out.println("Contents ������ ȣ��, ++contentsPKValue");
	}
}
