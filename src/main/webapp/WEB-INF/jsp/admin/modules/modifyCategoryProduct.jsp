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
                                       name="${CategoryProductValidator.IMG_KEY}"
                                       accept=".jpg, .jpeg, .bmp, .gif, .png"
                                >
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
<script>
    function initCategoryProductModal(table){
        const modalUrlJson = '<c:url value="/admin/categoryProducts.json"/>';

        const categoryProductModal = $('#categoryProductModal');
        const categoryProductForm = $('#categoryProductForm', categoryProductModal);

        categoryProductModal.on("show.bs.modal", function (e) {
            // Remove old validation messages
            validationUtils.clearVerifyMessages(categoryProductForm);

            // Reset inputs
            categoryProductForm[0].reset();
            categoryProductForm.find('input').val("");
            categoryProductForm.find('input[type="text"]').html("");

            // Get type of action and data
            const action = $(e.relatedTarget).data('action');
            const data = $(e.relatedTarget).closest('tr').data('json');

            let titleLabel;
            let submitLabel;

            // set data
            const realData = {};
            if (action === 'modify') {
                realData.id = data.id;
                realData.name = data.name;
                realData.description = data.description;
                realData.img = data.img;

                submitLabel = "<fmt:message key="categoryProducts.label.modifyForm.submit"/>";
                titleLabel = "<fmt:message key="categoryProducts.label.modifyForm.title"/>";
            } else if (action === 'create') {
                realData.id = '';
                realData.name = '';
                realData.description = '';
                realData.img = undefined;

                submitLabel = "<fmt:message key="categoryProducts.label.createForm.submit"/>";
                titleLabel = "<fmt:message key="categoryProducts.label.createForm.title"/>";
            } else {
                console.error("Action not recognized");
                return;
            }

            // Set title and submit label
            $('#categoryProductFormSub', categoryProductModal).html(submitLabel);
            $('.modal-title', categoryProductModal).html(titleLabel);

            // Set action value
            $('input[name="action"]', categoryProductForm).val(action);

            // Set input values/placeholders
            $('input[name="id"]', categoryProductForm).val(realData.id);
            $('input[name="${CategoryProductValidator.NAME_KEY}"]', categoryProductForm).attr('placeholder', realData.name);
            $('textarea[name="${CategoryProductValidator.DESCRIPTION_KEY}"]', categoryProductForm).attr('placeholder', realData.description);

            const prefix = "<c:url value="/${pageContext.servletContext.getInitParameter('categoryProductImgsFolder')}/"/>";

            // Set image source
            const divImg1 = $('#divImg', categoryProductForm);
            divImg1.find('img').attr("src", realData.img !== undefined? prefix + realData.img: "");

            // Trigger validation using event change
            $('input[name="${CategoryProductValidator.NAME_KEY}"]', categoryProductForm).trigger('change');

        });


        const isCreate = (form, name) => {return form.find('[name="action"]').val() === 'create';};

        const checkFile = validationUtils.validateFile(
            /.*(jpg|jpeg|png|gif|bmp).*/,
            ${CategoryProductValidator.IMG_MAX_SIZE},
            isCreate, {
                fileEmptyOrNull: "<fmt:message key="validateCategoryProduct.errors.Img.FILE_EMPTY_OR_NULL"/>",
                fileTooBig: "<fmt:message key="validateCategoryProduct.errors.Img.FILE_TOO_BIG"/>",
                fileContentTypeMissingOrType: "<fmt:message key="validateCategoryProduct.errors.Img.FILE_CONTENT_TYPE_MISSING_OR_EMPTY"/>",
                fileOfWrongType: "<fmt:message key="validateCategoryProduct.errors.Img.FILE_OF_WRONG_TYPE"/>",
            }
        );

        const checkName = validationUtils.validateString(
            ${CategoryProductValidator.NAME_MAX_LEN},
            isCreate, {
                emptyOrNull: "<fmt:message key="validateCategoryProduct.errors.Name.STRING_EMPTY_OR_NULL"/>",
                tooLong: "<fmt:message key="validateCategoryProduct.errors.Name.STRING_TOO_LONG"/>",
            }
        );

        const checkDescription = validationUtils.validateString(
            ${CategoryProductValidator.DESCRIPTION_MAX_LEN},
            isCreate, {
                emptyOrNull: "<fmt:message key="validateCategoryProduct.errors.Description.STRING_EMPTY_OR_NULL"/>",
                tooLong: "<fmt:message key="validateCategoryProduct.errors.Description.STRING_TOO_LONG"/>",
            }
        );

        $.each(categoryProductForm.find('.file-input'), function (index, value) {
            const k = $(value);
            k.find('+ div').find('>button.clear-btn').click(function () {
                k.val("");
            });
        });

        categoryProductForm.find('button.clear-btn, button.del-btn, button.ins-btn').click(function () {
            // Trigger update
            $('input[name="${CategoryProductValidator.NAME_KEY}"]', categoryProductForm).trigger('change');
        });

        function validate() {
            const obj = {};

            checkName(obj, categoryProductForm, "${CategoryProductValidator.NAME_KEY}");
            checkDescription(obj, categoryProductForm, "${CategoryProductValidator.DESCRIPTION_KEY}");
            checkFile(obj, categoryProductForm, '${CategoryProductValidator.IMG_KEY}');

            return validationUtils.updateVerifyMessages(categoryProductForm, obj);
        }

        formUtils.timedChange(categoryProductForm, validate);

        categoryProductForm.submit(function (e) {
            e.preventDefault();

            if (validate()) {
                validationUtils.formSubmitWithValidation(
                    modalUrlJson,
                    categoryProductForm, {
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
                            categoryProductModal.modal('toggle');
                        }
                    }
                );
            }
        });
    }
</script>
