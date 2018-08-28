<%-- 

    l'intestazione per admin page
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>



<!DOCTYPE html>
<html>
        <head>

                <%--impostazione base--%>
                <meta charset="utf-8">
                <meta http-equiv="x-ua-compatible" content="ie=edge">
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no, user-scalable=no">

                <%--titolo della pagina--%>
                <title>
                        <c:out value="${head_title}" default="non hai ancora un titolo"/>
                </title>

                <%--icone del sito--%>
                <link rel="icon" href="<c:url value="/favicon.ico"/>" type="image/vnd.microsoft.icon">
                <link rel="shortcut icon" href="<c:url value="/favicon.ico"/>" type="image/vnd.microsoft.icon">

                <%--jquery--%>
                <script type="text/javascript" src="<c:url value="/libs/jquery/jquery-3.3.1.min.js"/>"></script>
                <%--js di bootstrap--%>
                <script type="text/javascript" src="<c:url value="/libs/bootstrap-4.1.1-dist/js/bootstrap.bundle.min.js"/>"></script>
                <%--js personale di sito--%>
                <script type="text/javascript" src="<c:url value="/js/adminPagesScript.js"/>"></script>

                <%--css di bootstrap--%>
                <link rel="stylesheet" href="<c:url value="/libs/bootstrap-4.1.1-dist/css/bootstrap.min.css"/>" type="text/css"
                      media="all">
                <%--css di fontawesome--%>
                <link rel="stylesheet" href="<c:url value="/libs/fontawesome-free-5.1.1-web/css/all.min.css"/>" type="text/css"
                      media="all">
                <%--css personale di sito--%>
                <link rel="stylesheet" href="<c:url value="/css/adminPages.css"/>" type="text/css" media="all">

                <!-- Latest compiled and minified CSS -->
                <link rel="stylesheet" href="<c:url value="/libs/bootstrap-select-1.13.1/css/bootstrap-select.min.css"/>">

                <!-- Latest compiled and minified JavaScript -->
                <script src="<c:url value="/libs/bootstrap-select-1.13.1/js/bootstrap-select.min.js"/>"></script>
        </head>

        <body class="admin-page">

                <div class="">

                        <%-- la sezione di header--%>
                        <header class="header">

                                <%-- barra top per mobile--%>
                                <div class="mobile-menu-bar fixed-top  bg-info">
                                        <a id="mobile-menu-active-link" class="" href="javascript:;" title="mobile-menu">
                                                <i class="fas fa-caret-square-down"></i> MENU
                                        </a>
                                </div>


                                <%-- barra laterale--%>
                                <div class="asider-left">



                                        <%-- menu--%>
                                        <div class="admin-menu-box">
                                                <nav class="admin-menu navbar fixed-top bg-info">



                                                        <ul class="navbar-nav w-100">



                                                                <li class="nav-item">
                                                                        <a class="nav-link" href="<c:url value="/admin/home"/>">
                                                                                <i class="fas fa-tachometer-alt"></i>  <b>dashboard</b>
                                                                        </a>
                                                                </li>


                                                                <li class="nav-item">
                                                                        <a class="nav-link" href="<c:url value="/admin/categoryLists"/>">
                                                                                <i class="fas fa-sitemap"></i> <b>gestisce le categorie di lista</b>
                                                                        </a>
                                                                </li>


                                                                <li class="nav-item">
                                                                        <a class="nav-link" href="<c:url value="/admin/categoryProducts"/>">
                                                                                <i class="fas fa-store"></i> <b>gestisce le categorie di prodotto</b>
                                                                        </a>
                                                                </li>


                                                                <li class="nav-item">
                                                                        <a class="nav-link" href="<c:url value="/"/>">
                                                                                <i class="fas fa-shopping-basket"></i> <b>gestisce i prodotti</b>
                                                                        </a>
                                                                </li>

                                                                <li class="nav-item">
                                                                        <a class="nav-link" href="<c:url value="/admin/users"/>">
                                                                                <i class="fas fa-users"></i> <b>gestisce gli utente</b>
                                                                        </a>
                                                                </li>

                                                                <li class="nav-item">
                                                                        <a class="nav-link" href="<c:url value="/"/>">
                                                                                <i class="fas fa-sign-out-alt"></i> <b>ritorna a home</b>
                                                                        </a>
                                                                </li>


                                                        </ul>
                                                </nav>

                                        </div>

                                </div>



                        </header>

                        <%-- la sezione body principale--%>
                        <section class="admin-main-container">
                                <%@ include file="../../jspf/i18n_switcher.jsp" %>
                                <div class="admin-main-body">




