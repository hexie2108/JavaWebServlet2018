<%-- 
    la finestra per aggiungere prodotto in lista spesa
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%--  box --%>
<div class="modal fade custom-modal" id="boxAddItem">
    <div class="modal-dialog">
        <div class="modal-content">

            <%-- box-head --%>
            <div class="modal-header">
                <%-- stampa il nome del prodtto come titolo --%>
                <h4 class="modal-title">
                    <i class="fas fa-shopping-basket"></i>
                    <span class="item-name"></span>
                </h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>

            <%-- box-body --%>
            <div class="modal-body">
                <div class="left-body">

                    <%-- img di prodotto --%>
                    <img class="item-img img-fluid" alt="item-name"/>

                    <%--log di prodotto  --%>
                    <div class="item-logo">
                        <p class="font-weight-bold">
                            <i class="far fa-bookmark"></i> logo:
                        </p>
                        <div class="item-logo-img-box">
                            <img class="item-logo-img img-fluid" alt="logo"/>
                        </div>
                    </div>

                </div>

                <div class="right-body">
                    <div class="item-information">

                        <%-- link della categoria di prodotto --%>
                        <div class="item-cat">
                            <p class="font-weight-bold">
                                <i class="fas fa-store"></i> categoria:
                            </p>
                            <p>
                                <a class="item-cat-link" href="#">nome della categoria</a>
                            </p>
                        </div>

                        <%-- la descrizione del prodotto --%>
                        <div class="item-description">
                            <p class="font-weight-bold">
                                <i class="far fa-file-alt"></i> descrizione:
                            </p>
                            <p class="item-description-text">
                                la descrizione del prodotto
                            </p>
                        </div>

                    </div>

                    <div class="add-box">

                        <%-- se è un utente anonimo--%>
                        <c:if test="${empty sessionScope.user}">

                            <%--  form di inserimento per utente anonimo--%>
                            <form action="${pageContext.request.contextPath}/service/updateItemInListUnloggedUserOnlyService"
                                  method="GET">
                                <label for="select-list" class="d-block font-weight-bold"><i class="fas fa-list"></i>
                                    Seleziona la lista:</label>
                                <select id="select-list" class="form-control custom-select w-100" name="listId">
                                    <option value="default">Default list</option>
                                </select>
                                <div class="operation mt-3">

                                    <input id="productIdToAdd" type="hidden" name="productId" value="1"/>
                                    <input type="hidden" name="action" value="insert"/>
                                    <button class="btn btn-info d-inline-block" type="submit">
                                        <i class="fas fa-cart-plus"></i> aggiunge
                                    </button>

                                </div>
                            </form>

                        </c:if>

                        <%-- se è un utente loggato--%>
                        <c:if test="${not empty sessionScope.user}">

                            <%-- se utente ha qualche lista con il permesso di inserire il prodotto--%>
                            <c:if test="${not empty requestScope.addbleLists}">

                                <%--  form per inserire il prodotto nella lista selezionata--%>
                                <form action="${pageContext.request.contextPath}/service/updateItemInListService"
                                      method="GET">

                                    <label for="select-list" class="d-block font-weight-bold"><i
                                            class="fas fa-list"></i> Seleziona la lista:</label>
                                    <select id="select-list" class="form-control custom-select w-100" name="listId">

                                            <%--  stampa i nomi di tutte le liste  con il permesso di inserire il prodotto--%>
                                        <c:forEach var="shoppingList" items="${requestScope.addbleLists}">
                                            <option value="${shoppingList.id}" ${sessionScope.myListId==shoppingList.id?"selected=\"selected\"" : ""} >${shoppingList.name}</option>
                                        </c:forEach>

                                    </select>

                                    <div class="operation mt-3">
                                        <input id="productIdToAdd" type="hidden" name="productId" value="1"/>
                                        <input type="hidden" name="action" value="insert"/>
                                        <button class="btn btn-info d-inline-block" type="submit">
                                            <i class="fas fa-cart-plus"></i> aggiunge
                                        </button>
                                            <%-- link per creare la nuova --%>
                                        <a class="btn btn-info d-inline-block"
                                           href="${pageContext.request.contextPath}/updateList"><i
                                                class="fas fa-plus"></i> crea una nuova</a>

                                    </div>
                                </form>

                            </c:if>

                            <%-- se utente loggato non ha alcuna lista con il permesso di inserire il prodotto--%>
                            <c:if test="${empty requestScope.addbleLists}">

                                <%-- stampa l'avviso--%>
                                <p class="font-weight-bold">non hai ancora una lista</p>

                                <%-- link per creare la nuova lista--%>
                                <div class="operation mt-3">
                                    <a class="btn btn-info d-inline-block"
                                       href="${pageContext.request.contextPath}/updateList"><i class="fas fa-plus"></i>
                                        crea una nuova</a>
                                </div>

                            </c:if>

                        </c:if>

                    </div>
                </div>
            </div>

            <%-- box-footer  --%>
            <div class="modal-footer">

            </div>

        </div>
    </div>
</div>