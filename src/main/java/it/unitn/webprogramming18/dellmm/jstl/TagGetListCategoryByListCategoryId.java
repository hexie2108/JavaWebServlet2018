package it.unitn.webprogramming18.dellmm.jstl;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryListDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.CategoryList;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Tag che get e set il nome di categoria della lista come l'attributo della richiesta
 *
 * @author mikuc
 */
public class TagGetListCategoryByListCategoryId extends SimpleTagSupport {

    private CategoryListDAO categoryListDAO;
    private Integer listCategoryId;

    private synchronized void setDAO(ServletContext ctx) throws JspException{
        if (categoryListDAO == null) {
            DAOFactory daoFactory = (DAOFactory) ctx.getAttribute("daoFactory");
            if (daoFactory == null) {
                throw new JspException("Impossible to get db factory for user storage system");
            }

            try {
                categoryListDAO = daoFactory.getDAO(CategoryListDAO.class);
            } catch (DAOFactoryException ex){
                throw new JspException("Impossible to get CategoryListDAO for user storage system", ex);
            }
        }
    }

    /**
     * @param listCategoryId il id della categoria lista
     */
    public void setListCategoryId(int listCategoryId) {
        this.listCategoryId = listCategoryId;
    }

    @Override
    public void doTag() throws JspException, IOException {
        setDAO(((PageContext)getJspContext()).getServletContext());

        if (this.listCategoryId != null) {

            CategoryList categoryOfList;
            try {
                categoryOfList = categoryListDAO.getByPrimaryKey(listCategoryId);
            } catch (DAOException ex) {
                throw new JspException("errore durante ottenimento del nome della categoria di lista");
            }

            //set il nome della categoria della lista come l'attributo della richiesta
            ((PageContext) getJspContext()).getRequest().setAttribute("categoryOfList", categoryOfList);

        } else {
            throw new JspException("manca il parametro id categoria per ottenere il nome di categoria della lista");
        }
    }

}
