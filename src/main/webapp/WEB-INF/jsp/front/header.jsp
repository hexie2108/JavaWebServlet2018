<%-- 

    l'intestazione front-end
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="custom" uri="/WEB-INF/custom.tld" %>

<%@ page import="java.util.Arrays" %>

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
    <script type="text/javascript" src="<c:url value="/js/script.js"/>"></script>
    <%--jQuery personale di sito--%>
    <script type="text/javascript" src="<c:url value="/js/jqscript.js"/>"></script>

    <%--css di bootstrap--%>
    <link rel="stylesheet" href="<c:url value="/libs/bootstrap-4.1.1-dist/css/bootstrap.min.css"/>" type="text/css"
          media="all">
    <%--css di fontawesome--%>
    <link rel="stylesheet" href="<c:url value="/libs/fontawesome-free-5.1.1-web/css/all.min.css"/>" type="text/css"
          media="all">
    <%--css personale di sito--%>
    <link rel="stylesheet" href="<c:url value="/css/style.css"/>" type="text/css" media="all">

    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="<c:url value="/libs/bootstrap-select-1.13.1/css/bootstrap-select.min.css"/>">

    <!-- Latest compiled and minified JavaScript -->
    <script src="<c:url value="/libs/bootstrap-select-1.13.1/js/bootstrap-select.min.js"/>"></script>
</head>

<body class="front-page">

<div class="container">

    <%-- la sezione di header--%>
    <header class="header">
        <custom:getAllCategoryOfProduct/>

        <%-- barra top--%>
        <div class="top-bar  fixed-top bg-dark ">

            <%-- logo di sito--%>
            <div class="site-logo-section">

                <a href="<c:url value="/"/>" title="home">
                    <img class="logo d-inline" src="<c:url value="/image/base/logo.png"/>" alt="logo"/>
                    <h2 class="site-title d-inline">
                        Il nome del sito
                    </h2>
                </a>

            </div>

            <%-- menu top--%>
            <div class="site-top-menu float-right">
                <nav class="navbar navbar-expand-sm navbar-dark">
                    <ul class="navbar-nav ">

                        <%-- se è utente anonimo--%>
                        <c:if test="${empty sessionScope.user}">

                            <li class="nav-item">
                                <a class="nav-link" href="<c:url value="/register"/>">
                                    <i class="fas fa-user-plus"></i> ISCRIVERSI
                                </a>
                            </li>

                            <li class="nav-item">
                                <a class="nav-link" href="<c:url value="/login"/>">
                                    <i class="fas fa-sign-in-alt"></i> LOGIN
                                </a>
                            </li>

                        </c:if>

                        <%-- se è utente loggato --%>
                        <c:if test="${not empty sessionScope.user}">

                            <li class="nav-item">
                                <a class="nav-link" href="<c:url value="/mylists"/>">
                                    <i class="fas fa-list"></i> MIE LISTE
                                </a>
                            </li>

                            <li class="nav-item">
                                <a class="nav-link" href="#">
                                    <i class="fa fa-envelope"></i> NOTIFICA
                                </a>
                            </li>

                            <li class="nav-item">
                                <a class="nav-link" href="#">
                                    <i class="fas fa-user"></i></i> PROFILO
                                </a>
                            </li>

                            <li class="nav-item">
                                <a class="nav-link" href="<c:url value="/logout"/>">
                                    <i class="fas fa-sign-out-alt"></i> LOGOUT
                                </a>
                            </li>

                        </c:if>

                    </ul>
                </nav>
            </div>
        </div>

        <%-- finestra di image top--%>
        <div class="top-windows ">
            <div class="search-section col">

                <%-- form di ricerca--%>
                <form id="search-form" class="mt-5" action="<c:url value="/search"/>">
                    <div class="input-group mb-3">
                        <select class="selectpicker form-control col-3" multiple title="All" name="catId"
                                data-selected-text-format="count" data-live-search="true"
                                data-header="Select none to not filter, select some to filter"
                        >
                            <c:forEach var="category" items="${categoryProductList}">
                                <option value="${category.id}" ${paramValues.catId.stream().anyMatch((e) -> e.equals(category.id.toString())).get()?'selected':''}>${category.name}</option>
                            </c:forEach>
                        </select>
                        <input type="search" class="form-control" name="searchWords" placeholder="cerchi qualcosa?"
                               required="required" value="${not empty param.searchWords?param.searchWords:''}">
                        <div class="input-group-append">
                            <button type="submit" class="btn btn-info"><i class="fas fa-search"></i> CERCA</button>
                        </div>
                    </div>
                </form>

            </div>
        </div>

        <%--menu principale--%>
        <div class="menu-nav">
            <nav class="navbar navbar-expand-sm bg-info navbar-dark">
                <ul class="navbar-nav nav-justified justify-content-center w-100">

                    <li class="nav-item active">
                        <a class="nav-link " href="<c:url value="/"/>" title="home">
                            HOME
                        </a>
                    </li>

                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" data-toggle="dropdown">
                            CATEGORIA
                        </a>
                        <div class="dropdown-menu">
                            <%--get tutte le categorie di prodotto--%>
                            <c:forEach var="category" items="${categoryProductList}">
                                <a class="dropdown-item"
                                   href="<c:url value="/category?catId=${category.id}"/>">${category.name}</a>
                            </c:forEach>
                        </div>
                    </li>

                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value="/updateProduct"/>">
                            AGGIUNGE PRODOTTO
                        </a>
                    </li>

                    <li class="nav-item">
                        <a class="nav-link" href="#">
                            link3
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#">
                            link4
                        </a>
                    </li>

                </ul>
            </nav>
        </div>

    </header>

    <%-- la sezione body principale--%>
    <section class="main-container row mb-2 mt-2 pb-1 pt-1">





