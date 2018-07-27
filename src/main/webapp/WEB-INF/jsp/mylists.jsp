<%-- 
    
    Created on : 2018-7-15, 7:55:07
    Author     : liu
    pagina per visualizzare tutte le liste possiedute da un utente
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="custom" uri="/WEB-INF/custom.tld"%>

<jsp:include page="/WEB-INF/jsp/header.jsp"></jsp:include>


        <div class="mylist-main-section col-12">
                <div class="content">

                        <!-- breadcrumb-->
                        <div class="breadcrumbs d-inline">

                                <a href="${pageContext.request.contextPath}">
                                <i class="fas fa-home"></i>
                        </a>
                        <span>&gt;</span>                       
                        <span>
                                le mie liste
                        </span>  

                </div>


                <div class="add-list-box">
                        <a class="btn btn-info" href="">crea una nuova lista</a>
                </div>
                <!-- se possiede almeno una lista owner -->         
                <c:if test="${not empty ownedLists}">
                        <div class="owner-lists my-lists">
                                <h2>
                                        le liste che possiede
                                </h2>
                                <c:forEach var="list" items="${ownedLists}">
                                        <div class="list-item">
                                                <div class="list-head">
                                                        <div class="list-left-head">
                                                                <div class="list-logo">
                                                                        <img class="img-fluid" src="${pageContext.request.contextPath}/${list.img}" alt="logo"/>
                                                                </div>
                                                                <div class="list-info">
                                                                        <span class="list-name">
                                                                                nome: <b>${list.name}</b>
                                                                        </span>
                                                                        <span class="list-category">
                                                                                categoria: 
                                                                                <b>
                                                                                        <custom:getListCategoryNameByListCategoryId listCategoryId="${list.categoryList}"/>
                                                                                </b>
                                                                        </span>
                                                                        <div class="list-description">

                                                                                <b>descrizione: </b> ${list.description}

                                                                        </div>
                                                                </div>

                                                        </div>
                                                        <div class="list-right-head"> 
                                                                <a class="link-to-shared btn btn-info" href="#">${ownedListsMap.get(list).get("numberOfShared")} condivisione</a>
                                                                <a class="link-to-comment btn btn-info" href="#">${ownedListsMap.get(list).get("numberComment")} commenti</a>
                                                        </div>
                                                </div>
                                                <div class="list-body">
                                                        <!-- stampa tutti i prodotti ancora da comprare -->       
                                                        <c:forEach var="product" items="${ownedListsMap.get(list).get('productsListNotBuy')}">
                                                                <div id="productIdInList-${product.id}" class="list-product">
                                                                        <a href="javascript:;" data-toggle="modal" data-target="#boxShowItem" onclick="showProductWindowsFromList(${product.id}, true, false, true)">
                                                                                <img class="img img-fluid" src="${pageContext.request.contextPath}/${product.img}" alt="${product.name}" />
                                                                        </a>

                                                                        <!-- campo necessario per visuallizare il prodotto nella finestrina-->
                                                                        <input class="name" type="hidden" value="${product.name}" />
                                                                        <input class="logo-img" type="hidden" value="${pageContext.request.contextPath}/${product.logo}" />
                                                                        <input class="cat-link" type="hidden" value="${pageContext.request.contextPath}/category?catid=${product.categoryProductId}"/>
                                                                        <input class="cat-name" type="hidden" value="<custom:getCategoryNameById categoryId="${product.categoryProductId}" />"/>
                                                                        <input class="description" type="hidden" value="${product.description}"/>
                                                                        <input class="list-id" type="hidden" value="${list.id}"/>

                                                                </div>
                                                        </c:forEach>
                                                        <!-- stampa tutti i prodotti già comprato -->    
                                                        <c:forEach var="product" items="${ownedListsMap.get(list).get('productsListBought')}">
                                                                <div id="productIdInList-${product.id}" class="list-product bought-item">
                                                                        <a href="javascript:;" data-toggle="modal" data-target="#boxShowItem" onclick="showProductWindowsFromList(${product.id}, true, true, true)">
                                                                                <img class="img img-fluid" src="${pageContext.request.contextPath}/${product.img}" alt="${product.name}" />
                                                                        </a>

                                                                        <!-- campo necessario per visuallizare il prodotto nella finestrina-->
                                                                        <input class="name" type="hidden" value="${product.name}" />
                                                                        <input class="logo-img" type="hidden" value="${pageContext.request.contextPath}/${product.logo}" />
                                                                        <input class="cat-link" type="hidden" value="${pageContext.request.contextPath}/category?catid=${product.categoryProductId}"/>
                                                                        <input class="cat-name" type="hidden" value="<custom:getCategoryNameById categoryId="${product.categoryProductId}" />"/>
                                                                        <input class="description" type="hidden" value="${product.description}"/>
                                                                        <input class="list-id" type="hidden" value="${list.id}"/>

                                                                </div>
                                                        </c:forEach>
                                                </div> 
                                        </div>


                                </c:forEach>
                        </div>       
                </c:if> 

                <!-- se possiede almeno una lista condivisa dagli altri -->         
                <c:if test="${not empty sharedLists}">
                        <div class="shared-lists my-lists">
                                <h2>
                                        le liste condivise dagli altri
                                </h2>
                                <c:forEach var="list" items="${sharedLists}">
                                        <div class="list-item">
                                                <div class="list-head">
                                                        <div class="list-left-head">
                                                                <div class="list-logo">
                                                                        <img class="img-fluid" src="${pageContext.request.contextPath}/${list.img}" alt="logo"/>
                                                                </div>
                                                                <div class="list-info">
                                                                        <span class="list-name">
                                                                                nome: <b>${list.name}</b>
                                                                        </span>
                                                                        <span class="list-category">
                                                                                categoria: 
                                                                                <b>
                                                                                        <custom:getListCategoryNameByListCategoryId listCategoryId="${list.categoryList}"/>
                                                                                </b>
                                                                        </span>
                                                                        <div class="list-description">
                                                                                <b>descrizione: </b> ${list.description}
                                                                        </div>
                                                                </div>

                                                        </div>
                                                        <div class="list-right-head"> 
                                                                <div class="permission">
                                                                        <span class="modify-list">
                                                                                modificare la lista: ${sharedListsMap.get(list).get("permission").modifyList?"si":"no"}
                                                                        </span>
                                                                        <span class="delete-list">
                                                                                eliminare la lista: ${sharedListsMap.get(list).get("permission").deleteList?"si":"no"}
                                                                        </span>
                                                                        <span class="add-item">
                                                                                aggiungere il prodotto: ${sharedListsMap.get(list).get("permission").addObject?"si":"no"}
                                                                        </span>
                                                                        <span class="delete-item">
                                                                                elimina il prodotto: ${sharedListsMap.get(list).get("permission").deleteObject?"si":"no"}
                                                                        </span>

                                                                </div>

                                                                <a class="link-to-comment btn btn-info" href="#">${sharedListsMap.get(list).get("numberComment")} commenti</a>
                                                        </div>
                                                </div>
                                                <div class="list-body">
                                                        <!-- stampa tutti i prodotti ancora da comprare -->       
                                                        <c:forEach var="product" items="${sharedListsMap.get(list).get('productsListNotBuy')}">
                                                                <div id="productIdInList-${product.id}" class="list-product">
                                                                        <a href="javascript:;" data-toggle="modal" data-target="#boxShowItem" onclick="showProductWindowsFromList(${product.id}, true, false, true)">
                                                                                <img class="img img-fluid" src="${pageContext.request.contextPath}/${product.img}" alt="${product.name}" />
                                                                        </a>


                                                                        <!-- campo necessario per visuallizare il prodotto nella finestrina-->
                                                                        <input class="name" type="hidden" value="${product.name}" />
                                                                        <input class="logo-img" type="hidden" value="${pageContext.request.contextPath}/${product.logo}" />
                                                                        <input class="cat-link" type="hidden" value="${pageContext.request.contextPath}/category?catid=${product.categoryProductId}"/>
                                                                        <input class="cat-name" type="hidden" value="<custom:getCategoryNameById categoryId="${product.categoryProductId}" />"/>
                                                                        <input class="description" type="hidden" value="${product.description}"/>
                                                                        <input class="list-id" type="hidden" value="${list.id}"/>

                                                                </div>
                                                        </c:forEach>
                                                        <!-- stampa tutti i prodotti già comprato -->    
                                                        <c:forEach var="product" items="${sharedListsMap.get(list).get('productsListBought')}">
                                                                <div id="productIdInList-${product.id}" class="list-product bought-item">
                                                                        <a href="javascript:;" data-toggle="modal" data-target="#boxShowItem" onclick="showProductWindowsFromList(${product.id}, true, true, true)">
                                                                                <img class="img img-fluid" src="${pageContext.request.contextPath}/${product.img}" alt="${product.name}" />
                                                                        </a>

                                                                        <!-- campo necessario per visuallizare il prodotto nella finestrina-->
                                                                        <input class="name" type="hidden" value="${product.name}" />
                                                                        <input class="logo-img" type="hidden" value="${pageContext.request.contextPath}/${product.logo}" />
                                                                        <input class="cat-link" type="hidden" value="${pageContext.request.contextPath}/category?catid=${product.categoryProductId}"/>
                                                                        <input class="cat-name" type="hidden" value="<custom:getCategoryNameById categoryId="${product.categoryProductId}" />"/>
                                                                        <input class="description" type="hidden" value="${product.description}"/>
                                                                        <input class="list-id" type="hidden" value="${list.id}"/>


                                                                </div>
                                                        </c:forEach>
                                                </div> 
                                        </div>


                                </c:forEach>
                        </div>       
                </c:if>     

                <c:if test="${empty ownedLists && empty sharedLists}">
                        Opps! non hai neanche una lista
                </c:if>
        </div>               
</div>

<jsp:include page="/WEB-INF/jsp/footer.jsp"></jsp:include>
