<%-- 

    la finestra laterale di lista di spesa nelle pagine front-end

--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="custom" uri="/WEB-INF/custom.tld" %>
<%@include file="/WEB-INF/jspf/i18n.jsp"%>

<%-- il float bottone per visualizzare la finestra laterale di lista spessa--%>
<div class="list-button-float-box">

    <button type="button" class="btn btn-info" data-toggle="collapse" data-target="#list-float-box">
        <i class="fas fa-shopping-cart"></i>
        <br>
        <span><fmt:message key="my list" /></span>
    </button>

</div>

<%-- finestra laterale body--%>
<div id="list-float-box" class="collapse fade">

    <div class="list-body">

        <%-- bottone per chiudere--%>
        <div class="clearfix">
            <button type="button" class="btn btn-info float-right" data-toggle="collapse" data-target="#list-float-box">
                &times;
            </button>
        </div>


        <%--form per cambiare la lista --%>
        <div class="list-selector mb-2">

            <%--se è un utente anonimo --%>
            <c:if test="${empty sessionScope.user}">

                <form action="#">
                    <label for="select-list" class="d-block">
                        <i class="fas fa-list"></i> <fmt:message key="selectTheList" />:
                    </label>
                    <select id="select-list" class="form-control custom-select w-50" name="listId">
                        <option value="default"><fmt:message key="defaultList" /></option>
                    </select>
                </form>

            </c:if>

            <%--se è un utente loggato --%>
            <c:if test="${not empty sessionScope.user}">

                <%--se possiede almeno una lista --%>
                <c:if test="${not empty sessionScope.myListId}">

                    <form action="<c:url value="/service/changeListService"/>" method="GET">
                        <label for="select-list" class="d-block">
                            <i class="fas fa-list"></i> <fmt:message key="selectTheList" />:
                        </label>
                        <select id="select-list" class="form-control custom-select w-50" name="listId">

                                <%--stampa tutte le liste che possiede --%>
                            <c:forEach var="shoppingList" items="${requestScope.allMyList}">
                                <option value="${shoppingList.id}" ${sessionScope.myListId == shoppingList.id?"selected=\"selected\"":""}>${shoppingList.name}</option>
                            </c:forEach>

                        </select>
                        <div class="d-inline-block w-25">
                            <input class="btn btn-info" type="submit" value="<fmt:message key="change" />"/>
                        </div>
                    </form>

                </c:if>

                <%--se non ha nessuna lista --%>
                <c:if test="${empty sessionScope.myListId}">

                    <h2>
                        <fmt:message key="oops! you do not even have a list" />
                    </h2>
                    <%-- link per creare una nuova lista--%>
                    <div class="add-list-box">
                        <a class="btn btn-info" href="<c:url value="/updateList"/>"><i
                                class="fas fa-plus"></i> <fmt:message key="createTheNewList" /></a>
                    </div>

                </c:if>


            </c:if>

        </div>

        <%--se è un utente anonimo --%>
        <c:if test="${empty sessionScope.user}">

            <%-- form per cambiare il tipo di lista per utente anonimo --%>
            <div class="list-type-selector mb-3">

                <form action="<c:url value="/service/updateItemInListUnloggedUserOnlyService"/>"
                      method="GET">
                    <label for="type-list" class="d-block">
                        <i class="fas fa-sitemap"></i> <fmt:message key="list type" />:
                    </label>
                    <select id="type-list" class="form-control custom-select w-50" name="categoryList"
                            onchange="checkValueOfCategoryList(this)">
                            <%--se non è ancora stato selezionato una categoria --%>
                        <c:if test="${empty cookie.localShoppingListCategory}">
                            <option value="-1">-------</option>
                        </c:if>
                        <custom:getAllCategoryOfShoppingList/>

                            <%--stampa tutte le categorie di lista --%>
                        <c:forEach var="categoryOfShoppingList" items="${categoryList}">

                            <option value="${categoryOfShoppingList.id}" ${not empty cookie.localShoppingListCategory && cookie.localShoppingListCategory.value == categoryOfShoppingList.id ? " selected=\"selected\"" : ""}>${categoryOfShoppingList.name}</option>

                        </c:forEach>

                    </select>

                    <div class="d-inline-block w-25">
                        <input type="hidden" name="action" value="changeListCategory"/>
                        <input id="submitToChangeCategoryList" class="btn btn-info" type="submit" value="<fmt:message key="change" />"
                               disabled="disabled"/>
                    </div>

                </form>

            </div>

        </c:if>

        <%--se è un utente anonimo oppure se è un utente loggato che possiede almeno una lista--%>
        <c:if test="${empty sessionScope.user || not empty sessionScope.myListId}">

            <%--lista di prodotto body --%>
            <div class="list-content table-responsive">

                <table class="table">

                    <thead>
                    <tr>
                        <th><fmt:message key="img" /></th>
                        <th><fmt:message key="name" /></th>
                        <th><i class="fas fa-edit"></i></th>
                    </tr>
                    </thead>

                    <tbody>
                        <%-- se è un utente anonimo--%>
                    <c:if test="${empty sessionScope.user}">
                        <%-- get la lista di prodotto dalla cookie--%>
                        <custom:getElementsOfShppingListByCookie/>
                    </c:if>

                        <%-- se la lista corrente contiene alcuni prodotto ancora da comprare--%>
                    <c:if test="${not empty productsOfMyList}">

                        <%-- stampa tutti i prodotti della lista--%>
                        <c:forEach var="product" items="${productsOfMyList}">

                            <tr id="productIdInList-${product.id}">

                                    <%-- link per visualizzare il prodtto in finestrina--%>
                                <td class="td-img">

                                    <a href="javascript:;" data-toggle="modal" data-target="#boxShowItem"

                                        <%-- se è un utente anonimo --%>
                                            <c:if test="${empty sessionScope.user}">
                                                onclick="showProductWindowsFromList(${product.id}, false, false, true)">
                                            </c:if>
                                        <%-- se è un utente loggato--%>
                                            <c:if test="${not empty sessionScope.user}">
                                                onclick="showProductWindowsFromList(${product.id}, true, false,${MylistPermission.deleteObject})">
                                            </c:if>

                                    <img class="img"
                                         src="<c:url value="/image/product/${product.img}"/>"
                                         alt="${product.name}"/>

                                    </a>

                                </td>

                                    <%-- campi necessari per visuallizare il prodotto nella finestrina--%>
                                <td class="td-name">

                                    <span>${product.name}</span>


                                    <input class="name" type="hidden" value="${product.name}"/>
                                    <input class="logo-img" type="hidden"
                                           value="<c:url value="/image/product/${product.logo}"/>"/>
                                    <input class="cat-link" type="hidden"
                                           value="<c:url value="/category?catId=${product.categoryProductId}"/>"/>
                                        <%-- get il nome della categoria del prodotto--%>
                                    <custom:getCategoryNameById categoryId="${product.categoryProductId}"/>
                                    <input class="cat-name" type="hidden" value="${categoryName}"/>
                                    <input class="description" type="hidden" value="${product.description}"/>
                                    <input class="list-id" type="hidden" value="${sessionScope.myListId}"/>

                                </td>

                                    <%-- operazioni --%>
                                <td class="td-buttons">

                                        <%-- se è un utente anonimo--%>
                                    <c:if test="${empty sessionScope.user}">

                                        <%-- link per eliminare il prodotto dalla cookie--%>
                                        <a href="<c:url value="/service/updateItemInListUnloggedUserOnlyService?action=delete&productId=${product.id}"/>"
                                           title="<fmt:message key="delete" />" onclick="if(!confirm('<fmt:message key="are you sure?" />')) return false;">
                                            <i class="fas fa-ban"></i>
                                        </a>

                                    </c:if>

                                        <%-- se è un utente loggato--%>
                                    <c:if test="${not empty sessionScope.user}">

                                        <%-- link per segna il prodotto come comprato--%>
                                        <a href="<c:url value="/service/updateItemInListService?action=bought&productId=${product.id}&listId=${sessionScope.myListId}"/>"
                                           title="<fmt:message key="bought" />">
                                            <i class="fas fa-check-circle"></i>
                                        </a>

                                        <%-- se utente loggato ha il permesso di eliminare il prodotto--%>
                                        <c:if test="${MylistPermission.deleteObject == true}">

                                            <%-- link per eliminare il prodotto--%>
                                            <a href="<c:url value="/service/updateItemInListService?action=delete&productId=${product.id}&listId=${sessionScope.myListId}"/>"
                                               title="<fmt:message key="delete" />" onclick="if(!confirm('<fmt:message key="are you sure?" />')) return false;">
                                                <i class="fas fa-ban"></i>
                                            </a>

                                        </c:if>

                                    </c:if>

                                </td>

                            </tr>

                        </c:forEach>

                    </c:if>

                        <%-- se la lista corrente contiene alcuni prodotto già comprato--%>
                    <c:if test="${not empty productsBoughtOfMyList}">

                        <%-- stampa tutti i prodotti della lista--%>
                        <c:forEach var="product" items="${productsBoughtOfMyList}">

                            <tr id="productIdInList-${product.id}" class="bought-item">

                                    <%-- link per visualizzare il prodtto in finestrina--%>
                                <td class="td-img">

                                    <a href="javascript:;" data-toggle="modal" data-target="#boxShowItem"

                                        <%-- se è un utente anonimo --%>
                                            <c:if test="${empty sessionScope.user}">
                                                onclick="showProductWindowsFromList(${product.id}, false, true, true)">
                                            </c:if>
                                        <%-- se è un utente loggato--%>
                                            <c:if test="${not empty sessionScope.user}">
                                                onclick="showProductWindowsFromList(${product.id}, true, true,${MylistPermission.deleteObject})">
                                            </c:if>

                                    <img class="img"
                                         src="<c:url value="/image/product/${product.img}"/>"
                                         alt="${product.name}"/>

                                    </a>

                                </td>

                                    <%-- campi necessari per visuallizare il prodotto nella finestrina--%>
                                <td class="td-name">

                                    <span>${product.name}</span>

                                    <input class="name" type="hidden" value="${product.name}"/>
                                    <input class="logo-img" type="hidden"
                                           value="<c:url value="/image/product/${product.logo}"/>"/>
                                    <input class="cat-link" type="hidden"
                                           value="<c:url value="/category?catId=${product.categoryProductId}"/>"/>

                                        <%-- get il nome della categoria del prodotto--%>
                                    <custom:getCategoryNameById categoryId="${product.categoryProductId}"/>
                                    <input class="cat-name" type="hidden" value="${categoryName}"/>

                                    <input class="description" type="hidden" value="${product.description}"/>
                                    <input class="list-id" type="hidden" value="${sessionScope.myListId}"/>

                                </td>

                                    <%-- operazioni --%>
                                <td class="td-buttons">

                                        <%-- se è un utente loggato--%>
                                    <c:if test="${not empty sessionScope.user}">

                                        <%-- se utente loggato ha il permesso di eliminare il prodotto--%>
                                        <c:if test="${MylistPermission.deleteObject == true}">

                                            <%-- link per eliminare il prodotto--%>
                                            <a href="<c:url value="/service/updateItemInListService?action=delete&productId=${product.id}&listId=${sessionScope.myListId}"/>"
                                               title="<fmt:message key="delete" />">
                                                <i class="fas fa-ban"></i>
                                            </a>

                                        </c:if>

                                    </c:if>

                                </td>

                            </tr>

                        </c:forEach>

                    </c:if>

                        <%-- se la lista corrente è vuoto--%>
                    <c:if test="${empty productsOfMyList && empty productsBoughtOfMyList}">

                        <%-- stampa l'avviso--%>
                        <tr>
                            <td colspan="3">
                                <fmt:message key="it is empty" />
                            </td>
                        </tr>

                    </c:if>

                    </tbody>

                </table>

            </div>

        </c:if>

    </div>
</div>

