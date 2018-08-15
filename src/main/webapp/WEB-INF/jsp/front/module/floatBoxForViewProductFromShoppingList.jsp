<%-- 
    la finestra per visualizzare il prodotto dalla lista di spesa
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="custom" uri="/WEB-INF/custom.tld" %>


<!-- finestrina -->
<div class="modal fade custom-modal" id="boxShowItem">
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


                            <%--  form per eliminare il prodotto dalla cookie --%>
                            <form action="<c:url value="/service/updateItemInListUnloggedUserOnlyService"/>"
                                  method="GET">

                                <input class="productIdFromList" type="hidden" name="productId" value="1"/>
                                <input type="hidden" name="action" value="delete"/>
                                <button class="submit-button btn btn-danger" type="submit">
                                    <i class="fa fa-ban"></i> elimina
                                </button>
                            </form>


                        </c:if>

                        <%-- se è un utente loggato--%>
                        <c:if test="${not empty sessionScope.user}">

                            <%-- form per segna il prodotto come già comprato--%>
                            <div class="formToBuy">

                                <form action="<c:url value="/service/updateItemInListService"/>"
                                      method="GET">

                                    <input class="productIdFromList" type="hidden" name="productId" value="1"/>
                                    <input class="listIdFromList" type="hidden" name="listId"/>
                                    <input type="hidden" name="action" value="bought"/>

                                    <button class="submit-button btn btn-info" type="submit">
                                        <i class="fas fa-check"></i> comprato
                                    </button>
                                </form>

                            </div>

                            <%-- form per segna il prodotto come già comprato--%>
                            <div class="formToDelete">

                                <form action="<c:url value="/service/updateItemInListService"/>"
                                      method="GET">

                                    <input class="productIdFromList" type="hidden" name="productId" value="1"/>
                                    <input class="listIdFromList" type="hidden" name="listId"/>
                                    <input type="hidden" name="action" value="delete"/>
                                    <button class="submit-button btn btn-danger" type="submit">
                                        <i class="fa fa-ban"></i> elimina
                                    </button>
                                </form>

                            </div>


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