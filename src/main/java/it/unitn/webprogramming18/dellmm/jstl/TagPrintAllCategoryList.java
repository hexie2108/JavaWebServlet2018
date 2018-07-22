/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.jstl;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCCategoryListDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.CategoryList;
import java.io.IOException;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author mikuc
 */
public class TagPrintAllCategoryList extends SimpleTagSupport
{

        private String catoryIdOfCurrentList;
        private CategoryListDAO categoryListDAO = new JDBCCategoryListDAO();
        // private StringWriter sw = new StringWriter();

        /**
         * @param catoryIdOfCurrentList the categoryId to set
         */
        public void setCatoryIdOfCurrentList(String catoryIdOfCurrentList)
        {
                this.catoryIdOfCurrentList = catoryIdOfCurrentList;
        }

        @Override
        public void doTag() throws JspException, IOException
        {
               
                List<CategoryList> categoryList;
                try
                {
                        categoryList = categoryListDAO.getAll();
                }
                catch (DAOException ex)
                {
                        throw new JspException("errore durante ottenimento la lista di categoria della lista di spesa");
                }

                JspWriter jspWriter = getJspContext().getOut();

                for (CategoryList singleCategory : categoryList)
                {

                        jspWriter.println("<option value=\"" + singleCategory.getId() + "\" " + (singleCategory.getId() == Integer.parseInt(catoryIdOfCurrentList) ? " selected=\"selected\"" : "") + ">" + singleCategory.getName() + "</option>");
                }

        }

}
