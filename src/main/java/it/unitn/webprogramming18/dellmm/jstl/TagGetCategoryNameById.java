/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.jstl;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCCategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Tag che get e set il nome di categoria del prodotto come l'attributo della richiesta
 *
 * @author mikuc
 */
public class TagGetCategoryNameById extends SimpleTagSupport {

    private CategoryProductDAO categoryProductDAO;
    private Integer categoryId;

    /**
     * set valore categoryId dal attributo del tag
     *
     * @param categoryId the categoryId to set
     */
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    private synchronized void setDAO(ServletContext ctx) throws JspException{
        if (categoryProductDAO == null) {
            DAOFactory daoFactory = (DAOFactory) ctx.getAttribute("daoFactory");
            if (daoFactory == null) {
                throw new JspException("Impossible to get db factory for user storage system");
            }

            try {
                categoryProductDAO = daoFactory.getDAO(CategoryProductDAO.class);
            } catch (DAOFactoryException ex){
                throw new JspException("Impossible to get CategoryProductDAO for user storage system", ex);
            }
        }
    }

    @Override
    public void doTag() throws JspException, IOException {
        setDAO(((PageContext) getJspContext()).getServletContext());

        //se id categoria non è nullo
        if (this.categoryId != null) {

            String categoryName;
            try {
                categoryName = categoryProductDAO.getByPrimaryKey(categoryId).getName();
            } catch (DAOException ex) {
                throw new JspException("errore durante ottenimento del nome di categoria");
            }

            //set il nome della categoria come l'attributo della richiesta
            ((PageContext) getJspContext()).getRequest().setAttribute("categoryName", categoryName);

        } else {
            throw new JspException("manca il parametro id categoria per ottenere il nome di categoria");
        }
    }

}
