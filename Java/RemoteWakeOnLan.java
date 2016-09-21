import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 远程启动PC<br/>
 * 发送一个数据包，前6位放入0xFF的二进制，第7位开始把mac地址二进制放入16次
 * @author kaven 2016-09-20
 *
 */
public class RemoteWakeOnLan {

	private static final String DST_IP = "127.0.0.1";
	private static final int DST_PORT = 20105;
	private static final String DST_MAC = "F0-76-1C-0B-21-1A";
	
	private void wakeOnLan(){
		byte[] mac = macToBytes(DST_MAC);
		InetAddress address = null;
		try {
			address = InetAddress.getByName(DST_IP);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		//前6位放入0xFF的二进制，后面存放16次mac地址二进制，每次都有6个byte
		byte[] magicPackage = new byte[6 + 6 * 16];
		//前6位放入0xFF的二进制
		for(int i = 0; i < 6; i++){
			magicPackage[i] = (byte)0xFF;
		}
		//第7位开始把mac地址二进制放入16次
		for(int i = 6; i < magicPackage.length; i++){
			for(int j = 0; j < mac.length; j++){
				magicPackage[i] = mac[j];
			}
		}
		DatagramPacket dp = new DatagramPacket(magicPackage, magicPackage.length, address, DST_PORT);
		DatagramSocket ds = null;
		try {
			ds = new DatagramSocket();
			ds.send(dp);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			ds.close();
		}
		System.out.println("OK");
	}
	
	/**
	 * 把MAC地址字符串转换成二进制
	 * @param hexStr 以“:”或“-”分隔的字符串
	 * @return
	 * @author kaven
	 * @date 2016-09-20
	 */
	private byte[] macToBytes(String hexStr){
		byte[] bytes = new byte[6];
		if(hexStr != null && !"".equals(hexStr)){
			String[] hex = hexStr.split(":|-");
			if(hex.length != 6){
				throw new IllegalArgumentException("Invalid MAC address");
			}else{
				try{
					for(int i = 0; i < bytes.length; i++){
						bytes[i] = (byte)Integer.parseInt(hex[i], 16);
					}
				}catch(NumberFormatException e){
					throw new IllegalArgumentException("Invalid hex digit in MAC address", e);
				}
			}
		}else{
			throw new IllegalArgumentException("Invalid MAC address");
		}
		return bytes;
	}
	
	public static void main(String[] args) {
		RemoteWakeOnLan rwon = new RemoteWakeOnLan();
		rwon.wakeOnLan();
	}

}
