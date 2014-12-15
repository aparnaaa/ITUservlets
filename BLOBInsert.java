import java.sql.*;
import java.io.*;

// Make sure you have a table that can accept a BLOB for this to work
// CREATE TABLE LARGE_OBJECT_TEST(NAME VARCHAR2(30), BINARY_FILE BLOB)

public class BLOBInsert {


	public static void main (String[] args) {


		try {

// Check if the arguments to his program are right or not 

			if (args.length != 1) {
	    			System.err.println("Usage:  java BLOBInsert binary_file.ext");
	    			System.exit(1);
			}

// Make the connection to the DB

			Class.forName("oracle.jdbc.OracleDriver");
			System.out.println("Driver loaded");

			String url="jdbc:oracle:thin:@localhost:1521:xe";
			String user = "win7";
			String pwd = "win7";

			Connection DB_mobile_conn = DriverManager.getConnection(url,user,pwd);
			System.out.println("Database Connect ok");

// load the binary file into the DB

			String binary_file_name= args[0].trim();

            		File binary_file = new File(binary_file_name);
			FileInputStream fis = new FileInputStream(binary_file);
			BufferedInputStream bis = new BufferedInputStream(fis);

			byte[] bytes = new byte [(int) binary_file.length()];

			bis.read(bytes);
	

			PreparedStatement query_stmt=DB_mobile_conn.prepareStatement("insert into large_object_test values(?,?)");
			
			query_stmt.setString(1,binary_file_name);
			query_stmt.setBytes(2,bytes);
			query_stmt.executeUpdate();
			System.out.println("binary file loaded into Table ok");

// close all open data structures 

			query_stmt.close();
			bis.close();
			fis.close();
			DB_mobile_conn.close();


        	} catch (Exception exp){
			System.out.println("Exception = "+exp);
		}
	}
}
