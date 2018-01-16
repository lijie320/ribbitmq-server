package cn.et;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Pub_log {
	/**
	 * 给交换器取名称
	 */
	private static final String EXCHANGE_NAME = "amq-log";

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
		//定义创建一个交换器 参数1 名称  参数2 交换器类型 参数3表示将交换器信息永久保存在服务器磁盘上 关闭rabbitmqserver也不会丢失  
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout",true);  
        String message = null;  
        //同时发送5条消息  
        for(int i=0;i<=5;i++){  
            message="发送第"+i+"消息";  
            //第二个参数就是routingkey  不填 默认会转发给所有的订阅者队列  
            channel.basicPublish(EXCHANGE_NAME, "", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));  
        }    
        System.out.println(" [x] Sent 6 message"); 
        channel.close();  
        connection.close();  
		
	}
}
