<%-- 
    l'intestazione di user system
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
    <link rel="icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/vnd.microsoft.icon">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/vnd.microsoft.icon">

    <%--jquery--%>
    <script type="text/javascript" src="${pageContext.request.contextPath}/libs/jquery/jquery-3.3.1.min.js"></script>
    <%--js di bootstrap--%>
    <script type="text/javascript" src="${pageContext.request.contextPath}/libs/bootstrap-4.1.1-dist/js/bootstrap.bundle.min.js"></script>

    <%--js personale--%>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/user-system-script.js"></script>


    <%--css di bootstrap--%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/libs/bootstrap-4.1.1-dist/css/bootstrap.min.css" type="text/css"
          media="all">
    <%--css di fontawesome--%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/libs/fontawesome-free-5.1.1-web/css/all.min.css" type="text/css"
          media="all">
    <%--css personale --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user-system-style.css" type="text/css"
          media="all">

</head>

<body class="user-system">


<div class="container-fluid" id="div-signin">


    <%-- la sezione di header--%>
    <header class="header">

        <%-- barra top--%>
        <div class="top-bar  fixed-top bg-white ">

            <%-- logo di sito--%>
            <div class="site-logo-section">

                <a href="${pageContext.request.contextPath}" title="home">
                    <img class="logo d-inline" src="${pageContext.request.contextPath}/image/base/logo.png" alt="logo"/>
                    <h2 class="site-title d-inline ">
                        Il nome del sito
                    </h2>
                </a>

            </div>

        </div>

    </header>


    <div class="right-windows">
        <div id="div-inner">






