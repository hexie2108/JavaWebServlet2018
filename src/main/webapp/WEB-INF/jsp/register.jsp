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
    <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/libs/fontawesome-free-5.1.1-web/css/all.min.css">

    <style type="text/css">
        #customAvatar:checked ~ #customAvatarImg {
            display: inline-block;
        }

        #customAvatar:not(checked) ~ #customAvatarImg {
            display: none;
        }

        input[type="radio"]:checked ~ .img-input{
            filter: contrast(5%);
        }

        .img-input {
            width: 3.5rem;
            height: 3.5rem;
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
                    <input id="inputFirstName" class="form-control" placeholder="Nome" required="" autofocus=""
                           type="text" name="${RegistrationValidator.FIRST_NAME_KEY}"
                           value="${param[RegistrationValidator.FIRST_NAME_KEY]}">
                </div>
                <span id="spanFirstName" class="help-block">
                    ${requestScope.messages.get(RegistrationValidator.FIRST_NAME_KEY)}
                </span>
            </div>

            <div id="divLastName" class="col-sm-6 <c:if test='${not empty requestScope.messages.get(RegistrationValidator.LAST_NAME_KEY)}'>has-error</c:if>">
                <div class="input-group">
                    <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                    <label for="inputLastName" class="sr-only">Cognome</label>
                    <input id="inputLastName" class="form-control" placeholder="Cognome" required="" autofocus=""
                           type="text" name="${RegistrationValidator.LAST_NAME_KEY}"
                           value="${param[RegistrationValidator.LAST_NAME_KEY]}">
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
                    <input id="inputEmail" class="form-control" placeholder="Email address" required="" autofocus=""
                           type="email" name="${RegistrationValidator.EMAIL_KEY}"
                           value="${param[RegistrationValidator.EMAIL_KEY]}">
                </div>
                <span id="spanEmail" class="help-block">
                    ${requestScope.messages.get(RegistrationValidator.EMAIL_KEY)}
                </span>
            </div>
        </div>

        <div class="form-group row">
            <div id="divPassword" class="col-sm-6 <c:if test='${not empty requestScope.messages.get(RegistrationValidator.FIRST_PWD_KEY)}'>has-error</c:if>">
                <div class="input-group">
                    <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                    <label for="inputPassword" class="sr-only">Password</label>
                    <input id="inputPassword" class="form-control" placeholder="Password" required=""
                           type="password" name="${RegistrationValidator.FIRST_PWD_KEY}"
                           value="">
                    <span id="strongPassword" class="input-group-addon">Score: x/x</span>
                </div>
                <span id="spanPassword" class="help-block">
                    ${requestScope.messages.get(RegistrationValidator.FIRST_PWD_KEY)}
                </span>
            </div>
            <div id="divPassword2" class="col-sm-6 <c:if test='${not empty requestScope.messages.get(RegistrationValidator.SECOND_PWD_KEY)}'>has-error</c:if>">
                <div class="input-group">
                    <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                    <label for="inputPassword2" class="sr-only">Repeat password</label>
                    <input id="inputPassword2" class="form-control" placeholder="Repeat password" required=""
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
                <c:forEach items="${RegistrationValidator.DEFAULT_AVATARS}" var="av" varStatus="st">
                <label>
                    <input class="d-none" required="" type="radio" name="${RegistrationValidator.AVATAR_KEY}" value="${av}" <c:if test="${st.first}">checked</c:if>>
                    <img class="img-input" src="${pageContext.servletContext.getInitParameter("avatarsFolder")}/${av}">
                </label>
                </c:forEach>
                <label>
                    <input class="d-none" required="" type="radio" name="${RegistrationValidator.AVATAR_KEY}" value="custom" id="customAvatar">
                    <img src="${pageContext.servletContext.contextPath}/libs/fontawesome-free-5.1.1-web/svgs/regular/plus-square.svg" class="img-input">
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

        <div class="panel panel-default">
            <div class="panel-heading">
                <h2 class="panel-title">Informativa alla privacy</h2>
            </div>
            <div class="panel-body" id="privacyPolicy">
                Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo. Quisque sit amet est et sapien ullamcorper pharetra. Vestibulum erat wisi, condimentum sed, commodo vitae, ornare sit amet, wisi. Aenean fermentum, elit eget tincidunt condimentum, eros ipsum rutrum orci, sagittis tempus lacus enim ac dui. Donec non enim in turpis pulvinar facilisis. Ut felis. Praesent dapibus, neque id cursus faucibus, tortor neque egestas augue, eu vulputate magna eros eu erat. Aliquam erat volutpat. Nam dui mi, tincidunt quis, accumsan porttitor, facilisis luctus, metus
            </div>
        </div>

        <div class="form-group row-fluid">
            <div id="divPrivacy" class="col-sm-12 <c:if test='${not empty requestScope.messages.get(RegistrationValidator.INF_PRIVACY_KEY)}'>has-error</c:if>">
                <div class="input-group">
                    <input id="inputInfPrivacy" required=""
                           type="checkbox" name="${RegistrationValidator.INF_PRIVACY_KEY}"
                           value="${param[RegistrationValidator.INF_PRIVACY_KEY]}">Accetta l'informativa sulla privacy
                    <label for="inputInfPrivacy" ></label>
                    <span id="spanInfPrivacy" class="help-block">
                        ${requestScope.messages.get(RegistrationValidator.INF_PRIVACY_KEY)}
                    </span>
                </div>
            </div>
        </div>

        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
    </form>
</div>
<script src="${pageContext.servletContext.contextPath}/libs/zxcvbn/zxcvbn.js"></script>
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
        const strPwd = form.find('#strongPassword');
        const URL = '${pageContext.servletContext.contextPath}/${PagePathsConstants.VALIDATE_REGISTRATION}?strict=';

        function upd(d){
            return updateVerifyMessages(form, add_file_errors(form, d));
        }


        form.find('#inputPassword').on("keyup", function(){
            strPwd.text("Score: " + zxcvbn(this.value).score + "/4");
        });

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
