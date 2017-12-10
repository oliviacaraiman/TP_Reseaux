///A Simple Web Server (WebServer.java)

package http.server;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javax.imageio.ImageIO;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
		for (;;) {
			try {
				// wait for a connection
				Socket remote = s.accept();
				// remote is now the connected socket
				System.out.println("Connection, sending data.");
				BufferedReader in = new BufferedReader(new InputStreamReader(remote.getInputStream()));
				//PrintWriter out = new PrintWriter(remote.getOutputStream());
				PrintStream outMedia = new PrintStream(remote.getOutputStream());
				// read the data sent. We basically ignore it,
				// stop reading once a blank line is hit. This
				// blank line signals the end of the client HTTP
				// headers.

				// Send the response
				// Send the headers
				outMedia.println("HTTP/1.0 200 OK");
				outMedia.println("Content-Type: text/html");
				outMedia.println("Server: Bot");
				// this blank line signals the end of the headers
				outMedia.println("");
				// Send the HTML page
				outMedia.println("<H1>Welcome to the Ultra Mini-WebServer</H1>");
				
				//String root = "L:\\Mes Documents\\RESEAUX\\HTTPServer\\src\\";
				String root = "C:\\Users\\Lucie\\git\\TP_Reseaux\\src\\";


				String str = ".";
				while (str != null && !str.equals("")) {
					str = in.readLine();
					// str = "GET /test.txt";
					if (str != null && str.substring(0, 3).equals("GET")) {
						String request = str.substring(5, str.length() - " HTTP\1.1".length()-1);
						// type "GET /path" = on enlève le /
						doGet(request, outMedia, root);
						str = "";
					} else if (str != null && str.substring(0, 4).equals("POST")) {
						String request = str.substring(6, str.length() - " HTTP\1.1".length());
						// type "GET /path" = on enlève le /

					//	doPost(in, outMedia,root);
						str = "";
					} else if (str != null && str.substring(0, 4).equals("HEAD")) {
						String request = str.substring(6, str.length() - " HTTP\1.1".length());
						// type "GET /path" = on enlève le /
						//doHead(request, outMedia, root);
						str = "";
					} else if (str != null && str.substring(0, 3).equals("PUT")) {
						String request = str.substring(5, str.length() - " HTTP\1.1".length());
						// type "GET /path" = on enlève le /
						//doPut(request, outMedia, root);
						str = "";
					} else if (str != null && str.substring(0, 6).equals("DELETE")) {
						String request = str.substring(8, str.length() - " HTTP\1.1".length());
						// type "GET /path" = on enlève le /
						//doDelete(request, out, root);
						str = "";
					}

				}
				outMedia.flush();
				remote.close();
			} catch (Exception e) {
				System.out.println("Error: " + e);
				e.printStackTrace();
			}
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
			
			outMedia.println("you requested : " +req);
			String extension = getExtension(req);
			outMedia.println("the extension is : " +extension);
			
			String textPath = root + req;
			Path path = Paths.get(textPath);
			byte[] fileContents = Files.readAllBytes(path);
			
			
			
			if (extension.equals("txt")) {
//				FileReader fr = new FileReader("C:\\Users\\Lucie\\git\\TP_Reseaux\\src\\" + req);
//				BufferedReader br = new BufferedReader(fr);
//				while ((br.readLine()) != null) {
//					//outMedia.print("<h4>Contenu du fichier</h4>");
//					outMedia.print("<h1>" + br.read() + "</h1>");
//				}
//				br.close();
				
				outMedia.print("<h4>Contenu du fichier</h4>");
				outMedia.write(fileContents);
				outMedia.close();
				
				
			} else if (extension.equals("png") || extension.equals("jpeg") || extension.equals("jpg")) {
				FileInputStream fis = new FileInputStream(root + req);
				// a finir

//				Path path = Paths.get("L:\\Mes Documents\\RESEAUX\\HTTPServer\\src\\" + req);
//				SmallBinaryFiles binary = new SmallBinaryFiles();
//				byte[] bytes = binary.readSmallBinaryFile(FILE_NAME);
//				Files.write(path, aBytes); // creates, overwrites
				
//				Path path = FileSystems.getDefault().getPath(root + req);
//				byte[] fileContents =  Files.readAllBytes(path);
//				outMedia.write(fileContents);
//				
				File srcimg = new File(root +req);
				RenderedImage img = ImageIO.read(srcimg);
				
				//BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(fileContents));
				ImageIO.write(img, extension, outMedia);
				
				

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		// res.setContentType("text/html");
		// PrintWriter out = res.getWriter();
		// String name = req.getParameter("name");
		/*
		 * out.println("<HTML>"); //out.println("<HEAD> <TITLE> Hello," + name +
		 * "</TITLE></HEAD>"); out.println("<BODY>"); //out.println("Hello, " +
		 * name); out.println("</BODY>"); out.println("</HTML>");
		 */
		outMedia.close();
	}

	public void doPost(BufferedReader req, PrintWriter out, String root) {
		try {
			out.println(req);
			// FileReader fr = new FileReader("L:\\Mes
			// Documents\\RESEAUX\\HTTPServer\\src\\" + req);
			// BufferedReader br = new BufferedReader(fr);
			String strRead = req.readLine();
			// lecture de head
			while (strRead != null && !strRead.equals("")) {
				// out.print("<h4>test</h4>");
				out.print("<h1>" + strRead + "</h1>");
				strRead = req.readLine();

			}
			// lecture du body
			strRead = req.readLine();
			File file = new File(root +"received.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(root + "received.txt", true);

			BufferedWriter bw = new BufferedWriter(fw);
			while (strRead != null && !strRead.equals("")) {
				// out.print("<h4>test</h4>");
				out.print("<h1>" + strRead + "</h1>");
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

		out.close();
	}

	public void doHead(String req, PrintWriter out, String root) {
		try {
			out.println(req);
			FileReader fr = new FileReader(root + req);
			BufferedReader br = new BufferedReader(fr);
			String str =".";
			while ((str) != null && !str.equals("")) {
				
				//les headers sont séparés du reste par un saut de ligne
				
				// out.print("<h4>test</h4>");
				
				str = br.readLine();
				//if(str.substring(1,6).equals("/HEAD")) break;
				//si on considère que la balise </HEAD" est mise sur une nouvelle ligne
				
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

	public void doPut(String req, PrintWriter out, String root) {
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

	public void doDelete(String req, PrintWriter out,String root) {
		try {
			Path path =  FileSystems.getDefault().getPath(root + req);
			Files.deleteIfExists(path);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		out.close();
	}

	String getExtension(String request) {
		if(request != null && request.contains(".")){
			String[] result = request.split("\\.");
						
			return result[result.length -1];
		}
		else
		{
			return "none";
		}
		
	}
}

// contenu binaire = fileinputstream

//http://www.javapractices.com/topic/TopicAction.do?Id=245