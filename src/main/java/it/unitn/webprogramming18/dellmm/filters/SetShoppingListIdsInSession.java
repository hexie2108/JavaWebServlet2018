/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.filters;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

public class SetShoppingListIdsInSession implements Filter
{

        /**
         *
         * @param request The servlet request we are processing
         * @param response The servlet response we are creating
         * @param chain The filter chain we are processing
         *
         * @exception IOException if an input/output error occurs
         * @exception ServletException if a servlet error occurs
         */
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
        {
                
                HttpSession session = ((HttpServletRequest)request).getSession();
                
                //non ha ottenuto ancora la lista di prorie shopping liste
                if(session.getAttribute("myListOfShoppingLists")!=null ){
                        
                        //se è un utente anonimo
                        if(session.getAttribute("user") == null){
                                
                        }
                        
                        
                        
                        
                        
                }
                
                chain.doFilter(request, response);

        }

        /**
         * Destroy method for this filter
         */
        public void destroy()
        {
        }

        /**
         * Init method for this filter
         */
        public void init(FilterConfig filterConfig)
        {

        }

}
