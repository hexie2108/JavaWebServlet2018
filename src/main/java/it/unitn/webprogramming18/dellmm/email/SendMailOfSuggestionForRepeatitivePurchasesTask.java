/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.email;

import it.unitn.webprogramming18.dellmm.db.daos.LogDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.Log;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.List;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;

/**
 *
 * @author mikuc
 */
public class SendMailOfSuggestionForRepeatitivePurchasesTask extends TimerTask

{

        private final LogDAO logDAO;
        private final UserDAO userDAO;
        private final ProductDAO productDAO;
        private final EmailFactory emailFactory;
        private final int predictionDay = 1;
        private String basepath;

        public SendMailOfSuggestionForRepeatitivePurchasesTask(EmailFactory emailFactoryMain, String basepath, DAOFactory daoFactory) throws Exception
        {
                if (daoFactory == null)
                {
                        throw new Exception("Impossible to get db factory for user storage system");
                }

                try
                {
                        userDAO = daoFactory.getDAO(UserDAO.class);
                        logDAO = daoFactory.getDAO(LogDAO.class);
                        productDAO = daoFactory.getDAO(ProductDAO.class);
                }
                catch (DAOFactoryException ex)
                {
                        throw new Exception("Impossible to get UserDAO, LogDAO or ProductDAO for user storage system", ex);
                }

                this.emailFactory = emailFactoryMain;
                this.basepath = basepath;
        }

        @Override
        public void run()
        {
                Timestamp currentTime = new Timestamp(System.currentTimeMillis());

                try
                {
                        Log log = logDAO.getLogNotEmailYet(currentTime, 1);

                        //se ci sono log che può mandare email per riaquisto
                        if (log != null)
                        {
                                //get user
                                User user = userDAO.getByPrimaryKey(log.getUserId());
                                //get la lista di prodotto che soddisfa la condizione di notifica
                                List<Product> listProduct = productDAO.getListProductFromLogNotEmailYetByUserId(log.getUserId(), currentTime, predictionDay);
                                //set stato email di log in true per evitare rinvio
                                logDAO.setEmailStatusTrueByUserId(log.getUserId());
                                //invia email
                                emailFactory.sendEmailOfSuggestionForRepeatitivePurchases(user, basepath, listProduct);

                        }
                }
                catch (DAOException | MessagingException | UnsupportedEncodingException ex)
                {
                        Logger.getLogger(SendMailOfSuggestionForRepeatitivePurchasesTask.class.getName()).log(Level.SEVERE, null, ex);
                }

        }

}
