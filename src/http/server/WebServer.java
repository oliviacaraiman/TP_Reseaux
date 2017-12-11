///A Simple Web Server (WebServer.java)

package http.server;

import java.awt.image.RenderedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

/**
 * Example program from Chapter 1 Programming Spiders, Bots and Aggregators in
 * Java Copyright 2001 by Jeff Heaton
 * 
 * WebServer is a very simple web-server. Any request is responded with a very
 * simple web-page.
 * 
 * @author Jeff Heaton
 * @version 1.0
 */
public class WebServer {

	/**
	 * WebServer constructor.
	 */
	protected void start() {
		ServerSocket s;

		System.out.println("Webserver starting up on port 80");
		System.out.println("(press ctrl-c to exit)");
		try {
			// create the main server socket
			s = new ServerSocket(3000);
		} catch (Exception e) {
			System.out.println("Error: " + e);
			return;
		}

		System.out.println("Waiting for connection");
		int i = 0;
		for (;;) {
			try {
				// wait for a connection
				Socket remote = s.accept();
				// remote is now the connected socket
				System.out.println("Connection, sending data.");
				BufferedReader in = new BufferedReader(new InputStreamReader(remote.getInputStream()));
				// PrintWriter out = new PrintWriter(remote.getOutputStream());
				OutputStream out = remote.getOutputStream();
				PrintStream outMedia = new PrintStream(remote.getOutputStream());

				// BufferedOutputstream

				// read the data sent. We basically ignore it,
				// stop reading once a blank line is hit. This
				// blank line signals the end of the client HTTP
				// headers.

				// Send the response
				// Send the headers
				if (i == 0) {
					outMedia.println("HTTP/1.0 200 OK");
					outMedia.println("Content-Type: text/html");
					outMedia.println("Server: Bot");
					// this blank line signals the end of the headers
					outMedia.println("");
					// Send the HTML page
					outMedia.println("<H1>Welcome to the Ultra Mini-WebServer</H1>");
					outMedia.flush();
				}

				// String root = "L:\\Mes
				// Documents\\RESEAUX\\HTTPServer\\src\\";
				// String root = "C:\\Users\\Lucie\\git\\TP_Reseaux\\src\\";
				// String root = "D:\\java\\TP2_Reseaux\\src\\";

				String root = "L:\\Mes documents\\RESEAUX\\HTTPServer\\src\\";

				String str = ".";
				while (str != null && !str.equals("")) {
					str = in.readLine();

					System.out.println(str);
					// str = "GET /test.txt";
					if (str != null && str.substring(0, 3).equals("GET")) {
						String request = str.substring(5, str.length() - "HTTP\1.1".length() - 2);
						System.out.println(request);
						// type "GET /path" = on enl�ve le /
						if (!request.equals("favicon.ico"))
							doGet(request, outMedia, root);
						str = "";
					} else if (str != null && str.substring(0, 4).equals("POST")) {
						String request = str.substring(6, str.length() - "HTTP\1.1".length()- 2);
						// type "GET /path" = on enl�ve le /
						doPost(in, outMedia, root);
						str = "";
					} else if (str != null && str.substring(0, 4).equals("HEAD")) {
						String request = str.substring(6, str.length() -  "HTTP\1.1".length() - 2);
						doHead(request, outMedia, root);
						str = "";
					} else if (str != null && str.substring(0, 3).equals("PUT")) {
						String request = str.substring(5, str.length() - "HTTP\1.1".length() - 2);
						// type "GET /path" = on enl�ve le /
						// doPut(request, outMedia, root);
						str = "";
					} else if (str != null && str.substring(0, 6).equals("DELETE")) {
						String request = str.substring(8, str.length() - "HTTP\1.1".length() - 2);
						// type "GET /path" = on enl�ve le /
						// doDelete(request, out, root);
						str = "";
					}

				}
				outMedia.flush();
				remote.close();
			} catch (Exception e) {
				System.out.println("Error: " + e);
				e.printStackTrace();
			}
			i++;
		}
	}

	/**
	 * Start the application.
	 * 
	 * @param args
	 *            Command line parameters are not used.
	 */
	public static void main(String args[]) {
		WebServer ws = new WebServer();
		ws.start();
	}

	public void doGet(String req, PrintStream outMedia, String root) {
		try {

			outMedia.flush();

			String extension = getExtension(req);
			String err ="200 OK";
			Path path = Paths.get(root + req);
			byte[] fileContents = Files.readAllBytes(path);
			
			if (extension.equals("txt") || extension.equals("html")) {
				
				Header(err, extension, outMedia,fileContents);
				outMedia.write(fileContents);
				outMedia.close();

			} else if (extension.equals("png") || extension.equals("jpeg") || extension.equals("jpg")) {
				
				Header(err, extension, outMedia,fileContents);
				outMedia.write(fileContents);
				outMedia.close();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	
		/*
		 * out.println("<HTML>"); //out.println("<HEAD> <TITLE> Hello," + name +
		 * "</TITLE></HEAD>"); out.println("<BODY>"); //out.println("Hello, " +
		 * name); out.println("</BODY>"); out.println("</HTML>");
		 */
		outMedia.close();
	}

	public void doPost(BufferedReader req, PrintStream outMedia, String root) {
		try {
			outMedia.println(req);
			// FileReader fr = new FileReader("L:\\Mes
			// Documents\\RESEAUX\\HTTPServer\\src\\" + req);
			// BufferedReader br = new BufferedReader(fr);
			String strRead = req.readLine();

			// lecture de head
			while (strRead != null && !strRead.equals("")) {
				// out.print("<h4>test</h4>");
				System.out.println("header: " + strRead);
				// outMedia.print("<h1>" + strRead + "</h1>");
				strRead = req.readLine();

			}
			// lecture du body
			strRead = req.readLine();
			File file = new File(root + "received.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(root + "received.txt", true);

			BufferedWriter bw = new BufferedWriter(fw);
			while (strRead != null && !strRead.equals("")) {
				// out.print("<h4>test</h4>");
				outMedia.print("<h1>" + strRead + "</h1>");

				bw.write(strRead);
				bw.newLine();
				strRead = req.readLine();
				bw.write(strRead + "\n");
				bw.newLine();
			}

			bw.close();
			/*
			 * bw.write(out + "\n"); bw.newLine();
			 */
			// br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		outMedia.close();
	}

	public void doHead(String req, PrintStream outMedia, String root) {
		try{
//			out.println(req);
//			FileReader fr = new FileReader(root + req);
//			BufferedReader br = new BufferedReader(fr);
//			String str = ".";
//			while ((str) != null && !str.equals("")) {
//
//				// les headers sont s�par�s du reste par un saut de ligne
//
//				// out.print("<h4>test</h4>");
//
//				str = br.readLine();
//				
//
//				out.print("<h1>" + br.read() + "</h1>");
//
//			}
			/*
			 * bw.write(out + "\n"); bw.newLine();
			 */
//			br.close();
			
			String extension = getExtension(req);
			String err ="200 OK";
			Path path = Paths.get(root + req);
			byte[] fileContents = Files.readAllBytes(path);
			
			if (extension.equals("txt") || extension.equals("html")) {
				
				Header(err, extension, outMedia,fileContents);
				outMedia.close();

			} else if (extension.equals("png") || extension.equals("jpeg") || extension.equals("jpg")) {
				
				Header(err, extension, outMedia,fileContents);
				outMedia.close();

			}
			
			
			
//			String strRead = req.readLine();
//			// lecture de head
//			while (strRead != null && !strRead.equals("")) {
//				// out.print("<h4>test</h4>");
//				System.out.println("header: " + strRead);
//				// outMedia.print("<h1>" + strRead + "</h1>");
//				strRead = req.readLine();
//
//			}
//			// lecture du body
//			strRead = req.readLine();
//			File file = new File(root + "received.txt");
//			if (!file.exists()) {
//				file.createNewFile();
//			}
//			FileWriter fw = new FileWriter(root + "received.txt", true);
//
//			BufferedWriter bw = new BufferedWriter(fw);
//			while (strRead != null && !strRead.equals("")) {
//				// out.print("<h4>test</h4>");
//				outMedia.print("<h1>" + strRead + "</h1>");
//
//				bw.write(strRead);
//				bw.newLine();
//				strRead = req.readLine();
//				bw.write(strRead + "\n");
//				bw.newLine();
//			}
//
//			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		outMedia.close();	
	}
	
	
	public void storeHeader(String filename,String err, String extension, PrintStream outMedia, byte[] fileContents){
		
	}
	
	public void doPut(String req, PrintStream out, String root) {
		try {
			out.println(req);
			FileReader fr = new FileReader(root + req);
			BufferedReader br = new BufferedReader(fr);
			while ((br.readLine()) != null) {
				// out.print("<h4>test</h4>");
				out.print("<h1>" + br.read() + "</h1>");

			}
			/*
			 * bw.write(out + "\n"); bw.newLine();
			 */
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		out.close();
	}

	public void doDelete(String req, PrintWriter out, String root) {
		try {
			Path path = FileSystems.getDefault().getPath(root + req);
			Files.deleteIfExists(path);

		} catch (IOException e) {
			e.printStackTrace();
		}

		out.close();
	}

	String getExtension(String request) {

		if (request != null && request.contains(".")) {
			String[] result = request.split("\\.");
			return result[result.length - 1 ];
		} else {
			return "none";
		}

	}

	void Header(String err, String extension, PrintStream outMedia, byte[] fileContents) {
		outMedia.println("HTTP/1.0 " + err);
		outMedia.println("Content-Type: image/" + extension);
		outMedia.println("Server: Bot");
		outMedia.println("Content-Length: " + fileContents.length);

		// this blank line signals the end of the headers
		outMedia.println("");
		// Send the HTML page
	}

}

// contenu binaire = fileinputstream

// headers construction
// https://stackoverflow.com/questions/20889076/constructing-http-headers-for-java-http-server

// http://www.javapractices.com/topic/TopicAction.do?Id=245