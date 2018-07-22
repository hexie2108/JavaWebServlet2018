/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.jstl;

import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;
import java.io.IOException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author mikuc
 */
public class TagGetElementsOfShoppingListByCookie extends SimpleTagSupport
{

        @Override
        public void doTag() throws JspException, IOException
        {
                String basePath = ((PageContext) getJspContext()).getServletContext().getContextPath();
                PageContext pageContext = (PageContext) getJspContext();
                Cookie cookies[] = ((HttpServletRequest) pageContext.getRequest()).getCookies();
                String localtListProduct = null;
                String[] productsId = null;
                JspWriter jspWriter = getJspContext().getOut();

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
                //se è stato aggiunto l'elemento nella lista locale, lo trasforma in array di stringa
                if (localtListProduct != null && localtListProduct!="")
                {
                        productsId = localtListProduct.split(",");

                        ProductDAO productDAO = new JDBCProductDAO();
                        Product product = null;

                        

                        try
                        {
                                if (productsId != null && productsId.length > 0)
                                {

                                        for (int i = 0; i < productsId.length; i++)
                                        {
                                                
                                                product = productDAO.getByPrimaryKey(Integer.parseInt(productsId[i]));
                                                jspWriter.println("<tr>");
                                                jspWriter.println("<td class=\"td-img\">"
                                                            + "<img src=\"" + basePath + "/" + product.getImg() + "\" alt=\"" + product.getName() + "\" />"
                                                            + "</td>");
                                                jspWriter.println("<td class=\"td-name\">"
                                                            + "<span>" + product.getName() + "</span>"
                                                            + "</td>");
                                                jspWriter.println("<td class=\"td-buttons\">"
                                                            + ""
                                                            + "<a href=\""+basePath+"/service/updateItemInListUnloggedUserOnlyService?action=delete&productId="+product.getId()+"\" title=\"delete\"><i class=\"fas fa-ban\"></i></a>"
                                                            + "</td>");
                                                jspWriter.println("</tr>");
                                        }
                                }

                        }

                        catch (DAOException ex)
                        {
                                throw new JspException("errore durante ottenimento del nome di categoria");
                        }

                }
                else
                {
                        jspWriter.println("<tr><td colspan=\"3\">è ancora vuoto</td></tr>");

                }
        }
}
