<%-- 
    Document   : header
    Created on : 2018-7-15, 7:55:07
    Author     : liu
    componente che visualizza la finestra di shopping list sul sito
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="custom" uri="/WEB-INF/custom.tld"%>

<div class="list-button-float-box">
        <button type="button" class="btn btn-info" data-toggle="collapse"  data-target="#list-float-box"><i class="fas fa-shopping-cart"></i><br><span>Mia</span><br><span>Lista</span></button>

</div>
<div id="list-float-box" class="collapse fade"  >

        <div class="list-body">
                <div class="clearfix">
                        <button type="button" class="btn btn-info float-right" data-toggle="collapse" data-target="#list-float-box">&times;</button>
                </div>

                <!--se è un utente anonimo-->
                <c:if test="${empty sessionScope.user}">

                        <div class="list-selector mb-2">
                                <form action="#">
                                        <label  for="select-list" class="d-block"><i class="fas fa-list"></i> Seleziona la lista:</label>
                                        <select id="select-list" class="form-control custom-select w-50" name="listId">
                                                <option value="default">Default list</option>
                                        </select>
                                </form>
                        </div>
                        <div class="list-type-selector mb-3">
                                <form action="${pageContext.request.contextPath}/service/updateItemInListUnloggedUserOnlyService" method="GET">
                                        <label  for="type-list"  class="d-block"><i class="fas fa-sitemap"></i> Tipo della lista:</label>
                                        <select id="type-list" class="form-control custom-select w-50" name ="categoryList" onchange="checkValueOfCategoryList(this)">
                                                <!-- se è stato assegnato una categoria di lista locale-->
                                                <c:if test="${not empty cookie.localShoppingListCategory}">
                                                        <custom:printAllCategoryList catoryIdOfCurrentList="${cookie.localShoppingListCategory.value}"></custom:printAllCategoryList>
                                                </c:if>
                                                <!-- se non è ancora stato assegnato una categoria di lista locale-->
                                                <c:if test="${empty cookie.localShoppingListCategory}">
                                                        <option value="-1">-------</option>
                                                        <custom:printAllCategoryList catoryIdOfCurrentList="-1"></custom:printAllCategoryList>
                                                </c:if>
                                        </select>
                                        <div class="d-inline-block w-25">
                                                <input type="hidden" name="action" value="changeListCategory"/>
                                                <input id="submitToChangeCategoryList" class="btn btn-info" type="submit" value="cambia" disabled="disabled"/>
                                        </div>
                                </form>
                        </div>
                </c:if>                       
                <c:if test="${not empty sessionScope.user}">
                        <!-- utente loggato-->
                        <!-- per memorizzare quale lista è stato scelto si usa un attributo sessione che memorizza list id -->
                        <!-- se utente non ha neanche una lista-->
                        <c:if test="${empty sessionScope.myListId}">

                                <div class="list-selector mb-2">
                                        <h2>
                                                Opps! non hai neanche una lista
                                        </h2>
                                </div>
                                <div class="list-manage">
                                        <a class="btn btn-info w-100" href="#"><i class="fas fa-plus"></i> Crea una nuova lista</a>
                                </div>
                        </c:if>
                        <!-- se utente ha alcuna lista-->
                        <c:if test="${not empty sessionScope.myListId}">
                                <div class="list-selector mb-2">
                                        <form action="${pageContext.request.contextPath}/service/changeListService" method="GET">
                                                <label  for="select-list" class="d-block"><i class="fas fa-list"></i> Seleziona la lista:</label>

                                                <select id="select-list" class="form-control custom-select w-50" name="listId">
                                                        <c:forEach var="shoppingList" items="${requestScope.allMyList}">
                                                                <option value="${shoppingList.id}" ${sessionScope.myListId == shoppingList.id?"selected=\"selected\"":""}>${shoppingList.name}</option>
                                                        </c:forEach>
                                                </select>
                                                <div class="d-inline-block w-25">
                                                        <input class="btn btn-info" type="submit" value="cambia"/>
                                                </div>
                                        </form>
                                </div>
                        </c:if>

                </c:if>                                     

                <div class="list-content table-responsive">
                        <table class="table">
                                <thead>
                                        <tr>
                                                <th>img</th>
                                                <th>nome</th>
                                                <th><i class="fas fa-edit"></i></th>
                                        </tr>
                                </thead>
                                <tbody>
                                        <!-- se è un utente anonimo-->
                                        <c:if test="${empty sessionScope.user}">
                                                <custom:getElementsOfShppingListByCookie/>
                                        </c:if> 
                                        ${a}
                                        <!-- se la lista corrente contiene alcuni prodotto ancora da comprare-->
                                        <c:if test="${not empty productsOfMyList}">
                                                <c:forEach var="product" items="${productsOfMyList}">
                                                        <tr id="productIdInList-${product.id}" >
                                                                <td class="td-img">
                                                                        <!-- se è un utente anonimo-->
                                                                        <c:if test="${empty sessionScope.user}">
                                                                                <a href="javascript:;" data-toggle="modal" data-target="#boxShowItem" onclick="showProductWindowsFromList(${product.id}, false, false,true)">
                                                                        </c:if>
                                                                                <!-- se è un utente loggato-->
                                                                          <c:if test="${not empty sessionScope.user}">
                                                                                   <a href="javascript:;" data-toggle="modal" data-target="#boxShowItem" onclick="showProductWindowsFromList(${product.id}, true, false,${MylistPermission.deleteObject})">
                                                                          </c:if>

                                                                                        <img class="img" src="${pageContext.request.contextPath}/${product.img}" alt="${product.name}" />
                                                                                </a>
                                                                </td>
                                                                <td class="td-name">
                                                                        <span>${product.name}</span>

                                                                        <!-- campo necessario per visuallizare il prodotto nella finestrina-->
                                                                        <input class="name" type="hidden" value="${product.name}" />
                                                                        <input class="logo-img" type="hidden" value="${pageContext.request.contextPath}/${product.logo}" />
                                                                        <input class="cat-link" type="hidden" value="${pageContext.request.contextPath}/category?catid=${product.categoryProductId}"/>

                                                                        <custom:getCategoryNameById categoryId="${product.categoryProductId}" />
                                                                        <input class="cat-name" type="hidden" value="${categoryName}"/>
                                                                        <input class="description" type="hidden" value="${product.description}"/>
                                                                        <input class="list-id" type="hidden" value="${sessionScope.myListId}"/>

                                                                </td>
                                                                <td class="td-buttons">
                                                                        <!-- se è un utente anonimo-->
                                                                        <c:if test="${empty sessionScope.user}">
                                                                                <a href="${pageContext.request.contextPath}/service/updateItemInListUnloggedUserOnlyService?action=delete&productId=${product.id}" title="elimina">
                                                                                        <i class="fas fa-ban"></i>
                                                                                </a>
                                                                        </c:if>
                                                                        <!-- se è un utente loggato-->
                                                                        <c:if test="${not empty sessionScope.user}">
                                                                                <a href="${pageContext.request.contextPath}/service/updateItemInListService?action=bought&productId=${product.id}&listId=${sessionScope.myListId}" title="comprato">
                                                                                        <i class="fas fa-check-circle"></i>
                                                                                </a>
                                                                                <!-- se utente loggato può eliminare prodotto della lista-->      
                                                                                <c:if test="${MylistPermission.deleteObject == true}">
                                                                                        <a href="${pageContext.request.contextPath}/service/updateItemInListService?action=delete&productId=${product.id}&listId=${sessionScope.myListId}" title="elimina">
                                                                                                <i class="fas fa-ban"></i>
                                                                                        </a>
                                                                                </c:if>
                                                                        </c:if>

                                                                </td>
                                                        </tr>
                                                </c:forEach>
                                        </c:if>
                                        <!-- se la lista corrente contiene alcuni prodotto già comprato-->
                                        <c:if test="${not empty productsBoughtOfMyList}">
                                                <c:forEach var="product" items="${productsBoughtOfMyList}">
                                                        <tr id="productIdInList-${product.id}" class="bought-item" >
                                                                <td class="td-img">
                                                                        
                                                                        <!-- se è un utente anonimo-->
                                                                        <c:if test="${empty sessionScope.user}">
                                                                                <a href="javascript:;" data-toggle="modal" data-target="#boxShowItem" onclick="showProductWindowsFromList(${product.id}, false, true,true)">
                                                                        </c:if>
                                                                                <!-- se è un utente loggato-->
                                                                          <c:if test="${not empty sessionScope.user}">
                                                                                    <a href="javascript:;" data-toggle="modal" data-target="#boxShowItem" onclick="showProductWindowsFromList(${product.id}, true, true,${MylistPermission.deleteObject})">
                                                                          </c:if>
                                                                        
                                                                                <img class="img" src="${pageContext.request.contextPath}/${product.img}" alt="${product.name}" />
                                                                        </a>
                                                                </td>
                                                                <td class="td-name">
                                                                        <span>${product.name}</span>

                                                                        <!-- campo necessario per visuallizare il prodotto nella finestrina-->
                                                                        <input class="name" type="hidden" value="${product.name}" />
                                                                        <input class="logo-img" type="hidden" value="${pageContext.request.contextPath}/${product.logo}" />
                                                                        <input class="cat-link" type="hidden" value="${pageContext.request.contextPath}/category?catid=${product.categoryProductId}"/>

                                                                        <custom:getCategoryNameById categoryId="${product.categoryProductId}" />
                                                                        <input class="cat-name" type="hidden" value="${categoryName}"/>

                                                                        <input class="description" type="hidden" value="${product.description}"/>
                                                                        <input class="list-id" type="hidden" value="${sessionScope.myListId}"/>

                                                                </td>
                                                                <td class="td-buttons">
                                                                        
                                                                        <!-- se è un utente loggato-->
                                                                        <c:if test="${not empty sessionScope.user}">
                                                                                <!-- se utente loggato può eliminare prodotto della lista-->      
                                                                                <c:if test="${MylistPermission.deleteObject == true}">
                                                                                        <a href="${pageContext.request.contextPath}/service/updateItemInListService?action=delete&productId=${product.id}&listId=${sessionScope.myListId}" title="elimina">
                                                                                                <i class="fas fa-ban"></i>
                                                                                        </a>
                                                                                </c:if>
                                                                        </c:if>

                                                                </td>
                                                        </tr>
                                                </c:forEach>
                                        </c:if>
                                        <!-- se la lista corrente è vuoto-->
                                        <c:if test="${empty productsOfMyList && empty productsBoughtOfMyList}">
                                                <tr><td colspan = "3">è ancora vuoto</td></tr>
                                        </c:if>
                                </tbody>
                        </table>
                </div>

        </div>
</div>

