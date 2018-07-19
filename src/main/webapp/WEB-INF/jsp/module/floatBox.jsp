<%-- 
    Document   : header
    Created on : 2018-7-15, 7:55:07
    Author     : liu
    componente che visualizza la finestra di shopping list sul sito
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="list-button-float-box">
        <button type="button" class="btn btn-info" data-toggle="collapse" data-target="#list-float-box"><i class="fas fa-shopping-cart"></i><br><span>Mia</span><br><span>Lista</span></button>

</div>
<div id="list-float-box" class="collapse fade">
        
        <div class="list-body">
                <button type="button" class="btn btn-info" data-toggle="collapse" data-target="#list-float-box">&times;</button>
                <div class="list-selector">
                        Nome della lista
                        <select>
                                <option>Default list</option>
                        </select>
                </div>
                <div class="list-type-selector">
                        Tipo della lista
                        <select>
                                <option>tipo di lista</option>
                        </select>
                </div>
                <div class="list-manage">
                        manage
                </div>
                <div class="list-content">
                        <ul>
                                <li>1</li>
                                <li>2</li>
                                <li>3</li>
                                <li>4</li>
                        </ul>
                </div>
                
        </div>
</div>
        
