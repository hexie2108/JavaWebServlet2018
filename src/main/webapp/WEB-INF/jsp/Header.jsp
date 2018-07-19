<%-- 
    Document   : header
    Created on : 2018-7-15, 7:55:07
    Author     : liu
    l'intestazione comune per tutti front-page jsp
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
        <head>
                <!--impostazione base-->
                <meta charset="utf-8">
                <meta http-equiv="x-ua-compatible" content="ie=edge">
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no, user-scalable=no">
                <!--titolo della pagina-->
                <title>
                        <c:out value="${head_title}" default="non hai ancora un titolo"></c:out>
                </title>
                <!--icone del sito-->
                 <link rel="icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/vnd.microsoft.icon"> 
                <link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/vnd.microsoft.icon"> 

                <!--jquery-->
                <script type="text/javascript" src="${pageContext.request.contextPath}/libs/jquery/jquery-3.3.1.slim.min.js"></script>
                <!--js di bootstrap-->
                <script type="text/javascript" src="${pageContext.request.contextPath}/libs/bootstrap/bootstrap.min.js"></script>
                <!--css di bootstrap-->
                <link rel="stylesheet" href="${pageContext.request.contextPath}/libs/bootstrap/bootstrap.min.css" type="text/css" media="all">
                <!--css di fontawesome-->
                <link rel="stylesheet" href="${pageContext.request.contextPath}/libs/fontAwesome/all.css" type="text/css" media="all">
                <!--css personale di sito-->
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css" media="all">

        </head>
        
        <body class="front-page">
                <div class="container">
                        <!-- la sezione di header-->
                        <header class="header">
                                
                                <!-- barra menu top-->
                                <div class="top-bar  fixed-top row bg-dark ">
                                        
                                        <div class="site-logo-section col-1">
                                                <span class="vertical"></span>
                                                        <img class="logo" src="${pageContext.request.contextPath}/image/base/logo.png" alt="logo"/>
                                                        
                                        </div> 
                                                        
                                        <div class="site-name-section  col-6">
                                                <h2 class="text-white">Il nome del sito</h2>
                                         </div>
                                        <nav class="navbar navbar-expand-sm navbar-dark col-4" >
                                                <ul class="navbar-nav ">
                                                        <li class="nav-item">
                                                                <a class="nav-link" href="#">
                                                                        <i class="fas fa-list"></i> MIA LISTA
                                                                </a>
                                                        </li>
                                                        <li class="nav-item">
                                                                <a class="nav-link" href="#">
                                                                        <i class="fa fa-envelope"></i> NOTIFICA
                                                                </a>
                                                        </li>
                                                        <li class="nav-item">
                                                                <a class="nav-link" href="#">
                                                                        <i class="fas fa-user-plus"></i> ISCRIVERSI
                                                                </a>
                                                        </li>
                                                        <li class="nav-item">
                                                                <a class="nav-link" href="#">
                                                                        <i class="fas fa-lock"></i>   LOGIN
                                                                </a>
                                                        </li>
                                                </ul>      
                                        </nav>
                                </div>
                                                        
                                 <!-- finestra di image top-->                       
                                <div class="top-windows ">
                                        
                                        <div class="search-section col">
                                                <form class="mt-5" action="${pageContext.request.contextPath}/search">
                                                        <div class="input-group mb-3">
                                                                <input type="search" class="form-control" name="searchWords" placeholder="cerchi qualcosa?" required="required">
                                                                <div class="input-group-append">
                                                                        <input class="btn btn-info" type="submit" value="CERCA"/>
                                                                        
                                                                </div>
                                                        </div> 
                                                </form>
                                        </div>
                                </div>
                                                        
                               <!--menu principale-->                                                
                                <div class="menu-nav">
                                        <nav class="navbar navbar-expand-sm bg-info navbar-dark" >
                                                <ul class="navbar-nav nav-justified justify-content-center w-100">
                                                        <li class="nav-item active">
                                                                <a class="nav-link " href="${pageContext.request.contextPath}" title="home">
                                                                         HOME
                                                                </a>
                                                        </li>
                                                        <li class="nav-item">
                                                                <a class="nav-link" href="#">
                                                                        AGGIUNGE PRODOTTO
                                                                </a>
                                                        </li>
                                                        <li class="nav-item">
                                                                <a class="nav-link" href="#">
                                                                        link2
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
                                                                         
                                                                         
                         <!-- la sezione body principale-->                                                 
                        <section class="main-container row mb-2 mt-2 pb-1 pt-1">





