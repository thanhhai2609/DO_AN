package dao;

import utils.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.CartItem;
import model.OrderDTO;
import model.OrderItem;
import model.ProductDTO;

public class OrderItemDAO extends DBContext {

    private ProductDAO pDao = new ProductDAO();

    // Câu lệnh SQL để lấy các sản phẩm trong đơn hàng
    private static final String GET_ORDER_ITEM_BY_ORDER_ID = "SELECT order_id, product_id, SUM(quantity) AS quantity, price FROM OrderItem WHERE order_id = ? GROUP BY order_id, product_id, price";

    // Câu lệnh SQL để tạo một sản phẩm mới trong đơn hàng
    private static final String CREATE_NEW_ORDER_ITEM = "INSERT INTO OrderItem (quantity, price, product_id, order_id) VALUES (?, ?, ?, ?)";

    public List<OrderItem> getOrderItemByOrderId(int id) throws SQLException {
        List<OrderItem> list = new ArrayList<>();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ptm = null;
        try {
            con = getConnection();
            if (con != null) {
                ptm = con.prepareStatement(GET_ORDER_ITEM_BY_ORDER_ID);
                ptm.setInt(1, id);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    int quantity = rs.getInt("quantity");
                    double price = rs.getDouble("price");
                    int productID = rs.getInt("product_id");
                    ProductDTO product = pDao.getProductByID(productID);
                    int orderID = rs.getInt("order_id");
                    OrderItem order = new OrderItem(quantity, price, product, orderID);
                    list.add(order);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(con, ptm, rs);
        }

        return list;
    }

    public boolean createNewOrderDetail(CartItem item, OrderDTO order) throws SQLException {
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(CREATE_NEW_ORDER_ITEM);
                ptm.setInt(1, item.getQuantity());
                ptm.setDouble(2, item.getProduct().getSalePrice());
                ptm.setInt(3, item.getProduct().getId());
                ptm.setInt(4, order.getOrderID());
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
        OrderItemDAO dao = new OrderItemDAO();
        List<OrderItem> list = dao.getOrderItemByOrderId(1);
        for (OrderItem orderItem : list) {
            System.out.println(orderItem.getProduct().getName());
        }
    }
}
