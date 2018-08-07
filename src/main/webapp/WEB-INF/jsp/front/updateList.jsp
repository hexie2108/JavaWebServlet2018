<%-- 
    pagina per creare/modificare la lista 
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="custom" uri="/WEB-INF/custom.tld"%>

<jsp:include page="/WEB-INF/jsp/front/header.jsp"/>


<div class="updatelist-main-section col-12">
        <div class="content">

                <%-- breadcrumb--%>
                <div class="breadcrumbs">
                        <a href="${pageContext.request.contextPath}">
                                <i class="fas fa-home"></i>
                        </a>
                        <span>&gt;</span>
                        <a href="${pageContext.request.contextPath}/mylists">
                                <i class="fas fa-list"></i> le mie liste
                        </a>
                        <span>&gt;</span>
                        <span>
                                <i class="fas fa-edit"></i> ${head_title} ${not empty list?list.name:""}
                        </span>

                </div>

                <div class="updatelist mt-3">

                        <form action="${pageContext.request.contextPath}/service/updateListService" method="POST" enctype="multipart/form-data">

                                <%--parte di nome --%>
                                <div class="form-group">
                                        <label for="listName"><i class="fas fa-info-circle"></i> il nome della lista:</label>
                                        <input type="text" class="form-control"   id="listName" name="listName" required="required" value="${not empty list?list.name:""}">
                                </div>

                                <%--parte di categoria --%>
                                <div class="form-group">
                                        <label for="listCategory"><i class="fas fa-sitemap"></i> la categoria della lista:</label>
                                        <select id="type-list" class="form-control custom-select" id="listCategory" name ="listCategory">
                                                <%--get tutte le categorie di lista --%>
                                                <custom:getAllCategoryOfShoppingList />

                                                <%--stampa tutte le categorie di lista --%>
                                                <c:forEach var="categoryOfShoppingList" items="${categoryList}">

                                                        <option value="${categoryOfShoppingList.id}"} ${not empty list && list.categoryList == categoryOfShoppingList.id ?"selected=\"selected\"":""}>${categoryOfShoppingList.name}</option>

                                                </c:forEach>
                                        </select>
                                </div>

                                <%--parte di descrizione --%>
                                <div class="form-group">
                                        <label for="listName"><i class="far fa-file-alt"></i> la descrizione della lista:</label>
                                        <textarea  class="form-control"   id="listDescription" name="listDescription" rows="5" required="required">${not empty list?list.description:""}</textarea>
                                </div>

                                <%--parte di immagine --%>
                                <div class="form-group">
                                        <label for="listImg"><i class="far fa-image"></i> l'immagine della lista:</label>

                                        <%--se è in caso di update --%>
                                        <c:if test="${not empty list}">
                                                <div class="custom-file-input-old-image mb-3">
                                                        <%--stampa l'immagine vecchia --%>
                                                        <img class="img-fluid" src="${pageContext.request.contextPath}/image/list/${list.img}"alt="l'immagine di categoria"/>
                                                </div>
                                        </c:if>

                                        <div class="custom-file mb-3">
                                                <input  type="file" class="custom-file-input"  id="listImg" name="listImg"  accept="image/jpeg, image/png, image/gif, image/bmp" ${empty list?"required=\"required\"":""}>
                                                <label class="custom-file-label" for="listImg">seleziona file</label>
                                        </div>

                                </div>

                                <%--parte di suggerimenti --%>
                                <div class="form-group">
                                        <label>accetta solo file *.jpg, *.png, *.gif, *.bmp</label>
                                </div>

                                <div class="form-group">     
                                        <c:if test="${not empty list}">
                                                <input type="hidden" name="action" value="update"/>
                                                <input type="hidden" name="listId" value="${list.id}"/>
                                                <button type="submit" class="btn btn-info w-50">aggiorna</button>
                                        </c:if>
                                        <c:if test="${empty list}">
                                                <input type="hidden" name="action" value="insert"/>
                                                <button type="submit" class="btn btn-info w-50">crea</button>
                                        </c:if>
                                </div>

                        </form>

                </div>

        </div>               
</div>


<%-- pié di pagina--%>
<jsp:include page="/WEB-INF/jsp/front/footer.jsp"/>
