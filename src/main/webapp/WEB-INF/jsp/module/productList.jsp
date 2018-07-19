<%-- 
    Document   : header
    Created on : 2018-7-15, 7:55:07
    Author     : liu
    componente per visualizzare la lista di prodotto
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="custom" uri="/WEB-INF/custom.tld"%>


<div class="list-body">
        <c:forEach var="product" items="${productList}">
                <div class="list-item card d-inline-flex m-1 p-1">
                        <img class="list-item-img img-fluid card-img-top" src="${pageContext.request.contextPath}/${product.img}" alt="${product.name}"/>

                        <div class="list-item-cat">
                                <c:if test="${empty categoria}">
                                        <a href="${pageContext.request.contextPath}/category?catid=${product.categoryProductId}">
                                                <custom:getCategoryNameById categoryId="${product.categoryProductId}">
                                                </custom:getCategoryNameById>
                                        </a>
                                </c:if>
                        </div>
                        
                        <div class="list-item-title card-title">
                                ${product.name}
                        </div>
                        <div class="list-item_info text-center row">
                                <div class="list-item-log card-text d-inline-block col">
                                        ${product.logo}
                                </div>
                                <div class="list-item-add  d-inline-block col">
                                        <button class="btn btn-info" data-toggle="modal" data-target="#boxAddItem" onclick="setProductIdForAddInList(${product.id})">Aggiunge</button>
                                </div>
                        </div>
                        <div class="list-item-description card-text">
                                ${product.description}

                        </div>

                </div>
        </c:forEach>
</div>
