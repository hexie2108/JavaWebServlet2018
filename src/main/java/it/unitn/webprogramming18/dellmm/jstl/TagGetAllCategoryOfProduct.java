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
import it.unitn.webprogramming18.dellmm.javaBeans.CategoryProduct;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Tag che get e set tutte categorie del prodotto come l'attributo della
 * richiesta
 *
 * @author mikuc
 */
public class TagGetAllCategoryOfProduct extends SimpleTagSupport {

    private CategoryProductDAO categoryProductDAO;

    private synchronized void setDAO(ServletContext ctx) throws JspException{
        if (categoryProductDAO == null) {
            DAOFactory daoFactory = (DAOFactory) ctx.getAttribute("daoFactory");
            if (daoFactory == null) {
                throw new JspException("Impossible to get db factory for user storage system");
            }

            try {
                categoryProductDAO = daoFactory.getDAO(CategoryProductDAO.class);
            } catch (DAOFactoryException ex) {
                throw new JspException("Impossible to get CategoryProductDAO for user storage system", ex);
            }
        }
    }

    @Override
    public void doTag() throws JspException, IOException {
        setDAO(((PageContext) getJspContext()).getServletContext());

        List<CategoryProduct> categoryProductList;
        try {
            categoryProductList = categoryProductDAO.getAll();
        } catch (DAOException ex) {
            throw new JspException(ex.getMessage(), ex);
        }

        //set la lista della categoria di prodotto nella richiesta
        ((PageContext) getJspContext()).getRequest().setAttribute("categoryProductList", categoryProductList);
    }
}
