<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="it.unitn.webprogramming18.dellmm.servlets.userSystem.ForgotPasswordServlet" %>

<%@include file="../jspf/i18n.jsp" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><fmt:message key="forgotPassword.title"/></title>

    <link rel="stylesheet" href="<c:url value="/libs/bootstrap-4.1.1-dist/css/bootstrap.min.css"/>">
    <script src="<c:url value="/libs/jquery/jquery-3.3.1.min.js"/>"></script>
    <script src="<c:url value="/libs/bootstrap-4.1.1-dist/js/bootstrap.bundle.min.js"/>"></script>

    <link rel="stylesheet" href="<c:url value="/css/tmpToDelete/login-style.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/tmpToDelete/common.css"/>">

    <link rel="stylesheet" href="<c:url value="/libs/fontawesome-free-5.1.1-web/css/all.min.css"/>">
</head>
<body>
<%@include file="../jspf/i18n_switcher.jsp" %>
<nav class="navbar navbar-default navbar-static-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="/">Brand</a>
        </div>
    </div>
</nav>
<div class="container-fluid" id="div-signin">
    <div id="div-inner">
        <form id="form-forgot" method="post">
            <div class="container-fluid">
                <h2 class="form-signin-heading"><fmt:message key="forgotPassword.label.recoverPwd"/></h2>
                <div class="form-group">
                    <div class="input-group">
                        <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                        <label for="inputEmail" class="sr-only"><fmt:message key="user.label.email"/></label>
                        <input id="inputEmail" class="form-control" placeholder="Email" required="" autofocus=""
                               type="text" name="${ForgotPasswordServlet.EMAIL_KEY}">
                    </div>
                </div>
                <div class="alert d-none" id="id-res">
                </div>

                <div class="btn-group btn-group-justified" role="group" aria-label="...">
                    <div class="btn-group" role="group" id="id-annulla">
                        <a href="${param[ForgotPasswordServlet.PREV_URL_KEY]}" class="btn btn-default"
                           role="button"><fmt:message key="login.label.cancel"/></a>
                    </div>
                    <div class="btn-group" role="group" id="id-accedi">
                        <button class="btn btn-primary" type="submit"><fmt:message
                                key="forgotPassword.label.resetPwd"/></button>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>
<script src="<c:url value="/js/userValidate.js"/>"></script>
<script>
    $(document).ready(() => {
        const form = $('#form-forgot');

        form.submit(function (e) {
            e.preventDefault();

            const resDiv = $('#id-res');
            const url = '<c:url value="/forgotPassword.json"/>';

            const unknownErrorMessage = '<fmt:message key="generic.errors.unknownError"/>';
            const successMessage = '<fmt:message key="forgotPassword.success"/>';

            formSubmit(
                url,
                form, {
                    'multipart': false,
                    'session': false,
                    'redirectUrl': null,
                    'unknownErrorMessage': unknownErrorMessage,
                    'successMessage': successMessage,
                    'resDiv': resDiv
                }
            )
        });
    })
</script>

</body>
</html>
