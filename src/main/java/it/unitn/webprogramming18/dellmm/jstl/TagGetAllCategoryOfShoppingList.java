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
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Tag che get e set tutte categorie della lista spesa per utente anonimo come
 * l'attributo della richiesta
 *
 * @author mikuc
 */
public class TagGetAllCategoryOfShoppingList extends SimpleTagSupport
{

        private final CategoryListDAO categoryListDAO = new JDBCCategoryListDAO();

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
                        throw new JspException(ex.getMessage(), ex);
                }

                //set la lista di categoria  nella richiesta
                ((PageContext) getJspContext()).getRequest().setAttribute("categoryList", categoryList);


        }

}
