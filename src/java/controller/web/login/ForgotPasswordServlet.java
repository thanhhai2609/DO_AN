
package controller.web.login;

import dao.UserDAO;
import java.io.IOException;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Email;
import model.UserDTO;


@WebServlet(name = "ForgotPasswordServlet", urlPatterns = {"/ForgotPasswordServlet"})
public class ForgotPasswordServlet extends HttpServlet {

    private static final String FORGOT_PAGE = "view/jsp/home/forgot_password.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = FORGOT_PAGE;
        HttpSession session = request.getSession();
        String emailInput = request.getParameter("txtEmail");
        String txtCode = request.getParameter("txtCode");
        String status = request.getParameter("status");
        String password = request.getParameter("txtPassword");
        String confirm = request.getParameter("txtConfirm");
        UserDAO ud = new UserDAO();
        Email handleEmail = new Email();
        String message = "";
        String check = null;
        UserDTO user = null;
        String code_str = null;
        String emailsession = null;
        try {
            if ("forgot".equals(status)) {
                request.setAttribute("STATUS", status);
            }
            if (emailInput != null) {
                user = ud.getUserByEmail(emailInput);
                if (user != null) {
                    Random random = new Random();
                    message = "EXIST - email hợp lệ, kiểm tra email của bạn để lấy mã đặt lại mật khẩu";
                    check = "true";
                    status = "confirm";
                    // Tạo số nguyên ngẫu nhiên có 6 chữ số
                    Integer code = 100000 + random.nextInt(900000);
                    code_str = code.toString();
                    String subject = handleEmail.subjectForgotPass();
                    String msgEmail = handleEmail.messageFogot(user.getUserName(), code);
                    handleEmail.sendEmail(subject, msgEmail, emailInput);
                } else {
                    message = "NOT EXIST - Invalid email";
                    check = "false";
                }
                emailsession = emailInput;
                session.setAttribute("email", emailsession);

            }
            if (txtCode != null) {
                code_str = (String) session.getAttribute("code");
                if (txtCode.equals(code_str)) {
                    message = "Mã đúng, nhập mật khẩu mới của bạn!";
                    check = "true";
                    status = "enterpass";
                } else {
                    message = "Mã sai, thử lại!";
                    check = "false";
                    status = "confirm";
                }
            }
            if (password != null && confirm != null) {
                if (password.equalsIgnoreCase(confirm)) {
                    String email = (String) session.getAttribute("email");
                    user = ud.getUserByEmail(email);
                    if (ud.updatePasswordUser(user, password)) {
                        message = "Mật khẩu mới đã được cập nhật";
                        check = "true";
                        status = "success";
                    } else {
                        message = "Lỗi, thử lại!";
                        check = "false";
                        status = "enterpass";
                    }
                } else {
                    message = "Mật khẩu không khớp, vui lòng thử lại!";
                    check = "false";
                    status = "enterpass";
                }
            }
            // 
            session.setAttribute("code", code_str);
            request.setAttribute("check", check);
            request.setAttribute("message", message);
            request.setAttribute("STATUS", status);

        } catch (Exception ex) {
            log("ForgotPasswordServlet error:" + ex.getMessage());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
