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

    <style type="text/css">
        #customAvatar:checked ~ #customAvatarImg {
            display: inline-block;
        }

        #customAvatar:not(checked) ~ #customAvatarImg {
            display: none;
        }

        .img-radio:checked ~ .img-check{
            visibility: visible;
        }

        .img-input {
            width: 3.5rem;
            height: 3.5rem;
        }

        .img-check {
            margin-left: -1.625rem;
            z-index: 1;
            border-radius: 50%;

            color: #00c217;
            background: white;

            position: absolute;

            margin-top: 0.125rem;

            width: 1.5rem;
            height: 1.484375rem;
            font-size: 1.5rem;

            visibility: hidden;
        }
    </style>

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
                $('form').submit(function(){
                    if(!$('#customAvatar').is(':checked')){
                        $('#customAvatarImg').val("");
                    }
                });
            </script>
        </div>

        <button class="btn btn-lg btn-primary btn-block" type="submit">Apply</button>
    </form>
</div>
<script>
    "use strict";

    $(document).ready(function() {
        function request_errors(form, async, url){
            return $.ajax({
                dataType: "json",
                url : url,
                type: "post",
                async: async,
                data: form.find("input").serialize()
            });
        }

        function updateVerifyMessages(form, data) {
            // Prendi tutti gli <input> che ci sono nella pagina e per ognuno prendine il nome
            const inputs = form.find('input').map(function(){return this.name;}).get();
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

        function add_file_errors(form, data){
            const checked_radio = form.find('input[name="${RegistrationValidator.AVATAR_KEY}"]:checked');
            const avatarImgCustom = form.find('input[name="${RegistrationValidator.AVATAR_IMG_KEY}"]')[0];

            if(checked_radio.length === 0 || checked_radio.val() !== "${RegistrationValidator.CUSTOM_AVATAR}" ) {
                return data;
            }

            // Se l'estensione per leggere i file è supportata faccio il controllo altrimenti no
            // (fatto successivamente dal server)
            if (!window.FileReader) {
                return data;
            }

            // Se il browser ha l'estensione che permette di accedere alla proprietà files continuo altrimenti no
            // (fatto successivamente dal server)
            if (!avatarImgCustom.files) {
                return data;
            }

            const fileToUpload = avatarImgCustom.files[0];

            if(!fileToUpload) {
                data["${RegistrationValidator.AVATAR_IMG_KEY}"] = "No file";
            } else if (fileToUpload.size < ${RegistrationValidator.MIN_LEN_FILE}) {
                data["${RegistrationValidator.AVATAR_IMG_KEY}"] = "File has zero size";
            } else if(fileToUpload.size > ${RegistrationValidator.MAX_LEN_FILE}){
                data["${RegistrationValidator.AVATAR_IMG_KEY}"] = "File has size > 15MB";
            } else if(window.Blob && !fileToUpload.type.startsWith("image/")) {
                data["${RegistrationValidator.AVATAR_IMG_KEY}"] = "File must be an image";
            }

            return data;
        }

        // Salva oggetti in modo da doverli cercare una sola volta
        const form=$('#form-register');
        const URL = '${pageContext.servletContext.contextPath}/${PagePathsConstants.VALIDATE_REGISTRATION}';

        function upd(d){
            return updateVerifyMessages(form, add_file_errors(form, d));
        }

        form.find('input').on("blur", function(){ request_errors(form, true, URL).done(upd); });

        form.on("submit",function(){
            const request = request_errors(form, false, URL);

            let data;
            request.done(function(data2){data=data2});

            return upd(data);
        });
    });
</script>
</body>
</html>
