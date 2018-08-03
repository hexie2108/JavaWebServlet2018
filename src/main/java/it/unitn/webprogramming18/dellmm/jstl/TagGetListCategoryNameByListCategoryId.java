
package it.unitn.webprogramming18.dellmm.jstl;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCCategoryListDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Tag che get e set il nome di categoria della lista come l'attributo della richiesta
 *
 * @author mikuc
 */
public class TagGetListCategoryNameByListCategoryId extends SimpleTagSupport
{

        private Integer listCategoryId;
        private final CategoryListDAO categoryListDAO = new JDBCCategoryListDAO();

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

                if (this.listCategoryId != null)
                {

                        String categoryName;
                        try
                        {
                                categoryName = categoryListDAO.getByPrimaryKey(listCategoryId).getName();
                        }
                        catch (DAOException ex)
                        {
                                throw new JspException("errore durante ottenimento del nome della categoria di lista");
                        }

                        //set il nome della categoria della lista come l'attributo della richiesta
                        ((PageContext) getJspContext()).getRequest().setAttribute("categoryListName", categoryName);

                }
                else
                {
                        throw new JspException("manca il parametro id categoria per ottenere il nome di categoria della lista");
                }
        }

}
