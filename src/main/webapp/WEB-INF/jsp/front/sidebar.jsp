<%-- 
sidebar di home
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="custom" uri="/WEB-INF/custom.tld" %>


<aside class="sidebar col-3">

        <div class="tag-category">

                <h2 class="text-center">
                        <i class="fas fa-store"></i> CATEGORIA
                </h2>

                <%-- la lista di tutti categoira --%>
                <div class="list-group">

                        <%-- get tutte le categorie di prodotto --%>
                        <custom:getAllCategoryOfProduct />

                        <c:forEach var="category" items="${categoryProductList}">

                                <a class="list-group-item list-group-item-action text-center" href ="${pageContext.request.contextPath}/category?catId=${category.id}" >
                                        ${category.name}
                                </a>

                        </c:forEach>

                </div>

        </div>


</aside>