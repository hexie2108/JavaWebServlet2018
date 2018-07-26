<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page import="it.unitn.webprogramming18.dellmm.servlets.userSystem.ResetPasswordServlet" %>


<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>SignIn</title>

    <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/libs/bootstrap-4.1.1-dist/css/bootstrap.min.css">
    <script src="${pageContext.servletContext.contextPath}/libs/jquery/jquery-3.3.1.min.js"></script>
    <script src="${pageContext.servletContext.contextPath}/libs/bootstrap-4.1.1-dist/js/bootstrap.min.js"></script>

    <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/css/tmpToDelete/login-style.css">
    <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/css/tmpToDelete/common.css">
</head>
<body>
<nav class="navbar navbar-default navbar-static-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="/">Brand</a>
        </div>
    </div>
</nav>
<div class="container-fluid" id="div-signin">
    <div id="div-inner">
        <form id="form-signin" method="post">
            <div class="container-fluid">
                <h2 class="form-signin-heading">Recupera password</h2>
                <div class="form-group">
                    <div class="input-group">
                        <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                        <label for="inputPassword" class="sr-only">Email address</label>
                        <input id="inputPassword" class="form-control" placeholder="Password" required="" autofocus=""
                               type="password" name="${ResetPasswordServlet.PWD_KEY}"
                               value="${param[ResetPasswordServlet.PWD_KEY]}">
                    </div>
                </div>

                <div class="btn-group btn-group-justified" role="group" aria-label="...">
                    <div class="btn-group" role="group" id="id-annulla">
                        <%-- TODO: Gestione prevUrl --%>
                        <a href="${param.prevUrl}" class="btn btn-default" role="button">Annulla</a>
                    </div>
                    <div class="btn-group" role="group" id="id-accedi">
                        <button class="btn btn-primary" type="submit">Reset password</button>
                    </div>
                </div>

                <c:if test="${not empty param[ResetPasswordServlet.MSG_KEY]}">
                    <div class="alert alert-danger" id="id-UP-alert">
                        ${param[ResetPasswordServlet.MSG_KEY]}
                    </div>
                </c:if>

                <input type="hidden" name="id" id="inputId" value="${param[ResetPasswordServlet.ID_KEY]}">
            </div>
        </form>
    </div>
</div>
</body>
</html>

