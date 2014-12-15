import java.io.*;
import java.sql.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;



/** Simple servlet used to test server.
 *  <P>
 *  Taken from Core Servlets and JavaServer Pages 2nd Edition
 *  from Prentice Hall and Sun Microsystems Press,
 *  http://www.coreservlets.com/.
 *  &copy; 2003 Marty Hall; may be freely used or adapted.
 */

public class MediaStreamView extends HttpServlet {
  public void doGet(HttpServletRequest inRequest, HttpServletResponse outResponse)
      throws ServletException, IOException {

// Define Basic Variables

   			ServletContext sc = getServletContext();
   			InetAddress thisIP=null;
   
   
// Get the Image from the Database 

    try {

// Setup the HTML response to be sent to the browser

   			PrintWriter out = null;
  			outResponse.setContentType("text/html");
			out = outResponse.getWriter();
// Connect to the DB
			Class.forName("oracle.jdbc.OracleDriver");
			System.out.println("Driver loaded");

			String url="jdbc:oracle:thin:@localhost:1521:xe";
			String user = "CON1";
			String pwd = "Sricharan1";

			Connection DB_mobile_conn = DriverManager.getConnection(url,user,pwd);
			System.out.println("Database Connect ok");

// Get the IP of the webserver

			try {
				thisIP = InetAddress.getLocalHost();
			} catch (Exception exp) {
				sc.log("IP = "+thisIP.getHostAddress()+" Exception = " +exp);
			}

// Select all rows from the large Object table
			PreparedStatement query_stmt=DB_mobile_conn.prepareStatement("select * from MEDIA2");
			ResultSet query_rs=query_stmt.executeQuery();

// Get the ResultSet Metadata
			ResultSetMetaData rsmd = query_rs.getMetaData();
			int queryColCount = rsmd.getColumnCount();
//		System.out.println("The column count of the query is = "+queryColCount);

    			out.println("<!DOCTYPE HTML>");
                	out.println("<HTML LANG=\"en\">");
                	out.println("<HEAD><TITLE>Hello NPS MEDIA TEST</TITLE></HEAD>");
                	out.println("<BODY>");

			out.println("<table border=\"1\">");
		
			String Media_Type=null;
			String Media_ID=null;
			int rowcount=0;

			while (query_rs.next()) {
				if (rowcount == 0) {
					String colName="";
					for ( int i = 1; i <= queryColCount; i++) { 
						colName= colName + "<td>"+rsmd.getColumnName(i)+"</td>";
					}
  					out.println("<tr>"+colName +"</tr>" );
					rowcount++;
				}
//				sc.log("Column Returned");
				String row = "";
				for (int col=1; col <= queryColCount;col++){
					if (col==1) {
						Media_ID=query_rs.getString(col);
						row = row + "<td>"+ query_rs.getString(col)+"</td>" ;
					} else if (col==2){
						row = row + "<td>"+ query_rs.getString(col)+"</td>"; 						
					} else  if (col==3) {
						Media_Type=(query_rs.getString(col)).trim();
						row = row + "<td>"+ query_rs.getString(col)+"</td>";						
					}else if (col==4) {
						if (Media_Type.equals("image/jpeg") || Media_Type.equals("image/png") || Media_Type.equals("image/gif") || Media_Type.equals("image/tiff") || Media_Type.equals("image/png")) {
							row = row + "<td>"+"<img src =\"http://"+thisIP.getHostAddress()+"/examples/servlets/servlet/MediaViewServlet?id="+Media_ID+"\" height=\"320\" width=\"240\">"+"</td>";
						} else if (Media_Type.equals("video/mp4") || Media_Type.equals("video/mpeg") || Media_Type.equals("video/ogg")) {
							row = row + "<td>"+"<video width=\"320\" height=\"240\" controls><source src=\"http://"+thisIP.getHostAddress()+"/examples/servlets/servlet/MediaViewServlet?id="+Media_ID+"\" type=\""+Media_Type+"\"></video></td>";
						} else if (Media_Type.equals("audio/mp3") || Media_Type.equals("audio/mp4") || Media_Type.equals("audio/mpeg") || Media_Type.equals("audio/ogg")) {
							row = row + "<td>"+"<audio width=\"320\" height=\"240\" controls><source src=\"http://"+thisIP.getHostAddress()+"/examples/servlets/servlet/MediaViewServlet?id="+Media_ID+"\" type=\""+Media_Type+"\"></audio></td>";
						} else {
							row = row + "<td>Unsupported Media</td>";
							System.out.println("Unsupported Media");
						}
					}else {
						System.out.println("Invalid Column");
					}
				}
    				out.println("<tr>"+row +"</tr>" );
			}
			out.println("</table>");
                	out.println("</BODY>");
			out.println("</HTML>");
			query_stmt.close();
			query_rs.close();
			DB_mobile_conn.close();
    } catch (Exception exp) {
	sc.log("Exception = " +exp);
    }
  }
}
