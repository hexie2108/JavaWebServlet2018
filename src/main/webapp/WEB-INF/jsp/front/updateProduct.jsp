<%-- 
    pagina per inserire il prodotto privato nella proria lista di spesa
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="custom" uri="/WEB-INF/custom.tld" %>
<%@include file="/WEB-INF/jspf/i18n.jsp"%>

<jsp:include page="/WEB-INF/jsp/front/header.jsp"/>


<div class="update-product -main-section col-12">
        <div class="content">

                <%-- breadcrumb--%>
                <div class="breadcrumbs">
                        <a href="<c:url value="/"/>">
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

                                <form action="<c:url value="/service/updateProductService"/>" method="POST"
                                      enctype="multipart/form-data">

                                        <%--parte di nome --%>
                                        <div class="form-group">
                                                <label for="productName"><i class="fas fa-shopping-basket"></i> <fmt:message key="the name of the product" />:</label>
                                                <input type="text" class="form-control"   id="productName" name="productName" required="required" value="" maxlength="44">
                                        </div>

                                        <%--parte di logo --%>
                                        <div class="form-group">

                                                <label for="prodocutLogo"><i class="far fa-bookmark"></i> <fmt:message key="the product logo" />:</label>
                                                <div class="custom-file mb-3">
                                                        <input type="file" class="custom-file-input" id="prodocutLogo" name="prodocutLogo"
                                                               accept="image/jpeg, image/png, image/gif, image/bmp" required="required">
                                                        <label class="custom-file-label" for="prodocutLogo"><fmt:message key="select file" /></label>
                                                </div>

                                        </div>

                                        <%--parte di categoria --%>
                                        <div class="form-group">
                                                <label for="productCategory"><i class="fas fa-store"></i> <fmt:message key="the category of the product" />:</label>
                                                <select id="type-list" class="form-control custom-select" id="productCategory"
                                                        name="productCategory">
                                                        <%--get tutte le categorie diel prodotto --%>
                                                        <custom:getAllCategoryOfProduct/>

                                                        <%--stampa tutte le categorie del prodotto --%>
                                                        <c:forEach var="categoryOfProduct" items="${categoryProductList}">

                                                                <option value="${categoryOfProduct.id}" }>${categoryOfProduct.name}</option>

                                                        </c:forEach>
                                                </select>
                                        </div>


                                        <%--parte di descrizione --%>
                                        <div class="form-group">
                                                <label for="productDescription"><i class="far fa-file-alt"></i> <fmt:message key="description" />:</label>
                                                <textarea class="form-control" id="productDescription" name="productDescription" rows="5"
                                                          required="required"></textarea>
                                        </div>

                                        <%--parte di immagine --%>
                                        <div class="form-group">
                                                <label for="ProductImg"><i class="far fa-image"></i> <fmt:message key="the image of the product" />:</label>

                                                <div class="custom-file mb-3">
                                                        <input type="file" class="custom-file-input" id="productImg" name="productImg"
                                                               accept="image/jpeg, image/png, image/gif, image/bmp" required="required">
                                                        <label class="custom-file-label" for="productImg"><fmt:message key="select file" /></label>
                                                </div>


                                        </div>

                                        <%--parte di lista di spesa da aggiungere --%>
                                        <div class="form-group">
                                                <label for="listId"><i class="fas fa-list"></i> <fmt:message key="the list to be inserted" />:</label>
                                                <select id="type-list" class="form-control custom-select" id="listId" name="listId">

                                                        <%--stampa tutte le liste di spese con il permesso di inserire il prodotto --%>
                                                        <c:forEach var="shoppingList" items="${requestScope.addbleLists}">
                                                                <option value="${shoppingList.id}">${shoppingList.name}</option>
                                                        </c:forEach>
                                                </select>
                                        </div>


                                        <%--parte di suggerimenti --%>
                                        <div class="form-group">
                                                <label><fmt:message key="only accept files" /> *.jpg, *.png, *.gif, *.bmp</label>
                                        </div>


                                        <div class="form-group">

                                                <button type="submit" class="submit-button-front-page btn btn-info "><fmt:message key="insert" /></button>

                                        </div>

                                </form>

                        </c:if>

                        <%-- se utente non ha neanche una lista con il permesso di inserire il prodotto--%>
                        <c:if test="${empty requestScope.addbleLists}">

                                <div class="emtpy-list">
                                        <%-- stampa l'avviso--%>
                                        <h2 class="mb-3"><fmt:message key="you do not have a list yet to insert the product" /></h2>

                                                <%-- link per creare la nuova lista--%>
                                                <div>
                                                        <a class="btn btn-info d-inline-block" href="<c:url value="/updateList"/>"><i
                                                                        class="fas fa-plus"></i> <fmt:message key="createTheNewList" /></a>
                                                </div>
                                </div>

                        </c:if>


                </div>

        </div>
</div>


<%-- pié di pagina--%>
<jsp:include page="/WEB-INF/jsp/front/footer.jsp"/>
