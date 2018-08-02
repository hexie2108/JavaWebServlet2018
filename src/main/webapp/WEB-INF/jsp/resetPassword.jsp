<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page import="it.unitn.webprogramming18.dellmm.servlets.userSystem.ResetPasswordServlet" %>

<%@ include file="../jspf/i18n.jsp"%>

<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><fmt:message key="resetPassword.title"/></title>

    <link rel="stylesheet" href="<c:url value="/libs/bootstrap-4.1.1-dist/css/bootstrap.min.css"/>">
    <script src="<c:url value="/libs/jquery/jquery-3.3.1.min.js"/>"></script>
    <script src="<c:url value="/libs/bootstrap-4.1.1-dist/js/bootstrap.bundle.min.js"/>"></script>

    <link rel="stylesheet" href="<c:url value="/css/tmpToDelete/login-style.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/tmpToDelete/common.css"/>">
</head>
<body>
<%@ include file="../jspf/i18n_switcher.jsp"%>
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
                <h2 class="form-signin-heading"><fmt:message key="resetPassword.title"/></h2>
                <div class="form-group">
                    <div class="input-group">
                        <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                        <label for="inputPassword" class="sr-only"><fmt:message key="user.label.password"/></label>
                        <input id="inputPassword" class="form-control" placeholder="<fmt:message key="user.label.password"/>" required="" autofocus=""
                               type="password" name="${ResetPasswordServlet.PWD_KEY}">
                    </div>
                </div>

                <input type="hidden" name="id" id="inputId" value="${param[ResetPasswordServlet.ID_KEY]}">

                <div class="alert alert-danger d-none" id="id-alert">
                </div>

                <div class="btn-group btn-group-justified" role="group" aria-label="...">
                    <div class="btn-group" role="group" id="id-annulla">
                        <%-- TODO: Gestione prevUrl --%>
                        <a href="${param.prevUrl}" class="btn btn-default" role="button"><fmt:message key="resetPassword.label.cancel"/></a>
                    </div>
                    <div class="btn-group" role="group" id="id-accedi">
                        <button class="btn btn-primary" type="submit"><fmt:message key="resetPassword.label.submit"/></button>
                    </div>
                </div>

                <script>
                    const form = $('#form-signin');
                    const alertDiv = $('#id-alert');

                    form.submit(function(e){
                        e.preventDefault();

                        $.ajax({
                            dataType: "json",
                            url : '<c:url value="/resetPassword.json"/>',
                            type: "post",
                            async: false,
                            data: form.serialize()
                        }).done(( data) => {
                            window.location.replace('<c:url value="/login"/>');
                        }).fail( (jqXHR) => {
                            alertDiv.html(
                                (typeof jqXHR.responseJSON === 'object' &&
                                    jqXHR.responseJSON !== null &&
                                    jqXHR.responseJSON['message'] !== undefined)?
                                    jqXHR.responseJSON['message']:
                                    "<fmt:message key="generic.errors.unknownError"/>"
                            );

                            alertDiv.removeClass("d-none");
                        });
                    });
                </script>
            </div>
        </form>
    </div>
</div>
</body>
</html>

