import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/** Simple servlet used to test server.
 *  <P>
 *  Taken from Core Servlets and JavaServer Pages 2nd Edition
 *  from Prentice Hall and Sun Microsystems Press,
 *  http://www.coreservlets.com/.
 *  &copy; 2003 Marty Hall; may be freely used or adapted.
 */

public class HelloNPSServlet extends HttpServlet {
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    String docType = "<!DOCTYPE HTML>\n";
    out.println(docType +
                "<HTML LANG=\"en\">\n" +
                "<HEAD><TITLE>Hello NPS</TITLE></HEAD>\n" +
                "<BODY>\n" +
                "<h1>Hello NPS</h1>\n" +
                "</BODY></HTML>");
  }
}
