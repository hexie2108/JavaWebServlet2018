

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.CategoryListValidator" %>
<%@include file="/WEB-INF/jspf/i18n.jsp"%>


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
<script>
    function initCategoryListModal(table) {
        const categoryListModal = $('#modifyCategoryListModal');
        const categoryListForm = categoryListModal.find('#modifyCategoryListForm');
        const modalUrlJson = '<c:url value="/admin/categoryLists.json"/>';

        categoryListModal.on('show.bs.modal', function (e) {
            // Clear validation messages
            validationUtils.clearVerifyMessages(categoryListForm);

            // Reset values
            categoryListForm[0].reset();
            categoryListForm.find('input').val("");
            categoryListForm.find('input[type="text"]').html("");

            // Get action from the data-action in the clicked button
            const action = $(e.relatedTarget).data('action');

            // Get data-json from the row that contains the clicked button
            const data = $(e.relatedTarget).closest('tr').data('json');

            let titleLabel;
            let submitLabel;

            // Set values
            const realData = {};
            if (action === 'modify') {
                realData.id = data.id;
                realData.name = data.name;
                realData.description = data.description;
                realData.img1 = data.img1;
                realData.img2 = data.img2;
                realData.img3 = data.img3;

                titleLabel = "<fmt:message key="categoryLists.label.modifyForm.title"/>";
                submitLabel = "<fmt:message key="categoryLists.label.modifyForm.submit"/>";

            } else if (action === 'create' || action === 'reset') {
                realData.id = '';
                realData.name = '';
                realData.description = '';
                realData.img1 = undefined;
                realData.img2 = undefined;
                realData.img3 = undefined;

                titleLabel = "<fmt:message key="categoryLists.label.createForm.title"/>";
                submitLabel = "<fmt:message key="categoryLists.label.createForm.submit"/>";
            } else {
                console.error("Action not recognized");
                return;
            }

            // Set text of title and submit button
            $('#modifyCategoryListFormSub', categoryListModal).html(submitLabel);
            $('.modal-title', categoryListModal).html(titleLabel);

            // Set action to do
            $('input[name="action"]', categoryListForm).val(action);

            // Set values
            $('input[name="id"]', categoryListForm).val(realData.id);
            $('input[name="${CategoryListValidator.NAME_KEY}"]', categoryListForm).attr('placeholder', realData.name);
            $('textarea[name="${CategoryListValidator.DESCRIPTION_KEY}"]', categoryListForm).attr('placeholder', realData.description);


            const prefix = "<c:url value="/${pageContext.servletContext.getInitParameter('categoryListImgsFolder')}/"/>";

            // Get images
            const divImg1 = $('#divImg1', categoryListForm);
            const divImg2 = $('#divImg2', categoryListForm);
            const divImg3 = $('#divImg3', categoryListForm);

            // Set source of the image(or empty image if img is undefined)
            $('img', divImg1).attr("src", realData.img1 !== undefined? prefix + realData.img1: "");
            $('img', divImg2).attr("src", realData.img2 !== undefined? prefix + realData.img2: "");
            $('img', divImg3).attr("src", realData.img3 !== undefined? prefix + realData.img3: "");

            const m = {
                'img2': $('> div > .input-group', divImg2),
                'img3': $('> div > .input-group', divImg3)
            };

            $.each(m, function (key, value) {
                if (realData[key]) {
                    value.find('> .input-group-append, > input').show();
                    value.find('> button.ins-btn').hide();
                } else {
                    value.find('> .input-group-append, > input').hide();
                    value.find('> button.ins-btn').show();
                }
            });

            // Trigger validation using event change
            $('input[name="${CategoryListValidator.NAME_KEY}"]', categoryListForm).trigger('change');

        });

        const isCreate = (form, name) => {
            return form.find('[name="action"]').val() === 'create';
        };

        const requiredImg = (form, name) => {
            return form.find('[name="action"]').val() === 'create' && name === '${CategoryListValidator.IMG1_KEY}';
        };

        const checkFiles = validationUtils.validateFile(
            /.*(jpg|jpeg|png|gif|bmp).*/,
            ${CategoryListValidator.MAX_LEN_FILE},
            requiredImg, {
                fileEmptyOrNull: "<fmt:message key="validateCategoryList.errors.IMG_MISSING"/>",
                fileTooBig: "<fmt:message key="validateCategoryList.errors.IMG_TOO_BIG"/>",
                fileContentTypeMissingOrType: "<fmt:message key="validateCategoryList.errors.IMG_CONTENT_TYPE_MISSING"/>",
                fileOfWrongType: "<fmt:message key="validateCategoryList.errors.IMG_NOT_IMG"/>",
            }
        );

        const checkName = validationUtils.validateString(
            ${CategoryListValidator.NAME_MAX_LEN},
            isCreate, {
                emptyOrNull: "<fmt:message key="validateCategoryList.errors.NAME_MISSING"/>",
                tooLong: "<fmt:message key="validateCategoryList.errors.NAME_TOO_LONG"/>",
            }
        );

        const checkDescription = validationUtils.validateString(
            ${CategoryListValidator.DESCRIPTION_MAX_LEN},
            isCreate, {
                emptyOrNull: "<fmt:message key="validateCategoryList.errors.DESCRIPTION_MISSING"/>",
                tooLong: "<fmt:message key="validateCategoryList.errors.DESCRIPTION_TOO_LONG"/>",
            }
        );

        $.each(categoryListForm.find('.file-input'), function (index, value) {
            const k = $(value);
            k.find('+ div').find('>button.clear-btn').click(function () {
                k.val("");
            });
        });

        categoryListForm.find('.input-group button.ins-btn').click(function () {
            const iGr = $(this).parent();

            $('> .input-group-append, > input', iGr).show();
            $('> button.ins-btn', iGr).hide();

            iGr.parent().find('input[type="hidden"]').val("");
        });

        categoryListForm.find('.input-group button.del-btn').click(function () {
            const iGr = $(this).parent().parent();

            $('> .input-group-append, > input', iGr).val("");
            $('> .input-group-append, > input', iGr).hide();
            $('> button.ins-btn', iGr).show();

            iGr.parent().find('input[type="hidden"]').val("delete");
        });

        categoryListForm.find('button.clear-btn, button.del-btn, button.ins-btn').click(function () {
            // Trigger update
            $('input[name="${CategoryListValidator.NAME_KEY}"]', categoryListForm).trigger('change');
        });


        function validate(){
            const obj = {};

            checkName(obj, categoryListForm, "${CategoryListValidator.NAME_KEY}");
            checkDescription(obj, categoryListForm, "${CategoryListValidator.DESCRIPTION_KEY}");

            checkFiles(obj, categoryListForm, '${CategoryListValidator.IMG1_KEY}');
            checkFiles(obj, categoryListForm, '${CategoryListValidator.IMG2_KEY}');
            checkFiles(obj, categoryListForm, '${CategoryListValidator.IMG3_KEY}');

            return validationUtils.updateVerifyMessages(categoryListForm, obj)
        }

        formUtils.timedChange(categoryListForm, validate);

        categoryListForm.submit(function (e) {
            e.preventDefault();

            if (validate()) {
                validationUtils.formSubmitWithValidation(
                    modalUrlJson,
                    categoryListForm, {
                        multipart: true,
                        session: true,
                        redirectUrl: null,
                        successAlert: {
                            title: "<fmt:message key="generic.label.successTitle"/>",
                            message: "<fmt:message key="generic.text.success"/>",
                            closeLabel: "<fmt:message key="generic.label.close"/>"
                        },
                        failAlert: {
                            title: "<fmt:message key="generic.label.errorTitle"/>",
                            message: "<fmt:message key="generic.errors.unknownError"/>",
                            closeLabel: "<fmt:message key="generic.label.close"/>"
                        },
                        successCallback: function () {
                            table.ajax.reload();
                            table.draw();
                            categoryListModal.modal('toggle');
                        }
                    }
                );
            }
        });
    }
</script>
