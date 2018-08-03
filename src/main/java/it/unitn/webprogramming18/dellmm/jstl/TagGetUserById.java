/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.jstl;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCUserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Tag che get e set istanza di utente dato id utente come l'attributo della richiesta
 *
 * @author mikuc
 */
public class TagGetUserById extends SimpleTagSupport
{

        private Integer userId;
        private final UserDAO userDAO = new JDBCUserDAO();

        
        /**
         * @param userId id utente
         */
        public void setUserId(int userId)
        {
                this.userId = userId;
        }

        @Override
        public void doTag() throws JspException, IOException
        {
               
                if (this.userId != null)
                {
                        User user = null;
                        try
                        {
                                user = userDAO.getByPrimaryKey(userId);
                        }
                        catch (DAOException ex)
                        {
                                throw new JspException(ex.getMessage(),ex);
                        }

                        //set l'istanza user nella richiesta
                        ((PageContext) getJspContext()).getRequest().setAttribute("SingleUser", user);

                }
                else
                {
                        throw new JspException("manca il parametro id utente per ottenere l'istanza di utente");
                }
        }

}
