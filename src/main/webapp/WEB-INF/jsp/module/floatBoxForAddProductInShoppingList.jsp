<%-- 
    Document   : header
    Created on : 2018-7-15, 7:55:07
    Author     : liu
    la finestrina per aggiungere prodotto in shopping
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!-- finestrina -->
<div class="modal fade" id="boxAddItem">
        <div class="modal-dialog">
                <div class="modal-content">

                        <!-- box-head -->
                        <div class="modal-header">
                                <h4 class="modal-title">Aggiunge</h4>
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                        </div>

                        <!-- box-body -->
                        <div class="modal-body">
                                <!-- utente anonimo-->
                                <form action="#">
                                        <label  for="select-list" class="d-block">Seleziona la lista:</label>
                                        <select id="select-list" class="form-control custom-select w-50">
                                                <option>Default list</option>
                                        </select>
                                        <div class="d-inline-block w-25">
                                                <input class="btn btn-info disabled" type="submit" value="aggiunge"/>
                                        </div>
                                </form>
                        </div>

                        <!-- box-footer  -->
                        <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">chiude</button>
                        </div>

                </div>
        </div>
</div>