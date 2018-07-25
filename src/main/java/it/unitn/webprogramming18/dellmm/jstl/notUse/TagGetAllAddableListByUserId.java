/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.jstl.notUse;

import it.unitn.webprogramming18.dellmm.db.daos.ListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCListDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.ShoppingList;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author mikuc
 */
public class TagGetAllAddableListByUserId extends SimpleTagSupport
{

        private ListDAO listDAO = new JDBCListDAO();
        List<ShoppingList> listOfShoopingList;

        @Override
        public void doTag() throws JspException, IOException
        {
                JspWriter jspWriter = getJspContext().getOut();
                String basePath = ((PageContext) getJspContext()).getServletContext().getContextPath();
                PageContext pageContext = (PageContext) getJspContext();
                User user = (User) ((HttpServletRequest) pageContext.getRequest()).getSession().getAttribute("user");
                Integer myListId = (Integer) ((HttpServletRequest) pageContext.getRequest()).getSession().getAttribute("myListId");

                try
                {
                        listOfShoopingList = listDAO.getAllAddableListByUserId(user.getId());
                }
                catch (DAOException ex)
                {
                        throw new JspException(ex.getMessage(), ex);
                }

                //se possiede qualche lista con il permesso di aggiungere il prodotto
                if (listOfShoopingList != null && listOfShoopingList.size() > 0)
                {

                        for (ShoppingList shoppingList : listOfShoopingList)
                        {
                                jspWriter.println("<option value=\"" + shoppingList.getId() + "\" " + (myListId == shoppingList.getId() ? "selected=\"selected\"" : "") + ">");
                                jspWriter.println(shoppingList.getName());
                                jspWriter.println("</option>");
                        }

                        jspWriter.println("</select>"
                                    + "<div class=\"operation mt-3\">\n"
                                    + "<input id=\"productIdToAdd\" type=\"hidden\" name=\"productId\" value=\"1\"/>\n"
                                    + "<input type=\"hidden\" name=\"action\" value=\"insert\"/>\n"
                                    + "<input class=\"btn btn-info d-inline-block\" type=\"submit\" value=\"aggiunge\" />\n"
                                    + "<a class=\"btn btn-info d-inline-block\" href=\"#\">crea una nuova</a>\n"
                                    + "</div\n");
                }
                
                //se non possiede nessuna lista con il permesso di aggiungere il prodotto
                else
                {
                        jspWriter.println("</select>\n"
                                    + "<div class=\"operation mt-3\">\n"
                                    + "<p>non hai ancora una lista</p>\n"
                                    + "<a class=\"btn btn-info d-inline-block\" href=\"#\">crea una nuova</a>\n"
                                    + "</div\n");
                }

        }
}
