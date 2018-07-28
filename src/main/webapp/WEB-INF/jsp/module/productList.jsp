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
                <div id="item-${product.id}" class="list-item card d-inline-flex m-2 p-2">
                        <img class="list-item-img img-fluid card-img-top" src="${pageContext.request.contextPath}/${product.img}" alt="${product.name}"/>

                        <div class="list-item-cat ${not empty categoria ?"hiddin-cat":""}">

                                <a class="list-item-cat-link" href="${pageContext.request.contextPath}/category?catid=${product.categoryProductId}">
                                        <custom:getCategoryNameById categoryId="${product.categoryProductId}">
                                        </custom:getCategoryNameById>
                                </a>

                        </div>

                        <div class="list-item-title card-title mb-2 mt-2">
                                ${product.name}
                        </div>

                        <div class="list-item-description card-text mb-2">
                                ${product.description}

                        </div>
                        <div class="list-item-info">       
                                <div class="list-item-logo  d-inline-block w-50">
                                        <img class="list-item-logo-img img-fluid" src="${pageContext.request.contextPath}/${product.logo}" alt="logo"/>

                                </div>

                        </div>

                        <div class="list-item-add">
                                <button class="list-item-add-button btn btn-info" data-toggle="modal" data-target="#boxAddItem" onclick="setProductIdForAddInList(${product.id})"><i class="fas fa-cart-plus"></i> Aggiunge</button>
                        </div>
                        
                </div>
        </c:forEach>
</div>
