<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page import="it.unitn.webprogramming18.dellmm.servlets.userSystem.LoginServlet" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.PagePathsConstants" %>

<%@include file="../jspf/i18n.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><fmt:message key="login.title"/></title>

    <link rel="stylesheet" href="<c:url value="/libs/bootstrap-4.1.1-dist/css/bootstrap.min.css"/>">
    <script src="<c:url value="/libs/jquery/jquery-3.3.1.min.js"/>"></script>
    <script src="<c:url value="/libs/bootstrap-4.1.1-dist/js/bootstrap.bundle.min.js"/>" crossorigin="anonymous"></script>

    <link rel="stylesheet" href="<c:url value="/css/tmpToDelete/login-style.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/tmpToDelete/common.css"/>">
</head>
<body>
<%@include file="../jspf/i18n_switcher.jsp"%>
<div class="container-fluid" id="div-signin">
    <div id="div-inner">
        <form id="form-signin" method="post">
            <div class="container-fluid">
                <h2 class="form-signin-heading"><fmt:message key="login.label.signin"/></h2>
                <div class="form-group">
                    <div class="input-group">
                        <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                        <label for="inputEmail" class="sr-only"><fmt:message key="user.label.email"/></label>
                        <input id="inputEmail" class="form-control" placeholder="<fmt:message key="user.label.email"/>" required="" autofocus=""
                               type="text" name="${LoginServlet.EMAIL_KEY}">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                        <label for="inputPassword" class="sr-only"><fmt:message key="user.label.password"/></label>
                        <input id="inputPassword" class="form-control" placeholder="<fmt:message key="user.label.password"/>" required=""
                               type="password" name="${LoginServlet.PWD_KEY}">
                    </div>
                </div>

                <input type="hidden" name="${LoginServlet.PREV_URL_KEY}"
                       id="inputPrevUrl" value="${param[LoginServlet.PREV_URL_KEY]}">
                <input type="hidden" name="${LoginServlet.NEXT_URL_KEY}"
                       id="inputNextUrl" value="${param[LoginServlet.NEXT_URL_KEY]}">

                <div class="alert alert-danger d-none" id="id-alert" style="">
                </div>

                <div class="checkbox pull-left">
                    <label><input class="noMarginTop" name="remember" type="checkbox" value=""><fmt:message key="login.label.rememberMe"/></label>
                </div>
                <a id="pwdDimenticata" class="pull-right"
                   href="<c:url value="/${PagePathsConstants.FORGOT_PASSWORD}"/>"><fmt:message key="login.label.forgotPassword"/></a>

                <div class="btn-group btn-group-justified" role="group" aria-label="...">
                    <div class="btn-group" role="group" id="id-annulla">
                        <a href="${param[LoginServlet.PREV_URL_KEY]}" class="btn btn-default" role="button"><fmt:message key="login.label.cancel"/> </a>
                    </div>
                    <div class="btn-group" role="group" id="id-accedi">
                        <button class="btn btn-primary" type="submit"><fmt:message key="login.label.signin"/></button>
                    </div>
                </div>

            </div>
            <script>
                const form = $('#form-signin');
                const alertDiv = $('#id-alert');

                form.submit(function(e){
                    e.preventDefault();

                    $.ajax({
                        dataType: "json",
                        url : '<c:url value="/login.json"/>',
                        type: "post",
                        async: false,
                        data: form.serialize(),
                        xhrFields: {
                            withCredentials: true
                        }
                    }).done(( data) => {
                        window.location.replace(data['nextUrl']);
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
        </form>
        <div class="content-divider"><span class="content-divider-text"><fmt:message key="login.label.notRegistered"/></span></div>
        <a href="<c:url value="/${PagePathsConstants.REGISTER}"/>"
           class="btn btn-default" role="button" id="register-btn"><fmt:message key="login.label.register"/></a>
    </div>
</div>
</body>
</html>
