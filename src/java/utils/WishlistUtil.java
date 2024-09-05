/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import dao.ProductDAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ProductDTO;


public class WishlistUtil {

    private static HashMap<Integer, ProductDTO> listItemsInWishlist = new HashMap<>();

    public HashMap<Integer, ProductDTO> createWishlist(ProductDTO item) {
        listItemsInWishlist = new HashMap<>();
        listItemsInWishlist.put(item.getId(), item);
        return listItemsInWishlist;
    }

    public HashMap<Integer, ProductDTO> addItemToWishlist(ProductDTO item) {
        if (!checkItemExist(item)) {
            listItemsInWishlist.put(item.getId(), item);
        }
        return listItemsInWishlist;
    }

    public boolean checkItemExist(ProductDTO product) {
        for (Integer id : listItemsInWishlist.keySet()) {
            if (product.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public HashMap<Integer, ProductDTO> removeItem(ProductDTO product) {
        if (listItemsInWishlist.containsKey(product.getId())) {
            listItemsInWishlist.remove(product.getId());
        }
        return listItemsInWishlist;
    }

    // Xử lý với Cookie
    public Cookie getCookieByName(HttpServletRequest request, String cookieName) {
        Cookie[] arrCookies = request.getCookies();
        if (arrCookies != null) {
            for (Cookie cookie : arrCookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public void saveWishlistToCookie(HttpServletRequest request,
            HttpServletResponse response, String strItemsInWishList) {
        String cookieName = "Wishlist";

        Cookie cookieCart = getCookieByName(request, cookieName);

        if (cookieCart != null) {

            cookieCart.setValue(strItemsInWishList);

        } else {

            cookieCart = new Cookie(cookieName, strItemsInWishList);

        }

        cookieCart.setMaxAge(60 * 60 * 24 * 30 * 3);

        response.addCookie(cookieCart);

    }

    public String convertToString() {
        List<ProductDTO> list = new ArrayList<>(listItemsInWishlist.values());
        String result = "";
        for (ProductDTO productDTO : list) {
            result += productDTO.getId() + ",";
        }
        return result;

    }

    public List<ProductDTO> getWishlistFromCookie(Cookie cookieWishlist) {
        ProductDAO pDao = new ProductDAO();
        List<ProductDTO> listItemsCart = new ArrayList<>();
        String inputString = cookieWishlist.getValue();
        if (inputString.endsWith(",")) {
            inputString = inputString.substring(0, inputString.length() - 1);
        }

        if (inputString.length() > 0) {
            // Chia chuỗi thành các phần tử
            String[] elements = inputString.split(",");

            for (int i = 0; i < elements.length; i++) {
                ProductDTO product = pDao.getProductByID(Integer.parseInt(elements[i].trim()));
                listItemsCart.add(product);
            }
        }
        // add to util
        for (ProductDTO productDTO : listItemsCart) {
            addItemToWishlist(productDTO);
        }

        return listItemsCart;
    }
}
