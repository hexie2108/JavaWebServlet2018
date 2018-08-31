<%@ page import="it.unitn.webprogramming18.dellmm.util.CategoryProductValidator" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.ProductValidator" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="custom" uri="/WEB-INF/custom.tld" %>

<div class="modal fade" id="productModal">
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
                <form id="productForm" method="post" enctype="multipart/form-data">
                    <input class="form-control" type="hidden" name="id" value=""/>
                    <input class="form-control" type="hidden" name="action" value=""/>
                    <div class="form-group row">
                        <div class="col-sm-12">
                            <label class="sr-only" for="input${ProductValidator.NAME_KEY}"><fmt:message
                                    key="product.label.name"/></label>
                            <div class="input-group">
                                <div class="input-group-prepend"><span class="input-group-text"><fmt:message
                                        key="product.label.name"/></span></div>
                                <input id="input${ProductValidator.NAME_KEY}" class="form-control" placeholder=""
                                       autofocus="" type="text" name="${ProductValidator.NAME_KEY}">
                            </div>
                            <div class="error-messages">
                                <p id="span${ProductValidator.NAME_KEY}"></p>
                            </div>
                        </div>
                    </div>

                    <div class="form-group row">
                        <div class="col-sm-12">
                            <div class="input-group">
                                <div class="input-group-prepend"><label for="input${ProductValidator.DESCRIPTION_KEY}" class="input-group-text"><fmt:message
                                        key="product.label.description"/></label></div>
                                <textarea id="input${ProductValidator.DESCRIPTION_KEY}" class="form-control"
                                          name="${ProductValidator.DESCRIPTION_KEY}" rows="4"></textarea>
                            </div>
                            <div class="error-messages">
                                <p id="span${ProductValidator.DESCRIPTION_KEY}"></p>
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
                                                                        for="input${ProductValidator.IMG_KEY}"><fmt:message
                                        key="product.label.img"/></label></div>
                                <input type="file" class="file-input form-control"
                                       id="input${ProductValidator.IMG_KEY}"
                                       name="${ProductValidator.IMG_KEY}"
                                       accept=".jpg, .jpeg, .bmp, .gif, .png"
                                >
                                <div class="input-group-append">
                                    <button class="btn btn-outline-secondary input-group-text clear-btn" type="button">
                                        &times;
                                    </button>
                                </div>
                            </div>
                            <div class="error-messages">
                                <p id="span${ProductValidator.IMG_KEY}"></p>
                            </div>
                        </div>
                    </div>

                    <div class="form-group row" id="divLogo">
                        <div class="col-sm-2">
                            <img class="mx-auto img-fluid"/>
                        </div>
                        <div class="col-sm-10 my-auto">
                            <div class="input-group">
                                <div class="input-group-prepend"><label class="input-group-text"
                                                                        for="input${ProductValidator.LOGO_KEY}"><fmt:message
                                        key="product.label.logo"/></label></div>
                                <input type="file" class="file-input form-control"
                                       id="input${ProductValidator.LOGO_KEY}"
                                       name="${ProductValidator.LOGO_KEY}"
                                       accept=".jpg, .jpeg, .bmp, .gif, .png"
                                >
                                <div class="input-group-append">
                                    <button class="btn btn-outline-secondary input-group-text clear-btn" type="button">
                                        &times;
                                    </button>
                                </div>
                            </div>
                            <div class="error-messages">
                                <p id="span${ProductValidator.LOGO_KEY}"></p>
                            </div>
                        </div>
                    </div>

                    <div class="form-group row">
                        <div class="col-sm-12">
                            <div class="input-group">
                                <div class="input-group-prepend">
                                        <label class="input-group-text" for="input${ProductValidator.CATEGORY_KEY}">
                                                <fmt:message  key="category"/>
                                        </label>
                                </div>
                                <custom:getAllCategoryOfProduct/>

                                <select class="form-control" id="input${ProductValidator.CATEGORY_KEY}" name="${ProductValidator.CATEGORY_KEY}">
                                    <c:forEach var="category" items="${categoryProductList}">
                                        <option value="${category.id}">${category.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="error-messages">
                                <p id="span${ProductValidator.CATEGORY_KEY}"></p>
                            </div>
                        </div>
                    </div>

                    <div class="form-group row">
                        <div class="col-sm-12">
                            <div class="input-group">
                                <div class="input-group-prepend"><label class="input-group-text"
                                                                        for="input${ProductValidator.PRIVATE_LIST_ID}"><fmt:message
                                        key="product.label.privateListId"/></label></div>
                                <input id="input${ProductValidator.PRIVATE_LIST_ID}" type="number" class="form-control" name="${ProductValidator.PRIVATE_LIST_ID}" />
                            </div>
                            <div class="error-messages">
                                <p id="span${ProductValidator.PRIVATE_LIST_ID}"></p>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal"><fmt:message
                        key="generic.label.close"/></button>
                <button type="submit" form="productForm" class="btn btn-primary"
                        id="productFormSub"></button>
            </div>
        </div>
    </div>
</div>
<script>
    function initProductModal(table){
        const modalUrlJson = '<c:url value="/admin/products.json"/>';

        const productModal = $('#productModal');
        const productForm = $('#productForm', productModal);

        productModal.on("show.bs.modal", function (e) {
            // Remove old validation messages
            validationUtils.clearVerifyMessages(productForm);

            // Reset inputs
            productForm[0].reset();
            productForm.find('input').val("");
            productForm.find('input[type="text"]').html("");

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
                realData.logo = data.logo;
                realData.categoryName = data.categoryName;
                realData.categoryId = data.categoryId;
                realData.privateListId = data.privateListId;

                submitLabel = "<fmt:message key="products.label.modifyForm.submit"/>";
                titleLabel = "<fmt:message key="products.label.modifyForm.title"/>";
            } else if (action === 'create') {
                realData.id = '';
                realData.name = '';
                realData.description = '';
                realData.img = undefined;
                realData.logo = undefined;
                realData.categoryId = '';
                realData.categoryName = '';
                realData.privateListId = '';

                submitLabel = "<fmt:message key="products.label.createForm.submit"/>";
                titleLabel = "<fmt:message key="products.label.createForm.title"/>";
            } else {
                console.error("Action not recognized");
                return;
            }

            // Set title and submit label
            $('#productFormSub', productModal).html(submitLabel);
            $('.modal-title', productModal).html(titleLabel);

            // Set action value
            $('input[name="action"]', productForm).val(action);

            // Set input values/placeholders
            $('input[name="id"]', productForm).val(realData.id);
            $('input[name="${ProductValidator.NAME_KEY}"]', productForm).attr('placeholder', realData.name);
            $('textarea[name="${ProductValidator.DESCRIPTION_KEY}"]', productForm).attr('placeholder', realData.description);

            const prefix = "<c:url value="/${pageContext.servletContext.getInitParameter('productImgsFolder')}/"/>";

            // Set image source
            const divImg1 = $('#divImg', productForm);
            const divLogo = $('#divLogo', productForm);

            divImg1.find('img').attr("src", realData.img !== undefined? prefix + realData.img : "");
            divLogo.find('img').attr("src", realData.logo!== undefined? prefix + realData.logo: "");

            $('select[name="${ProductValidator.CATEGORY_KEY}"] option:selected').prop("selected", false);
            $('select[name="${ProductValidator.CATEGORY_KEY}"] option[value="'+ realData.categoryId +'"]').prop("selected", true);

            $('input[name="${ProductValidator.PRIVATE_LIST_ID}"]').val(realData.privateListId);

            // Trigger validation using event change
            $('input[name="${ProductValidator.NAME_KEY}"]', productForm).trigger('change');
        });


        const isCreate = (form, name) => {return form.find('[name="action"]').val() === 'create';};

        const checkImg = validationUtils.validateFile(
            /.*(jpg|jpeg|png|gif|bmp).*/,
            ${ProductValidator.IMG_MAX_SIZE},
            isCreate, {
                fileEmptyOrNull: "<fmt:message key="validateProduct.errors.Img.FILE_EMPTY_OR_NULL"/>",
                fileTooBig: "<fmt:message key="validateProduct.errors.Img.FILE_TOO_BIG"/>",
                fileContentTypeMissingOrType: "<fmt:message key="validateProduct.errors.Img.FILE_CONTENT_TYPE_MISSING_OR_EMPTY"/>",
                fileOfWrongType: "<fmt:message key="validateProduct.errors.Img.FILE_OF_WRONG_TYPE"/>",
            }
        );

        const checkLogo = validationUtils.validateFile(
            /.*(jpg|jpeg|png|gif|bmp).*/,
            ${ProductValidator.IMG_MAX_SIZE},
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

        $.each(productForm.find('.file-input'), function (index, value) {
            const k = $(value);
            k.find('+ div').find('>button.clear-btn').click(function () {
                k.val("");
            });
        });

        productForm.find('button.clear-btn, button.del-btn, button.ins-btn').click(function () {
            // Trigger update
            $('input[name="${ProductValidator.NAME_KEY}"]', productForm).trigger('change');
        });

        function validate() {
            const obj = {};

            checkName(obj, productForm, "${ProductValidator.NAME_KEY}");
            checkDescription(obj, productForm, "${ProductValidator.DESCRIPTION_KEY}");
            checkImg(obj, productForm, '${ProductValidator.IMG_KEY}');
            checkLogo(obj, productForm, '${ProductValidator.LOGO_KEY}');

            return validationUtils.updateVerifyMessages(productForm, obj);
        }

        formUtils.timedChange(productForm, validate);

        productForm.submit(function (e) {
            e.preventDefault();

            if (validate()) {
                validationUtils.formSubmitWithValidation(
                    modalUrlJson,
                    productForm, {
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
                        waitAlert: {
                            title: "<fmt:message key="generic.modal.waitOperation.title"/>",
                            message: "<fmt:message key="generic.modal.waitOperation.message"/>",
                        },
                        successCallback: function () {
                            table.ajax.reload();
                            table.draw();
                            productModal.modal('toggle');
                        }
                    }
                );
            }
        });
    }
</script>
