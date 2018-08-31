/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlets.front;

import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;
import it.unitn.webprogramming18.dellmm.util.i18n;

import java.io.IOException;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * servlet della pagina per inserire il prodotto privato
 *
 * @author luca_morgese
 */
public class UpdateProductServlet extends HttpServlet
{

        private static final String JSP_PAGE_PATH = "/WEB-INF/jsp/front/updateProduct.jsp";

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {

                //Language bundle
                ResourceBundle rb = i18n.getBundle(request);

                //set titolo della pagina
                request.setAttribute(ConstantsUtils.HEAD_TITLE, rb.getString("frontPage.title.addPrivateProduct"));
                request.getRequestDispatcher(JSP_PAGE_PATH).forward(request, response);

        }

}
