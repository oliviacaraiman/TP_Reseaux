//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.PrintStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//public void doHead(String req, PrintStream outMedia, String root) {
//	try {
//		// out.println(req);
//		// FileReader fr = new FileReader(root + req);
//		// BufferedReader br = new BufferedReader(fr);
//		// String str = ".";
//		// while ((str) != null && !str.equals("")) {
//		//
//		// // les headers sont séparés du reste par un saut de ligne
//		//
//		// // out.print("<h4>test</h4>");
//		//
//		// str = br.readLine();
//		//
//		//
//		// out.print("<h1>" + br.read() + "</h1>");
//		//
//		// }
//		/*
//		 * bw.write(out + "\n"); bw.newLine();
//		 */
//		// br.close();
//
//		String extension = getExtension(req);
//		String err = "200 OK";
//		Path path = Paths.get(root + req);
//		byte[] fileContents = Files.readAllBytes(path);
//
//		String type = "";
//
//		if (extension.equals("txt") || extension.equals("html")) {
//
//			type = "text";
//
//		} else if (extension.equals("png") || extension.equals("jpeg") || extension.equals("jpg")) {
//
//			type = "image";
//
//		}
//
//		// write header in a file
//		storeHeader(root + req, err, type, extension, outMedia, fileContents.length);
//		// build equivalent fileContents pour le nouveau fichier
//		String err_txt = "200 OK";
//		Path path_txt = Paths.get(root + req + "_header.txt");
//		byte[] txtContents = Files.readAllBytes(path_txt);
//		// print header du resource.ext_header.txt
//		Header(err_txt, "text", "txt", outMedia, txtContents.length);
//		// print header.txt
//		outMedia.print("\n");
//		outMedia.write(txtContents);
//		outMedia.close();
//
//		// String strRead = req.readLine();
//		// // lecture de head
//		// while (strRead != null && !strRead.equals("")) {
//		// // out.print("<h4>test</h4>");
//		// System.out.println("header: " + strRead);
//		// // outMedia.print("<h1>" + strRead + "</h1>");
//		// strRead = req.readLine();
//		//
//		// }
//		// // lecture du body
//		// strRead = req.readLine();
//		// File file = new File(root + "received.txt");
//		// if (!file.exists()) {
//		// file.createNewFile();
//		// }
//		// FileWriter fw = new FileWriter(root + "received.txt", true);
//		//
//		// BufferedWriter bw = new BufferedWriter(fw);
//		// while (strRead != null && !strRead.equals("")) {
//		// // out.print("<h4>test</h4>");
//		// outMedia.print("<h1>" + strRead + "</h1>");
//		//
//		// bw.write(strRead);
//		// bw.newLine();
//		// strRead = req.readLine();
//		// bw.write(strRead + "\n");
//		// bw.newLine();
//		// }
//		//
//		// bw.close();
//
//	} catch (IOException e) {
//		e.printStackTrace();
//	}
//
//	outMedia.close();
//}
//
//public void storeHeader(String filename, String err, String type, String extension, PrintStream outMedia,
//		int size) {
//
//	try {
//
//		File file = new File(filename + "_header.txt");
//		if (!file.exists()) {
//			file.createNewFile();
//		}
//		FileWriter fw = new FileWriter(filename + "_header.txt", true);
//
//		BufferedWriter bw = new BufferedWriter(fw);
//
//		bw.write("HTTP/1.0 " + err);
//		bw.newLine();
//		bw.write("Content-Type: " + type + "/" + extension);
//		bw.newLine();
//		bw.write("Server: Bot");
//		bw.newLine();
//		bw.write("Content-Length: " + size);
//		bw.newLine();
//
//		bw.close();
//
//	} catch (IOException e) {
//
//
//	}
//}