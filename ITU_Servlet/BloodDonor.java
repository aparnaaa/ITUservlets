import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;

public class BloodDonor extends HttpServlet {
  public void doGet(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException {

// class to write out to the log files
   ServletContext sc = getServletContext();
   response.setContentType("text/html");
   PrintWriter out = response.getWriter();
   String docType = "<!DOCTYPE HTML>\n";
   out.println(docType +
                "<HTML>\n" +
                "<HEAD><TITLE>BloodDonor Applicants</TITLE></HEAD>\n" +
                "<BODY>\n" +
                "<H1>BloodDonor Applicants</H1>\n");

// Get the Data from the Database 
    try {
		Class.forName("oracle.jdbc.OracleDriver");
		System.out.println("Driver loaded");

		String url="jdbc:oracle:thin:@localhost:1521:xe";
		String user = "CON1";
		String pwd = "Sricharan1";
		String name = request.getParameter("name");
		String telephone = request.getParameter("telephone");
		String email = request.getParameter("email");
		String birthdate = request.getParameter("birthDate");
		String age = request.getParameter("age");
		String gender = request.getParameter("gender");
		String group = request.getParameter("group");
//		System.out.println("animals array from browser = "+animals);
		
		Connection DB_mobile_conn = DriverManager.getConnection(url,user,pwd);
		System.out.println("Database Connect ok");//DONOR_SEQ.CURRVAL
		PreparedStatement prep_stmt = DB_mobile_conn.prepareStatement("INSERT INTO BLOOD_DONOR VALUES (DONOR_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)");
		prep_stmt.setString(1,name);
		prep_stmt.setString(2,telephone);
		prep_stmt.setString(3,email);
		prep_stmt.setString(4,birthdate);
		prep_stmt.setString(5,age);
		prep_stmt.setString(6,gender);
		prep_stmt.setString(7,group);
		prep_stmt.executeUpdate();
		
		String query = "select * from BLOOD_DONOR ORDER BY donor_id";
		
		if (query != null) {
			Statement query_stmt=DB_mobile_conn.createStatement();
			ResultSet query_rs=query_stmt.executeQuery(query);
			int columnCount = query_rs.getMetaData().getColumnCount();
			out.println("Query is <i>"+query+"</i><br>");
			out.println("<table border=1 cellpadding=3 cellspacing=0>");
			out.println("<tr bgcolor=#C0C0C0>");
			for (int i = 1; i <= columnCount; i++) {
				out.println("<td>");
				out.println("<b>"+query_rs.getMetaData().getColumnName(i)+"</b>");
				out.println("</td>");
			}
			out.println("</tr>");
			
			while (query_rs.next()) {
				out.println("<tr>");
				for (int i = 1; i <= columnCount; i++) {
					out.println("<td>");
					out.println(query_rs.getString(i));
					out.println("</td>");
				}
				out.println("</tr>");
				sc.log("Column Returned");
			}
			out.println("</table><br>");
			query_rs.close();
			query_stmt.close();
		}
		
    } catch (Exception exp) {
		out.println("Exception = " +exp);
		System.out.println("Exception = " +exp);
		exp.printStackTrace();
    }
	
    out.println(docType +"</BODY></HTML>");
  }
  
   public void doPost(HttpServletRequest request, HttpServletResponse response)
   throws IOException, ServletException
    {
        doGet(request, response);
    }
}
