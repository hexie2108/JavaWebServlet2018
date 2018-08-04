<%-- 
la lista di prodotto
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="custom" uri="/WEB-INF/custom.tld"%>


<div class="list-body">

        <%-- stampa la lista di prodotto --%>
        <c:forEach var="product" items="${productList}">

                <div id="item-${product.id}" class="list-item card d-inline-flex m-2 p-2">

                        <%-- img di  prodotto --%>
                        <img class="list-item-img img-fluid card-img-top" src="${pageContext.request.contextPath}/${product.img}" alt="${product.name}"/>

                        <%-- link della categoria di prodotto --%>
                        <div class="list-item-cat ${not empty categoria ?"hiddin-cat":""}">
                                <a class="list-item-cat-link" href="${pageContext.request.contextPath}/category?catId=${product.categoryProductId}">

                                        <%-- get il nome della categoria di prodotto--%>
                                        <custom:getCategoryNameById categoryId="${product.categoryProductId}"/>
                                        ${categoryName}

                                </a>
                        </div>

                        <%-- nome di  prodotto --%>
                        <div class="list-item-title card-title mb-2 mt-2">
                                ${product.name}
                        </div>

                        <%-- descrizione di  prodotto --%>
                        <div class="list-item-description card-text mb-2">
                                ${product.description}
                        </div>

                        <%-- logo di  prodotto --%>
                        <div class="list-item-info">       
                                <div class="list-item-logo  d-inline-block w-50">
                                        <img class="list-item-logo-img img-fluid" src="${pageContext.request.contextPath}/${product.logo}" alt="logo"/>
                                </div>
                        </div>

                        <%-- bottone di inserimento del prodotto --%>
                        <div class="list-item-add">
                                <button class="list-item-add-button btn btn-info" data-toggle="modal" data-target="#boxAddItem" onclick="setProductIdForAddInList(${product.id})">
                                        <i class="fas fa-cart-plus"></i> Aggiunge
                                </button>
                        </div>

                </div>

        </c:forEach>

</div>

<jsp:include page="/WEB-INF/jsp/module/pagination.jsp" />
