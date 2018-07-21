/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.jstl;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCCategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.CategoryProduct;
import java.io.IOException;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author mikuc
 */
public class TagPrintAllCategoryProduct extends SimpleTagSupport
{

        CategoryProductDAO categoryProductDAO;
                    
        @Override
        public void doTag() throws JspException, IOException
        {

                categoryProductDAO = new JDBCCategoryProductDAO();
                List<CategoryProduct> categoryList;
                try
                {
                        categoryList = categoryProductDAO.getAll();
                }
                catch (DAOException ex)
                {
                        throw new JspException("errore durante ottenimento la lista di categoria");
                }
                String basePath = ((PageContext) getJspContext()).getServletContext().getContextPath();
                JspWriter jspWriter = getJspContext().getOut();
                jspWriter.println("<div class=\"list-group\">");
                for (CategoryProduct categoryProduct : categoryList)
                {

                        jspWriter.println("<a class=\"list-group-item list-group-item-action text-center\" href =\"" + basePath + "/category?catid=" + categoryProduct.getId() + "\">" + categoryProduct.getName() + "</a>");
                }
                jspWriter.println("</div>");

        }

}
