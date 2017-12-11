package http.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class WebPing {
	public static void main(String[] args) {

		if (args.length != 2) {
			System.err.println("Usage java WebPing <server host name> <server port number>");
			return;
		}
		
//		-------------------
		Socket sock = null;
		DataOutputStream os = null;
        DataInputStream is = null;
//		-------------------

		String httpServerHost = args[0];
		int httpServerPort = Integer.parseInt(args[1]);
		try {
			InetAddress addr;
			sock = new Socket(httpServerHost, httpServerPort);
			addr = sock.getInetAddress();
			System.out.println("Connected to " + addr);
			//os = new DataOutputStream(sock.getOutputStream());
            //is = new DataInputStream(sock.getInputStream());
		} catch (java.io.IOException e) {
			System.out.println("Can't connect to " + httpServerHost + ":" + httpServerPort);
			System.out.println(e);
		}
//		close and clean streams
		try {
			//os.close();
			//is.close();
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
}