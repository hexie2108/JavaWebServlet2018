<%-- la pagina di registrazione --%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.FormValidator" %>

<%@ include file="/WEB-INF/jspf/i18n.jsp"%>


<jsp:include page="/WEB-INF/jsp/userSystem/header.jsp"/>

<div>
        <form id="form-register" name="form-register" method="post" enctype="multipart/form-data" autocomplete="off" onsubmit="return validateRegister()" action="${pageContext.request.contextPath}/service/registerService">

                <h2 class="form-title">
                        <i class="fas fa-user-plus"></i> <fmt:message key="register.label.title"/> ${a}
                </h2>


                <div class="form-group row ">

                        <div id="divEmail" class="email-group col-sm-12">

                                <div class="input-group">
                                        <div class="input-group-prepend">
                                                <span class="input-group-text input-box">
                                                        <i class="fas fa-at"></i> 
                                                </span>
                                        </div>
                                        <input id="inputEmail" class="form-control input-box" placeholder="<fmt:message key="user.label.email"/>" required="" 
                                               type="email" name="${FormValidator.EMAIL_KEY}"  autocomplete="off" maxlength="${FormValidator.EMAIL_MAX_LEN}"
                                               data-toggle="popover" data-html="true" data-placement="top"  data-trigger="focus"
                                               title="suggerimenti" data-content=" la lunghezza deve essere limitata a 44" >


                                </div>

                                <div class="error-messages error-email">
                                        <p class="null">
                                                <i class="fas fa-exclamation-triangle"></i>  <fmt:message key="validateUser.errors.EMAIL_MISSING"/>
                                        </p>
                                        <p class="max-length">
                                                <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.EMAIL_TOO_LONG"/>
                                        </p>
                                        <p class="invalid">
                                                <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.EMAIL_NOT_VALID"/>
                                        </p>
                                        <p class="repeat">
                                                <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.EMAIL_ALREADY_USED"/>
                                        </p>

                                </div>
                        </div>

                </div>

                <div class="form-group row  mt-4">

                        <div class="first-name-group col-sm-6 ">

                                <div class="input-group ">

                                        <div class="input-group-prepend">
                                                <span class="input-group-text input-box">
                                                        <i class="fas fa-user"></i>
                                                </span>
                                        </div>
                                        <input id="inputFirstName" class="form-control input-box" placeholder="<fmt:message key="user.label.name"/>" required="" 
                                               type="text" name="${FormValidator.FIRST_NAME_KEY}"  maxlength="${FormValidator.FIRST_NAME_MAX_LEN}"
                                               data-toggle="popover" data-html="true" data-placement="top"  data-trigger="focus"
                                               title="suggerimenti" data-content="la lunghezza deve essere limitata a 44"
                                               >

                                </div>
                                <div class="error-messages error-first-name">

                                        <p class="null">
                                                <i class="fas fa-exclamation-triangle"></i>   <fmt:message key="validateUser.errors.FIRST_NAME_MISSING"/>
                                        </p>
                                        <p class="max-length">
                                                <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.FIRST_NAME_TOO_LONG"/>
                                        </p>

                                </div>

                        </div>

                        <div class="last-name-group col-sm-6 ">

                                <label for="inputLastName" class="sr-only"><fmt:message key="user.label.surname"/></label>
                                <div class="input-group">
                                        <div class="input-group-prepend">
                                                <span class="input-group-text input-box">
                                                        <i class="fas fa-user"></i>
                                                </span>
                                        </div>

                                        <input id="inputLastName" class="form-control input-box" placeholder="<fmt:message key="user.label.surname"/>" required="" 
                                               type="text" name="${FormValidator.LAST_NAME_KEY}" maxlength="${FormValidator.LAST_NAME_MAX_LEN}"
                                               data-toggle="popover" data-html="true" data-placement="top"  data-trigger="focus"
                                               title="suggerimenti" data-content="la lunghezza deve essere limitata a 44"
                                               >

                                </div>

                                <div class="error-messages error-last-name">

                                        <p class="null">
                                                <i class="fas fa-exclamation-triangle"></i>  <fmt:message key="validateUser.errors.LAST_NAME_MISSING"/>
                                        </p>
                                        <p class="max-length">
                                                <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.LAST_NAME_TOO_LONG"/>
                                        </p>

                                </div>

                        </div>
                </div>



                <div class="form-group row ">

                        <div id="divPassword" class="password-group col-sm-6">

                                <div class="input-group">
                                        <div class="input-group-prepend">
                                                <span class="input-group-text input-box">
                                                        <i class="fas fa-key"></i>
                                                </span>
                                        </div>
                                        <input id="inputPassword" class="form-control input-box" placeholder="<fmt:message key="user.label.password"/>" required=""
                                               type="password" name="${FormValidator.FIRST_PWD_KEY}" maxlength="${FormValidator.PWD_MAX_LEN}"
                                               data-toggle="popover" data-html="true" data-placement="top"  data-trigger="focus"
                                               title="suggerimenti" data-content="deve essere:<br/>
                                               1. Lunga almeno 8 caratteri e al massimo 44 caratteri<br/>
                                               2. Avere almeno 1 lettera minuscola<br/>
                                               3. Avere almeno 1 lettera maiuscola<br/>
                                               4. Avere almeno 1 numero<br/>
                                               5. Avere almeno 1 simbolo<br/>"

                                               value="">

                                </div>

                                <div class="progress progress-bar-div">
                                        <div class="progress-bar progress-bar-striped progress-bar-animated"></div>
                                </div>

                                <div class="error-messages error-password">

                                        <p class="null">
                                                <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.PASSWORD_MISSING"/>
                                        </p>
                                        <p class="max-length">
                                                <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.PASSWORD_TOO_LONG"/>
                                        </p>
                                        <p class="min-length">
                                                <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.PASSWORD_TOO_SHORT"/>
                                        </p>
                                        <p class="invalid">
                                                <i class="fas fa-exclamation-triangle"></i>  <fmt:message key="validateUser.errors.PASSWORD_NOT_VALID"/>
                                        </p>

                                </div>          


                        </div>

                        <div id="divPassword2" class="password2-group col-sm-6">


                                <div class="input-group">
                                        <div class="input-group-prepend">
                                                <span class="input-group-text input-box">
                                                        <i class="fas fa-key"></i>
                                                </span>
                                        </div>
                                        <input id="inputPassword2" class="input-box form-control" placeholder="<fmt:message key="user.label.repeatPassword"/>" required=""
                                               type="password" name="${FormValidator.SECOND_PWD_KEY}" maxlength="${FormValidator.PWD_MAX_LEN}"
                                               data-toggle="popover" data-html="true" data-placement="top"  data-trigger="focus"
                                               title="suggerimenti" data-content="deve essere uguale a password appena inserito"
                                               value="">

                                </div>
                                <div class="error-messages error-password2">

                                        <p class="null">
                                                <i class="fas fa-exclamation-triangle"></i>   <fmt:message key="validateUser.errors.PASSWORD2_MISSING"/>
                                        </p>
                                        <p class="no-equal">
                                                <i class="fas fa-exclamation-triangle"></i>  <fmt:message key="validateUser.errors.PASSWORD2_NOT_SAME"/>
                                        </p>

                                </div>
                        </div>

                </div>

                <div class="avatar-group form-group">
                        <c:forEach items="${FormValidator.DEFAULT_AVATARS}" var="av" varStatus="status">
                                <div class="avatar-box custom-control custom-radio custom-control-inline">
                                        <input type="radio" class="custom-control-input  img-radio default-avatar" id="avatar-${status.index}" name="${FormValidator.AVATAR_KEY}" value="${av}" ${status.index==0?"checked":""} required="required" />
                                        <label class="custom-control-label" for="avatar-${status.index}">
                                                <img class="img-input img-fluid" src="<c:url value="/image/user/${av}"/>" />
                                                <span class="img-check">
                                                                <i class="far fa-check-circle "></i>
                                                        </span>
                                        </label>

                                </div>
                        </c:forEach>

                        <div class="avatar-box custom-control custom-radio custom-control-inline">
                                <input type="radio" class="custom-control-input  img-radio" id="avatar-custom" name="${FormValidator.AVATAR_KEY}" value="custom"  required="required" />
                                <label class="custom-control-label" for="avatar-custom">
                                        <img class="img-input img-fluid" src="<c:url value="/image/base/custom-avatar.svg"/>" />
                                        <span class="img-check">
                                                        <i class="far fa-check-circle "></i>
                                                </span>
                                </label>

                        </div>
                        <div class="error-messages error-avatar">

                                <p class="null">
                                        <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.AVATAR_MISSING"/>
                                </p>
                                <p class="invalid">
                                        <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.AVATAR_NOT_VALID"/>
                                </p>
                        </div>


                </div>

                <%--parte di immagine --%>
                <div class="form-group custom-avatar-uploader">

                        <div class=" custom-file input-group mb-3">

                                <input type="file" class="custom-file-input"  id="customAvatarImg" name="${FormValidator.AVATAR_IMG_KEY}" accept="image/jpeg, image/png, image/gif, image/bmp">
                                <label class="custom-file-label input-box" for="customAvatarImg">seleziona file</label>
                                <%-- serve per ripristinare il segnaposto --%>
                                <label class="custom-file-label-origin d-none">seleziona file</label>
                        </div>

                        <%--parte di suggerimenti --%>
                        <div class="form-group">
                                <label>accetta solo file *.jpg, *.png, *.gif, *.bmp</label>
                        </div>

                        <div class="error-messages error-custom-avatar-img">

                                <p class="null">
                                        <i class="fas fa-exclamation-triangle"></i>  <fmt:message key="validateUser.errors.AVATAR_IMG_MISSING"/>
                                </p>
                                <p class="max-length">
                                        <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.AVATAR_IMG_TOO_BIG"/>
                                </p>
                                <p class="min-length">
                                        <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.AVATAR_IMG_ZERO_DIM"/>
                                </p>
                                <p class="invalid">
                                        <i class="fas fa-exclamation-triangle"></i>  <fmt:message key="validateUser.errors.AVATAR_IMG_NOT_IMG"/>
                                </p>

                        </div>


                </div>


                <%-- accetare privacy non è obbligatoria alla registrazione--%>
                <div class="form-group row-fluid">

                        <div id="divPrivacy">
                                <div class="input-group custom-control custom-checkbox">
                                        <input id="inputInfPrivacy" class="custom-control-input"  type="checkbox" name="${FormValidator.INF_PRIVACY_KEY}" value="accepted">
                                        <label class="form-check-label custom-control-label ml-1" for="inputInfPrivacy">
                                                <a href="javascript:;" data-toggle="modal" data-target="#boxShowPrivacy" >
                                                        <fmt:message key="register.label.privacyStatementCheckbox"/>
                                                </a>
                                        </label>

                                </div>

                        </div>

                </div>



                <button class="btn btn-lg btn-primary btn-block" type="submit">
                        <fmt:message key="register.label.submit"/>
                </button>


        </form>

</div>

<div class="link-utils">     
        <div class="content-top">
                <a class=" " href="<c:url value="/resendEmail"/>">Richiede il rinvio di email</a>
        </div>
        <div class="content-divider">
                <span class="content-divider-text">
                        <fmt:message key="login.label.alreadyRegistered"/>
                </span>
        </div>
        <div class="content-bottom text-center">
                <a class="" href="<c:url value="/login"/>">
                        <i class="fas fa-sign-in-alt"></i> <fmt:message key="login.label.login"/>
                </a>
        </div>
</div>


<%-- finestra modale di privacy --%>   
<div class="modal fade" id="boxShowPrivacy">
        <div class="modal-dialog">
                <div class="modal-content">

                        <%-- box-head --%>
                        <div class="modal-header">

                                <h4 class="modal-title">
                                        <fmt:message key="register.label.privacyStatement"/>
                                </h4>

                                <button type="button" class="close" data-dismiss="modal">&times;</button>

                        </div>
                        <%-- box-body --%>
                        <div class="modal-body">
                                <div id="privacyPolicy">
                                        <fmt:message key="register.text.privacyStatement"/>
                                </div>
                        </div>

                        <%-- box-footer  --%>
                        <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">chiude</button>
                        </div>

                </div>
        </div>
</div>       

<script src="<c:url value="/libs/zxcvbn/zxcvbn.js"/>"></script>

<jsp:include page="/WEB-INF/jsp/userSystem/footer.jsp"/>
