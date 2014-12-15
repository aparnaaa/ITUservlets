import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;

public class BloodDonorSearch extends HttpServlet {
  public void doGet(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException {

// class to write out to the log files
   ServletContext sc = getServletContext();
   response.setContentType("text/html");
   PrintWriter out = response.getWriter();
   String docType = "<!DOCTYPE HTML>\n";
   out.println(docType +
                "<HTML>\n" +
                "<HEAD><TITLE>Blood Donor Search</TITLE></HEAD>\n" +
                "<BODY>\n" +
                "<H1>Registered Blood Donor</H1>\n");

// Get the Data from the Database 
    try {
		Class.forName("oracle.jdbc.OracleDriver");
		System.out.println("Driver loaded");

		String url="jdbc:oracle:thin:@localhost:1521:xe";
		String user = "CON1";
		String pwd = "Sricharan1";
		Connection DB_mobile_conn = DriverManager.getConnection(url,user,pwd);
		String searchType = request.getParameter("searchType");
		String searchString = request.getParameter("searchString");
		String selectBloodGroup = request.getParameter("selectBloodGroup");
		String bloodGroup = request.getParameter("bloodGroup");
		
		//Either blood group or criteria should have value else throw exception
		if ( (searchType == null || searchString == null) && selectBloodGroup == null) {
			throw new Exception("Select appropriate input");
		}
		
		String query = "select * from BLOOD_DONOR WHERE ";
		if (selectBloodGroup != null && !selectBloodGroup.equals("")){
			query = query + "blood_group ='" +selectBloodGroup + "'";
		} else if (searchType.equals("gender")) {
			query = query + "gender = '" + searchString + "'";
		} else if (searchType.equals("email")) {
		    query = query + "email = '" + searchString + "'";
		} 
		
		query = query + " order by donor_id asc";
		
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
