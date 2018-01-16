package cn.et;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 
 * @author Administrator
 *
 */
public class RibbitmqPub {
	private final static String QUEUE_NAME = "MAIL_QUEU";

	public static byte[] getByte(Object obj) throws ClassNotFoundException, IOException{
		ByteArrayOutputStream bas = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(bas);
		os.writeObject(obj);
		return bas.toByteArray();
	}
	
	public static Object getObject(byte[] bos) throws ClassNotFoundException, IOException{
		ByteArrayInputStream bas = new ByteArrayInputStream(bos);
		ObjectInputStream os = new ObjectInputStream(bas);
		return os.readObject();
	}
	public static void main(String[] args) throws Exception {
		Map<String, String> map = new HashMap<String, String>(0);
		map.put("mailTo", "154388366@qq.com");
		map.put("subject", "测试题目的关键");
		map.put("content", "这是一个很强的软件。欢迎使用");
		// 连接远程rabbit-server服务器
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.74.128");
		factory.setPort(5672);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		// 定义创建一个队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		String message = "Hello World!";
		// 发送消息
		channel.basicPublish("", QUEUE_NAME, null, getByte(map)); 
		// 注意发送和接受段相同字符集否则出现乱码
		System.out.println(" [x] Sent '" + message + "'");
		channel.close();
		connection.close();
		
	}
}
