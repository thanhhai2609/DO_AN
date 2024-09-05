package controller.admin;

import dao.OrderDAO;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ChartServlet", urlPatterns = {"/ChartServlet"})
public class ChartServlet extends HttpServlet {
    private static final String CHART = "view/jsp/admin/admin_chart.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        OrderDAO oDao = new OrderDAO();
        try {
            double year2018 = oDao.getTotalMoneyByYear(2018);
            double year2019 = oDao.getTotalMoneyByYear(2019);
            double year2020 = oDao.getTotalMoneyByYear(2020);
            double year2021 = oDao.getTotalMoneyByYear(2021);
            double year2022 = oDao.getTotalMoneyByYear(2022);
            double year2023 = oDao.getTotalMoneyByYear(2023);
            double year2024 = oDao.getTotalMoneyByYear(2024);
            
            double month1 = oDao.getTotalMoneyByMonth(1);
            double month2 = oDao.getTotalMoneyByMonth(2);
            double month3 = oDao.getTotalMoneyByMonth(3);
            double month4 = oDao.getTotalMoneyByMonth(4);
            double month5 = oDao.getTotalMoneyByMonth(5);
            double month6 = oDao.getTotalMoneyByMonth(6);
            double month7 = oDao.getTotalMoneyByMonth(7);
            double month8 = oDao.getTotalMoneyByMonth(8);
            double month9 = oDao.getTotalMoneyByMonth(9);
            double month10 = oDao.getTotalMoneyByMonth(10);
            double month11 = oDao.getTotalMoneyByMonth(11);
            double month12 = oDao.getTotalMoneyByMonth(12);

//            double day1 = oDao.getTotalMoneyByDayOfMonth(1);
//            double day2 = oDao.getTotalMoneyByDayOfMonth(2);
//            double day3 = oDao.getTotalMoneyByDayOfMonth(3);
//            double day4 = oDao.getTotalMoneyByDayOfMonth(4);
//            double day5 = oDao.getTotalMoneyByDayOfMonth(5);
//            double day6 = oDao.getTotalMoneyByDayOfMonth(6);
//            double day7 = oDao.getTotalMoneyByDayOfMonth(7);
//            double day8 = oDao.getTotalMoneyByDayOfMonth(8);
//            double day9 = oDao.getTotalMoneyByDayOfMonth(9);
//            double day10 = oDao.getTotalMoneyByDayOfMonth(10);
//            double day11 = oDao.getTotalMoneyByDayOfMonth(11);
//            double day12 = oDao.getTotalMoneyByDayOfMonth(12);
//            request.setAttribute("DAY1", day1);
//            request.setAttribute("DAY2", day2);
//            request.setAttribute("DAY3", day3);
//            request.setAttribute("DAY4", day4);
//            request.setAttribute("DAY5", day5);
//            request.setAttribute("DAY6", day6);
//            request.setAttribute("DAY7", day7);
//            request.setAttribute("DAY8", day8);
//            request.setAttribute("DAY9", day9);
//            request.setAttribute("DAY10", day10);
//            request.setAttribute("DAY11", day11);
//            request.setAttribute("DAY12", day12);
            request.setAttribute("YEAR18", year2018);
            request.setAttribute("YEAR19", year2019);
            request.setAttribute("YEAR20", year2020);
            request.setAttribute("YEAR21", year2021);
            request.setAttribute("YEAR22", year2022);
            request.setAttribute("YEAR23", year2023);
            request.setAttribute("YEAR24", year2024);
            
            request.setAttribute("MONTH1", month1);
            request.setAttribute("MONTH2", month2);
            request.setAttribute("MONTH3", month3);
            request.setAttribute("MONTH4", month4);
            request.setAttribute("MONTH5", month5);
            request.setAttribute("MONTH6", month6);
            request.setAttribute("MONTH7", month7);
            request.setAttribute("MONTH8", month8);
            request.setAttribute("MONTH9", month9);
            request.setAttribute("MONTH10", month10);
            request.setAttribute("MONTH11", month11);
            request.setAttribute("MONTH12", month12);
            
            int selectedMonth = Integer.parseInt(request.getParameter("month")); 
            if (selectedMonth < 1 || selectedMonth > 12) {
                selectedMonth = Calendar.getInstance().get(Calendar.MONTH) + 1; 
            }
            double[] dailyTotals = oDao.getTotalMoneyByDayOfMonth(selectedMonth); 
            request.setAttribute("DAILY_TOTALS", Arrays.toString(dailyTotals));
            request.setAttribute("SELECTED_MONTH", selectedMonth);
            
            request.setAttribute("CURRENTSERVLET", "Chart");
        } catch (Exception ex) {
            log("CharSerlvet error:" + ex.getMessage());
        } finally {
            request.getRequestDispatcher(CHART).forward(request, response);
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
