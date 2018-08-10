<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page import="it.unitn.webprogramming18.dellmm.servlets.userSystem.ResetPasswordServlet" %>

<%@ page import="it.unitn.webprogramming18.dellmm.util.RegistrationValidator" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.ConstantsUtils" %>

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

    <link rel="stylesheet" href="<c:url value="/libs/fontawesome-free-5.1.1-web/css/all.min.css"/>" type="text/css" media="all">
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
                    <label for="inputPassword" class="sr-only"><fmt:message key="user.label.password"/></label>
                    <div class="input-group">
                        <div class="input-group-prepend"><i class="input-group-text fas fa-key"></i></div>
                        <input id="inputPassword" class="form-control" placeholder="<fmt:message key="user.label.password"/>" required="" autofocus=""
                               type="password" name="${RegistrationValidator.FIRST_PWD_KEY}">
                        <span id="spanPassword"></span>
                    </div>
                </div>

                <input type="hidden" name="id" id="inputId" value="${param[ResetPasswordServlet.ID_KEY]}">

                <div class="alert d-none" id="id-res">
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
            </div>
        </form>
    </div>
</div>

<script src="<c:url value="/js/userValidate.js"/>"></script>
<script>
    const form = $('#form-signin');

    const resDiv = $('#id-res');
    const unknownErrorMessage = '<fmt:message key="generic.errors.unknownError"/>';
    const successMessage = '<fmt:message key="loggedResetPassword.success"/>';

    const url = '<c:url value="/resetPassword.json"/>';
    const validationUrl = '<c:url value="/${ConstantsUtils.VALIDATE_REGISTRATION}"/>';

    form.find('input').on('blur change',function(){
        request_user_validation(form, false, validationUrl)
            .done(function(d){
                updateVerifyMessages(form, d);
            });
    });

    form.submit(function(e){
        e.preventDefault();

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
</script>
</body>
</html>

