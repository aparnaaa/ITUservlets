import java.io.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import java.sql.*;


public class MediaStreamServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ServletContext sc = getServletContext();// for logging

		sc.log("BinaryPostServlet: received raw post data");

		String Load_FileName=null;
		String Load_Folder=null;
		String Media_Type=null;

		boolean fileuploaded=false;

		String ajaxUpdateResult="";

		DiskFileItemFactory  fileItemFactory = new DiskFileItemFactory ();

		// Set the size threshold, above which content will be stored on disk.
		fileItemFactory.setSizeThreshold(1024*1024*1024); //1 GIG

		ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
		
		try {			
			List items = uploadHandler.parseRequest(request);
			Iterator itr = items.iterator();
			while(itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				if (item.isFormField()) {
					ajaxUpdateResult += "Field " + item.getFieldName() + " with value: " + item.getString() + " is successfully read\n\r";
					sc.log(ajaxUpdateResult);
				} else {
					Media_Type=(item.getContentType()).trim();
					String fileName = item.getName();
					System.out.println("File Name "+fileName );
					Load_FileName= fileName;			
					InputStream in=item.getInputStream();
					byte[] bytes = new byte [ (int) item.getSize()];
					bytes = item.get();
					in.close();

// Make the connection to the DB
					Class.forName("oracle.jdbc.OracleDriver");
					System.out.println("Driver loaded");

					String url="jdbc:oracle:thin:@localhost:1521:xe";
					String user = "win7";
					String pwd = "win7";

					Connection DB_mobile_conn = DriverManager.getConnection(url,user,pwd);
					System.out.println("Database Connect ok");

// load the binary file into the DB          				
					PreparedStatement query_stmt=DB_mobile_conn.prepareStatement("insert into MEDIA2 values(MEDIA_SEQ.NEXTVAL,?,?,?)");
					query_stmt.setString(1,Load_FileName);
					query_stmt.setString(2,Media_Type);
					query_stmt.setBytes(3,bytes);
					query_stmt.executeUpdate();
					System.out.println("binary file loaded into Table ok");

					ajaxUpdateResult="file upload Successful";
					fileuploaded=true;
				}
			}

		} catch (FileUploadException e) {
			ajaxUpdateResult="file upload failed";
			fileuploaded=false;
			sc.log("Error encountered while uploading file",e);			
		} catch(Exception e) {
			ajaxUpdateResult="file upload failed";
			fileuploaded=false;
			sc.log("Error encountered while uploading file",e);
		}
// Send an update back to the client
		response.getWriter().println(ajaxUpdateResult);

	}

}

