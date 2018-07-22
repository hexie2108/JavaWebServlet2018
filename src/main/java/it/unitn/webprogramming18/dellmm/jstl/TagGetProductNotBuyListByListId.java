/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.jstl;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.PermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCCategoryListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCPermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.CategoryList;
import it.unitn.webprogramming18.dellmm.javaBeans.Permission;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;
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
 * datto il id della lista , stampa tutti i prodotti
 * @author mikuc
 */
public class TagGetProductNotBuyListByListId extends SimpleTagSupport
{

        private Integer listId;
        private ProductDAO productDAO = new JDBCProductDAO();
        private PermissionDAO permissionDAO = new JDBCPermissionDAO();
        List<Product> productsOfMyList = null;

        /**
         * @param listId the id of list
         */
        public void setListId(int listId)
        {
                this.listId = listId;
        }

        @Override
        public void doTag() throws JspException, IOException
        {

                JspWriter jspWriter = getJspContext().getOut();
                String basePath = ((PageContext) getJspContext()).getServletContext().getContextPath();
                PageContext pageContext = (PageContext) getJspContext();
                User user = (User) ((HttpServletRequest) pageContext.getRequest()).getSession().getAttribute("user");

                try
                {
                        productsOfMyList = productDAO.getProductsNotBuyInListByListId(listId);

                        //se la lista di spesa non è vuota
                        if (productsOfMyList != null && productsOfMyList.size() > 0)
                        {
                                //get il permesso sulla lista
                                Permission permission = permissionDAO.getUserPermissionOnListByIds(user.getId(), listId);
                                //stampa tutti i prodotti
                                for (Product product : productsOfMyList)
                                {
                                        jspWriter.println("<tr>");
                                        jspWriter.println("<td class=\"td-img\">"
                                                    + "<img src=\"" + basePath + "/" + product.getImg() + "\" alt=\"" + product.getName() + "\" />"
                                                    + "</td>");
                                        jspWriter.println("<td class=\"td-name\">"
                                                    + "<span>" + product.getName() + "</span>"
                                                    + "</td>");
                                        jspWriter.println("<td class=\"td-buttons\"><a href=\"#\" title=\"comprato\"><i class=\"fas fa-check-circle\"></i></a>");
                                        if (permission.isDeleteObject())
                                        {
                                                jspWriter.println("<a href=\"#\" title=\"elimina\"><i class=\"fas fa-ban\"></i></a>");
                                        }
                                        jspWriter.println("</td>");
                                        jspWriter.println("</tr>");
                                }
                        }
                        //se la lista è vuota
                        else
                        {
                                jspWriter.println("<tr>< td colspan = \"3\">è ancora vuoto</td></tr>");

                        }
                }
                catch (DAOException ex)
                {
                        throw new JspException(ex.getMessage(), ex);
                }

        }

}
