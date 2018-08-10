<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.RegistrationValidator" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.ConstantsUtils" %>

<%@ include file="../jspf/i18n.jsp"%>

<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><fmt:message key="modifyUser.title"/></title>

    <script src="<c:url value="/libs/jquery/jquery-3.3.1.min.js"/>"></script>
    <script src="<c:url value="/libs/bootstrap-4.1.1-dist/js/bootstrap.bundle.min.js"/>"></script>
    <link rel="stylesheet" href="<c:url value="/libs/bootstrap-4.1.1-dist/css/bootstrap.min.css"/>">

    <link rel="stylesheet" href="<c:url value="/libs/fontawesome-free-5.1.1-web/css/all.min.css"/>" type="text/css" media="all">
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
        <h2 class="form-signin-heading"><fmt:message key="modifyUser.label.form"/></h2>
        <div class="form-group row">
            <div id="divFirstName" class="col-sm-6">
                <label for="inputFirstName" class="sr-only"><fmt:message key="user.label.name"/></label>
                <div class="input-group ">
                    <div class="input-group-prepend"><i class="input-group-text fas fa-user"></i></div>
                    <div class="input-group-prepend"><span class="input-group-text">${sessionScope.user.name}</span></div>
                    <input id="inputFirstName" class="form-control" placeholder="<fmt:message key="user.label.name"/>" autofocus=""
                           type="text" name="${RegistrationValidator.FIRST_NAME_KEY}">
                    <span id="spanFirstName"></span>
                </div>
            </div>

            <%-- ---%>
            <div id="divLastName" class="col-sm-6">
                <label for="inputLastName" class="sr-only"><fmt:message key="user.label.surname"/></label>
                <div class="input-group">
                    <div class="input-group-prepend"><i class="input-group-text fas fa-user"></i></div>
                    <div class="input-group-prepend"><span class="input-group-text">${sessionScope.user.surname}</span></div>
                    <input id="inputLastName" class="form-control" placeholder="<fmt:message key="user.label.surname"/>" autofocus=""
                           type="text" name="${RegistrationValidator.LAST_NAME_KEY}">
                    <span id="spanLastName"></span>
                </div>
            </div>
        </div>


        <div class="form-group row">
            <div id="divEmail" class="col-sm-6">
                <label for="inputEmail" class="sr-only"><fmt:message key="user.label.email"/></label>
                <div class="input-group">
                    <div class="input-group-prepend"><i class="input-group-text fas fa-at"></i></div>
                    <div class="input-group-prepend"><span class="input-group-text">${sessionScope.user.email}</span></div>
                    <input id="inputEmail" class="form-control" placeholder="<fmt:message key="user.label.email"/>" autofocus=""
                           type="email" name="${RegistrationValidator.EMAIL_KEY}">
                    <span id="spanEmail"></span>
                </div>
            </div>
        </div>

        <div class="form-group row">
            <div class="input-group col-sm-12">
                <c:set var="customI" value="${RegistrationValidator.DEFAULT_AVATARS.stream().noneMatch(x -> sessionScope.user.img.equals(x)).get()}" scope="page"/>
                <c:if test="${customI}">
                    <label>
                        <input class="d-none img-radio" required="" type="radio" name="${RegistrationValidator.AVATAR_KEY}" value="" checked>
                        <img src="<c:url value="${pageContext.servletContext.getInitParameter('avatarsFolder')}/${sessionScope.user.img}"/>" class="img-input"
                        ><i class="far fa-check-circle img-check"></i>
                    </label>
                </c:if>
                <c:forEach items="${RegistrationValidator.DEFAULT_AVATARS}" var="av">
                    <label>
                        <input class="d-none img-radio" required="" type="radio" name="${RegistrationValidator.AVATAR_KEY}"
                               value="${av}" ${sessionScope.user.img.equals(av)?'checked':''}
                        >
                        <img src="<c:url value="${pageContext.servletContext.getInitParameter('avatarsFolder')}/${av}"/>" class="img-input"
                        ><i class="far fa-check-circle img-check"></i>
                    </label>
                </c:forEach>
                <label>
                    <input class="d-none img-radio" required="" type="radio" name="${RegistrationValidator.AVATAR_KEY}" value="custom" id="customAvatar">
                    <img src="<c:url value="/libs/fontawesome-free-5.1.1-web/svgs/regular/plus-square.svg"/>" class="img-input"
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
        </div>

        <div class="alert d-none" id="id-res">
        </div>

        <button class="btn btn-lg btn-primary btn-block" type="submit"><fmt:message key="modifyUser.label.submit"/></button>
    </form>
</div>
<script src="<c:url value="/js/userValidate.js"/>"></script>
<script>
    "use strict";

    $(document).ready(function() {
        // Salva oggetti in modo da doverli cercare una sola volta
        const form=$('#form-register');
        const URL = '<c:url value="/${ConstantsUtils.VALIDATE_REGISTRATION}"/>';
        const urlJSON = '<c:url value="/modifyUser.json"/>';
        const resDiv = $('#id-res');

        const unknownErrorMessage = '<fmt:message key="generic.errors.unknownError"/>';
        const successMessage = '<fmt:message key="modifyUser.success"/>';

        form.find('input,select').on('blur change', () => {
            request_user_validation(form, true, URL).done((d) => updateVerifyMessages(form, add_file_errors(form,d)));
        });

        form.submit(function(e){
            e.preventDefault();

            if(!$('#customAvatar').is(':checked')){
                $('#customAvatarImg').val("");
            }

            formSubmit(
                urlJSON,
                form, {
                    'multipart': true,
                    'session': false,
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
