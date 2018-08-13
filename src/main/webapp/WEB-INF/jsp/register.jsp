<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.RegistrationValidator" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.ConstantsUtils" %>

<%@ include file="../jspf/i18n.jsp" %>

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

<%@ include file="../jspf/i18n_switcher.jsp" %>

<nav class="navbar navbar-default navbar-static-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="/">Brand</a>
        </div>
    </div>
</nav>
<div class="container-fluid">
    <form id="form-register" method="post" enctype="multipart/form-data">
        <h2><fmt:message key="register.label.title"/></h2>
        <div class="form-group row">
            <div class="col-sm-6">
                <label for="inputFirstName" class="sr-only"><fmt:message key="user.label.name"/></label>
                <div class="input-group ">
                    <div class="input-group-prepend"><i class="input-group-text fas fa-user"></i></div>
                    <input id="inputFirstName" class="form-control" placeholder="<fmt:message key="user.label.name"/>"
                           required="" autofocus=""
                           type="text" name="${RegistrationValidator.FIRST_NAME_KEY}">
                    <span id="spanFirstName"></span>
                </div>
            </div>

            <div class="col-sm-6">
                <label for="inputLastName" class="sr-only"><fmt:message key="user.label.surname"/></label>
                <div class="input-group">
                    <div class="input-group-prepend"><i class="input-group-text fas fa-user"></i></div>
                    <input id="inputLastName" class="form-control" placeholder="<fmt:message key="user.label.surname"/>"
                           required="" autofocus=""
                           type="text" name="${RegistrationValidator.LAST_NAME_KEY}">
                    <span id="spanLastName"></span>
                </div>
            </div>
        </div>

        <div class="form-group row">
            <div id="divEmail" class="col-sm-12">
                <label for="inputEmail" class="sr-only"><fmt:message key="user.label.email"/></label>
                <div class="input-group">
                    <div class="input-group-prepend"><i class="input-group-text fas fa-at"></i></div>
                    <input id="inputEmail" class="form-control" placeholder="<fmt:message key="user.label.email"/>"
                           required="" autofocus=""
                           type="email" name="${RegistrationValidator.EMAIL_KEY}">
                    <span id="spanEmail"></span>
                </div>
            </div>
        </div>

        <div class="form-group row">
            <div id="divPassword" class="col-sm-6">
                <label for="inputPassword" class="sr-only"><fmt:message key="user.label.password"/></label>
                <div class="input-group">
                    <div class="input-group-prepend"><i class="input-group-text fas fa-key"></i></div>
                    <input id="inputPassword" class="form-control"
                           placeholder="<fmt:message key="user.label.password"/>" required=""
                           type="password" name="${RegistrationValidator.FIRST_PWD_KEY}"
                           value="">
                    <div class="input-group-append"><span class="input-group-text" id="strongPassword"><fmt:message
                            key="user.label.passwordScore"/>: x/x</span></div>
                    <span id="spanPassword"></span>
                </div>
            </div>
            <div id="divPassword2" class="col-sm-6">
                <label for="inputPassword2" class="sr-only"><fmt:message key="user.label.repeatPassword"/></label>
                <div class="input-group">
                    <span class="input-group-prepend"><i class="input-group-text fas fa-key"></i></span>
                    <input id="inputPassword2" class="form-control"
                           placeholder="<fmt:message key="user.label.repeatPassword"/>" required=""
                           type="password" name="${RegistrationValidator.SECOND_PWD_KEY}"
                           value="">
                    <span id="spanPassword2"></span>
                </div>
            </div>
        </div>

        <div class="form-group row">
            <c:forEach items="${RegistrationValidator.DEFAULT_AVATARS}" var="av">
                <label>
                    <input class="d-none img-radio" required="" type="radio" name="${RegistrationValidator.AVATAR_KEY}"
                           value="${av}" ${st.first?'checked':''}>
                    <img src="<c:url value="${pageContext.servletContext.getInitParameter('avatarsFolder')}/${av}"/>"
                         class="img-input"
                    ><i class="far fa-check-circle img-check"></i>
                </label>
            </c:forEach>
            <label>
                <input class="d-none img-radio" required="" type="radio" name="${RegistrationValidator.AVATAR_KEY}"
                       value="custom" id="customAvatar">
                <img src="<c:url value="/libs/fontawesome-free-5.1.1-web/svgs/regular/plus-square.svg"/>"
                     class="img-input"
                ><i class="far fa-check-circle img-check"></i>
                <input id="customAvatarImg"
                       type="file" name="${RegistrationValidator.AVATAR_IMG_KEY}"
                       accept="image/*">
            </label>
            <div class="form-control d-none" id="inputAvatar"></div>
            <span id="spanAvatar"></span>
            <div class="form-control d-none" id="inputAvatarImg"></div>
            <span id="spanAvatarImg"></span>
        </div>

        <div class="card mb-3">
            <div class="card-header"><b class="card-title"><fmt:message key="register.label.privacyStatement"/></b>
            </div>
            <div class="card-body" id="privacyPolicy"><fmt:message key="register.text.privacyStatement"/></div>
        </div>

        <div class="form-group row-fluid">
            <div id="divPrivacy" class="col-sm-12">
                <div class="input-group">
                    <input id="inputInfPrivacy" required=""
                           type="checkbox" name="${RegistrationValidator.INF_PRIVACY_KEY}">
                    <label class="form-check-label ml-1" for="inputInfPrivacy"><fmt:message
                            key="register.label.privacyStatementCheckbox"/></label>
                    <span id="spanInfPrivacy"></span>
                </div>
            </div>
        </div>

        <div class="alert d-none" id="id-res">
        </div>

        <button class="btn btn-lg btn-primary btn-block" type="submit"><fmt:message
                key="register.label.submit"/></button>
    </form>
</div>
<script src="<c:url value="/libs/zxcvbn/zxcvbn.js"/>"></script>
<script src="<c:url value="/js/userValidate.js"/>"></script>
<script>
    $(document).ready(function () {
        // Salva oggetti in modo da doverli cercare una sola volta
        const form = $('#form-register');
        const strPwd = form.find('#strongPassword');
        const URL = '<c:url value="/${ConstantsUtils.VALIDATE_REGISTRATION}?strict="/>';

        const urlJSON = '<c:url value="/register.json"/>';
        const resDiv = $('#id-res');
        const unknownErrorMessage = '<fmt:message key="generic.errors.unknownError"/>';

        form.find('#inputPassword').on("keyup", function () {
            strPwd.text("<fmt:message key="user.label.passwordScore"/>: " + zxcvbn(this.value).score + "/4");
        });

        function upd(d) {
            return updateVerifyMessages(form, add_file_errors(form, d));
        }

        form.find('input').blur(function () {
            request_user_validation(form, true, URL).done(upd);
        });

        form.submit(function (e) {
            e.preventDefault();

            if (!$('#customAvatar').is(':checked')) {
                $('#customAvatarImg').val("");
            }

            formSubmit(
                urlJSON,
                form, {
                    'multipart': true,
                    'session': true,
                    'redirectUrl': '<c:url value="/"/>',
                    'unknownErrorMessage': unknownErrorMessage,
                    'resDiv': resDiv
                }
            );
        });
    });
</script>
</body>
</html>
