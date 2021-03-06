/*
 * AA 2016-2017
 * Introduction to Web Programming
 * Lab 08 - JSP ToDoManager
 * UniTN
 */
package it.unitn.webprogramming18.dellmm.listeners;

import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.db.utils.factories.jdbc.JDBCDAOFactory;
import it.unitn.webprogramming18.dellmm.email.EmailFactory;
import it.unitn.webprogramming18.dellmm.email.SendMailOfSuggestionForRepeatitivePurchasesTask;
import it.unitn.webprogramming18.dellmm.email.exceptions.EmailFactoryException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.logging.Logger;

/**
 * Web application lifecycle listener.
 *
 * @author Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
 * @since 2017.04.17
 */
public class WebAppContextListener implements ServletContextListener
{
        private ScheduledThreadPoolExecutor executor = null;

        /**
         * The serlvet container call this method when initializes the
         * application for the first time.
         *
         * @param sce the event fired by the servlet container when initializes
         * the application
         * @author Stefano Chirico
         * @since 1.0.170417
         */
        @Override
        public void contextInitialized(ServletContextEvent sce)
        {
                /* Init DB */
                final String dburl = sce.getServletContext().getInitParameter("dburl");
                final String dbuser = sce.getServletContext().getInitParameter("dbuser");
                final String dbpwd = sce.getServletContext().getInitParameter("dbpwd");

                /*inizializza c3p0*/
                JDBCDAOFactory jdbcDaoFactory = null;
                try
                {
                        JDBCDAOFactory.configure(dburl, dbuser, dbpwd);
                        jdbcDaoFactory = JDBCDAOFactory.getInstance();
                        sce.getServletContext().setAttribute("daoFactory", (DAOFactory) jdbcDaoFactory);
                }
                catch (DAOFactoryException ex)
                {
                        Logger.getLogger(getClass().getName()).severe(ex.toString());
                        throw new RuntimeException(ex);
                }

                /* Init email */
                final String smtpHostname = sce.getServletContext().getInitParameter("smtpHostname");
                final String smtpPort = sce.getServletContext().getInitParameter("smtpPort");
                final String smtpUsername = sce.getServletContext().getInitParameter("smtpUsername");
                final String smtpPassword = sce.getServletContext().getInitParameter("smtpPassword");

                EmailFactory emailFactory = null;
                try
                {
                        EmailFactory.configure(smtpHostname, smtpPort, smtpUsername, smtpPassword);
                        emailFactory = EmailFactory.getInstance();

                        sce.getServletContext().setAttribute("emailFactory", emailFactory);
                }
                catch (EmailFactoryException ex)
                {
                        Logger.getLogger(getClass().getName()).severe(ex.toString());

                        throw new RuntimeException(ex);
                }

                /*---------------suggerimento per riaquisto---------------------------*/
                //genera url
                String basepath = sce.getServletContext().getInitParameter("domainForEmailLink") + sce.getServletContext().getContextPath();

                //crea task per invaire email
                SendMailOfSuggestionForRepeatitivePurchasesTask task = null;
                try
                {
                        task = new SendMailOfSuggestionForRepeatitivePurchasesTask(emailFactory, basepath, jdbcDaoFactory);
                        //crea esecutore con 5 thread
                        executor = new ScheduledThreadPoolExecutor(5);

                        //iniza esegue tast, dopo 5minuti dall'avvio di tomcat, e poi ripete ogni 24ore
                        executor.scheduleAtFixedRate(task, 60 * 2, 60 * 5, TimeUnit.SECONDS);
                }
                catch (Exception e)
                {
                        throw new RuntimeException(e);
                }
        }

        /**
         * The servlet container call this method when destroyes the
         * application.
         *
         * @param sce the event generated by the servlet container when
         * destroyes the application.
         * @author Stefano Chirico
         * @since 1.0.170417
         */
        @Override
        public void contextDestroyed(ServletContextEvent sce)
        {
                DAOFactory daoFactory = (DAOFactory) sce.getServletContext().getAttribute("daoFactory");
                if (daoFactory != null)
                {
                        daoFactory.shutdown();
                }
                daoFactory = null;

                executor.shutdown();
        }
}
