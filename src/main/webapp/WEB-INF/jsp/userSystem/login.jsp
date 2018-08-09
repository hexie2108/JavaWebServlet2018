<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<%@ page import="it.unitn.webprogramming18.dellmm.util.PagePathsConstants" %>
<%@ page import="it.unitn.webprogramming18.dellmm.servlets.userSystem.LoginServlet" %>

<html>
        <head>
                <meta charset="utf-8">
                <meta http-equiv="X-UA-Compatible" content="IE=edge">
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <title>SignIn</title>

                <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/libs/bootstrap/bootstrap.min.css">
                <script src="${pageContext.servletContext.contextPath}/libs/jquery/jquery-3.3.1.slim.min.js"></script>
                <script src="${pageContext.servletContext.contextPath}/libs/bootstrap/bootstrap.min.js"></script>

                <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/css/tmpToDelete/login-style.css">
                <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/css/tmpToDelete/common.css">
        </head>
        <body>
                <div class="container-fluid" id="div-signin">
                        <div id="div-inner">
                                <form id="form-signin" method="post">
                                        <div class="container-fluid">
                                                <h2 class="form-signin-heading">Accedi</h2>
                                                <div class="form-group">
                                                        <div class="input-group">
                                                                <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                                                                <label for="inputEmail" class="sr-only">Email address</label>
                                                                <input id="inputEmail" class="form-control" placeholder="Email" required="" autofocus=""
                                                                       type="text" name="${LoginServlet.EMAIL_KEY}"
                                                                       value="${param[LoginServlet.EMAIL_KEY]}">
                                                        </div>
                                                </div>

                                                <div class="form-group">
                                                        <div class="input-group">
                                                                <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                                                                <label for="inputPassword" class="sr-only">Password</label>
                                                                <input id="inputPassword" class="form-control" placeholder="Password" required=""
                                                                       type="password" name="${LoginServlet.PWD_KEY}"
                                                                       value="">
                                                        </div>
                                                </div>

                                                <input type="hidden" name="${LoginServlet.PREV_URL_KEY}"
                                                       id="inputPrevUrl" value="${param[LoginServlet.PREV_URL_KEY]}">
                                                <input type="hidden" name="${LoginServlet.NEXT_URL_KEY}"
                                                       id="inputNextUrl" value="${param[LoginServlet.NEXT_URL_KEY]}">

                                                <div class="alert d-none" id="id-res">
                                                </div>

                                                <div class="checkbox pull-left">
                                                        <label><input class="noMarginTop" name="remember" type="checkbox" value="">Remember me</label>
                                                </div>
                                                <a id="pwdDimenticata" class="pull-right" href="${pageContext.request.contextPath}/${PagePathsConstants.FORGOT_PASSWORD}">Password dimenticata</a>

                                                <div class="btn-group btn-group-justified" role="group" aria-label="...">
                                                        <div class="btn-group" role="group" id="id-annulla">
                                                                <a href="${param[LoginServlet.PREV_URL_KEY]}" class="btn btn-default" role="button">Annulla</a>
                                                        </div>
                                                        <div class="btn-group" role="group" id="id-accedi">
                                                                <button class="btn btn-primary" type="submit">Accedi</button>
                                                        </div>
                                                </div>

                                        </div>
                                </form>
                                <div class="content-divider"><span class="content-divider-text">Non sei registrato?</span></div>
                                <a href="${pageContext.servletContext.contextPath}/${PagePathsConstants.REGISTER}"
                                   class="btn btn-default" role="button" id="register-btn">Registrati</a>
                        </div>
                </div>


                <script src="<c:url value="/js/userValidate.js"/>"></script>
                <script>
                        const form = $('#form-signin');
                        const resDiv = $('#id-res');
                        const url = '<c:url value="/login.json"/>';

                        const unknownErrorMessage = 'unknownError';

                        form.submit(function (e) {
                                e.preventDefault();


                                formSubmit(
                                        url,
                                        form, {
                                                'multipart': false,
                                                'session': true,
                                                'redirectUrl': '<c:url value="/"/>',
                                                'unknownErrorMessage': unknownErrorMessage,
                                                'resDiv': resDiv
                                        }
                                );
                        });
                </script>
        </body>
</html>
