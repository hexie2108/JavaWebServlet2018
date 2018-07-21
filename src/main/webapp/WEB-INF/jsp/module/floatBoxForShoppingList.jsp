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

                <c:choose>
                        <c:when test="${empty sessionScope.user}">
                                <!-- utente anonimo-->
                                <div class="list-selector mb-2">
                                        <form action="#">
                                                <label  for="select-list" class="d-block">Seleziona la lista:</label>
                                                <select id="select-list" class="form-control custom-select w-50" name="listId">
                                                        <option value="default">Default list</option>
                                                </select>

                                        </form>
                                </div>
                                <div class="list-type-selector mb-3">
                                        <form action="#">
                                                <label  for="type-list"  class="d-block">Tipo della lista:</label>
                                                <select id="type-list" class="form-control custom-select w-50">
                                                        <option>-----</option>
                                                        <custom:printAllCategoryList catoryIdOfCurrentList="-1"></custom:printAllCategoryList>
                                                        </select>
                                                        <div class="d-inline-block w-25">
                                                                <input class="btn btn-info" type="submit" value="cambia"/>
                                                        </div>
                                                </form>
                                        </div>
                                        <div class="list-manage">

                                        </div>
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

                                                        <custom:getElementsOfShppingListByCookie></custom:getElementsOfShppingListByCookie>

                                                        </tbody>
                                                </table>
                                        </div>
                        </c:when>
                        <c:otherwise>
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
                                                <a class="btn btn-info w-100" href="#">Crea una nuova lista</a>
                                        </div>
                                </c:if>

                                <!-- se utente ha alcuna lista-->
                                <c:if test="${not empty sessionScope.myListId}">
                                        <div class="list-selector mb-2">
                                                <form action="#">
                                                        <label  for="select-list" class="d-block">Seleziona la lista:</label>

                                                        <select id="select-list" class="form-control custom-select w-50" name="listId">
                                                                <c:forEach var="shoppingList" items="${sessionScope.allMyList}">
                                                                        <option value="${shoppingList.id}">${shoppingList.name}</option>
                                                                </c:forEach>
                                                        </select>

                                                        <div class="d-inline-block w-25">
                                                                <input class="btn btn-info disabled" type="submit" value="cambia"/>
                                                        </div>
                                                </form>
                                        </div>
                                        <div class="list-type-selector mb-3">
                                                <form action="#">
                                                        <label  for="type-list"  class="d-block">Tipo della lista:</label>
                                                        <select id="type-list" class="form-control custom-select w-50">
                                                                <custom:printAllCategoryList catoryIdOfCurrentList="-1"></custom:printAllCategoryList>
                                                                </select>
                                                                <div class="d-inline-block w-25">
                                                                        <input class="btn btn-info" type="submit" value="cambia"/>
                                                                </div>
                                                        </form>
                                                </div>
                                                <div class="list-manage">
                                                        <a class="btn btn-info w-100" href="#">salvare lista</a>
                                                </div>
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

                                                                <c:forEach var="product" items="${sessionScope.productsOfMyList}">

                                                                        <tr>
                                                                                <td class="td-img">
                                                                                        <img src="${pageContext.request.contextPath}/${product.img}"  alt="${product.name}" />
                                                                                </td>
                                                                                <td class="td-name">
                                                                                        <span>${product.name}</span>
                                                                                </td>
                                                                                <td class="td-buttons">
                                                                                        <a href="#" title="comprato"><i class="fas fa-check-circle"></i></a>
                                                                                                <c:if test="${sessionScope.myListPermission.deleteObject==true}">
                                                                                                <a href="#" title="elimina"><i class="fas fa-ban"></i></a>
                                                                                                </c:if>
                                                                                </td>
                                                                        </tr>
                                                                </c:forEach>

                                                        </tbody>
                                                </table>
                                        </div>
                                </c:if>       

                        </c:otherwise>
                </c:choose>




        </div>
</div>

