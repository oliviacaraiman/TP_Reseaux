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
import java.text.SimpleDateFormat;
import java.util.LinkedList;

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
				InputStream readByte = remote.getInputStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(readByte));
				PrintStream outMedia = new PrintStream(remote.getOutputStream());
				
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

				// read the data sent. We basically ignore it,
				// stop reading once a blank line is hit. This
				// blank line signals the end of the client HTTP
				// headers.

				// Send the response for the welcome page
			//	doGet("",outMedia);
				 
				/**
				 * read the HTTP request and decide which of the following methods is called
				 */
				String str = ".";
				while (str != null && !str.equals("")) {
					str = in.readLine();

					System.out.println(str);
					if (str != null && str.substring(0, 3).equals("GET")) {
						String request = str.substring(5, str.length() - "HTTP\1.1".length() - 2);
						System.out.println(request);
						if (!request.equals("favicon.ico"))
							doGet(request, outMedia);
						str = "";
					} else if (str != null && str.substring(0, 4).equals("POST")) {
						String request = str.substring(6, str.length() - "HTTP\1.1".length() - 2);
						doPost(in, request, outMedia,readByte);
						str = "";
					} else if (str != null && str.substring(0, 4).equals("HEAD")) {
						String request = str.substring(6, str.length() - "HTTP\1.1".length() - 2);
						doHead(request, outMedia/*, root*/);
						str = "";
					} else if (str != null && str.substring(0, 3).equals("PUT")) {
						String request = str.substring(5, str.length() - "HTTP\1.1".length() - 2);
						doPut(in, request, outMedia,readByte);
						str = "";
					} else if (str != null && str.substring(0, 6).equals("DELETE")) {
						String request = str.substring(8, str.length() - "HTTP\1.1".length() - 2);
						doDelete(request, outMedia);
						str = "";
					} else {
						setStatus(501, outMedia);;
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
	
	/**
	 * HTTP GET method, returning the while web page identified by the URL
	 * @param req : the requested resource
	 * @param outMedia : the output stream (to send the response to the request)
	 * @throws IOException
	 */

	public void doGet(String req, PrintStream outMedia) throws IOException {
		try {
			outMedia.flush();
			
			if (req.equals("")) req = "welcome.html"; 
				
			String extension = getExtension(req);
			String err = "200 OK";

			Path path = Paths.get( req);
			byte[] fileContents = Files.readAllBytes(path);

			if (extension.equals("txt") || extension.equals("html")) {

				Header(err, "text", extension, outMedia, fileContents.length);
				outMedia.write(fileContents);
				outMedia.close();

			} else if (extension.equals("png") || extension.equals("jpeg") || extension.equals("jpg")) {

				Header(err, "image", extension, outMedia, fileContents.length);
				outMedia.write(fileContents);
				outMedia.close();

			}else if (extension.equals("mp3")) {

				Header(err, "audio", extension, outMedia, fileContents.length);
				outMedia.write(fileContents);
				outMedia.close();

			}else if (extension.equals("mp4")) {

				Header(err, "video", extension, outMedia, fileContents.length);
				outMedia.write(fileContents);
				outMedia.close();

			}
		} catch (IOException e) {
			setStatus(404, outMedia);
			e.printStackTrace();
		}
		outMedia.close();
	}

	/**
	 * HTTP POST method, extending the previous web page content 
	 * Useful data is collected from the request body
	 * @param in : input stream containing the input data
	 * @param req : the requested resource
	 * @param outMedia : the output stream (to send the response to the request)
	 * @param readByte : input data in bytes
	 * @throws IOException
	 */
	public void doPost(BufferedReader in, String req, PrintStream outMedia, InputStream readByte) throws IOException {
		try {
			String strRead = in.readLine();
			String content_type = "";
			int content_length = 0;
			while (strRead != null && !strRead.equals("")) {

				strRead = in.readLine();
				System.out.println("head : " + strRead);
				String[] infos = strRead.split(":");
				if(infos[0].equals("content-length") || infos[0].equals("Content-length"))
				{
					content_length = Integer.parseInt(infos[1].substring(1, infos[1].length()));
				}
			}
			
			char charRead = (char)in.read();
			FileOutputStream fos = new FileOutputStream(req,true);
			
			while (content_length !=1 /*&& strRead != null && !strRead.equals("null")*/) {
				content_length--;
				fos.write((byte)charRead);
				charRead = (char)in.read();
				System.out.println(charRead);
			}
			fos.write((byte)charRead);
			fos.write("\n".getBytes());
			fos.close();
			
			String extension = getExtension(req);
			Path path = Paths.get( req);
			byte[] fileContents = Files.readAllBytes(path);
			
			Header("200 OK", "text", extension, outMedia, fileContents.length);
			outMedia.write(fileContents);
			outMedia.close();
			
		} catch (IOException e) {
			setStatus(404, outMedia);
			e.printStackTrace();
		}
		outMedia.close();
	}

	/**
	 * HTTP HEAD method, sending the header of the response
	 * @param req: the requested resource
	 * @param outMedia : the output stream (to send the response to the request)
	 * @throws IOException
	 */
	public void doHead(String req, PrintStream outMedia) throws IOException {
		try {
			

			String extension = getExtension(req);
			String err = "200 OK";
			Path path = Paths.get(req);
			byte[] fileContents = Files.readAllBytes(path);
			String type = "";

			if (extension.equals("txt") || extension.equals("html")) {
				type = "text";
				Header(err, type, extension, outMedia, fileContents.length);

			} else if (extension.equals("png") || extension.equals("jpeg") || extension.equals("jpg")) {
				type = "image";
				Header(err, type, extension, outMedia, fileContents.length);
			}
			outMedia.close();

		} catch (IOException e) {
			setStatus(404, outMedia);
			e.printStackTrace();
		}
		outMedia.close();
	}

	/**
	 * HTTP PUT method, replacing the previous web page content/placing documents directly on the server
	 * Useful data is collected from the request body
	 * @param in : input stream containing the input data
	 * @param req : the requested resource
	 * @param outMedia : the output stream (to send the response to the request)
	 * @param readByte : input data in bytes
	 * @throws IOException
	 */
	
	// TO DO !!!!!!
	public void doPut(BufferedReader in, String req, PrintStream outMedia, InputStream readByte) {
		try {
//			out.println(req);
//			FileReader fr = new FileReader(root + req);
//			BufferedReader br = new BufferedReader(fr);
//			while ((br.readLine()) != null) {
//				out.print("<h1>" + br.read() + "</h1>");
//
//			}
//			br.close();
			
			//*****//
			File file = new File(req);
			if (!file.exists()) {
				file.createNewFile();
				//on parse body pour écrire dans file
				
				String strRead = in.readLine();
				String content_type = "";
				int content_length = 0;
				while (strRead != null && !strRead.equals("")) {

					strRead = in.readLine();
					System.out.println("head : " + strRead);
					String[] infos = strRead.split(":");
					if(infos[0].equals("content-length") || infos[0].equals("Content-length"))
					{
						content_length = Integer.parseInt(infos[1].substring(1, infos[1].length()));
					}
				}
				
				//char charRead = (char)in.read();
				byte byteRead = (byte) readByte.read();
				FileOutputStream fos = new FileOutputStream(req,true);
				
				while (content_length !=1 /*&& strRead != null && !strRead.equals("null")*/) {
					content_length--;
					//fos.write((byte)charRead);
					//charRead = (char)in.read();
					//System.out.println(charRead);
					fos.write(byteRead);
					byteRead = (byte) readByte.read();
					
				}
				fos.write(byteRead);
				//.write("\n".getBytes());
				fos.close();
				
				String extension = getExtension(req);
				Path path = Paths.get( req);
				byte[] fileContents = Files.readAllBytes(path);
				
				Header("200 OK", "text", extension, outMedia, fileContents.length);
				outMedia.write(fileContents);
				outMedia.close();
				
				
			}
			else
			{
				//error
				//file already exists
				//setStatus(?,outMedia);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		outMedia.close();
	}

	/**
	 * HTTP DELETE method, deleting documents from the server 
	 * @param req: the requested resource
	 * @param outMedia : the output stream (to send the response to the request)
	 */
	public void doDelete(String req, PrintStream out) {
		try {
			Path path = FileSystems.getDefault().getPath(req);
			Files.deleteIfExists(path);

		} catch (IOException e) {
			e.printStackTrace();
		}
		out.close();
	}

	/**
	 * Get the extension of the requested document
	 * @param request : the requested resource
	 * @return
	 */
	String getExtension(String request) {

		if (request != null && request.contains(".")) {
			String[] result = request.split("\\.");
			return result[result.length - 1];
		} else {
			return "none";
		}
	}

	/**
	 * set the authorization rules depending on the client's access rights and protection rules
	 * @param status : error code
	 * @param outMedia : output stream (to send the response to the request)
	 * @throws IOException
	 */
	public void setStatus(int status, PrintStream outMedia) throws IOException {
		switch (status){
		case 404: 
		{
			System.out.println("File not found !");
			String s = "<h1>404 Not found !</h1>";
			byte[] fileContents = s.getBytes();
			Header("404 not found", "text", "html", outMedia, fileContents.length);
			outMedia.write(fileContents);
			outMedia.close();
			break;
		}
		case 403: 
		{
			String s = "<h1>403 Forbidden !</h1>";
			byte[] fileContents = s.getBytes();
			Header("403 forbidden", "text", "html", outMedia, fileContents.length);
			outMedia.write(fileContents);
			outMedia.close();
			break;
		}
		case 501:
		{
			System.out.println("Not implemented!");
			String s = "<h1>501 Not implemented !</h1>";
			byte[] fileContents = s.getBytes();
			Header("501 not implemented", "text", "html", outMedia, fileContents.length);
			outMedia.write(fileContents);
			outMedia.close();
			break;
		}
		}
	}
	
	/**
	 * create response header
	 * @param err : status code
	 * @param type : resource content type
	 * @param extension : resource extension
	 * @param outMedia : output stream (to send the response to the request)
	 * @param size : resource content length
	 */
	void Header(String err, String type, String extension, PrintStream outMedia, int size) {
		outMedia.println("HTTP/1.0 " + err);
		outMedia.println("Content-Type:" + type + "/" + extension);
		outMedia.println("Server: Bot");
		outMedia.println("Content-Length: " + size);

		// this blank line signals the end of the headers
		outMedia.println("");
		// Send the HTML page
	}

}
