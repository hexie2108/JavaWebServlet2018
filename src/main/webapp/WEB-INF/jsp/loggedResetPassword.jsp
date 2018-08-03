<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.RegistrationValidator" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.PagePathsConstants" %>

<%@ include file="../jspf/i18n.jsp"%>

<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><fmt:message key="loggedResetPassword.title"/></title>

    <script src="<c:url value="/libs/jquery/jquery-3.3.1.min.js"/>"></script>
    <script src="<c:url value="/libs/bootstrap-4.1.1-dist/js/bootstrap.bundle.min.js"/>"></script>
    <link rel="stylesheet" href="<c:url value="/libs/bootstrap-4.1.1-dist/css/bootstrap.min.css"/>">
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
<div class="container-fluid">
    <form id="form-register" method="post">
        <h2 class="form-signin-heading"><fmt:message key="loggedResetPassword.label.form"/></h2>

        <div class="form-group row">
            <div id="divPassword" class="col-sm-6">
                <div class="input-group">
                    <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                    <label for="inputPassword" class="sr-only"><fmt:message key="user.label.password"/></label>
                    <input id="inputPassword" class="form-control" placeholder="<fmt:message key="user.label.password"/>" required=""
                           type="password" name="${RegistrationValidator.FIRST_PWD_KEY}">
                    <span id="strongPassword" class="input-group-addon"><fmt:message key="user.label.passwordScore"/>: x/x</span>
                </div>
                <span id="spanPassword" class="help-block">
                </span>
            </div>
        </div>

        <div class="form-group row">
            <div id="divPassword2" class="col-sm-6">
                <div class="input-group">
                    <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                    <label for="inputPassword2" class="sr-only"><fmt:message key="user.label.repeatPassword"/></label>
                    <input id="inputPassword2" class="form-control" placeholder="<fmt:message key="user.label.repeatPassword"/>" required=""
                           type="password" name="${RegistrationValidator.SECOND_PWD_KEY}">
                </div>
                <span id="spanPassword2" class="help-block">
                </span>
            </div>
        </div>

        <div class="alert d-none" id="id-res">
        </div>

        <button class="btn btn-lg btn-primary btn-block" type="submit"><fmt:message key="loggedResetPassword.submit"/></button>
    </form>
</div>

<script src="<c:url value="/libs/zxcvbn/zxcvbn.js"/>"></script>
<script src="<c:url value="/js/userValidate.js"/>"></script>
<script>
    $(document).ready(function() {
        const url = '<c:url value="/${PagePathsConstants.VALIDATE_REGISTRATION}"/>';
        const urlJSON ='<c:url value="/loggedResetPassword.json"/>';
        const form=$('#form-register');
        const strPwd = form.find('#strongPassword');
        const inPwd = form.find('#inputPassword');

        const resDiv = $('#id-res');
        const unknownErrorMessage = '<fmt:message key="generic.errors.unknownError"/>';
        const successMessage = '<fmt:message key="loggedResetPassword.success"/>';

        inPwd.keyup(() => {
            strPwd.text("<fmt:message key="user.label.passwordScore"/>: " + zxcvbn(inPwd.val()).score + "/4");
        });

        form.find('input').blur(function(){
            request_user_validation(form, false, url)
                .done(function(d){
                    updateVerifyMessages(form, d);
                });
        });

        form.submit(function(e){
            e.preventDefault();

            formSubmit(
                urlJSON,
                form, {
                    'multipart': false,
                    'session': true,
                    'redirectUrl': null,
                    'unknownErrorMessage': unknownErrorMessage,
                    'successMessage': successMessage,
                    'resDiv': resDiv
                }
            );
        });
    });
</script>
</body>
</html>

