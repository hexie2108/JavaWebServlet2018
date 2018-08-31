<%-- 
sidebar di home
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="custom" uri="/WEB-INF/custom.tld" %>
<%@include file="/WEB-INF/jspf/i18n.jsp"%>


<aside class="sidebar col-3">

        <div class="tag-category">

                <h2 class="text-center category-name">
                        <i class="fas fa-store"></i> <fmt:message key="category"/>
                </h2>

                <%-- la lista di tutti categoira --%>
                <div class="list-group">

                        <%-- get tutte le categorie di prodotto --%>
                        <custom:getAllCategoryOfProduct/>

                        <c:forEach var="category" items="${categoryProductList}">

                                <a class="list-group-item list-group-item-action text-center"
                                   href="<c:url value="/category?catId=${category.id}"/>">
                                        ${category.name}
                                </a>

                        </c:forEach>

                </div>

        </div>


</aside>