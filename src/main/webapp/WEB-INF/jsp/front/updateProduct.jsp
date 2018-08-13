<%-- 
    pagina per inserire il prodotto privato nella proria lista di spesa
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="custom" uri="/WEB-INF/custom.tld"%>

<jsp:include page="/WEB-INF/jsp/front/header.jsp"/>


<div class="update-product -main-section col-12">
        <div class="content">

                <%-- breadcrumb--%>
                <div class="breadcrumbs">
                        <a href="${pageContext.request.contextPath}">
                                <i class="fas fa-home"></i>
                        </a>
                        <span>&gt;</span>

                        <span>
                                <i class="fas fa-edit"></i> ${head_title}
                        </span>

                </div>

                <div class="update-product mt-3">

                        <%-- se utente ha qualche lista con il permesso di inserire il prodotto--%>
                        <c:if test="${not empty requestScope.addbleLists}">

                                <form action="${pageContext.request.contextPath}/service/updateProductService" method="POST" enctype="multipart/form-data">

                                        <%--parte di nome --%>
                                        <div class="form-group">
                                                <label for="productName"><i class="fas fa-shopping-basket"></i> il nome del prodotto:</label>
                                                <input type="text" class="form-control"   id="productName" name="productName" required="required" value="" maxlength="44">
                                        </div>

                                        <%--parte di logo --%>
                                        <div class="form-group">

                                                <label for="prodocutLogo"><i class="far fa-bookmark"></i> il logo del prodotto:</label>
                                                <div class="custom-file mb-3">
                                                        <input  type="file" class="custom-file-input"  id="prodocutLogo" name="prodocutLogo"  accept="image/jpeg, image/png, image/gif, image/bmp" required="required">
                                                        <label class="custom-file-label" for="prodocutLogo">seleziona file</label>
                                                </div>

                                        </div>

                                        <%--parte di categoria --%>
                                        <div class="form-group">
                                                <label for="productCategory"><i class="fas fa-store"></i> la categoria del prodotto:</label>
                                                <select id="type-list" class="form-control custom-select" id="productCategory" name ="productCategory">
                                                        <%--get tutte le categorie diel prodotto --%>
                                                        <custom:getAllCategoryOfProduct />

                                                        <%--stampa tutte le categorie del prodotto --%>
                                                        <c:forEach var="categoryOfProduct" items="${categoryProductList}">

                                                                <option value="${categoryOfProduct.id}"}>${categoryOfProduct.name}</option>

                                                        </c:forEach>
                                                </select>
                                        </div>


                                        <%--parte di descrizione --%>
                                        <div class="form-group">
                                                <label for="productDescription"><i class="far fa-file-alt"></i> la descrizione del prodotto:</label>
                                                <textarea  class="form-control"   id="productDescription" name="productDescription" rows="5" required="required"></textarea>
                                        </div>

                                        <%--parte di immagine --%>
                                        <div class="form-group">
                                                <label for="ProductImg"><i class="far fa-image"></i> l'immagine del prodotto:</label>

                                                <div class="custom-file mb-3">
                                                        <input  type="file" class="custom-file-input"  id="productImg" name="productImg"  accept="image/jpeg, image/png, image/gif, image/bmp" required="required">
                                                        <label class="custom-file-label" for="productImg">seleziona file</label>
                                                </div>


                                        </div>

                                        <%--parte di lista di spesa da aggiungere --%>
                                        <div class="form-group">
                                                <label for="listId"><i class="fas fa-list"></i> la lista da inserire:</label>
                                                <select id="type-list" class="form-control custom-select" id="listId" name ="listId">

                                                        <%--stampa tutte le liste di spese con il permesso di inserire il prodotto --%>
                                                        <c:forEach var="shoppingList" items="${requestScope.addbleLists}">
                                                                <option value="${shoppingList.id}">${shoppingList.name}</option>
                                                        </c:forEach>
                                                </select>
                                        </div>


                                        <%--parte di suggerimenti --%>
                                        <div class="form-group">
                                                <label>accetta solo file *.jpg, *.png, *.gif, *.bmp</label>
                                        </div>


                                        <div class="form-group">     

                                                <button type="submit" class="btn btn-info w-50">inserisce</button>

                                        </div>

                                </form>

                        </c:if>

                        <%-- se utente non ha neanche una lista con il permesso di inserire il prodotto--%>
                        <c:if test="${empty requestScope.addbleLists}">

                                <div class="emtpy-list"> 
                                        <%-- stampa l'avviso--%>
                                        <h2 class="mb-3">non hai ancora una lista da poter inserire il prodotto</p>

                                        <%-- link per creare la nuova lista--%>
                                        <div>
                                                <a class="btn btn-info d-inline-block" href="${pageContext.request.contextPath}/updateList"><i class="fas fa-plus"></i> crea una nuova lista</a>
                                        </div>
                                </div>
                                        
                        </c:if>


                </div>

        </div>               
</div>


<%-- pié di pagina--%>
<jsp:include page="/WEB-INF/jsp/front/footer.jsp"/>
