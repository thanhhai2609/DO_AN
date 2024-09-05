/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.admin.management.order;

import dao.OrderDAO;
import dao.OrderItemDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.OrderDTO;
import model.OrderItem;


@WebServlet(name = "ManageOrderServlet", urlPatterns = {"/ManageOrderServlet"})
public class ManageOrderServlet extends HttpServlet {

    private final static String ORDER_PAGE = "view/jsp/admin/admin_order.jsp";
    private final static String ORDER_DETAIL_PAGE = "view/jsp/admin/admin_order_detail.jsp";
    private final static String CHANGE_STATUS = "changeStatus";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ORDER_PAGE;
        try {
            OrderDAO oDao = new OrderDAO();
            OrderItemDAO oIDao = new OrderItemDAO();
            List<OrderDTO> listOrders = oDao.getAllOrders();
            String action = request.getParameter("action");
            List<OrderDTO> listRecentOrders = oDao.getRecentOrders();
            
            if ("showdetail".equals(action)) {
                url = ORDER_DETAIL_PAGE;
                String bill_id = request.getParameter("bill_id");
                OrderDTO order = oDao.getOrdersByID(bill_id);
                int id = order.getOrderID();
                List<OrderItem> list = oIDao.getOrderItemByOrderId(id);
                request.setAttribute("LIST_PRODUCTS_IN_ORDER", list);

            } else if (CHANGE_STATUS.equals(action)) {
                oDao.UpdateStatus(request.getParameter("id"));
            }

            request.setAttribute("CURRENTSERVLET", "Order");
            request.setAttribute("LIST_ORDERS", listOrders);
            request.setAttribute("LAST_RECENT_ORDERS", listRecentOrders);
        } catch (Exception ex) {
            log("ManageOrderServlet error:" + ex.getMessage());
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
