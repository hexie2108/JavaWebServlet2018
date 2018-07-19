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
                <div class="list-selector mb-2">
                        <c:choose>
                                <c:when test="${empty sessionScope.user}">
                                        <!-- utente anonimo-->
                                        <form action="#">
                                                <label  for="select-list" class="d-block">Seleziona la lista:</label>
                                                <select id="select-list" class="form-control custom-select w-50" name="listId">
                                                        <option value="default">Default list</option>
                                                </select>
                                                <div class="d-inline-block w-25">
                                                        <input class="btn btn-info disabled" type="submit" value="cambia"/>
                                                </div>
                                        </form>
                                </c:when>
                                <c:otherwise>
                                        <!-- utente loggato-->
                                        <!-- per memorizzare quale lista è stato scelto si usa un attributo sessione che memorizza list id -->
                                        
                                        
                                </c:otherwise>
                        </c:choose>

                </div>
                <div class="list-type-selector mb-3">
                        <form action="#">
                                <label  for="type-list"  class="d-block">Tipo della lista:</label>
                                <select id="type-list" class="form-control custom-select w-50">
                                        <option>tipo di lista</option>
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
                                                <th>operazione</th>
                                        </tr>
                                </thead>
                                <tbody>
                                        <tr> 
                                                <custom:getElementsOfShppingListByCookie></custom:getElementsOfShppingListByCookie>
                                        </tr>
                                </tbody>
                        </table>
                </div>

        </div>
</div>

