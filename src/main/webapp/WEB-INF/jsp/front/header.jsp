<%-- 

    l'intestazione front-end
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="custom" uri="/WEB-INF/custom.tld" %>
<%@ include file="/WEB-INF/jspf/i18n.jsp"%>

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
                        <c:out value="${head_title}${param.head_title}" default="non hai ancora un titolo"/>
                </title>

                <%--icone del sito--%>
                <link rel="icon" href="<c:url value="/image/base/favicon.ico"/>" type="image/vnd.microsoft.icon">
                <link rel="shortcut icon" href="<c:url value="/image/base/favicon.ico"/>" type="image/vnd.microsoft.icon">

                <%--se utente non ha attivato JS , verrà rindirizzata alla pagina di avviso--%>
                <c:if test="${empty param.noscript}">
                        <noscript>
                        <meta http-equiv="refresh" content="0;url=<c:url value="/noscript"><c:param name="noscript" value="1"></c:param><c:param name="head_title" value="no script"></c:param></c:url>">
                                        </noscript>
                </c:if>

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

                <!-- Autocompletion search(using autcomplete jquery) -->
                <script src="<c:url value="/libs/typeahead.js/typeahead.jquery.min.js"/>"></script>
                <script src="<c:url value="/libs/typeahead.js/typeahead.bundle.min.js"/>"></script>

                <link rel="stylesheet" href="<c:url value="/libs/typeahead.js/typeahead.css"/>" type="text/css" media="all"/>
                <script>
                        $(document).ready(function () {
                                $.fn.serializeAndEncode = function () {
                                        return $.map(this.serializeArray(), function (val) {
                                                return [val.name, encodeURIComponent(val.value)].join('=');
                                        }).join('&');
                                };

                                $('[name="searchWords"]').typeahead({
                                        highlight: true,
                                        hint: true
                                }, {
                                        source: function (query, syncResults, asyncResults) {
                                                $.ajax({
                                                        url: '<c:url value="/service/autocompleteProduct.json"/>',
                                                        method: 'GET',
                                                        data: $('#search-form').serializeAndEncode()
                                                }).done(function (data) {
                                                        asyncResults(data);
                                                });
                                        }
                                });
                        });
                </script>
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
                                                        <img class="logo" src="<c:url value="/image/base/logo.png"/>" alt="logo"/>
                                                        <h2 class="site-title">
                                                                Il nome del sito
                                                        </h2>
                                                </a>

                                        </div>

                                        <%-- menu top--%>
                                        <div class="site-top-menu float-right">
                                                <nav class="navbar navbar-expand navbar-dark">
                                                        <ul class="navbar-nav ">

                                                                <li id="link-notifica" class="nav-item ${not empty cookie.notifica.value? "d-block":""}">
                                                                        <a class="nav-link" href="<c:url value="/map"/>">
                                                                                <i class="fa fa-envelope"></i> <span>1 NOTIFICA</span>
                                                                        </a>
                                                                </li>

                                                                <%-- se è utente anonimo--%>
                                                                <c:if test="${empty sessionScope.user}">

                                                                        <li class="nav-item">
                                                                                <a class="nav-link" href="<c:url value="/register"/>">
                                                                                        <i class="fas fa-user-plus"></i> <span>ISCRIVERSI</span>
                                                                                </a>
                                                                        </li>

                                                                        <li class="nav-item">
                                                                                <a class="nav-link" href="<c:url value="/login"/>">
                                                                                        <i class="fas fa-sign-in-alt"></i> <span>LOGIN</span>
                                                                                </a>
                                                                        </li>

                                                                </c:if>

                                                                <%-- se è utente loggato --%>
                                                                <c:if test="${not empty sessionScope.user}">

                                                                        <li class="nav-item">
                                                                                <a class="nav-link" href="<c:url value="/mylists"/>">
                                                                                        <i class="fas fa-list"></i> <span>MIE LISTE</span>
                                                                                </a>
                                                                        </li>



                                                                        <li class="nav-item">
                                                                                <a class="nav-link profile" href="<c:url value="/modifyUser"/>">
                                                                                        <img class="avatar img-fluid" src="<c:url value="/image/user/${sessionScope.user.img}"/>" alt="avatar"/> <span>PROFILO</span>
                                                                                </a>
                                                                        </li>

                                                                        <c:if test="${sessionScope.user.isAdmin}">
                                                                                <li class="nav-item">
                                                                                        <a class="nav-link" href="<c:url value="/admin/home"/>">
                                                                                                <i class="fas fa-tachometer-alt"></i> <span>ADMIN</span>
                                                                                        </a>
                                                                                </li>
                                                                        </c:if>

                                                                        <li class="nav-item">
                                                                                <a class="nav-link" href="<c:url value="/logout"/>">
                                                                                        <i class="fas fa-sign-out-alt"></i> <span>LOGOUT</span>
                                                                                </a>
                                                                        </li>

                                                                </c:if>

                                                                <jsp:include page="/WEB-INF/jspf/i18n_switcher.jsp"/>       

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
                                                                <div class="col-7 p-0">
                                                                        <input type="search" class="form-control typeahead" name="searchWords" placeholder="cerchi qualcosa?"
                                                                               required="required" value="${not empty param.searchWords?param.searchWords:''}">
                                                                </div>
                                                                <div class="input-group-append col-2 p-0">
                                                                        <button type="submit" class="btn btn-info w-100"><i class="fas fa-search"></i> CERCA</button>
                                                                </div>
                                                        </div>
                                                </form>

                                        </div>
                                </div>

                                <%--menu principale--%>
                                <div class="menu-nav">
                                        <nav class="navbar navbar-expand-sm bg-info navbar-dark">
                                                <ul class="navbar-nav nav-justified justify-content-center w-100">

                                                        <li class="nav-item mobile-menu">
                                                                <a id="mobile-menu-active-link" class="nav-link" href="javascript:;" title="mobile-menu">
                                                                        <i class="fas fa-caret-square-down"></i> MENU
                                                                </a>
                                                        </li>
                                                        <li class="nav-item desktop-item">
                                                                <a class="nav-link " href="<c:url value="/"/>" title="home">
                                                                        HOME
                                                                </a>
                                                        </li>

                                                        <li class="nav-item dropdown desktop-item">
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

                                                        <li class="nav-item desktop-item">
                                                                <a class="nav-link" href="<c:url value="/updateProduct"/>">
                                                                        AGGIUNGE PRODOTTO
                                                                </a>
                                                        </li>


                                                </ul>
                                        </nav>
                                </div>

                        </header>

                        <%-- la sezione body principale--%>
                        <section class="main-container row mb-2 mt-2 pb-1 pt-1">





