/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.jstl;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCCategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCUserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * stampare il nome di categoria datto id
 *
 * @author mikuc
 */
public class TagGetUserNameAndImgById extends SimpleTagSupport
{

        UserDAO userDAO = new JDBCUserDAO();

        private Integer userId;
        // private StringWriter sw = new StringWriter();

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
                String basePath = ((PageContext) getJspContext()).getServletContext().getContextPath();
                if (this.userId != null)
                {
                        User user = null;
                        try
                        {
                                user = userDAO.getByPrimaryKey(userId);
                        }
                        catch (DAOException ex)
                        {
                                throw new JspException("errore durante ottenimento delle info utente nella lista di commento");
                        }
                        getJspContext().getOut().println("<div class=\"user-img\"><img class=\"img-fluid\" src=\"" + basePath + "/" + user.getImg() + "\" alt=\" "+user.getName()+"\" /></div><span><i class=\"far fa-address-card\"></i> " + user.getName() + "</span>");
                }
        }

}
