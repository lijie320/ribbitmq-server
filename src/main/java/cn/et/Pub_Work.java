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

public class Pub_Work {
	private final static String QUEUE_NAME = "WORK_QUEU";

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
		// 连接远程rabbit-server服务器
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.74.128");
		factory.setPort(5672);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		// 定义创建一个队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		// 发送消息
		for(int i=0;i<=5;i++){  
            //发送消息  
            channel.basicPublish("", QUEUE_NAME, null, ("这是个"+i).getBytes("UTF-8"));  
        }   
		// 注意发送和接受段相同字符集否则出现乱码
		channel.close();
		connection.close();
		
	}
}
