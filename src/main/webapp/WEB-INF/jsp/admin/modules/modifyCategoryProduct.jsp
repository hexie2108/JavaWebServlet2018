<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="it.unitn.webprogramming18.dellmm.util.CategoryProductValidator" %>

<div class="modal fade" id="categoryProductModal">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"></h4>
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="<fmt:message key="generic.label.close"/>">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="categoryProductForm" method="post" enctype="multipart/form-data">
                    <input class="form-control" type="hidden" name="id" value=""/>
                    <input class="form-control" type="hidden" name="action" value="modify"/>
                    <div class="form-group row">
                        <div class="col-sm-12">
                            <label class="sr-only" for="input${CategoryProductValidator.NAME_KEY}"><fmt:message
                                    key="categoryProduct.label.name"/></label>
                            <div class="input-group">
                                <div class="input-group-prepend"><span class="input-group-text"><fmt:message
                                        key="categoryProduct.label.name"/></span></div>
                                <input id="input${CategoryProductValidator.NAME_KEY}" class="form-control" placeholder=""
                                       autofocus="" type="text" name="${CategoryProductValidator.NAME_KEY}">
                            </div>
                            <div class="error-messages">
                                <p id="span${CategoryProductValidator.NAME_KEY}"></p>
                            </div>
                        </div>
                    </div>

                    <div class="form-group row">
                        <div class="col-sm-12">
                            <label for="input${CategoryProductValidator.DESCRIPTION_KEY}" class="sr-only"><fmt:message
                                    key="categoryProduct.label.description"/></label>
                            <div class="input-group">
                                <div class="input-group-prepend"><span class="input-group-text"><fmt:message
                                        key="categoryProduct.label.description"/></span></div>
                                <textarea id="input${CategoryProductValidator.DESCRIPTION_KEY}" class="form-control"
                                          name="${CategoryProductValidator.DESCRIPTION_KEY}" rows="4"></textarea>
                            </div>
                            <div class="error-messages">
                                <p id="span${CategoryProductValidator.DESCRIPTION_KEY}"></p>
                            </div>
                        </div>
                    </div>

                    <div class="form-group row" id="divImg">
                        <div class="col-sm-2">
                            <img class="mx-auto img-fluid"/>
                        </div>
                        <div class="col-sm-10 my-auto">
                            <div class="input-group">
                                <div class="input-group-prepend"><label class="input-group-text"
                                                                        for="input${CategoryProductValidator.IMG_KEY}"><fmt:message
                                        key="categoryProduct.label.logo"/></label></div>
                                <input type="file" class="file-input form-control"
                                       id="input${CategoryProductValidator.IMG_KEY}"
                                       name="${CategoryProductValidator.IMG_KEY}">
                                <div class="input-group-append">
                                    <button class="btn btn-outline-secondary input-group-text clear-btn" type="button">
                                        &times;
                                    </button>
                                </div>
                            </div>
                            <div class="error-messages">
                                <p id="span${CategoryProductValidator.IMG_KEY}"></p>
                            </div>
                        </div>
                    </div>
                </form>

                <div class="alert d-none" id="id-modal-res">
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal"><fmt:message
                        key="generic.label.close"/></button>
                <button type="submit" form="categoryProductForm" class="btn btn-primary"
                        id="categoryProductFormSub"></button>
            </div>
        </div>
    </div>
</div>

