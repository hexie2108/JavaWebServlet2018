/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.jstl;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCCategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.DAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author mikuc
 */
public class TagGetCategoryNameById extends SimpleTagSupport
{
        private Integer categoryId;
       // private StringWriter sw = new StringWriter();

        /**
         * @param categoryId the categoryId to set
         */
        public void setCategoryId(int categoryId)
        {
                this.categoryId = categoryId;
        }

        @Override
        public void doTag() throws JspException, IOException
        {
                if(this.categoryId != null){
                        CategoryProductDAO categoryProductDAO = new JDBCCategoryProductDAO();
                        String categoryName;
                        try {
                                categoryName = categoryProductDAO.getByPrimaryKey(categoryId).getName();
                        }
                        catch (DAOException ex) {
                              throw new JspException("errore durante ottenimento del nome di categoria");
                        }
                        getJspContext().getOut().println(categoryName);
                }
        }
        
        
}
