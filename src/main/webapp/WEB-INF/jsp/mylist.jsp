<%-- 
    
    Created on : 2018-7-15, 7:55:07
    Author     : liu
    pagina per visualizzare una lista specificata
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="custom" uri="/WEB-INF/custom.tld"%>

<jsp:include page="/WEB-INF/jsp/header.jsp"></jsp:include>


        <div class="mylist-main-section col-12">
                <div class="content">

                        <!-- breadcrumb-->
                        <div class="breadcrumbs">
                                <a href="${pageContext.request.contextPath}"><i class="fas fa-home"></i></a>
                        <span>&gt;</span>
                        <a href="${pageContext.request.contextPath}/mylists"><i class="fas fa-list"></i> le mie liste</a>
                        <span>&gt;</span>
                        <span>${list.name}</span>
                </div>


                <div class="mylist">
                        <div class="list-left-body">
                                <div class="list-head">
                                        <div class="list-logo">
                                                <img class="img-fluid" src="${pageContext.request.contextPath}/${list.img}" alt="logo"/>
                                        </div>
                                        <div class="list-info">
                                                <span class="list-name">
                                                        <i class="fas fa-info-circle"></i> <b>nome:</b> 
                                                </span>
                                                <p>${list.name}</p>
                                                <span class="list-category">
                                                        <i class="fas fa-sitemap"></i> <b>categoria: </b>
                                                </span>
                                                <p>
                                                        <custom:getListCategoryNameByListCategoryId listCategoryId="${list.categoryList}"/>
                                                        ${categoryListName}
                                                </p>
                                        </div>
                                        <div class="list-description">
                                                <i class="far fa-file-alt"></i> <b>descrizione: </b>
                                                <p>${list.description}</p>
                                        </div>
                                        <div class="list-modify">
                                                <c:if test="${list.ownerId == sessionScope.user.id}">
                                                        <a class="btn btn-info" href="#" ><i class="fas fa-edit"></i> modifica</a>
                                                </c:if>
                                        </div>
                                </div>
                                <div class="list-permission">
                                        <p><i class="fas fa-lock"></i> <b>permesso:</b></p>
                                        <span class="modify-list" title="modifica la lista">
                                                <i class="fas fa-edit"></i> ${userPermissionsOnList.modifyList?"<i class=\"fas fa-check\"></i>":"<i class=\"fas fa-times\"></i>"}
                                        </span>
                                        <span class="delete-list" title="elimina la lista">
                                                <i class="fas fa-trash-alt"></i> ${userPermissionsOnList.deleteList?"<i class=\"fas fa-check\"></i>":"<i class=\"fas fa-times\"></i>"}
                                        </span>
                                        <span class="add-item" title="aggiunge il prodotto">
                                                <i class="fas fa-cart-plus"></i> ${userPermissionsOnList.addObject?"<i class=\"fas fa-check\"></i>":"<i class=\"fas fa-times\"></i>"}
                                        </span>
                                        <span class="delete-item" title="elimina il prodotto">
                                                <i class="fas fa-ban"></i> ${userPermissionsOnList.deleteObject?"<i class=\"fas fa-check\"></i>":"<i class=\"fas fa-times\"></i>"}
                                        </span>
                                </div>
                                <div class="list-sharing">
                                        <c:if test="${list.ownerId == sessionScope.user.id}">
                                                <button class="btn btn-info" data-toggle="modal" data-target="#boxSharing"><i class="fas fa-share-alt"></i> condivisione</button>
                                        </c:if>
                                </div>        

                        </div>

                        <div class="list-right-body">
                                <!-- intestazione della lista -->
                                <div class="list-item">
                                        <div class="item-img">
                                                <i class="far fa-image"></i>
                                        </div>
                                        <div class="item-name">
                                                <i class="fas fa-align-left"></i>
                                        </div>
                                        <div class="item-cat">
                                                <i class="fas fa-store"></i>
                                        </div>
                                        <div class="item-logo">
                                                <i class="far fa-bookmark"></i>
                                        </div>
                                        <div class="item-description">
                                                <i class="far fa-file-alt"></i>
                                        </div>
                                        <div class="item-manage">
                                                <i class="fas fa-wrench"></i>
                                        </div>        
                                </div>

                                <!-- prodotto ancora da comprare-->
                                <c:forEach var="product" items="${listProductsNotBuy}">
                                        <div class="list-item">
                                                <div class="item-img">
                                                        <img class="img-fluid" src="${pageContext.request.contextPath}/${product.img}" alt="${product.name}"/>
                                                </div>
                                                <div class="item-name">
                                                        <span>${product.name}</span>
                                                </div>
                                                <div class="item-cat">
                                                        <a href="${pageContext.request.contextPath}/category?catId=${product.categoryProductId}">
                                                                <custom:getCategoryNameById categoryId="${product.categoryProductId}"/>
                                                                ${categoryName}
                                                        </a>
                                                </div>
                                                <div class="item-logo">
                                                        <img class="img-fluid" src="${pageContext.request.contextPath}/${product.logo}" alt="logo"/>
                                                </div>
                                                <div class="item-description">
                                                        <p>
                                                                ${product.description}
                                                        </p>
                                                </div>
                                                <div class="item-manage">
                                                        <a class="btn btn-info" href="${pageContext.request.contextPath}/service/updateItemInListService?action=bought&productId=${product.id}&listId=${list.id}" title="comprato">
                                                                <i class="fas fa-check-circle"></i> comprato
                                                        </a>
                                                        <!-- se utente ha il permesso di eliminare prodotto della lista-->      
                                                        <c:if test="${userPermissionsOnList.deleteObject == true}">
                                                                <a class="btn btn-danger" href="${pageContext.request.contextPath}/service/updateItemInListService?action=delete&productId=${product.id}&listId=${list.id}" title="elimina">
                                                                        <i class="fas fa-ban"></i> elimina
                                                                </a>
                                                        </c:if>
                                                </div>        
                                        </div>
                                </c:forEach>


                                <!-- prodotto già comprato -->
                                <c:forEach var="product" items="${listProductsBought}">
                                        <div class="list-item item-bought">
                                                <div class="item-img">
                                                        <img class="img-fluid" src="${pageContext.request.contextPath}/${product.img}" alt="${product.name}"/>
                                                </div>
                                                <div class="item-name">
                                                        <span>${product.name}</span>
                                                </div>
                                                <div class="item-cat">
                                                        <a href="${pageContext.request.contextPath}/category?catId=${product.categoryProductId}">
                                                                <custom:getCategoryNameById categoryId="${product.categoryProductId}"/>
                                                                ${categoryName}
                                                        </a>
                                                </div>
                                                <div class="item-logo">
                                                        <img class="img-fluid" src="${pageContext.request.contextPath}/${product.logo}" alt="logo"/>
                                                </div>
                                                <div class="item-description">
                                                        <p>
                                                                ${product.description}
                                                        </p>
                                                </div>
                                                <div class="item-manage">

                                                        <!-- se utente ha il permesso di eliminare prodotto della lista-->      
                                                        <c:if test="${userPermissionsOnList.deleteObject == true}">
                                                                <a class="btn btn-danger" href="${pageContext.request.contextPath}/service/updateItemInListService?action=delete&productId=${product.id}&listId=${list.id}" title="elimina">
                                                                        <i class="fas fa-ban"></i> elimina
                                                                </a>
                                                        </c:if>
                                                </div>        
                                        </div>
                                </c:forEach>



                                <c:if test="${empty listProductsNotBuy && empty listProductsBought}">
                                        <div class="list-item item-empty"><h2>la lista è ancora vuota</h2></div>
                                </c:if>
                                        
                               <jsp:include page="/WEB-INF/jsp/module/comment.jsp"></jsp:include>                      
                        </div>
                </div>

                              

                </div>               
        </div>
<jsp:include page="/WEB-INF/jsp/module/floatBoxForSharing.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/footer.jsp"></jsp:include>
