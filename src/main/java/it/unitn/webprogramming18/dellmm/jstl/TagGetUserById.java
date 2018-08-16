/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.jstl;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCUserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.User;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Tag che get e set istanza di utente dato id utente come l'attributo della richiesta
 *
 * @author mikuc
 */
public class TagGetUserById extends SimpleTagSupport {

    private UserDAO userDAO;
    private Integer userId;


    private synchronized void setDAO(ServletContext ctx) throws JspException{
        if (userDAO == null) {
            DAOFactory daoFactory = (DAOFactory) ctx.getAttribute("daoFactory");
            if (daoFactory == null) {
                throw new JspException("Impossible to get db factory for user storage system");
            }

            try {
                userDAO = daoFactory.getDAO(UserDAO.class);
            } catch (DAOFactoryException ex){
                throw new JspException("Impossible to get ProductDAO for user storage system", ex);
            }
        }
    }

    /**
     * @param userId id utente
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public void doTag() throws JspException, IOException {
        setDAO(((PageContext)getJspContext()).getServletContext());

        if (this.userId != null) {
            User user = null;
            try {
                user = userDAO.getByPrimaryKey(userId);
            } catch (DAOException ex) {
                throw new JspException(ex.getMessage(), ex);
            }

            //set l'istanza user nella richiesta
            ((PageContext) getJspContext()).getRequest().setAttribute("SingleUser", user);

        } else {
            throw new JspException("manca il parametro id utente per ottenere l'istanza di utente");
        }
    }

}
