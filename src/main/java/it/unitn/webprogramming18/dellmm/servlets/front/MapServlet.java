/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlets.front;

import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * servlet della pagina per visuallizare la mappa
 *
 * @author luca_morgese
 */
public class MapServlet extends HttpServlet {

    private static final String JSP_PAGE_PATH = "/WEB-INF/jsp/front/map.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //set titolo della pagina
        request.setAttribute(ConstantsUtils.HEAD_TITLE, "Trova i negozi vicini a te");
        request.getRequestDispatcher(JSP_PAGE_PATH).forward(request, response);

    }

}
