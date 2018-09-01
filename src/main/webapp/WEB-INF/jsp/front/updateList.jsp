<%-- 
    pagina per creare/modificare la lista 
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="custom" uri="/WEB-INF/custom.tld" %>
<%@include file="/WEB-INF/jspf/i18n.jsp"%>

<jsp:include page="/WEB-INF/jsp/front/header.jsp"/>


<div class="updatelist-main-section col-12">
        <div class="content">

                <%-- breadcrumb--%>
                <div class="breadcrumbs">
                        <a href="<c:url value="/"/>">
                                <i class="fas fa-home"></i>
                        </a>
                        <span>&gt;</span>
                        <a href="<c:url value="/mylists"/>">
                                <i class="fas fa-list"></i> <fmt:message key="my lists" />
                        </a>
                        <span>&gt;</span>
                        <span>
                                <i class="fas fa-edit"></i> ${head_title} ${not empty list?list.name:""}
                        </span>

                </div>

                <div class="updatelist mt-3">

                        <form action="<c:url value="/service/updateListService"/>" method="POST"
                              enctype="multipart/form-data">

                                <%--parte di nome --%>
                                <div class="form-group">
                                        <label for="listName"><i class="fas fa-info-circle"></i> <fmt:message key="the name of the list" />:</label>
                                        <input type="text" class="form-control"   id="listName" name="listName" required="required" value="${not empty list?list.name:""}" maxlength="44">
                                </div>

                                <%--parte di categoria --%>
                                <div class="form-group">
                                        <label for="listCategory"><i class="fas fa-sitemap"></i> <fmt:message key="the category of the list" />:</label>
                                        <select id="type-list" class="form-control custom-select" id="listCategory" name="listCategory">
                                                <%--get tutte le categorie di lista --%>
                                                <custom:getAllCategoryOfShoppingList/>

                                                <%--stampa tutte le categorie di lista --%>
                                                <c:forEach var="categoryOfShoppingList" items="${categoryList}">

                                                        <option value="${categoryOfShoppingList.id}"
                                                                } ${not empty list && list.categoryList == categoryOfShoppingList.id ?"selected=\"selected\"":""}>${categoryOfShoppingList.name}</option>

                                                </c:forEach>
                                        </select>
                                </div>

                                <%--parte di descrizione --%>
                                <div class="form-group">
                                        <label for="listDescription"><i class="far fa-file-alt"></i> <fmt:message key="description" />:</label>
                                        <textarea class="form-control" id="listDescription" name="listDescription" rows="5"
                                                  required="required">${not empty list?list.description:""}</textarea>
                                </div>

                                <%--parte di immagine --%>
                                <div class="form-group">
                                        <label for="listImg"><i class="far fa-image"></i> <fmt:message key="the image" />:</label>

                                        <%--se è in caso di update --%>
                                        <c:if test="${not empty list}">
                                                <div class="custom-file-input-old-image mb-3">
                                                        <%--stampa l'immagine vecchia --%>
                                                        <img class="img-fluid" src="<c:url value="/image/list/${list.img}"/>"
                                                             alt="<fmt:message key="the category image" />"/>
                                                </div>
                                        </c:if>

                                        <div class="custom-file mb-3">
                                                <input type="file" class="custom-file-input" id="listImg" name="listImg"
                                                       accept="image/jpeg, image/png, image/gif, image/bmp" ${empty list?"required=\"required\"":""}>
                                                <label class="custom-file-label" for="listImg"><fmt:message key="select file" /></label>
                                        </div>

                                </div>

                                <%--parte di suggerimenti --%>
                                <div class="form-group">
                                        <label><fmt:message key="only accept files" /> *.jpg, *.png, *.gif, *.bmp</label>
                                </div>

                                <div class="form-group">
                                        <c:if test="${not empty list}">
                                                <input type="hidden" name="action" value="update"/>
                                                <input type="hidden" name="listId" value="${list.id}"/>
                                                <button class="submit-button-front-page btn btn-info" type="submit" ><fmt:message key="update" /></button>
                                        </c:if>
                                        <c:if test="${empty list}">
                                                <input type="hidden" name="action" value="insert"/>
                                                <button class="ubmit-button-front-page btn btn-info" type="submit" ><fmt:message key="create" /></button>
                                        </c:if>
                                </div>

                        </form>

                </div>

        </div>
</div>


<%-- pié di pagina--%>
<jsp:include page="/WEB-INF/jsp/front/footer.jsp"/>
