import java.sql.*;
import java.io.*;


public class BLOBRetrieve {


	public static void main (String[] args) {


		try {

// Check command line parameters

			if (args.length != 2) {
	    			System.err.println("Usage:  java BLOBRetrieve binary_file_name destination_folder");
	    			System.exit(1);
			}

// Connect to the DB

			Class.forName("oracle.jdbc.OracleDriver");
			System.out.println("Driver loaded");

			String url="jdbc:oracle:thin:@localhost:1521:xe";
			String user = "win7";
			String pwd = "win7";

			Connection DB_mobile_conn = DriverManager.getConnection(url,user,pwd);
			System.out.println("Database Connect ok");

			String binary_file_name=args[0].trim();
			String dest_folder=args[1].trim();


// Retrieve the BLOB from the Database 

			PreparedStatement query_stmt=DB_mobile_conn.prepareStatement("select * from large_object_test where name=?");

			query_stmt.setString(1,binary_file_name);
			ResultSet query_rs=query_stmt.executeQuery();

			System.out.println("BLOB retrieved from Table ok");

// Write the BLOB to a file 


			byte[] binary_file;

			FileOutputStream fos;
			BufferedOutputStream bos;
			fos = new FileOutputStream (dest_folder+"\\"+binary_file_name);
			bos = new BufferedOutputStream(fos);

			// get the BLOB
			query_rs.next();
			binary_file = query_rs.getBytes("binary_file");
			bos.write(binary_file);

			System.out.println("Binary file written ok");

			
			bos.close();
			fos.close();
			query_stmt.close();
			query_rs.close();
			DB_mobile_conn.close();


        	} catch (Exception exp){
			System.out.println("Exception = "+exp);
		}
	}
}
