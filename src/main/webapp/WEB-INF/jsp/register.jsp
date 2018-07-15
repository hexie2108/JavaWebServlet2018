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
    <form id="form-register" method="post">
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
                           value="${param[RegistrationValidator.FIRST_PWD_KEY]}">
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
                           value="${param[RegistrationValidator.SECOND_PWD_KEY]}">
                </div>
                <span id="spanPassword2" class="help-block">
                    ${requestScope.messages.get(RegistrationValidator.SECOND_PWD_KEY)}
                </span>
            </div>
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
    function updateVerifyMessages(data) {
        let no_errors = true;
        // Prendi tutti gli <input> che ci sono nella pagina e per ognuno prendine il nome
        const inputs = $('input').map(function(){return this.name;}).get();
        // Per ogni input scrivi l'eventuale errore nello span dedicato e restituisci false se ha errori, true altrimenti
        const validityInputs = inputs.map(
            (key) => {
                if (data.hasOwnProperty(key)) {
                    $("#div" + key).addClass("has-error");
                    $("#span" + key).html(String(data[key]));
                    return false;
                }

                $("#div" + key).removeClass("has-error");
                $("#span" + key).html("");
                return true
            }
        );

        // Se degli input sono false(hanno errori) allora restituisci false, altrimenti true
        // Se false l'invio del form verrà bloccato altrimenti no
        return validityInputs.every( v => v );
    }

    $(document).ready(function() {
        const url = '${pageContext.servletContext.contextPath}/${PagePathsConstants.VALIDATE_PATH}';

        $('#inputPassword').on("keyup", function(){
            $('#strongPassword').text("Score: " + zxcvbn(this.value).score + "/4");
        });

        $('input').on("blur", () => {
            const form=$('#form-register');

            const request = $.ajax({
                dataType: "json",
                url : url,
                type: "post",
                data: form.serialize()
            });

            request.done(updateVerifyMessages);
        });

        $('#form-register').on("submit",function(){
            const form=$('#form-register');

            const request = $.ajax({
                dataType: "json",
                url : url,
                type: "post",
                async : false,
                data: form.serialize()
            });

            let data;
            request.done(function(data2){data=data2});

            return updateVerifyMessages(data);
        });
    });
</script>
</body>
</html>
