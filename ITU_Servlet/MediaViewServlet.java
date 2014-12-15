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

public class MediaViewServlet extends HttpServlet {
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

// Define Basic Variables

   	PrintWriter out = null;
   	ServletContext sc = getServletContext();
        
// Do it!
   
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "CON1", "Sricharan1");
            PreparedStatement ps = con.prepareStatement("select  MEDIA_TYPE, MEDIA_FILE from MEDIA2 where MEDIA_ID = ?");
            String media_id = request.getParameter("id");
            ps.setString(1,media_id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            Blob b = rs.getBlob("MEDIA_FILE");
            response.setContentType("MEDIA_TYPE");
            response.setContentLength((int)b.length());
            InputStream is = b.getBinaryStream();
            OutputStream os = response.getOutputStream();
            byte buf[] = new byte[(int)b.length()];
            is.read(buf);
            os.write(buf);
            os.close();
        }
        catch(Exception ex) {
             System.out.println(ex.getMessage());
        }
  }
}
