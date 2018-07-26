<%-- 
    Document   : header
    Created on : 2018-7-15, 7:55:07
    Author     : liu
    la finestrina per visualizzare il prodotto dalla lista di spesa
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="custom" uri="/WEB-INF/custom.tld"%>


<!-- finestrina -->
<div class="modal fade custom-modal" id="boxShowItem">
        <div class="modal-dialog">
                <div class="modal-content">

                        <!-- box-head -->
                        <div class="modal-header">
                                <h4 class="modal-title">il nome prodotto</h4>
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                        </div>

                        <!-- box-body -->
                        <div class="modal-body">
                                <div class="left-body">
                                        <img class="item-img img-fluid" alt="item-name" />
                                        <div class="item-logo text-center mt-2 mb-2">
                                                <p class="font-weight-bold">
                                                        logo:
                                                </p>
                                                <img class="item-logo-img img-fluid w-50 "  alt="logo" />
                                        </div>
                                </div>

                                <div class="right-body">
                                        <div class="item-information">


                                                <div class="item-cat">
                                                        <p class="font-weight-bold">
                                                                categoria:
                                                        </p>
                                                        <p>
                                                                <a class="item-cat-link" href="#" >nome della categoria</a>
                                                        </p>
                                                </div>
                                                <div class="item-description">
                                                        <p class="font-weight-bold">
                                                                descrizione:
                                                        </p>
                                                        <p class="item-description-text">
                                                                la descrizione del prodotto
                                                        </p>
                                                </div>

                                        </div>

                                        <div class="add-box">
                                                <!-- se è un utente anonimo-->
                                                <c:if test="${empty sessionScope.user}">
                                                        <form action="${pageContext.request.contextPath}/service/updateItemInListUnloggedUserOnlyService" method="GET">


                                                                <input class="productIdFromList" type="hidden" name="productId" value="1"/>
                                                                <input type="hidden" name="action" value="delete"/>
                                                                <input class="btn btn-info d-inline-block" type="submit" value="elimina" />


                                                        </form>
                                                </c:if>       
                                                <!-- se è un utente loggato--> 
                                                <c:if test="${not empty sessionScope.user}">
                                                        <div class="formToBuy d-inline-block">
                                                                <form action="${pageContext.request.contextPath}/service/updateItemInListService" method="GET">


                                                                        <input class="productIdFromList" type="hidden" name="productId" value="1"/>
                                                                        <input class="listIdFromList" type="hidden" name="listId" />
                                                                        <input type="hidden" name="action" value="bought"/>
                                                                        <input class="submit-button btn btn-info " style="" type="submit" value="comprato" />
                                                                </form>
                                                        </div>
                                                        <div class="formToDelete d-inline-block">
                                                                <form action="${pageContext.request.contextPath}/service/updateItemInListService" method="GET">


                                                                        <input class="productIdFromList" type="hidden" name="productId" value="1"/>
                                                                        <input class="listIdFromList" type="hidden" name="listId" />
                                                                        <input type="hidden" name="action" value="delete"/>
                                                                        <input class="submit-button btn btn-danger " style="" type="submit" value="elimina" />

                                                                </form>
                                                        </div>


                                                </c:if>  
                                        </div>               
                                </div>
                        </div>
                        <!-- box-footer  -->
                        <div class="modal-footer">

                        </div>

                </div>
        </div>
</div>