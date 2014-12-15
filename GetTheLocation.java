import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;

public class GetTheLocation extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {

	ServletContext sc = getServletContext();
      	PrintWriter out = response.getWriter();

	String Command= request.getParameter("COMMAND");

//Get the data from the client 
	if (Command.equals("GETDATA")) {


	  try {
		Class.forName("oracle.jdbc.OracleDriver");
		System.out.println("Driver loaded");

		String url="jdbc:oracle:thin:@localhost:1521:xe";
		String user = "win7";
		String pwd = "win7";

		Connection DB_mobile_conn = DriverManager.getConnection(url,user,pwd);
		Statement query_stmt=DB_mobile_conn.createStatement();

		String query="SELECT * FROM TRACKER_TABLE";
		ResultSet query_rs=query_stmt.executeQuery(query);
		ResultSetMetaData rsmd = query_rs.getMetaData();
		int queryColCount = rsmd.getColumnCount();
//		System.out.println("The column count of the query is = "+queryColCount);


		String outSTR =("[");
/*		
		String colName="";
		for ( int i = 1; i <= queryColCount; i++) {
			colName= colName + "<td>"+rsmd.getColumnName(i)+"</td>";
//			System.out.print(" "+rsmd.getColumnName(i);
		}

   		out.println("<tr>"+colName +"</tr>" );
*/


		while (query_rs.next()) {
			sc.log("Column Returned");
			String row = "";
			for (int col=1; col <= queryColCount;col++){
				row = row + "\""+ rsmd.getColumnName(col)+"\":\""+query_rs.getString(col)+"\"," ;

			}
    			outSTR=outSTR+"{"+row.substring(0,row.length()-1)+"},";
		}
		outSTR=outSTR.substring(0,outSTR.length()-1)+"]";
		out.println(outSTR);

		query_stmt.close();
		query_rs.close();

//		System.out.println(query);
//		out.println(query);
	  
           } catch (Exception exp) {
		out.println("Exception = " +exp);
		System.out.println("Exception = " +exp);
    	   }
	} else {
	
// Insert the Data into the Database 
    	  try {
		Class.forName("oracle.jdbc.OracleDriver");
		System.out.println("Driver loaded");

		String url="jdbc:oracle:thin:@localhost:1521:xe";
		String user = "win7";
		String pwd = "win7";

		Connection DB_mobile_conn = DriverManager.getConnection(url,user,pwd);
		System.out.println("Database Connect ok");
		String DateTime= request.getParameter("DateTime");
		String Latitude= request.getParameter("Latitude");
		String Longitude= request.getParameter("Longitude");

		String result = DateTime +" Latitude: "+ Latitude +" Longitude: "+Longitude;
		System.out.println("result string : "+result);
		out.println(result);
	
		PreparedStatement query_stmt=DB_mobile_conn.prepareStatement("insert into TRACKER_TABLE values(?,?,?)");
			
		query_stmt.setString(1,DateTime);
		query_stmt.setString(2,Longitude);
		query_stmt.setString(3,Latitude);
		query_stmt.executeUpdate();
		System.out.println("Location Data inserted in the DB OK");

		query_stmt.close();
		DB_mobile_conn.close();

           } catch (Exception exp) {
		out.println("Exception = " +exp);
		System.out.println("Exception = " +exp);
    	   }
	}


    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {
        doGet(request, response);
    }
}
