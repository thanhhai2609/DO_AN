package controller.web.login;

import encode.MaHoa; 
import dao.UserDAO;
import model.UserDTO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/RegisterServlet"})
public class RegisterServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RegisterServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegisterServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            String fName = request.getParameter("firstname");
            String lName = request.getParameter("lastname");
            String uName = request.getParameter("username");
            String uPass = request.getParameter("password");
            String email = request.getParameter("email");
            String avatar = request.getParameter("avatar");
            String action = request.getParameter("action");
            String message;

            UserDAO ud = new UserDAO();
            response.setContentType("text/html;charset=UTF-8");
            if (action != null && action.equals("CheckDuplicate")) {
                PrintWriter out = response.getWriter();
                String username = request.getParameter("username");
                boolean isDuplicate = ud.checkUserNameDuplicate(uName);
                if (isDuplicate) {
                    request.setAttribute("DUPLICATE", 1);
                    out.println("<h6 style='color: red'>Tên người dùng đã tồn tại!</h6>");
                }
                request.setAttribute("DUPLICATE", 0);
                return;
            }

            boolean isDup = ud.checkUserNameDuplicate(uName);
            if (isDup) {
                message = "Tên người dùng đã tồn tại!";
                request.setAttribute("ERROR", message);
                request.getRequestDispatcher("view/jsp/home/login.jsp").forward(request, response);
            } else {
                // Mã hóa mật khẩu bằng SHA-1
                String hashedPassword = MaHoa.toSHA1(uPass);

                UserDTO user = new UserDTO(0, fName, lName, email, (avatar == null ? "assets/img/users/user.jpg" : avatar), uName, hashedPassword, "", "", 2, true);
                ud.registerUser(user);
                message = "Đăng ký thành công. Vui lòng đăng nhập!";

                request.setAttribute("SUCCESS", message);
                request.getRequestDispatcher("view/jsp/home/login.jsp").forward(request, response);
            }
        } catch (Exception ex) {
            log("RegisterServlet error:" + ex.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
