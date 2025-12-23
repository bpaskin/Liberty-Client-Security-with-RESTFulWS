import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Properties;

import com.ibm.websphere.ssl.JSSEHelper;

import javax.net.ssl.SSLContext;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;

@WebServlet(name = "Rest", urlPatterns ="/rest")
public class CallRestWS extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        try {
            SSLContext sslContext = JSSEHelper.getInstance().getSSLContext("clientSSLConfig", Collections.emptyMap(), null);

            Client client = ClientBuilder.newBuilder().sslContext(sslContext).build();
            
            Properties props = client.target("https://localhost:9444/RestfulWS/properties").request(MediaType.APPLICATION_JSON).get(Properties.class);

            out.println("<html><body>");
            out.println("<h2>Properties from RestfulWS:</h2>");
            out.println("<ul>");
            props.forEach((k, v) -> {
                out.println("<li>" + k + ": " + v + "</li>");
                System.out.println(k + ":" + v);
            });
            out.println("</ul>");
            out.println("</body></html>");
            
            client.close();
        } catch (Exception e) {
            out.println("<html><body>");
            out.println("<h2>Error occurred:</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("</body></html>");
            e.printStackTrace();
        } finally {
            out.close();
        }
    }
}
