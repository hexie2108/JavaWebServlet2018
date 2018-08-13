<%-- la pagina di login --%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.FormValidator" %>

<%@include file="/WEB-INF/jspf/i18n.jsp"%>

<jsp:include page="/WEB-INF/jsp/userSystem/header.jsp"/>



<div class="form-box">
        <form id="form-signin" method="POST" onsubmit="return validateLogin()" action="${pageContext.request.contextPath}/service/loginService">

                <h2 class="form-title form-signin-heading">
                        <i class="fas fa-sign-in-alt"></i> <fmt:message key="login.label.signin"/>
                </h2>

                <c:if test="${not empty param.notice}">
                        <div class="notice">
                                
                                <c:choose>
                                        <c:when test="${param.notice == 'awaitingActivation'}">
                                                <div class="alert alert-info">
                                                        <i class="fas fa-info"></i> riceverà a breve un email con link di attivazione 
                                                </div>
                                        </c:when>
                                </c:choose>
                        </div>
                </c:if>

                <div class="form-group error-messages">
                        <p class="email-null">
                                <i class="fas fa-exclamation-triangle"></i>  <fmt:message key="validateUser.errors.EMAIL_MISSING"/>
                        </p>
                        <p class="password-null">
                                <i class="fas fa-exclamation-triangle"></i>  <fmt:message key="validateUser.errors.PASSWORD_MISSING"/>
                        </p>
                        <p class="email-max-length">
                                <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.EMAIL_TOO_LONG"/>
                        </p>
                        <p class="password-max-length">
                                <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.PASSWORD_TOO_LONG"/>
                        </p>
                        <p class="invalid-format-email">
                                <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.EMAIL_NOT_VALID"/>
                        </p>
                        <p class="no-equal">
                                <i class="fas fa-exclamation-triangle"></i> <fmt:message key="login.errors.wrongUsernameOrPassword"/>
                        </p>
                        <p class="no-validated-user">
                                <i class="fas fa-exclamation-triangle"></i> <fmt:message key="login.errors.noValidatedEmail"/>
                        </p> 

                </div>

                <div class="form-group">

                        <div id="divEmail" class="email-group">

                                <div class="input-group">
                                        <div class="input-group-prepend">
                                                <span class="input-group-text input-box">
                                                        <i class="fas fa-at"></i> 
                                                </span>
                                        </div>
                                        <input id="inputEmail" class="form-control input-box" placeholder="<fmt:message key="user.label.email"/>" required="" 
                                               type="email" name="${FormValidator.EMAIL_KEY}"  placeholder="<fmt:message key="user.label.email"/>" maxlength="${FormValidator.EMAIL_MAX_LEN}">


                                </div>

                        </div>

                </div>

                <div class="form-group">

                        <div id="divPassword" class="password-group">

                                <div class="input-group">
                                        <div class="input-group-prepend">
                                                <span class="input-group-text input-box">
                                                        <i class="fas fa-key"></i>
                                                </span>
                                        </div>
                                        <input id="inputPassword" class="form-control input-box" placeholder="<fmt:message key="user.label.password"/>" required=""
                                               type="password" name="${FormValidator.FIRST_PWD_KEY}" maxlength="${FormValidator.PWD_MAX_LEN}"  value="">

                                </div>

                        </div>

                </div>

                <div class="form-group">     

                        <div id="remember-group">
                                <div class="input-group custom-control custom-checkbox">
                                        <input id="remember" class="custom-control-input"  type="checkbox" name="${FormValidator.REMEMBER_KEY}" value="remember">

                                        <label class="form-check-label custom-control-label ml-1" for="remember">
                                                <fmt:message key="login.label.rememberMe"/>
                                        </label>

                                </div>

                        </div>      
                </div>

                <button class="btn btn-lg btn-primary btn-block" type="submit">
                        <fmt:message key="login.label.login"/>
                </button>

                <%-- input hidden per memorizzare la pagine di provenienza e pagina da rindirizzare--%>
                <input type="hidden" name="${FormValidator.PREV_URL_KEY}"
                       id="inputPrevUrl" value="${param[FormValidator.PREV_URL_KEY]}">
                <input type="hidden" name="${FormValidator.NEXT_URL_KEY}"
                       id="inputNextUrl" value="${param[FormValidator.NEXT_URL_KEY]}">


        </form>
</div>
<div class="link-utils">     
        <div class="content-top">
                <a id="pwdDimenticata" class="pull-right"
                   href="<c:url value="/"/>">
                        <fmt:message key="login.label.forgotPassword"/>
                </a>

        </div>
        <div class="content-divider">
                <span class="content-divider-text">
                        <fmt:message key="login.label.notRegistered"/>
                </span>
        </div>
        <div class="content-bottom text-center">
                <a href="<c:url value="/register"/>"
                   class="btn btn-default" role="button" id="register-btn">
                        <i class="fas fa-user-plus"></i> <fmt:message key="login.label.register"/>
                </a>
        </div>
</div>

<script src="<c:url value="/js/userValidate.js"/>"></script>

<jsp:include page="/WEB-INF/jsp/userSystem/footer.jsp"/>
