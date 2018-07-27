/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.jstl;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.CategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCCategoryListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCCategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *stampare il nome di categoria della lista datto id
 * @author mikuc
 */
public class TagGetListCategoryNameByListCategoryId extends SimpleTagSupport
{
        CategoryListDAO categoryListDAO= new JDBCCategoryListDAO();
        
        private Integer listCategoryId;
       // private StringWriter sw = new StringWriter();

        /**
         * @param listCategoryId il id della categoria lista
         */
        public void setListCategoryId(int listCategoryId)
        {
                this.listCategoryId = listCategoryId;
        }

        @Override
        public void doTag() throws JspException, IOException
        {
                if(this.listCategoryId != null){
                        
                        String categoryName;
                        try {
                                categoryName = categoryListDAO.getByPrimaryKey(listCategoryId).getName();
                        }
                        catch (DAOException ex) {
                              throw new JspException("errore durante ottenimento del nome della categoria di lista");
                        }
                        getJspContext().getOut().println(categoryName);
                }
        }
        
        
}
