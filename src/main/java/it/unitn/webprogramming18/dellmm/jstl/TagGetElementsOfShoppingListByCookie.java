/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * get prodotto della lista locale dalla cookie (utente anonimo)
 *
 * @author mikuc
 */
public class TagGetElementsOfShoppingListByCookie extends SimpleTagSupport
{

        CategoryProductDAO categoryProductDAO = new JDBCCategoryProductDAO();
        ProductDAO productDAO = new JDBCProductDAO();

        @Override
        public void doTag() throws JspException, IOException
        {

                PageContext pageContext = (PageContext) getJspContext();
                Cookie cookies[] = ((HttpServletRequest) pageContext.getRequest()).getCookies();
                
                String localtListProduct = null;
                String[] productsId = null;
                Product product = null;
                List<Product> productsOfMyList = new ArrayList<>();

                //se utente ha una cookie della lista locale
                if (cookies != null && cookies.length > 0)
                {
                        for (Cookie cookie : cookies)
                        {
                                if (cookie.getName().equals("localShoppingList"))
                                {
                                        localtListProduct = cookie.getValue();
                                }
                        }
                }
                //se ci sono il prodotto  nella lista locale, lo trasforma in array di stringa
                if (localtListProduct != null && localtListProduct != "")
                {
                        productsId = localtListProduct.split(",");
                        
                        try
                        {
                                if (productsId != null && productsId.length > 0)
                                {

                                        for (int i = 0; i < productsId.length; i++)
                                        {

                                                productsOfMyList.add(productDAO.getByPrimaryKey(Integer.parseInt(productsId[i])));
                                              

                                        }
                                }
                        }
                        catch (DAOException ex)
                        {
                                throw new JspException(ex.getMessage(), ex);
                        }
                }

                
                //set la lista di prodotto da comprare
                if (productsOfMyList.size() > 0)
                {
                        pageContext.getRequest().setAttribute("productsOfMyList", productsOfMyList);
                }
               
                
               

        }
}
