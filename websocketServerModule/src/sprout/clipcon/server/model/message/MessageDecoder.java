package sprout.clipcon.server.model.message;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import org.json.JSONObject;

/**
 * Ŭ���̾�Ʈ���� ���� string�� object(Message)�� decoding. */
public class MessageDecoder implements Decoder.Text<Message> {
	JSONObject json;

	public void destroy() {
	}

	public void init(EndpointConfig arg0) {
	}

	public Message decode(String incommingMessage) throws DecodeException {
		System.out.println("������ ���� string Ȯ��: " + incommingMessage);
		Message message = new Message().setJson(incommingMessage);
		// message.setJson(incommingMessage);
		
		return message;
	}

	public boolean willDecode(String message) {
		boolean flag = true;
		try {
		} catch (Exception e) {
			System.out.println("false!");
			flag = false;
		}
		return flag;
	}
}