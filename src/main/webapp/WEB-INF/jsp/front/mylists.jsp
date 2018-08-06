<%-- 
    pagina per visualizzare tutte le liste possiedute da un utente
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="custom" uri="/WEB-INF/custom.tld"%>

<jsp:include page="/WEB-INF/jsp/front/header.jsp"/>


<div class="mylists-main-section col-12">
        <div class="content">

                <%-- breadcrumb--%>
                <div class="breadcrumbs">
                        <a href="${pageContext.request.contextPath}">
                                <i class="fas fa-home"></i>
                        </a>
                        <span>&gt;</span>                       
                        <span>
                                <i class="fas fa-list"></i> le mie liste
                        </span>  

                </div>


                <%-- link per creare una nuova lista--%>
                <div class="add-list-box">
                        <a class="btn btn-info" href="${pageContext.request.contextPath}/updateList"><i class="fas fa-plus"></i> crea una nuova lista</a>
                </div>

                <%-- se possiede almeno una lista owner --%>         
                <c:if test="${not empty ownedLists}">

                        <div class="owner-lists my-lists">

                                <%-- titolo--%>
                                <h2>
                                        <i class="fas fa-list"></i> le liste che possiede
                                </h2>

                                <%-- stampa tutta la lista--%>
                                <c:forEach var="list" items="${ownedLists}">


                                        <div class="list-item">


                                                <div class="list-head">

                                                        <%-- tutte le informazioni della lista--%>
                                                        <div class="list-left-head">

                                                                <div class="list-logo">
                                                                        <img class="img-fluid" src="${pageContext.request.contextPath}/image/list/${list.img}" alt="logo"/>
                                                                </div>

                                                                <div class="list-info">

                                                                        <span class="list-name">
                                                                                <i class="fas fa-info-circle"></i> <b>nome:</b> ${list.name}
                                                                        </span>

                                                                        <span class="list-category">
                                                                                <i class="fas fa-sitemap"> <b></i> categoria: </b>
                                                                                <%-- get il nome della categoria di lista--%>
                                                                                <custom:getListCategoryByListCategoryId listCategoryId="${list.categoryList}" />
                                                                                ${categoryOfList.name}
                                                                        </span>

                                                                        <div class="list-description">
                                                                                <i class="far fa-file-alt"></i> <b>descrizione: </b> 
                                                                                ${list.description}
                                                                        </div>

                                                                </div>

                                                        </div>

                                                        <%-- il numero di condivisione e il numero di commento--%>                  
                                                        <div class="list-right-head"> 

                                                                <span class="button" >
                                                                        <i class="fas fa-share-alt"></i> ${ownedListsMap.get(list).get("numberOfShared")} condivisione
                                                                </span>
                                                                <span class="button">
                                                                        <i class="fas fa-comments"></i> ${ownedListsMap.get(list).get("numberComment")} commenti
                                                                </span>

                                                        </div>

                                                </div>


                                                <div class="list-body">

                                                        <%-- la lista di prodotto ancora da comprare--%>      
                                                        <c:forEach var="product" items="${ownedListsMap.get(list).get('productsListNotBuy')}">

                                                                <div id="productIdInList-${product.id}" class="list-product">

                                                                        <%-- link per visualizzare il prodotto in finestra--%>      
                                                                        <a href="javascript:;" data-toggle="modal" data-target="#boxShowItem" onclick="showProductWindowsFromList(${product.id}, true, false, true)">
                                                                                <img class="img img-fluid" src="${pageContext.request.contextPath}/image/product/${product.img}" alt="${product.name}" />
                                                                        </a>

                                                                        <%-- campi necessari per visuallizare il prodotto nella finestrina--%>
                                                                        <input class="name" type="hidden" value="${product.name}" />
                                                                        <input class="logo-img" type="hidden" value="${pageContext.request.contextPath}/${product.logo}" />
                                                                        <input class="cat-link" type="hidden" value="${pageContext.request.contextPath}/category?catId=${product.categoryProductId}"/>
                                                                        <%-- get il nome della categoria--%>      
                                                                        <custom:getCategoryNameById categoryId="${product.categoryProductId}" />
                                                                        <input class="cat-name" type="hidden" value="${categoryName}"/>

                                                                        <input class="description" type="hidden" value="${product.description}"/>
                                                                        <input class="list-id" type="hidden" value="${list.id}"/>

                                                                </div>

                                                        </c:forEach>

                                                        <%-- la lista di prodotto già comprato --%>      
                                                        <c:forEach var="product" items="${ownedListsMap.get(list).get('productsListBought')}">

                                                                <div id="productIdInList-${product.id}" class="list-product bought-item">

                                                                        <%-- link per visualizzare il prodotto in finestra--%>      
                                                                        <a href="javascript:;" data-toggle="modal" data-target="#boxShowItem" onclick="showProductWindowsFromList(${product.id}, true, true, true)">
                                                                                <img class="img img-fluid" src="${pageContext.request.contextPath}/image/product/${product.img}" alt="${product.name}" />
                                                                        </a>

                                                                        <%-- campi necessari per visuallizare il prodotto nella finestrina--%>
                                                                        <input class="name" type="hidden" value="${product.name}" />
                                                                        <input class="logo-img" type="hidden" value="${pageContext.request.contextPath}/${product.logo}" />
                                                                        <input class="cat-link" type="hidden" value="${pageContext.request.contextPath}/category?catId=${product.categoryProductId}"/>
                                                                        <%-- get il nome della categoria--%>
                                                                        <custom:getCategoryNameById categoryId="${product.categoryProductId}" />
                                                                        <input class="cat-name" type="hidden" value="${categoryName}"/>

                                                                        <input class="description" type="hidden" value="${product.description}"/>
                                                                        <input class="list-id" type="hidden" value="${list.id}"/>

                                                                </div>

                                                        </c:forEach>

                                                        <%-- se la lista non contiene nessuno prodotto--%>
                                                        <c:if test="${ownedListsMap.get(list).get('productsListNotBuy').size() ==0 && ownedListsMap.get(list).get('productsListBought').size()==0 }">
                                                                <div class="item-empty">
                                                                        <h2>la lista è ancora vuota</h2>
                                                                </div>

                                                        </c:if>

                                                </div> 

                                                <%-- link per visualizzare la lista in dettaglio--%>        
                                                <div class="list-detail-link">
                                                        <a class="btn btn-info"  href="${pageContext.request.contextPath}/mylist?listId=${list.id}">
                                                                <i class="fas fa-search-plus"></i> in dettaglio
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
                                        <i class="fas fa-list"></i> le liste condivise dagli altri
                                </h2>

                                <%-- stampa tutta la lista--%>
                                <c:forEach var="list" items="${sharedLists}">

                                        <div class="list-item">

                                                <div class="list-head">

                                                        <%-- tutte le informazioni della lista--%>
                                                        <div class="list-left-head">

                                                                <div class="list-logo">
                                                                        <img class="img-fluid" src="${pageContext.request.contextPath}/image/list/${list.img}" alt="logo"/>
                                                                </div>

                                                                <div class="list-info">

                                                                        <span class="list-name">
                                                                                <b><i class="fas fa-info-circle"></i> nome:</b> ${list.name}
                                                                        </span>

                                                                        <span class="list-category">
                                                                                <b><i class="fas fa-sitemap"></i> categoria:</b> 
                                                                                <%-- get il nome della categoria di lista--%>
                                                                                <custom:getListCategoryByListCategoryId listCategoryId="${list.categoryList}"/>
                                                                                ${categoryOfList.name}

                                                                        </span>

                                                                        <div class="list-description">
                                                                                <b><i class="far fa-file-alt"></i> descrizione: </b> ${list.description}
                                                                        </div>

                                                                </div>

                                                        </div>

                                                        <%-- stampa il permesso su questa lista e  il numero di commento--%>                    
                                                        <div class="list-right-head"> 

                                                                <div class="permission">

                                                                        <span class="modify-list" title="modifica la lista">
                                                                                <i class="fas fa-edit"></i> ${sharedListsMap.get(list).get("permission").modifyList?"<i class=\"fas fa-check\"></i>":"<i class=\"fas fa-times\"></i>"}
                                                                        </span>
                                                                        <span class="delete-list" title="elimina la lista">
                                                                                <i class="fas fa-trash-alt"></i> ${sharedListsMap.get(list).get("permission").deleteList?"<i class=\"fas fa-check\"></i>":"<i class=\"fas fa-times\"></i>"}
                                                                        </span>
                                                                        <span class="add-item" title="aggiunge il prodotto">
                                                                                <i class="fas fa-cart-plus"></i> ${sharedListsMap.get(list).get("permission").addObject?"<i class=\"fas fa-check\"></i>":"<i class=\"fas fa-times\"></i>"}
                                                                        </span>
                                                                        <span class="delete-item" title="elimina il prodotto">
                                                                                <i class="fas fa-ban"></i> ${sharedListsMap.get(list).get("permission").deleteObject?"<i class=\"fas fa-check\"></i>":"<i class=\"fas fa-times\"></i>"}
                                                                        </span>

                                                                </div>

                                                                <span class="button">
                                                                        <i class="fas fa-comments"></i> ${sharedListsMap.get(list).get("numberComment")} commenti
                                                                </span>

                                                        </div>

                                                </div>


                                                <div class="list-body">

                                                        <%-- la lista di prodotto ancora da comprare--%>         
                                                        <c:forEach var="product" items="${sharedListsMap.get(list).get('productsListNotBuy')}">

                                                                <div id="productIdInList-${product.id}" class="list-product">

                                                                        <%-- link per visualizzare il prodotto in finestra--%>      
                                                                        <a href="javascript:;" data-toggle="modal" data-target="#boxShowItem" onclick="showProductWindowsFromList(${product.id}, true, false, true)">
                                                                                <img class="img img-fluid" src="${pageContext.request.contextPath}/image/product/${product.img}" alt="${product.name}" />
                                                                        </a>


                                                                        <%-- campi necessari per visuallizare il prodotto nella finestrina--%>
                                                                        <input class="name" type="hidden" value="${product.name}" />
                                                                        <input class="logo-img" type="hidden" value="${pageContext.request.contextPath}/${product.logo}" />
                                                                        <input class="cat-link" type="hidden" value="${pageContext.request.contextPath}/category?catId=${product.categoryProductId}"/>

                                                                        <%-- get il nome della categoria--%>
                                                                        <custom:getCategoryNameById categoryId="${product.categoryProductId}" />
                                                                        <input class="cat-name" type="hidden" value="${categoryName}"/>

                                                                        <input class="description" type="hidden" value="${product.description}"/>
                                                                        <input class="list-id" type="hidden" value="${list.id}"/>

                                                                </div>

                                                        </c:forEach>

                                                        <%-- la lista di prodotto già comprato --%>      
                                                        <c:forEach var="product" items="${sharedListsMap.get(list).get('productsListBought')}">

                                                                <div id="productIdInList-${product.id}" class="list-product bought-item">

                                                                        <%-- link per visualizzare il prodotto in finestra--%>      
                                                                        <a href="javascript:;" data-toggle="modal" data-target="#boxShowItem" onclick="showProductWindowsFromList(${product.id}, true, true, true)">
                                                                                <img class="img img-fluid" src="${pageContext.request.contextPath}/image/product/${product.img}" alt="${product.name}" />
                                                                        </a>

                                                                        <%-- campi necessari per visuallizare il prodotto nella finestrina--%>
                                                                        <input class="name" type="hidden" value="${product.name}" />
                                                                        <input class="logo-img" type="hidden" value="${pageContext.request.contextPath}/${product.logo}" />
                                                                        <input class="cat-link" type="hidden" value="${pageContext.request.contextPath}/category?catId=${product.categoryProductId}"/>

                                                                        <%-- get il nome della categoria--%>
                                                                        <custom:getCategoryNameById categoryId="${product.categoryProductId}" />
                                                                        <input class="cat-name" type="hidden" value="${categoryName}"/>

                                                                        <input class="description" type="hidden" value="${product.description}"/>
                                                                        <input class="list-id" type="hidden" value="${list.id}"/>


                                                                </div>

                                                        </c:forEach>

                                                        <%-- se la lista non contiene nessuno prodotto--%>
                                                        <c:if test="${sharedListsMap.get(list).get('productsListNotBuy').size() ==0 && sharedListsMap.get(list).get('productsListBought').size()==0 }">
                                                                <div class="item-empty">
                                                                        <h2>la lista è ancora vuota</h2>
                                                                </div>

                                                        </c:if>

                                                </div> 

                                                <%-- link per visualizzare la lista in dettaglio--%>        
                                                <div class="list-detail-link">
                                                        <a class="btn btn-info" href="${pageContext.request.contextPath}/mylist?listId=${list.id}">
                                                                <i class="fas fa-search-plus"></i> in dettaglio
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
                                        Opps! non hai neanche una lista
                                </h2>
                        </div>

                </c:if>

        </div>               

</div>

<jsp:include page="/WEB-INF/jsp/front/footer.jsp"/>
