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

    <script src="${pageContext.servletContext.contextPath}/libs/jquery/jquery-3.3.1.min.js"></script>
    <script src="${pageContext.servletContext.contextPath}/libs/bootstrap-4.1.1-dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/libs/bootstrap-4.1.1-dist/css/bootstrap.min.css">
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
            <div id="divPassword" class="col-sm-6 <c:if test='${not empty requestScope.messages.get(RegistrationValidator.FIRST_PWD_KEY)}'>has-error</c:if>">
                <div class="input-group">
                    <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                    <label for="inputPassword" class="sr-only"><fmt:message key="user.label.password"/></label>
                    <input id="inputPassword" class="form-control" placeholder="<fmt:message key="user.label.password"/>" required=""
                           type="password" name="${RegistrationValidator.FIRST_PWD_KEY}"
                           value="">
                    <span id="strongPassword" class="input-group-addon"><fmt:message key="user.label.passwordScore"/>: x/x</span>
                </div>
                <span id="spanPassword" class="help-block">
                    ${requestScope.messages.get(RegistrationValidator.FIRST_PWD_KEY)}
                </span>
            </div>
        </div>

        <div class="form-group row">
            <div id="divPassword2" class="col-sm-6 <c:if test='${not empty requestScope.messages.get(RegistrationValidator.SECOND_PWD_KEY)}'>has-error</c:if>">
                <div class="input-group">
                    <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                    <label for="inputPassword2" class="sr-only"><fmt:message key="user.label.repeatPassword"/></label>
                    <input id="inputPassword2" class="form-control" placeholder="<fmt:message key="user.label.repeatPassword"/>" required=""
                           type="password" name="${RegistrationValidator.SECOND_PWD_KEY}"
                           value="">
                </div>
                <span id="spanPassword2" class="help-block">
                    ${requestScope.messages.get(RegistrationValidator.SECOND_PWD_KEY)}
                </span>
            </div>
        </div>

        <button class="btn btn-lg btn-primary btn-block" type="submit"><fmt:message key="loggedResetPassword.submit"/></button>
    </form>
</div>
<script>
    function updateVerifyMessages(data) {
        // Prendi tutti gli <input> che ci sono nella pagina e per ognuno prendine il nome
        const inputs = $('input').map(function(){return this.name;}).get();
        // Per ogni input scrivi l'eventuale errore nello span dedicato e restituisci false se ha errori, true altrimenti
        const validityInputs = inputs.map(
            (key) => {
                const div = $("#div" + key);
                const span = $("#span" + key);

                if (data.hasOwnProperty(key)) {
                    div.addClass("has-error");
                    span.html(String(data[key]));
                    return false;
                }

                div.removeClass("has-error");
                span.html("");
                return true;
            }
        );

        // Se degli input sono false(hanno errori) allora restituisci false, altrimenti true
        // Se false l'invio del form verrà bloccato altrimenti no
        return validityInputs.every( v => v );
    }

    $(document).ready(function() {
        const url = '${pageContext.servletContext.contextPath}/${PagePathsConstants.VALIDATE_REGISTRATION}';
        const form=$('#form-register');
        const strPwd = form.find('#strongPassword');

        form.find('#inputPassword').keyup(function(){
            strPwd.text("<fmt:message key="user.label.passwordScore"/>: " + zxcvbn(this.value).score + "/4");
        });

        form.find('input').blur(function(){
            const request = $.ajax({
                dataType: "json",
                url : url,
                type: "post",
                data: form.serialize(),
                xhrFields: {
                    withCredentials: true
                }
            });

            request.done(updateVerifyMessages);
        });

        form.submit(function(){
            const request = $.ajax({
                dataType: "json",
                url : url,
                type: "post",
                async : false,
                data: form.serialize(),
                xhrFields: {
                    withCredentials: true
                }
            });

            let data;
            request.done(function(data2){data=data2});

            return updateVerifyMessages(data);
        });
    });
</script>
</body>
</html>

