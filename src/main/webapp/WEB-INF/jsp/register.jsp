<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.RegistrationValidator" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.PagePathsConstants" %>

<%@ include file="../jspf/i18n.jsp"%>

<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><fmt:message key="register.title"/></title>

    <script src="<c:url value="/libs/jquery/jquery-3.3.1.min.js"/>"></script>
    <script src="<c:url value="/libs/bootstrap-4.1.1-dist/js/bootstrap.bundle.min.js"/>"></script>
    <link rel="stylesheet" href="<c:url value="/libs/bootstrap-4.1.1-dist/css/bootstrap.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/libs/fontawesome-free-5.1.1-web/css/all.min.css"/>">

    <link rel="stylesheet" href="<c:url value="/css/userPages.css"/>">
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
    <form id="form-register" method="post" enctype="multipart/form-data">
        <h2 class="form-signin-heading"><fmt:message key="register.label.title"/></h2>
        <div class="form-group row">
            <div id="divFirstName" class="col-sm-6  ${not empty requestScope.messages.get(RegistrationValidator.FIRST_NAME_KEY)?'has-error':''}">
                <div class="input-group ">
                    <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                    <label for="inputFirstName" class="sr-only"><fmt:message key="user.label.name"/></label>
                    <input id="inputFirstName" class="form-control" placeholder="<fmt:message key="user.label.name"/>" required="" autofocus=""
                           type="text" name="${RegistrationValidator.FIRST_NAME_KEY}"
                           value="${param[RegistrationValidator.FIRST_NAME_KEY]}">
                </div>
                <span id="spanFirstName" class="help-block">
                    ${requestScope.messages.get(RegistrationValidator.FIRST_NAME_KEY)}
                </span>
            </div>

            <div id="divLastName" class="col-sm-6 ${not empty requestScope.messages.get(RegistrationValidator.LAST_NAME_KEY)?'has-error':''}">
                <div class="input-group">
                    <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                    <label for="inputLastName" class="sr-only"><fmt:message key="user.label.surname"/></label>
                    <input id="inputLastName" class="form-control" placeholder="<fmt:message key="user.label.surname"/>" required="" autofocus=""
                           type="text" name="${RegistrationValidator.LAST_NAME_KEY}"
                           value="${param[RegistrationValidator.LAST_NAME_KEY]}">
                </div>
                <span id="spanLastName" class="help-block">
                    ${requestScope.messages.get(RegistrationValidator.LAST_NAME_KEY)}
                </span>
            </div>
        </div>


        <div class="form-group row">
            <div id="divEmail" class="col-sm-6 ${not empty requestScope.messages.get(RegistrationValidator.EMAIL_KEY)?'has-error':''}">
                <div class="input-group">
                    <span class="input-group-addon"><i class="glyphicon glyphicon-envelope"></i></span>
                    <label for="inputEmail" class="sr-only"><fmt:message key="user.label.email"/></label>
                    <input id="inputEmail" class="form-control" placeholder="<fmt:message key="user.label.email"/>" required="" autofocus=""
                           type="email" name="${RegistrationValidator.EMAIL_KEY}"
                           value="${param[RegistrationValidator.EMAIL_KEY]}">
                </div>
                <span id="spanEmail" class="help-block">
                    ${requestScope.messages.get(RegistrationValidator.EMAIL_KEY)}
                </span>
            </div>
        </div>

        <div class="form-group row">
            <div id="divPassword" class="col-sm-6 ${not empty requestScope.messages.get(RegistrationValidator.FIRST_PWD_KEY)?'has-error':''}">
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
            <div id="divPassword2" class="col-sm-6 ${not empty requestScope.messages.get(RegistrationValidator.SECOND_PWD_KEY)?'has-error':''}">
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

        <div class="form-group">
            <div class="row">
                <c:forEach items="${RegistrationValidator.DEFAULT_AVATARS}" var="av">
                    <label>
                        <input class="d-none img-radio" required="" type="radio" name="${RegistrationValidator.AVATAR_KEY}"
                               value="${av}" ${requestScope[RegistrationValidator.AVATAR_KEY].equals(av)?'checked':''}
                        >
                        <img src="<c:url value="${pageContext.servletContext.getInitParameter('avatarsFolder')}/${av}"/>" class="img-input"
                        ><i class="far fa-check-circle img-check"></i>
                    </label>
                </c:forEach>
                <label>
                    <input class="d-none img-radio" required="" type="radio" name="${RegistrationValidator.AVATAR_KEY}" value="custom" id="customAvatar"
                           ${requestScope[RegistrationValidator.AVATAR_KEY].equals(RegistrationValidator.CUSTOM_AVATAR)?'checked':''}
                    >
                    <img src="<c:url value="/libs/fontawesome-free-5.1.1-web/svgs/regular/plus-square.svg"/>" class="img-input"
                    ><i class="far fa-check-circle img-check"></i>
                    <input id="customAvatarImg"
                           type="file" name="${RegistrationValidator.AVATAR_IMG_KEY}"
                           accept="image/*">
                </label>
            </div>
            <span id="spanAvatar" class="help-block">
                ${requestScope.messages.get(RegistrationValidator.AVATAR_KEY)}
            </span>
            <span id="spanAvatarImg" class="help-block">
                ${requestScope.messages.get(RegistrationValidator.AVATAR_IMG_KEY)}
            </span>
        </div>

        <div class="panel panel-default">
            <div class="panel-heading">
                <h2 class="panel-title"><fmt:message key="register.label.privacyStatement"/></h2>
            </div>
            <div class="panel-body" id="privacyPolicy">
                <fmt:message key="register.text.privacyStatement"/>
            </div>
        </div>

        <div class="form-group row-fluid">
            <div id="divPrivacy" class="col-sm-12 ${not empty requestScope.messages.get(RegistrationValidator.INF_PRIVACY_KEY)?'has-error':''}">
                <div class="input-group">
                    <input id="inputInfPrivacy" required=""
                           type="checkbox" name="${RegistrationValidator.INF_PRIVACY_KEY}"
                           value="${param[RegistrationValidator.INF_PRIVACY_KEY]}">
                    <label for="inputInfPrivacy"><fmt:message key="register.label.privacyStatementCheckbox"/></label>
                    <span id="spanInfPrivacy" class="help-block">
                        ${requestScope.messages.get(RegistrationValidator.INF_PRIVACY_KEY)}
                    </span>
                </div>
            </div>
        </div>

        <button class="btn btn-lg btn-primary btn-block" type="submit"><fmt:message key="register.label.submit"/></button>
    </form>
</div>
<script src="<c:url value="/libs/zxcvbn/zxcvbn.js"/>"></script>
<script src="<c:url value="/js/userValidate.js"/>"></script>
<script>
    $(document).ready(function() {
        // Salva oggetti in modo da doverli cercare una sola volta
        const form=$('#form-register');
        const strPwd = form.find('#strongPassword');
        const URL = '<c:url value="/${PagePathsConstants.VALIDATE_REGISTRATION}?strict="/>';


        form.submit(function(){
            if(!$('#customAvatar').is(':checked')){
                $('#customAvatarImg').val("");
            }
        });

        form.find('#inputPassword').on("keyup", function(){
            strPwd.text("<fmt:message key="user.label.passwordScore"/>: " + zxcvbn(this.value).score + "/4");
        });


        function upd(d){
            return updateVerifyMessages(form, add_file_errors(form, d));
        }

        form.find('input').blur(function(){ request_errors(form, true, URL).done(upd); });

        form.submit(function(){
            const request = request_errors(form, false, URL);

            let data;
            request.done(function(data2){data=data2});

            return upd(data);
        });
    });
</script>
</body>
</html>
