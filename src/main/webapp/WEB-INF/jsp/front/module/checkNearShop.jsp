<%-- 
    
    parte che check se è in vicinanza di un negozio
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="custom" uri="/WEB-INF/custom.tld" %>

<c:if test="${empty cookie.NearShopChecked.value}">

        <div class="checkNearShop-box">
                <%-- box vuoto per map--%>
                <div id="mapHolder" class="d-none">
                </div>

                <%-- se utente è anonimo--%>
                <c:if test="${empty sessionScope.user}">
                        <custom:getListCategoryByListCategoryId listCategoryId="${cookie.localShoppingListCategory.value}"/>
                        <input id="categoryNamesForMap" type="hidden" value="${categoryOfList.name}">
                </c:if>

                <%-- se utente è loggato --%>
                <c:if test="${not empty sessionScope.user}">        
                        <input id="categoryNamesForMap" type="hidden" value="<c:forEach var="list" items="${allMyList}" varStatus="status">${status.index > 0 ? ",":""}<custom:getListCategoryByListCategoryId listCategoryId="${list.categoryList}"/>${categoryOfList.name}</c:forEach>" />
                </c:if>

        </div>        


        <script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAzzp8e9Yhu146D_iRX_C6tQxYi3TdoeFE&callback=checkNearShop&libraries=places"  type="text/javascript"></script>


</c:if>