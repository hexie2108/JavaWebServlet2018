<%--
  Created by IntelliJ IDEA.
  User: Matteo
  Date: 18/08/2018
  Time: 18:38
  To change this template use File | Settings | File Templates.
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="custom" uri="/WEB-INF/custom.tld" %>

<jsp:include page="/WEB-INF/jsp/front/header.jsp"/>

<div class="map main-section w-100 p-4">

        <div class="content">

                <h2 class="mb-2 mt-2">
                      <i class="fas fa-map-marked-alt"></i>  Trova i negozi vicini a te
                </h2>
                <p id="errText">

                </p>

                <div id="mapHolder" ></div>


                <%-- se utente è anonimo--%>
                <c:if test="${empty sessionScope.user}">
                        <custom:getListCategoryByListCategoryId listCategoryId="${cookie.localShoppingListCategory.value}"/>
                        <input id="categoryNamesForMap" type="hidden" value="${categoryOfList.name}">
                </c:if>

                <%-- se utente è loggato --%>
                <c:if test="${not empty sessionScope.user}">        
                        <input id="categoryNamesForMap" type="hidden" value="<c:forEach var="list" items="${allMyList}" varStatus="status">${status.index > 0 ? ",":""}<custom:getListCategoryByListCategoryId listCategoryId="${list.categoryList}"/>${categoryOfList.name}</c:forEach>" />
                </c:if>


                <script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAzzp8e9Yhu146D_iRX_C6tQxYi3TdoeFE&callback=initMap&libraries=places" type="text/javascript"></script>


        </div>
</div>

<%-- pié di pagina --%>
<jsp:include page="/WEB-INF/jsp/front/footer.jsp"/>
