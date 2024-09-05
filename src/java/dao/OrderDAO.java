package dao;

import utils.DBContext;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.OrderDTO;
import model.PaymentDTO;
import model.UserDTO;

public class OrderDAO extends DBContext {

    private UserDAO uDao = new UserDAO();
    private PaymentDAO pDao = new PaymentDAO();

    private static final String GET_TOTAL_SALE = "SELECT SUM(totalprice) AS TotalSale FROM Orders";
    private static final String GET_TOTAL_MONEY_DAY = "SELECT SUM(totalprice) AS TotalSale FROM Orders WHERE DAY(orderdate) = ? AND Status = 1";
    private static final String GET_TOTAL_MONEY_YEAR = "SELECT SUM(totalprice) AS TotalSale FROM Orders WHERE YEAR(orderdate) = ? AND Status = 1";
    private static final String GET_TOTAL_MONEY_MONTH = "SELECT SUM(totalprice) AS TotalSale FROM Orders WHERE MONTH(orderdate) = ? AND Status = 1";
    private static final String GET_NUMBER_ORDERS = "SELECT COUNT(*) AS Total FROM Orders";
    private static final String GET_TOTAL_ORDERS = "SELECT * FROM Orders";
    private static final String GET_TOTAL_SALE_TODAY = "SELECT SUM(totalprice) AS TotalSale FROM Orders WHERE DATE(orderdate) = CURDATE()";
    private static final String GET_ORDERS_USER = "SELECT * FROM Orders WHERE user_id = ?";
    private static final String GET_ORDERS_BYID = "SELECT * FROM Orders WHERE order_id = ?";
    private static final String GET_RECENT_ORDERS = "SELECT * FROM Orders ORDER BY orderdate DESC LIMIT 10;";
    private static final String UPDATE_STATUS = "UPDATE Orders SET status = 1 WHERE order_id = ?";
    private static final String GET_LATEST_ORDER = "SELECT * FROM Orders ORDER BY order_id DESC LIMIT 1";
    private static final String CREATE_ORDER = "INSERT INTO Orders (orderdate, totalprice, paymentid, user_id, status) VALUES (?, ?, ?, ?, 0)";

    public double getTotalSale() throws SQLException {
        double result = 0;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_TOTAL_SALE);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    result = rs.getDouble("TotalSale");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ptm, rs);
        }
        return result;
    }

    public double getTotalSaleToday() throws SQLException {
        double result = 0;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_TOTAL_SALE_TODAY);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    result = rs.getDouble("TotalSale");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ptm, rs);
        }
        return result;
    }

    
    
    public double getTotalMoneyByYear(int year) throws SQLException {
        double result = 0;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_TOTAL_MONEY_YEAR);
                ptm.setInt(1, year);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    result = rs.getDouble("TotalSale");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ptm, rs);
        }
        return result;
    }
    public double[] getTotalMoneyByDayOfMonth(int month) throws SQLException {
           Connection con = null;
           PreparedStatement pstmt = null;
           ResultSet rs = null;
           double[] dailyTotals = new double[31]; // Assuming a maximum of 31 days in a month

           try {
               con = getConnection(); // Replace with your method to get DB connection
               String sql = "SELECT DAY(orderDate) AS day, SUM(totalPrice) AS total "
                          + "FROM Orders "
                          + "WHERE MONTH(orderDate) = ? "
                          + "GROUP BY DAY(orderDate) "
                          + "ORDER BY DAY(orderDate)";
               pstmt = con.prepareStatement(sql);
               pstmt.setInt(1, month);
               rs = pstmt.executeQuery();

               while (rs.next()) {
                   int day = rs.getInt("day");
                   double total = rs.getDouble("total");
                   dailyTotals[day - 1] = total; 
               }
           } catch (SQLException e) {
               e.printStackTrace();
               throw e; // Handle exceptions as necessary
           } finally {
               // Clean up resources
               if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
               if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
               if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
           }

           return dailyTotals;
       }
 
    public double getTotalMoneyByMonth(int month) throws SQLException {
        double result = 0;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_TOTAL_MONEY_MONTH);
                ptm.setInt(1, month);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    result = rs.getDouble("TotalSale");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ptm, rs);
        }
        return result;
    }

    public List<OrderDTO> getRecentOrders() throws SQLException {
        List<OrderDTO> orders = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_RECENT_ORDERS);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    Date orderDate = rs.getDate("orderdate");
                    double totalPrice = rs.getDouble("totalprice");
                    int paymentId = rs.getInt("paymentid");
                    PaymentDTO payment = pDao.getPaymentById(paymentId);
                    int userID = rs.getInt("user_id");
                    UserDTO user = uDao.getUserByID(userID);
                    boolean status = rs.getBoolean("status");
                    OrderDTO order = new OrderDTO(orderId, orderDate, totalPrice, payment, user, status);
                    orders.add(order);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ptm, rs);
        }
        return orders;
    }

    public OrderDTO getTheLatestOrder() throws SQLException {
        OrderDTO order = null;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_LATEST_ORDER);
                rs = ptm.executeQuery();
                if (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    Date orderDate = rs.getDate("orderdate");
                    double totalPrice = rs.getDouble("totalprice");
                    int paymentId = rs.getInt("paymentid");
                    PaymentDTO payment = pDao.getPaymentById(paymentId);
                    int user_id = rs.getInt("user_id");
                    UserDTO user = uDao.getUserByID(user_id);
                    boolean status = rs.getBoolean("status");
                    order = new OrderDTO(orderId, orderDate, totalPrice, payment, user, status);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ptm, rs);
        }
        return order;
    }

    
    
    public List<OrderDTO> getOrdersByUserID(int userID) throws SQLException {
            List<OrderDTO> orders = new ArrayList<>();
            Connection conn = null;
            PreparedStatement ptm = null;
            ResultSet rs = null;
            try {
                conn = getConnection();
                if (conn != null) {
                    ptm = conn.prepareStatement(GET_ORDERS_USER);
                    ptm.setInt(1, userID);
                    rs = ptm.executeQuery();
                    while (rs.next()) {
                        int orderId = rs.getInt("order_id");
                        Date orderDate = rs.getDate("orderdate");
                        double totalPrice = rs.getDouble("totalprice");
                        int paymentId = rs.getInt("paymentid");
                        PaymentDTO payment = pDao.getPaymentById(paymentId);
                        boolean status = rs.getBoolean("status");
                        UserDTO user = uDao.getUserByID(userID);
                        OrderDTO order = new OrderDTO(orderId, orderDate, totalPrice, payment, user, status);
                        orders.add(order);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeResources(conn, ptm, rs);
            }
            return orders;
        }

    public OrderDTO getOrdersByID(String id) throws SQLException {
        OrderDTO order = null;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_ORDERS_BYID);
                ptm.setString(1, id);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    Date orderDate = rs.getDate("orderdate");
                    double totalPrice = rs.getDouble("totalprice");
                    int paymentId = rs.getInt("paymentid");
                    PaymentDTO payment = pDao.getPaymentById(paymentId);
                    boolean status = rs.getBoolean("status");
                    int userId = rs.getInt("user_id");
                    UserDTO user = uDao.getUserByID(userId);
                    order = new OrderDTO(orderId, orderDate, totalPrice, payment, user, status);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ptm, rs);
        }
        return order;
    }

    public int getTotalOrders() throws SQLException {
        int result = 0;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_NUMBER_ORDERS);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    result = rs.getInt("Total");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ptm, rs);
        }
        return result;
    }

    public List<OrderDTO> getAllOrders() throws SQLException {
        List<OrderDTO> orders = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_TOTAL_ORDERS);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    Date orderDate = rs.getDate("orderdate");
                    double totalPrice = rs.getDouble("totalprice");
                    int paymentId = rs.getInt("paymentid");
                    PaymentDTO payment = pDao.getPaymentById(paymentId);
                    int ID = rs.getInt("user_id");
                    UserDTO user = uDao.getUserByID(ID);
                    boolean status = rs.getBoolean("status");
                    OrderDTO order = new OrderDTO(orderId, orderDate, totalPrice, payment , user, status);
                    orders.add(order);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ptm, rs);
        }
        return orders;
    }

    public void UpdateStatus(String orderId) throws SQLException {
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(UPDATE_STATUS);
                ptm.setString(1, orderId);
                ptm.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ptm, null);
        }
    }

    public boolean CreateNewOrder(String date, double total, PaymentDTO payment, UserDTO user) throws SQLException {
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(CREATE_ORDER);
                ptm.setString(1, date);
                ptm.setDouble(2, total);
                ptm.setInt(3, payment.getPaymentID());
                ptm.setInt(4, user.getId());
                ptm.executeUpdate();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, ptm, null);
        }
        return false;
    }

    private void closeResources(Connection conn, PreparedStatement ptm, ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
        }
        if (ptm != null) {
            ptm.close();
        }
        if (conn != null) {
            conn.close();
        }
    }

    public static void main(String[] args) throws SQLException {
        OrderDAO dao = new OrderDAO();
        double totalOrders = dao.getTotalOrders();
        OrderDTO order = dao.getOrdersByID("1");
        System.out.println(totalOrders);
        System.out.println(order);
    }
}
