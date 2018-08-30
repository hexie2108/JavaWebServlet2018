<%-- 
    pagina per visualizzare una lista specificata
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="custom" uri="/WEB-INF/custom.tld" %>
<%@ include file="/WEB-INF/jspf/i18n.jsp"%>

<jsp:include page="/WEB-INF/jsp/front/header.jsp"/>


<div class="mylist-main-section col-12">
        <div class="content">

                <%-- breadcrumb--%>
                <div class="breadcrumbs">
                        <a href="<c:url value="/"/>">
                                <i class="fas fa-home"></i>
                        </a>
                        <span>&gt;</span>
                        <a href="<c:url value="/mylists"/>">
                                <i class="fas fa-list"></i> <fmt:message key="my lists" />
                        </a>
                        <span>&gt;</span>
                        <span>
                                ${list.name}
                        </span>
                </div>

                <div class="mylist">
                        <div class="list-left-body">

                                <%-- tutte le informazione sulla lista--%>
                                <div class="list-head">

                                        <%-- img di lista--%>
                                        <div class="list-logo">
                                                <img class="img-fluid" src="<c:url value="/image/list/${list.img}"/>"
                                                     alt="logo"/>
                                        </div>
                                        <div class="list-info mt-2">

                                                <%-- nome di lista--%>
                                                <span class="list-name">
                                                        <i class="fas fa-info-circle"></i> <b><fmt:message key="name" />:</b> 
                                                </span>
                                                <p>${list.name}</p>

                                                <%-- link di categoria di lista--%>
                                                <span class="list-category">
                                                        <i class="fas fa-sitemap"></i> <b><fmt:message key="category" />: </b>
                                                </span>

                                                <%-- get il nome della categoria--%>
                                                <custom:getListCategoryByListCategoryId listCategoryId="${list.categoryList}"/>
                                                <p>
                                                        ${categoryOfList.name}
                                                </p>
                                                <div class="list-category-img">
                                                        <img class="img-fluid" src="<c:url value="/image/categoryList/${categoryOfList.img1}"/>" alt=" ${categoryOfList.name}"/>
                                                        <c:if test="${not empty categoryOfList.img2}">
                                                                <img class="img-fluid" src="<c:url value="/image/categoryList/${categoryOfList.img2}"/>" alt=" ${categoryOfList.name}"/>
                                                        </c:if>
                                                        <c:if test="${not empty categoryOfList.img3}">
                                                                <img class="img-fluid" src="<c:url value="/image/categoryList/${categoryOfList.img3}"/>" alt=" ${categoryOfList.name}"/>
                                                        </c:if>
                                                </div>


                                        </div>

                                        <%-- descrizione della lista--%>
                                        <div class="list-description mt-3">
                                                <i class="far fa-file-alt"></i> <b><fmt:message key="description" />: </b>
                                                <p>${list.description}</p>
                                        </div>

                                        <%-- link per update e eliminare la lista--%>
                                        <div class="list-modify">

                                                <c:if test="${userPermissionsOnList.modifyList}">
                                                        <a class="modify btn btn-info"
                                                           href="<c:url value="/updateList?listId=${list.id}"/>"><i
                                                                        class="fas fa-edit"></i> <fmt:message key="modify" /></a>
                                                        </c:if>

                                                <c:if test="${userPermissionsOnList.deleteList}">
                                                        <a class="delete btn btn-danger"
                                                           href="<c:url value="/service/updateListService?action=delete&listId=${list.id}"/>" onclick="if(!confirm('<fmt:message key="are you sure?" />')) return false;">
                                                                <i  class="fas fa-trash-alt"></i> <fmt:message key="delete" /></a>
                                                        </c:if>

                                        </div>

                                </div>

                                <%-- stampa i permessi su questa lista--%>
                                <div class="list-permission">
                                        <p><i class="fas fa-lock"></i> <b><fmt:message key="permission" />:</b></p>
                                        <div class="single-permission">
                                                <span class="modify-list" title="<fmt:message key="editTheList" />">
                                                        <i class="fas fa-edit"></i> ${userPermissionsOnList.modifyList?"<i class=\"fas fa-check\"></i>":"<i class=\"fas fa-times\"></i>"}
                                                </span>
                                                <span class="delete-list" title="<fmt:message key="deleteTheList" />">
                                                        <i class="fas fa-trash-alt"></i> ${userPermissionsOnList.deleteList?"<i class=\"fas fa-check\"></i>":"<i class=\"fas fa-times\"></i>"}
                                                </span>
                                                <span class="add-item" title="<fmt:message key="addTheProduct" />">
                                                        <i class="fas fa-cart-plus"></i> ${userPermissionsOnList.addObject?"<i class=\"fas fa-check\"></i>":"<i class=\"fas fa-times\"></i>"}
                                                </span>
                                                <span class="delete-item" title="<fmt:message key="deleteTheProduct" />">
                                                        <i class="fas fa-ban"></i> ${userPermissionsOnList.deleteObject?"<i class=\"fas fa-check\"></i>":"<i class=\"fas fa-times\"></i>"}
                                                </span>
                                        </div>
                                </div>

                                <%-- buttone per visualizzare la finestra di sharing--%>
                                <div class="list-sharing">

                                        <%-- se sei proprietario della lista--%>
                                        <c:if test="${list.ownerId == sessionScope.user.id}">
                                                <button class="btn btn-info" data-toggle="modal" data-target="#boxSharing"><i
                                                                class="fas fa-share-alt"></i> <fmt:message key="sharing" />
                                                </button>
                                        </c:if>
                                </div>

                        </div>

                        <%-- lista di prodotto--%>
                        <div class="list-right-body">

                                <%-- intestazione della lista --%>
                                <div class="list-item list-head-info">
                                        <div class="item-img">
                                                <i class="far fa-image"></i>
                                        </div>
                                        <div class="item-name">
                                                <i class="fas fa-align-left"></i>
                                        </div>

                                        <div class="item-logo">
                                                <i class="far fa-bookmark"></i>
                                        </div>

                                        <div class="item-cat">
                                                <i class="fas fa-store"></i>
                                        </div>

                                        <div class="item-description">
                                                <i class="far fa-file-alt"></i>
                                        </div>
                                        <div class="item-manage">
                                                <i class="fas fa-wrench"></i>
                                        </div>
                                </div>

                                <%-- la lista del prodotto ancora da comprare--%>
                                <c:forEach var="product" items="${listProductsNotBuy}">

                                        <%-- tutte le informazioni del prodotto--%>
                                        <div class="list-item">

                                                <div class="item-img">
                                                        <img class="img-fluid" src="<c:url value="/image/product/${product.img}"/>"
                                                             alt="${product.name}"/>
                                                </div>

                                                <div class="item-name">
                                                        <span>${product.name}</span>
                                                </div>

                                                <div class="item-logo">
                                                        <img class="img-fluid"
                                                             src="<c:url value="/image/productLogo/${product.logo}"/>" alt="<fmt:message key="logo" />"/>
                                                </div>

                                                <div class="item-cat">
                                                        <a href="<c:url value="/category?catId=${product.categoryProductId}"/>">
                                                                <%-- get il nome della categoiria di prodotto--%>
                                                                <custom:getCategoryNameById categoryId="${product.categoryProductId}"/>
                                                                ${categoryName}
                                                        </a>
                                                </div>



                                                <div class="item-description">
                                                        <p>
                                                                ${product.description}
                                                        </p>
                                                </div>

                                                <div class="item-manage">

                                                        <%-- link per segnare il prodotto come già comprato--%>
                                                        <a class="btn btn-info"
                                                           href="<c:url value="/service/updateItemInListService?action=bought&productId=${product.id}&listId=${list.id}"/>"
                                                           title="<fmt:message key="bought" />">
                                                                <i class="fas fa-check-circle"></i> <b><fmt:message key="bought" /></b>
                                                        </a>

                                                        <%-- se utente ha il permesso di eliminare il prodotto dalla lista--%>
                                                        <c:if test="${userPermissionsOnList.deleteObject == true}">

                                                                <%-- link per eliminare il prodotto--%>
                                                                <a class="btn btn-danger"
                                                                   href="<c:url value="/service/updateItemInListService?action=delete&productId=${product.id}&listId=${list.id}"/>"
                                                                   title="<fmt:message key="delete" />" onclick="if(!confirm('<fmt:message key="are you sure?" />')) return false;">
                                                                        <i class="fas fa-ban"></i> <b><fmt:message key="delete" /></b>
                                                                </a>

                                                        </c:if>
                                                </div>

                                        </div>

                                </c:forEach>


                                <%-- la lista del prodotto già comprato --%>
                                <c:forEach var="product" items="${listProductsBought}">

                                        <%-- tutte le informazioni del prodotto--%>
                                        <div class="list-item item-bought">

                                                <div class="item-img">
                                                        <img class="img-fluid" src="<c:url value="/image/product/${product.img}"/>"
                                                             alt="${product.name}"/>
                                                </div>

                                                <div class="item-name">
                                                        <span>${product.name}</span>
                                                </div>

                                                <div class="item-logo">
                                                        <img class="img-fluid"
                                                             src="<c:url value="/image/productLogo/${product.logo}"/>" alt="<fmt:message key="logo" />"/>
                                                </div>

                                                <div class="item-cat">
                                                        <a href="<c:url value="/category?catId=${product.categoryProductId}"/>">
                                                                <%-- get il nome della categoiria di prodotto--%>
                                                                <custom:getCategoryNameById categoryId="${product.categoryProductId}"/>
                                                                ${categoryName}
                                                        </a>
                                                </div>



                                                <div class="item-description">
                                                        <p>
                                                                ${product.description}
                                                        </p>
                                                </div>

                                                <div class="item-manage">

                                                        <%-- se utente ha il permesso di eliminare il prodotto dalla lista--%>
                                                        <c:if test="${userPermissionsOnList.deleteObject == true}">

                                                                <%-- link per eliminare il prodotto--%>
                                                                <a class="btn btn-danger"
                                                                   href="<c:url value="/service/updateItemInListService?action=delete&productId=${product.id}&listId=${list.id}"/>"
                                                                   title="<fmt:message key="delete" />" onclick="if(!confirm('<fmt:message key="are you sure?" />')) return false;">
                                                                        <i class="fas fa-ban"></i> <b><fmt:message key="delete" /></b>
                                                                </a>

                                                        </c:if>

                                                </div>

                                        </div>

                                </c:forEach>


                                <%-- se la lista non contiene nessuno prodotto--%>
                                <c:if test="${empty listProductsNotBuy && empty listProductsBought}">

                                        <div class="list-item item-empty">
                                                <h2><fmt:message key="it is empty" /></h2>
                                        </div>

                                </c:if>

                                <%-- componente del commento--%>
                                <jsp:include page="/WEB-INF/jsp/front/module/comment.jsp"/>

                        </div>

                </div>

        </div>
</div>

<%-- finestra di sharing--%>
<jsp:include page="/WEB-INF/jsp/front/module/floatBoxForSharing.jsp"/>
<%-- pié di pagina--%>
<jsp:include page="/WEB-INF/jsp/front/footer.jsp"/>
