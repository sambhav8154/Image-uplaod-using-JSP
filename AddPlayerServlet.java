import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet(name = "AddPlayerServlet", urlPatterns = {"/addplayer"})
@MultipartConfig
public class AddPlayerServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
           
            Part p =  request.getPart("playername");
            Scanner scanner  = new Scanner( p.getInputStream());
            String playername = scanner.nextLine(); 
            
            
            Part photo =  request.getPart("playerphoto");
                        
           
            // Connect to Oracle
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","sambhav","sam");
            con.setAutoCommit(false);
            out.print("hello");
            PreparedStatement ps = con.prepareStatement("insert into players values(?,?)");
            ps.setString(1, playername);
            // size must be converted to int otherwise it results in error
            ps.setBinaryStream(2, photo.getInputStream(), (int)  photo.getSize());
            ps.executeUpdate();
            con.commit();
            con.close();
            out.println("Added Player Successfully. <p> <a href='listplayers'>List Players </a>");
        } 
        catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
        finally {            
            out.close();
        }
    }
}
