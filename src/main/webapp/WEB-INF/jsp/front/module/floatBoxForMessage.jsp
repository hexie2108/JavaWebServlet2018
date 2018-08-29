<%-- 
    
    Created on : 2018-7-15, 7:55:07
    Author     : liu
    finestra per avvisare il risultato delle operazione fatte
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/jspf/i18n.jsp"%>

<%-- se ci sono message nel attributo "result"--%>
<c:if test="${not empty sessionScope.result}">

        <div class="modal fade" id="boxShowMessage">
                <div class="modal-dialog">
                        <div class="modal-content">

                                <%-- box-head --%>
                                <div class="modal-header">

                                        <h4 class="modal-title">

                                                <c:choose>

                                                        <c:when test="${sessionScope.result == 'InsertOk'}">
                                                                <i class="fas fa-check-circle"></i>  <fmt:message key="congratulations" />
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'InsertFail'}">
                                                                <span class="error"><i class="fas fa-exclamation-triangle"></i> <fmt:message key="error" /></span>
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'DeleteOk'}">
                                                                <i class="fas fa-check-circle"></i>  <fmt:message key="congratulations" />
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'DeleteFail'}">
                                                                <span class="error"><i class="fas fa-exclamation-triangle"></i> <fmt:message key="error" /></span>
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'ChangeListCategoryOk'}">
                                                                <i class="fas fa-check-circle"></i> <fmt:message key="congratulations" />
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'BoughtOk'}">
                                                                <i class="fas fa-check-circle"></i> <fmt:message key="congratulations" />
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'commentInsertOk'}">
                                                                <i class="fas fa-check-circle"></i> <fmt:message key="congratulations" />
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'commentDeleteOk'}">
                                                                <i class="fas fa-check-circle"></i>  <fmt:message key="congratulations" />
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'sharingInsertOk'}">
                                                                <i class="fas fa-check-circle"></i>  <fmt:message key="congratulations" />
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'sharingUpdateOk'}">
                                                                <i class="fas fa-check-circle"></i>  <fmt:message key="congratulations" />
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'sharingDeleteOk'}">
                                                                <i class="fas fa-check-circle"></i> <fmt:message key="congratulations" />
                                                        </c:when>


                                                        <c:when test="${sessionScope.result == 'ListInsertOk'}">
                                                                <i class="fas fa-check-circle"></i>  <fmt:message key="congratulations" />
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'ListUpdateOk'}">
                                                                <i class="fas fa-check-circle"></i>  <fmt:message key="congratulations" />
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'ListDeleteOk'}">
                                                                <i class="fas fa-check-circle"></i> <fmt:message key="congratulations" />
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'privacy'}">
                                                                <i class="fas fa-info-circle"></i>   <fmt:message key="processingOfPersonalData" />
                                                        </c:when>


                                                </c:choose>

                                        </h4>

                                        <button type="button" class="close" data-dismiss="modal">&times;</button>

                                </div>

                                <%-- box-body --%>
                                <div class="modal-body">
                                        <p>

                                                <c:choose>
                                                        <c:when test="${sessionScope.result  == 'InsertOk'}">
                                                                <fmt:message key="InsertProductOk" />
                                                        </c:when>
                                                        <c:when test="${sessionScope.result  == 'InsertFail'}">
                                                                <span class="error">
                                                                        <fmt:message key="InsertProductFail" />
                                                                </span>
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'DeleteOk'}">
                                                                <fmt:message key="deleteProductOk" />
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'DeleteFail'}">
                                                                <span class="error">
                                                                        <fmt:message key="deleteProductFail" />
                                                                </span>
                                                        </c:when>
                                                        <c:when test="${sessionScope.result  == 'ChangeListCategoryOk'}">
                                                                <fmt:message key="changeListCategoryOk" />
                                                        </c:when>
                                                        <c:when test="${sessionScope.result  == 'BoughtOk'}">
                                                                <fmt:message key="boughtOk" />
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'commentInsertOk'}">
                                                                <fmt:message key="commentInsertOk" />
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'commentDeleteOk'}">
                                                                <fmt:message key="commentDeleteOk" />
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'sharingInsertOk'}">
                                                                <fmt:message key="sharingInsertOk" />
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'sharingUpdateOk'}">
                                                                <fmt:message key="sharingUpdateOk" />
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'sharingDeleteOk'}">
                                                                <fmt:message key="sharingDeleteOk" />
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'ListInsertOk'}">
                                                                <fmt:message key="listInsertOk" />
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'ListUpdateOk'}">
                                                                 <fmt:message key="listUpdateOk" />
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'ListDeleteOk'}">
                                                                 <fmt:message key="listDeleteOk" />
                                                        </c:when>
                                                        <c:when test="${sessionScope.result == 'privacy'}">
                                                                <fmt:message key="processingOfPersonalDataContent" />
                                                        </c:when>
                                                </c:choose>

                                        </p>
                                </div>



                                <%-- box-footer  --%>
                                <div class="modal-footer">
                                        <c:if test="${sessionScope.result == 'privacy'}">
                                                <a class="btn btn-info" href="<c:url value="/service/acceptPrivacyService"/>">
                                                        <fmt:message key="accept" />
                                                </a>
                                        </c:if>
                                        <button type="button" class="btn btn-secondary" data-dismiss="modal">
                                                <fmt:message key="close" />
                                        </button>
                                </div>

                        </div>
                </div>
        </div>

        <%-- cancella l'attributo "result" --%>
        <c:remove var="result" scope="session"/>

</c:if>


<%-- popup di privacy--%>
<c:if test="${empty cookie.acceptedPrivacy}">

        <div class="popup-privacy fixed-bottom">

                <div class="text">
                        <fmt:message key="cookiePrivacy" />
                </div>
                <div class="text-right mb-1">
                        <button class="btn btn-info" onclick="acceptedPrivacy();">
                                <fmt:message key="accept" />
                        </button>
                </div>

        </div>

</c:if>
