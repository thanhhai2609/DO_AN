package controller.web.shop;


import dao.ProductDAO;
import model.ProductDTO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SingleProductServlet", urlPatterns = {"/SingleProductServlet"})
public class SingleProductServlet extends HttpServlet {
    
    private static final String SINGLE_PRODUCT_PAGE ="view/jsp/home/single-product.jsp";
     
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = SINGLE_PRODUCT_PAGE;
        try {
            ProductDAO pDao = new ProductDAO();
            String product_id = request.getParameter("product_id");
            ProductDTO product = pDao.getProductByID(Integer.parseInt(product_id));
            List<ProductDTO> listProduct = pDao.getProductByCategoryId(pDao.getData(), product.getCategory().getId());
            List<ProductDTO> listSameCategory = new ArrayList<>();
            int count = 0;
            for (ProductDTO productDTO : listProduct) {
                if(productDTO.getId() != product.getId()){
                    listSameCategory.add(productDTO);
                    count++;
                    if(count == 4) {
                        break;
                    }
                }
            }
            
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage != null) {
                request.setAttribute("errorMessage", errorMessage);
            }

            request.setAttribute("PRODUCT", product);
            request.setAttribute("LIST_PRODUCTS_SAME_CATEGORY", listSameCategory);
            
        } catch (Exception ex) {
            log("SingleProductServlet error:" + ex.getMessage());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
