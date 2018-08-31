<%-- 
    pagina per visualizzare il form per aggiornare i dati utente
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="custom" uri="/WEB-INF/custom.tld" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.FormValidator" %>
<%@ include file="/WEB-INF/jspf/i18n.jsp"%>

<jsp:include page="/WEB-INF/jsp/front/header.jsp"/>


<div class="user-modify-main-section col-12">
        <div class="content">

                <%-- breadcrumb--%>
                <div class="breadcrumbs">
                        <a href="<c:url value="/"/>">
                                <i class="fas fa-home"></i>
                        </a>
                        <span>&gt;</span>

                        <span>
                                <i class="fas fa-edit"></i> ${head_title}
                        </span>

                </div>

                <div class="user-modify mt-3">




                        <form id="form-register" action="<c:url value="/service/modifyUserService"/>" method="POST" enctype="multipart/form-data" autocomplete="off" onsubmit="return validateModify()">

                                <c:if test="${not empty param.update}">
                                        <div class="form-group">
                                                <div class="alert alert-success">
                                                        <i class="fas fa-info"></i> <fmt:message key="the data has been updated correctly"/>
                                                </div>
                                        </div>
                                </c:if>

                                <div class="form-group">
                                        <label><fmt:message key="the email address can not be changed"/></label>
                                </div>
                                <div class="form-group row ">

                                        <div id="divEmail" class="email-group col-sm-12">

                                                <div class="input-group">
                                                        <div class="input-group-prepend">
                                                                <span class="input-group-text input-box">
                                                                        <i class="fas fa-at"></i> 
                                                                </span>
                                                        </div>
                                                        <input id="inputEmail" class="form-control input-box" placeholder="<fmt:message key="user.label.email"/>" 
                                                               type="email" name="${FormValidator.EMAIL_KEY}"  autocomplete="off" maxlength="${FormValidator.EMAIL_MAX_LEN}" value="${sessionScope.user.email} "disabled >


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
                                                        <input id="inputFirstName" class="form-control input-box" placeholder="<fmt:message key="user.label.name"/>" required=""   type="text" name="${FormValidator.FIRST_NAME_KEY}"  maxlength="${FormValidator.FIRST_NAME_MAX_LEN}"  value="${sessionScope.user.name}" data-toggle="popover" data-html="true" data-placement="top"  data-trigger="focus"    title="suggerimenti" data-content="la lunghezza deve essere limitata a 44"
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

                                                        <input id="inputLastName" class="form-control input-box" placeholder="<fmt:message key="user.label.surname"/>" required=""  type="text" name="${FormValidator.LAST_NAME_KEY}" maxlength="${FormValidator.LAST_NAME_MAX_LEN}" value="${sessionScope.user.surname}"   >

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

                                <div class="form-group">
                                        <label><fmt:message key="if you do not want to change your password, leave it blank"/></label>
                                </div>

                                <div class="form-group row ">

                                        <div id="divPassword" class="password-group col-sm-6">

                                                <div class="input-group">
                                                        <div class="input-group-prepend">
                                                                <span class="input-group-text input-box">
                                                                        <i class="fas fa-key"></i>
                                                                </span>
                                                        </div>
                                                        <input id="inputPassword" class="form-control input-box" placeholder="<fmt:message key="user.label.password"/>" 
                                                               type="password" name="${FormValidator.FIRST_PWD_KEY}" maxlength="${FormValidator.PWD_MAX_LEN}"
                                                               data-toggle="popover" data-html="true" data-placement="top"  data-trigger="focus"
                                                               title="<fmt:message key="suggestions"/>" data-content="<fmt:message key="suggestions.password"/>"
                                                               value="">

                                                </div>

                                                <div class="progress progress-bar-div">
                                                        <div class="progress-bar progress-bar-striped progress-bar-animated"></div>
                                                </div>

                                                <div class="error-messages error-password">


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
                                                        <input id="inputPassword2" class="input-box form-control" placeholder="<fmt:message key="user.label.repeatPassword"/>" 
                                                               type="password" name="${FormValidator.SECOND_PWD_KEY}" maxlength="${FormValidator.PWD_MAX_LEN}"
                                                               value="">

                                                </div>
                                                <div class="error-messages error-password2">


                                                        <p class="no-equal">
                                                                <i class="fas fa-exclamation-triangle"></i>  <fmt:message key="validateUser.errors.PASSWORD2_NOT_SAME"/>
                                                        </p>

                                                </div>
                                        </div>

                                </div>

                                <div class="form-group current-avatar">
                                        <label><i class="far fa-image"></i> <fmt:message key="the current avatar"/></label>
                                        <div class="w-25">
                                                <img class="img-fluid " src="<c:url value="/image/user/${sessionScope.user.img}"/>" alt="<fmt:message key="avatar"/>"/>
                                        </div>

                                </div>


                                <div class="avatar-group form-group">
                                        <div class=""><fmt:message key="select avatar"/>:</div>
                                        <div class="avatars">
                                                <c:forEach items="${FormValidator.DEFAULT_AVATARS}" var="av" varStatus="status">

                                                        <div class="avatar-box custom-control custom-radio custom-control-inline">
                                                                <input type="radio" class="custom-control-input  img-radio default-avatar" id="avatar-${status.index}" name="${FormValidator.AVATAR_KEY}" value="${av}" />
                                                                <label class="custom-control-label" for="avatar-${status.index}">
                                                                        <img class="img-input img-fluid" src="<c:url value="/image/user/${av}"/>" />
                                                                        <span class="img-check">
                                                                                <i class="far fa-check-circle "></i>
                                                                        </span>
                                                                </label>

                                                        </div>

                                                </c:forEach>

                                                <div class="avatar-box custom-control custom-radio custom-control-inline">
                                                        <input type="radio" class="custom-control-input  img-radio" id="avatar-custom" name="${FormValidator.AVATAR_KEY}" value="custom"  />
                                                        <label class="custom-control-label" for="avatar-custom">
                                                                <img class="img-input img-fluid" src="<c:url value="/image/base/custom-avatar.svg"/>" />
                                                                <span class="img-check">
                                                                        <i class="far fa-check-circle "></i>
                                                                </span>
                                                        </label>

                                                </div>
                                        </div>
                                        <div class="error-messages error-avatar">


                                                <p class="invalid">
                                                        <i class="fas fa-exclamation-triangle"></i> <fmt:message key="validateUser.errors.AVATAR_NOT_VALID"/>
                                                </p>
                                        </div>


                                </div>

                                <%--parte di immagine --%>
                                <div class="form-group custom-avatar-uploader">

                                        <div class=" custom-file input-group mb-3">

                                                <input type="file" class="custom-file-input"  id="customAvatarImg" name="${FormValidator.AVATAR_IMG_KEY}" accept="image/jpeg, image/png, image/gif, image/bmp">
                                                <label class="custom-file-label input-box" for="customAvatarImg"><fmt:message key="select file"/></label>
                                                <%-- serve per ripristinare il segnaposto --%>
                                                <label class="custom-file-label-origin d-none"><fmt:message key="select file"/></label>
                                        </div>

                                        <%--parte di suggerimenti --%>
                                        <div class="form-group">
                                                <label><fmt:message key="only accept files" /> *.jpg, *.png, *.gif, *.bmp</label>
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




                                <div class="form-group">

                                        <button type="submit" class="btn btn-info w-50"><fmt:message key="update" /></button>

                                </div>

                        </form>





                </div>

        </div>
</div>

<script type="text/javascript" src="<c:url value="/js/userSystemScript.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/userFormValidate.js"/>"></script>
<script src="<c:url value="/libs/zxcvbn/zxcvbn.js"/>"></script>


<%-- pié di pagina--%>
<jsp:include page="/WEB-INF/jsp/front/footer.jsp"/>
