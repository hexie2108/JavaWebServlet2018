<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.RegistrationValidator" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.PagePathsConstants" %>

<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Registrazione</title>

    <script src="${pageContext.servletContext.contextPath}/libs/jquery/jquery-3.3.1.min.js"></script>
    <script src="${pageContext.servletContext.contextPath}/libs/bootstrap-4.1.1-dist/js/bootstrap.js"></script>
    <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/libs/bootstrap-4.1.1-dist/css/bootstrap.min.css">

    <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/libs/fontawesome-free-5.1.1-web/css/all.min.css" type="text/css" media="all">
    <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/css/userPages.css">
</head>
<body>
<nav class="navbar navbar-default navbar-static-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="/">Brand</a>
        </div>
    </div>
</nav>
<div class="container-fluid">
    <form id="form-register" method="post" enctype="multipart/form-data">
        <h2 class="form-signin-heading">Registrazione</h2>
        <div class="form-group row">
            <div id="divFirstName" class="col-sm-6  <c:if test='${not empty requestScope.messages.get(RegistrationValidator.FIRST_NAME_KEY)}'>has-error</c:if>">
                <div class="input-group ">
                    <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                    <label for="inputFirstName" class="sr-only">Nome</label>
                    <span> ${sessionScope.user.name}</span>
                    <input id="inputFirstName" class="form-control" placeholder="Nome" autofocus=""
                           type="text" name="${RegistrationValidator.FIRST_NAME_KEY}"
                           value="${requestScope[RegistrationValidator.FIRST_NAME_KEY]}">
                </div>
                <span id="spanFirstName" class="help-block">
                    ${requestScope.messages.get(RegistrationValidator.FIRST_NAME_KEY)}
                </span>
            </div>

            <div id="divLastName" class="col-sm-6 <c:if test='${not empty requestScope.messages.get(RegistrationValidator.LAST_NAME_KEY)}'>has-error</c:if>">
                <div class="input-group">
                    <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                    <label for="inputLastName" class="sr-only">Cognome</label>
                    <span class="input-group-addon"> ${sessionScope.user.surname}</span>
                    <input id="inputLastName" class="form-control" placeholder="Cognome" autofocus=""
                           type="text" name="${RegistrationValidator.LAST_NAME_KEY}"
                           value="${requestScope[RegistrationValidator.LAST_NAME_KEY]}">
                </div>
                <span id="spanLastName" class="help-block">
                    ${requestScope.messages.get(RegistrationValidator.LAST_NAME_KEY)}
                </span>
            </div>
        </div>


        <div class="form-group row">
            <div id="divEmail" class="col-sm-6 <c:if test='${not empty requestScope.messages.get(RegistrationValidator.EMAIL_KEY)}'>has-error</c:if>">
                <div class="input-group">
                    <span class="input-group-addon"><i class="glyphicon glyphicon-envelope"></i></span>
                    <label for="inputEmail" class="sr-only">Email address</label>
                    <span> ${sessionScope.user.email}</span>
                    <input id="inputEmail" class="form-control" placeholder="Email address" autofocus=""
                           type="email" name="${RegistrationValidator.EMAIL_KEY}"
                           value="${requestScope[RegistrationValidator.EMAIL_KEY]}">
                </div>
                <span id="spanEmail" class="help-block">
                    ${requestScope.messages.get(RegistrationValidator.EMAIL_KEY)}
                </span>
            </div>
        </div>

        <div class="form-group">
            <div class="row">
                <c:set var="customI" value="${RegistrationValidator.DEFAULT_AVATARS.stream().noneMatch(x -> sessionScope.user.img.equals(x)).get()}" scope="page"/>
                <c:if test="${customI}">
                <label>
                    <input class="d-none img-radio" required="" type="radio" name="${RegistrationValidator.AVATAR_KEY}" value=""
                           <c:if test='${requestScope[RegistrationValidator.AVATAR_KEY].equals("")}'>checked</c:if>
                    >
                    <img src="${pageContext.servletContext.getInitParameter("avatarsFolder")}/${sessionScope.user.img}" class="img-input"
                    ><i class="far fa-check-circle img-check"></i>
                </label>
                </c:if>
                <c:forEach items="${RegistrationValidator.DEFAULT_AVATARS}" var="av">
                <label>
                    <input class="d-none img-radio" required="" type="radio" name="${RegistrationValidator.AVATAR_KEY}"
                           value="${av}" <c:if test="${requestScope[RegistrationValidator.AVATAR_KEY].equals(av)}">checked</c:if>
                    >
                    <img src="${pageContext.servletContext.getInitParameter("avatarsFolder")}/${av}" class="img-input"
                    ><i class="far fa-check-circle img-check"></i>
                </label>
                </c:forEach>
                <label>
                    <input class="d-none img-radio" required="" type="radio" name="${RegistrationValidator.AVATAR_KEY}" value="custom" id="customAvatar"
                           <c:if test="${requestScope[RegistrationValidator.AVATAR_KEY].equals(RegistrationValidator.CUSTOM_AVATAR)}">checked</c:if>
                    >
                    <img src="${pageContext.servletContext.contextPath}/libs/fontawesome-free-5.1.1-web/svgs/regular/plus-square.svg" class="img-input"
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

            <script>
            </script>
        </div>

        <button class="btn btn-lg btn-primary btn-block" type="submit">Apply</button>
    </form>
</div>
<script src="${pageContext.servletContext.contextPath}/js/userValidate.js"></script>
<script>
    "use strict";

    $(document).ready(function() {
        // Salva oggetti in modo da doverli cercare una sola volta
        const form=$('#form-register');
        const URL = '${pageContext.servletContext.contextPath}/${PagePathsConstants.VALIDATE_REGISTRATION}';


        form.submit(function(){
            if(!$('#customAvatar').is(':checked')){
                $('#customAvatarImg').val("");
            }
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
