import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;

/** Simple servlet used to test server.
 *  <P>
 *  Taken from Core Servlets and JavaServer Pages 2nd Edition
 *  from Prentice Hall and Sun Microsystems Press,
 *  http://www.coreservlets.com/.
 *  &copy; 2003 Marty Hall; may be freely used or adapted.
 */

public class HelloServletDB extends HttpServlet {
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
      throws ServletException, IOException {

// class to write out to the log files

   ServletContext sc = getServletContext();

   response.setContentType("text/html");
   PrintWriter out = response.getWriter();
   String docType = "<!DOCTYPE HTML>\n";
   out.println(docType +
                "<HTML>\n" +
                "<HEAD><TITLE>Hello DB</TITLE></HEAD>\n" +
                "<BODY>\n" +
                "<H1>Hello Oracle XE DB</H1>\n");


// Get the Data from the Database 

    try {

		Class.forName("oracle.jdbc.OracleDriver");
		System.out.println("Driver loaded");

		String url="jdbc:oracle:thin:@localhost:1521:xe";
		String user = "win7";
		String pwd = "win7";


		String query="select name from test_table ";

		Connection DB_mobile_conn = DriverManager.getConnection(url,user,pwd);
		System.out.println("Database Connect ok");

		Statement query_stmt=DB_mobile_conn.createStatement();

		ResultSet query_rs=query_stmt.executeQuery(query);

		while (query_rs.next()) {
//    			System.out.print("Column  returned ");
			sc.log("Column Returned");
//    			System.out.println(query_rs.getString(1));
    			out.println(docType + "<H1> column 1 = "+query_rs.getString(1)+"</H1>\n" );

		}

		query_rs.close();
		query_stmt.close();
		DB_mobile_conn.close();

    } catch (Exception exp) {
	out.println("Exception = " +exp);
	System.out.println("Exception = " +exp);
    }
    out.println(docType +"</BODY></HTML>");
  }
}
