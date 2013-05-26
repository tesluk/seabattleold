package utils;

import java.io.DataOutputStream;
import java.io.IOException;

public class DataSender {
	
	public static void send(String ip,int port, String data) {
		SocketBattleClient sbk = new SocketBattleClient(ip, port);
		try {
			System.out.println("Received: " + sbk.sendRequest(data));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
