<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="it.unitn.webprogramming18.dellmm.util.CategoryListValidator" %>

<div class="modal fade" id="modifyCategoryListModal">
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
                <form id="modifyCategoryListForm" method="post" enctype="multipart/form-data">
                    <input class="form-control" type="hidden" name="id" value=""/>
                    <input class="form-control" type="hidden" name="action" value="modify"/>
                    <div class="form-group row">
                        <div class="col-sm-12">
                            <label class="sr-only" for="input${CategoryListValidator.NAME_KEY}"><fmt:message
                                    key="categoryList.label.name"/></label>
                            <div class="input-group">
                                <div class="input-group-prepend"><span class="input-group-text"><fmt:message
                                        key="categoryList.label.name"/></span></div>
                                <input id="input${CategoryListValidator.NAME_KEY}" class="form-control" placeholder=""
                                       autofocus="" type="text" name="${CategoryListValidator.NAME_KEY}">
                            </div>
                            <div class="error-messages">
                                <p id="span${CategoryListValidator.NAME_KEY}"></p>
                            </div>
                        </div>
                    </div>

                    <div class="form-group row">
                        <div class="col-sm-12">
                            <label for="input${CategoryListValidator.DESCRIPTION_KEY}" class="sr-only"><fmt:message
                                    key="categoryList.label.description"/></label>
                            <div class="input-group">
                                <div class="input-group-prepend"><span class="input-group-text"><fmt:message
                                        key="categoryList.label.description"/></span></div>
                                <textarea id="input${CategoryListValidator.DESCRIPTION_KEY}" class="form-control"
                                          name="${CategoryListValidator.DESCRIPTION_KEY}" rows="4"></textarea>
                            </div>
                            <div class="error-messages">
                                <p id="span${CategoryListValidator.DESCRIPTION_KEY}"></p>
                            </div>
                        </div>
                    </div>

                    <div class="form-group row" id="divImg1">
                        <div class="col-sm-2">
                            <img class="mx-auto img-fluid"/>
                        </div>
                        <div class="col-sm-10 my-auto">
                            <div class="input-group">
                                <div class="input-group-prepend"><label class="input-group-text"
                                                                        for="input${CategoryListValidator.IMG1_KEY}"><fmt:message
                                        key="categoryList.label.img1"/></label></div>
                                <input type="file" class="file-input form-control"
                                       id="input${CategoryListValidator.IMG1_KEY}"
                                       name="${CategoryListValidator.IMG1_KEY}">
                                <div class="input-group-append">
                                    <button class="btn btn-outline-secondary input-group-text clear-btn" type="button">
                                        &times;
                                    </button>
                                </div>
                            </div>
                            <div class="error-messages">
                                <p id="span${CategoryListValidator.IMG1_KEY}"></p>
                            </div>
                        </div>
                    </div>

                    <div class="form-group row" id="divImg2">
                        <div class="col-sm-2">
                            <img class="mx-auto img-fluid"/>
                        </div>
                        <div class="col-sm-10 my-auto">
                            <input type="hidden" name="delete${CategoryListValidator.IMG2_KEY}">
                            <div class="input-group">
                                <div class="input-group-prepend"><label class="input-group-text"
                                                                        for="input${CategoryListValidator.IMG2_KEY}"><fmt:message
                                        key="categoryList.label.img2"/></label></div>
                                <button class="btn btn-primary ins-btn" type="button"><fmt:message
                                        key="categoryLists.label.insertImage"/></button>
                                <input type="file" class="file-input form-control"
                                       id="input${CategoryListValidator.IMG2_KEY}"
                                       name="${CategoryListValidator.IMG2_KEY}">
                                <div class="input-group-append">
                                    <button class="btn btn-outline-secondary input-group-text clear-btn" type="button">
                                        &times;
                                    </button>
                                </div>
                                <div class="input-group-append">
                                    <button class="btn btn-danger input-group-text del-btn" type="button"><i
                                            class="fas fa-trash-alt"></i></button>
                                </div>
                            </div>
                            <div class="error-messages">
                                <p id="span${CategoryListValidator.IMG2_KEY}"></p>
                            </div>
                        </div>
                    </div>

                    <div class="form-group row" id="divImg3">
                        <div class="col-sm-2">
                            <img class="mx-auto img-fluid"/>
                        </div>
                        <div class="col-sm-10 my-auto">
                            <input type="hidden" name="delete${CategoryListValidator.IMG3_KEY}">
                            <div class="input-group">
                                <div class="input-group-prepend"><label class="input-group-text"
                                                                        for="input${CategoryListValidator.IMG3_KEY}"><fmt:message
                                        key="categoryList.label.img3"/></label></div>
                                <button class="btn btn-primary ins-btn" type="button"><fmt:message
                                        key="categoryLists.label.insertImage"/></button>
                                <input type="file" class="file-input form-control"
                                       id="input${CategoryListValidator.IMG3_KEY}"
                                       name="${CategoryListValidator.IMG3_KEY}">
                                <div class="input-group-append">
                                    <button class="btn input-group-text clear-btn" type="button">&times;</button>
                                </div>
                                <div class="input-group-append">
                                    <button class="btn btn-danger input-group-text del-btn" type="button"><i
                                            class="fas fa-trash-alt"></i></button>
                                </div>
                            </div>
                            <div class="error-messages">
                                <p id="span${CategoryListValidator.IMG3_KEY}"></p>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal"><fmt:message
                        key="generic.label.close"/></button>
                <button type="submit" form="modifyCategoryListForm" class="btn btn-primary"
                        id="modifyCategoryListFormSub"></button>
            </div>
        </div>
    </div>
</div>

