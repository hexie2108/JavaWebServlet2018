package it.unitn.webprogramming18.dellmm.jstl;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCCategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * get e set i prodotti della lista locale dalla cookie (utente anonimo) come l'attributo della richiesta
 *
 * @author mikuc
 */
public class TagGetElementsOfShoppingListByCookie extends SimpleTagSupport {

    private final CategoryProductDAO categoryProductDAO = new JDBCCategoryProductDAO();
    private final ProductDAO productDAO = new JDBCProductDAO();

    @Override
    public void doTag() throws JspException, IOException {

        PageContext pageContext = (PageContext) getJspContext();
        Cookie cookies[] = ((HttpServletRequest) pageContext.getRequest()).getCookies();

        String localtListProductString = null;
        String[] productsId = null;
        Product product = null;
        List<Product> productsOfMyList = null;

        //controlla se utente ha una cookie sulla lista locale
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("localShoppingList")) {
                    localtListProductString = cookie.getValue();
                }
            }
        }
        //se ha una cokkie sulla lista locale non vuoto
        if (localtListProductString != null && !localtListProductString.equals("")) {
            //la trasforma in un'array
            productsId = localtListProductString.split(",");

            try {
                //se tale array non è nulla e non è vuota
                if (productsId != null && productsId.length > 0) {
                    //crea una lista di prodotto
                    productsOfMyList = new ArrayList<>();
                    for (String productId : productsId) {
                        //inserire il prodotto nella lista
                        product = productDAO.getByPrimaryKey(Integer.parseInt(productId));
                        productsOfMyList.add(product);
                    }
                    //set la lista di prodotto come l'attributo della richiesta
                    if (productsOfMyList.size() > 0) {
                        pageContext.getRequest().setAttribute("productsOfMyList", productsOfMyList);
                    }
                }
            } catch (DAOException ex) {
                throw new JspException(ex.getMessage(), ex);
            }
        }

    }
}
