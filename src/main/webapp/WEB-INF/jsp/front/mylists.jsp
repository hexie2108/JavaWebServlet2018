<%-- 
    pagina per visualizzare tutte le liste possiedute da un utente
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="custom" uri="/WEB-INF/custom.tld" %>
<%@include file="/WEB-INF/jspf/i18n.jsp"%>

<jsp:include page="/WEB-INF/jsp/front/header.jsp"/>


<div class="mylists-main-section col-12">
        <div class="content">

                <%-- breadcrumb--%>
                <div class="breadcrumbs">
                        <a href="<c:url value="/"/>">
                                <i class="fas fa-home"></i>
                        </a>
                        <span>&gt;</span>
                        <span>
                                <i class="fas fa-list"></i> <fmt:message key="my lists" />
                        </span>

                </div>


                <%-- link per creare una nuova lista--%>
                <div class="add-list-box">
                        <a class="btn btn-info" href="<c:url value="/updateList"/>">
                                <i class="fas fa-plus"></i>  <fmt:message key="createTheNewList" />
                        </a>
                </div>

                <%-- se possiede almeno una lista owner --%>
                <c:if test="${not empty ownedLists}">

                        <div class="owner-lists my-lists">

                                <%-- titolo--%>
                                <h2>
                                        <i class="fas fa-list"></i> <fmt:message key="owner lists" />
                                </h2>

                                <%-- stampa tutta la lista--%>
                                <c:forEach var="list" items="${ownedLists}">


                                        <div class="list-item">


                                                <div class="list-head">

                                                        <%-- tutte le informazioni della lista--%>
                                                        <div class="list-left-head">



                                                                <div class="list-info">
                                                                        <div class="list-logo">
                                                                                <img class="img-fluid"
                                                                                     src="<c:url value="/image/list/${list.img}"/>" alt="<fmt:message key="logo" />"/>
                                                                        </div>


                                                                        <div class="list-name">

                                                                                <p class="name">
                                                                                        <i class="fas fa-info-circle"></i> <b><fmt:message key="name" />:</b> 
                                                                                </p>
                                                                                <p class="value">
                                                                                        ${list.name}
                                                                                </p>
                                                                        </div>
                                                                        <div class="list-category">
                                                                                <p class="name">
                                                                                        <i class="fas fa-sitemap"> <b></i> <fmt:message key="category" />: </b>
                                                                                </p>
                                                                                <p class="value">
                                                                                        <%-- get il nome della categoria di lista--%>
                                                                                        <custom:getListCategoryByListCategoryId
                                                                                                listCategoryId="${list.categoryList}"/>
                                                                                        ${categoryOfList.name}
                                                                                </p>
                                                                        </div>

                                                                        <div class="list-description">
                                                                                <p class="name">
                                                                                        <i class="far fa-file-alt"></i> <b><fmt:message key="description" /> </b>
                                                                                </p>
                                                                                <p class="value">
                                                                                        ${list.description}
                                                                                </p>

                                                                        </div>

                                                                </div>

                                                        </div>

                                                        <%-- il numero di condivisione e il numero di commento--%>
                                                        <div class="list-right-head">

                                                                <span class="button">
                                                                        <i class="fas fa-share-alt"></i> ${ownedListsMap.get(list).get("numberOfShared")} <b><fmt:message key="sharing" /></b>
                                                                </span>
                                                                <span class="button">
                                                                        <i class="fas fa-comments"></i> ${ownedListsMap.get(list).get("numberComment")} <b><fmt:message key="comments" /></b>
                                                                </span>

                                                        </div>

                                                </div>


                                                <div class="list-body">

                                                        <%-- la lista di prodotto ancora da comprare--%>
                                                        <c:forEach var="product" items="${ownedListsMap.get(list).get('productsListNotBuy')}">

                                                                <div id="productIdInList-${product.id}" class="list-product">

                                                                        <%-- link per visualizzare il prodotto in finestra--%>
                                                                        <a href="javascript:;" data-toggle="modal" data-target="#boxShowItem"
                                                                           onclick="showProductWindowsFromList(${product.id}, true, false, true)">
                                                                                <img class="img img-fluid"
                                                                                     src="<c:url value="/image/product/${product.img}"/>"
                                                                                     alt="${product.name}"/>
                                                                        </a>

                                                                        <%-- campi necessari per visuallizare il prodotto nella finestrina--%>
                                                                        <input class="name" type="hidden" value="${product.name}"/>
                                                                        <input class="logo-img" type="hidden"
                                                                               value="<c:url value="/${product.logo}"/>"/>
                                                                        <input class="cat-link" type="hidden"
                                                                               value="<c:url value="/category?catId=${product.categoryProductId}"/>"/>
                                                                        <%-- get il nome della categoria--%>
                                                                        <custom:getCategoryNameById categoryId="${product.categoryProductId}"/>
                                                                        <input class="cat-name" type="hidden" value="${categoryName}"/>

                                                                        <input class="description" type="hidden" value="${product.description}"/>
                                                                        <input class="list-id" type="hidden" value="${list.id}"/>

                                                                </div>

                                                        </c:forEach>

                                                        <%-- la lista di prodotto già comprato --%>
                                                        <c:forEach var="product" items="${ownedListsMap.get(list).get('productsListBought')}">

                                                                <div id="productIdInList-${product.id}" class="list-product bought-item">

                                                                        <%-- link per visualizzare il prodotto in finestra--%>
                                                                        <a href="javascript:;" data-toggle="modal" data-target="#boxShowItem"
                                                                           onclick="showProductWindowsFromList(${product.id}, true, true, true)">
                                                                                <img class="img img-fluid"
                                                                                     src="<c:url value="/image/product/${product.img}"/>"
                                                                                     alt="${product.name}"/>
                                                                        </a>

                                                                        <%-- campi necessari per visuallizare il prodotto nella finestrina--%>
                                                                        <input class="name" type="hidden" value="${product.name}"/>
                                                                        <input class="logo-img" type="hidden"
                                                                               value="<c:url value="/${product.logo}"/>"/>
                                                                        <input class="cat-link" type="hidden"
                                                                               value="<c:url value="/category?catId=${product.categoryProductId}"/>"/>
                                                                        <%-- get il nome della categoria--%>
                                                                        <custom:getCategoryNameById categoryId="${product.categoryProductId}"/>
                                                                        <input class="cat-name" type="hidden" value="${categoryName}"/>

                                                                        <input class="description" type="hidden" value="${product.description}"/>
                                                                        <input class="list-id" type="hidden" value="${list.id}"/>

                                                                </div>

                                                        </c:forEach>

                                                        <%-- se la lista non contiene nessuno prodotto--%>
                                                        <c:if test="${ownedListsMap.get(list).get('productsListNotBuy').size() ==0 && ownedListsMap.get(list).get('productsListBought').size()==0 }">
                                                                <div class="item-empty">
                                                                        <h2><fmt:message key="it is empty" /></h2>
                                                                </div>

                                                        </c:if>

                                                </div>

                                                <%-- link per visualizzare la lista in dettaglio--%>
                                                <div class="list-detail-link">
                                                        <a class="btn btn-info" href="<c:url value="/mylist?listId=${list.id}"/>">
                                                                <i class="fas fa-search-plus"></i> <fmt:message key="in detail" />
                                                        </a>
                                                </div>

                                        </div>

                                </c:forEach>

                        </div>

                </c:if>

                <%-- se possiede almeno una lista condivisa dagli altri --%>
                <c:if test="${not empty sharedLists}">

                        <div class="shared-lists my-lists">

                                <%-- titolo--%>
                                <h2>
                                        <i class="fas fa-list"></i> <fmt:message key="the lists shared by the others" />
                                </h2>

                                <%-- stampa tutta la lista--%>
                                <c:forEach var="list" items="${sharedLists}">

                                        <div class="list-item">

                                                <div class="list-head">

                                                        <%-- tutte le informazioni della lista--%>
                                                        <div class="list-left-head">



                                                                <div class="list-info">

                                                                        <div class="list-logo">
                                                                                <img class="img-fluid"
                                                                                     src="<c:url value="/image/list/${list.img}"/>" alt="<fmt:message key="logo" />"/>
                                                                        </div>

                                                                        <div class="list-name">

                                                                                <p class="name">
                                                                                        <i class="fas fa-info-circle"></i> <b><fmt:message key="name" />:</b> 
                                                                                </p>
                                                                                <p class="value">
                                                                                        ${list.name}
                                                                                </p>
                                                                        </div>
                                                                        <div class="list-category">
                                                                                <p class="name">
                                                                                        <i class="fas fa-sitemap"> <b></i> <fmt:message key="category" />: </b>
                                                                                </p>
                                                                                <p class="value">
                                                                                        <%-- get il nome della categoria di lista--%>
                                                                                        <custom:getListCategoryByListCategoryId
                                                                                                listCategoryId="${list.categoryList}"/>
                                                                                        ${categoryOfList.name}
                                                                                </p>
                                                                        </div>

                                                                        <div class="list-description">
                                                                                <p class="name">
                                                                                        <i class="far fa-file-alt"></i> <b><fmt:message key="description" />: </b>
                                                                                </p>
                                                                                <p class="value">
                                                                                        ${list.description}
                                                                                </p>

                                                                        </div>

                                                                </div>



                                                        </div>

                                                        <%-- stampa il permesso su questa lista e  il numero di commento--%>
                                                        <div class="list-right-head">

                                                                <div class="permission">

                                                                        <span class="modify-list"
                                                                              title="<fmt:message key="editTheList" />">
                                                                                <i class="fas fa-edit"></i> ${sharedListsMap.get(list).get("permission").modifyList?"<i class=\"fas fa-check\"></i>":"<i class=\"fas fa-times\"></i>"}
                                                                        </span>
                                                                        <span class="delete-list" title="<fmt:message key="deleteTheList" />">
                                                                                <i class="fas fa-trash-alt"></i> ${sharedListsMap.get(list).get("permission").deleteList?"<i class=\"fas fa-check\"></i>":"<i class=\"fas fa-times\"></i>"}
                                                                        </span>
                                                                        <span class="add-item" title="<fmt:message key="addTheProduct" />">
                                                                                <i class="fas fa-cart-plus"></i> ${sharedListsMap.get(list).get("permission").addObject?"<i class=\"fas fa-check\"></i>":"<i class=\"fas fa-times\"></i>"}
                                                                        </span>
                                                                        <span class="delete-item" title="<fmt:message key="deleteTheProduct" />">
                                                                                <i class="fas fa-ban"></i> ${sharedListsMap.get(list).get("permission").deleteObject?"<i class=\"fas fa-check\"></i>":"<i class=\"fas fa-times\"></i>"}
                                                                        </span>

                                                                </div>

                                                                <span class="button">
                                                                        <i class="fas fa-comments"></i> ${sharedListsMap.get(list).get("numberComment")} <b><fmt:message key="comments" /></b>
                                                                </span>

                                                        </div>

                                                </div>


                                                <div class="list-body">

                                                        <%-- la lista di prodotto ancora da comprare--%>
                                                        <c:forEach var="product" items="${sharedListsMap.get(list).get('productsListNotBuy')}">

                                                                <div id="productIdInList-${product.id}" class="list-product">

                                                                        <%-- link per visualizzare il prodotto in finestra--%>
                                                                        <a href="javascript:;" data-toggle="modal" data-target="#boxShowItem"
                                                                           onclick="showProductWindowsFromList(${product.id}, true, false, true)">
                                                                                <img class="img img-fluid"
                                                                                     src="<c:url value="/image/product/${product.img}"/>"
                                                                                     alt="${product.name}"/>
                                                                        </a>


                                                                        <%-- campi necessari per visuallizare il prodotto nella finestrina--%>
                                                                        <input class="name" type="hidden" value="${product.name}"/>
                                                                        <input class="logo-img" type="hidden"
                                                                               value="<c:url value="/${product.logo}"/>"/>
                                                                        <input class="cat-link" type="hidden"
                                                                               value="<c:url value="/category?catId=${product.categoryProductId}"/>"/>

                                                                        <%-- get il nome della categoria--%>
                                                                        <custom:getCategoryNameById categoryId="${product.categoryProductId}"/>
                                                                        <input class="cat-name" type="hidden" value="${categoryName}"/>

                                                                        <input class="description" type="hidden" value="${product.description}"/>
                                                                        <input class="list-id" type="hidden" value="${list.id}"/>

                                                                </div>

                                                        </c:forEach>

                                                        <%-- la lista di prodotto già comprato --%>
                                                        <c:forEach var="product" items="${sharedListsMap.get(list).get('productsListBought')}">

                                                                <div id="productIdInList-${product.id}" class="list-product bought-item">

                                                                        <%-- link per visualizzare il prodotto in finestra--%>
                                                                        <a href="javascript:;" data-toggle="modal" data-target="#boxShowItem"
                                                                           onclick="showProductWindowsFromList(${product.id}, true, true, true)">
                                                                                <img class="img img-fluid"
                                                                                     src="<c:url value="/image/product/${product.img}"/>"
                                                                                     alt="${product.name}"/>
                                                                        </a>

                                                                        <%-- campi necessari per visuallizare il prodotto nella finestrina--%>
                                                                        <input class="name" type="hidden" value="${product.name}"/>
                                                                        <input class="logo-img" type="hidden"
                                                                               value="<c:url value="/${product.logo}"/>"/>
                                                                        <input class="cat-link" type="hidden"
                                                                               value="<c:url value="/category?catId=${product.categoryProductId}"/>"/>

                                                                        <%-- get il nome della categoria--%>
                                                                        <custom:getCategoryNameById categoryId="${product.categoryProductId}"/>
                                                                        <input class="cat-name" type="hidden" value="${categoryName}"/>

                                                                        <input class="description" type="hidden" value="${product.description}"/>
                                                                        <input class="list-id" type="hidden" value="${list.id}"/>


                                                                </div>

                                                        </c:forEach>

                                                        <%-- se la lista non contiene nessuno prodotto--%>
                                                        <c:if test="${sharedListsMap.get(list).get('productsListNotBuy').size() ==0 && sharedListsMap.get(list).get('productsListBought').size()==0 }">
                                                                <div class="item-empty">
                                                                        <h2><fmt:message key="it is empty" /></h2>
                                                                </div>

                                                        </c:if>

                                                </div>

                                                <%-- link per visualizzare la lista in dettaglio--%>
                                                <div class="list-detail-link">
                                                        <a class="btn btn-info" href="<c:url value="/mylist?listId=${list.id}"/>">
                                                                <i class="fas fa-search-plus"></i> <fmt:message key="in detail" />
                                                        </a>
                                                </div>
                                        </div>

                                </c:forEach>

                        </div>

                </c:if>

                <%-- se non hai neanche una lista--%>
                <c:if test="${empty ownedLists && empty sharedLists}">

                        <div class="shared-lists my-lists">
                                <h2>
                                       <fmt:message key="oops! you do not even have a list" />
                                </h2>
                        </div>

                </c:if>

        </div>

</div>

<jsp:include page="/WEB-INF/jsp/front/footer.jsp"/>
