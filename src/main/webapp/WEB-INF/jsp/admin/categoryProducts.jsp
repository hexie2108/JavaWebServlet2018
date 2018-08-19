<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page import="it.unitn.webprogramming18.dellmm.util.CategoryProductValidator" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.ConstantsUtils" %>

<%@ include file="../../jspf/i18n.jsp" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><fmt:message key="listCategories.title"/></title>

    <script src="<c:url value="/libs/jquery/jquery-3.3.1.min.js"/>"></script>
    <script src="<c:url value="/libs/bootstrap-4.1.1-dist/js/bootstrap.bundle.min.js"/>"></script>
    <link rel="stylesheet" href="<c:url value="/libs/bootstrap-4.1.1-dist/css/bootstrap.min.css"/>">

    <link rel="stylesheet" href="<c:url value="/libs/fontawesome-free-5.1.1-web/css/all.min.css"/>" type="text/css"
          media="all">

    <link rel="stylesheet" href="<c:url value="/css/adminPages.css"/>" type="text/css" media="all"/>
    <link rel="stylesheet" href="<c:url value="/css/validation.css"/>" type="text/css" media="all"/>
</head>
<body>
<%@ include file="../../jspf/i18n_switcher.jsp" %>

<form method="GET" id="filterForm"></form>
<div class="table-responsive">
    <table class="table dt-responsive nowrap w-100" id="categoryTable">
        <thead>
        <tr>
            <th><fmt:message key="categoryProduct.label.id"/></th>
            <th><fmt:message key="categoryProduct.label.logo"/></th>
            <th><fmt:message key="categoryProduct.label.name"/></th>
            <th><fmt:message key="categoryProduct.label.description"/></th>
            <th><fmt:message key="categoryProducts.label.actions"/></th>
        </tr>
        </thead>
        <tbody>
        </tbody>
        <tfoot>
        <tr>
            <td><input class="form-control" type="number" name="id" form="filterForm" value="${param['id']}"/></td>
            <td></td>
            <td><input class="form-control" type="text" name="name" form="filterForm" value="${param['name']}"/></td>
            <td><input class="form-control" type="text" name="description" form="filterForm"
                       value="${param['description']}"/></td>
            <td>
                <button class="btn btn-primary" id="btnNewCategoryProduct" data-toggle="modal"
                        data-target="#categoryProductModal"><i class="fas fa-plus"></i></button>
            </td>
        </tr>
        </tfoot>
    </table>
    <div class="alert alert-danger d-none" id="id-res">
    </div>
</div>

<%@include file="modules/modifyCategoryProduct.jsp" %>

<link rel="stylesheet" type="text/css" href="<c:url value="/libs/DataTables/datatables.min.css"/>"/>
<script src="<c:url value="/js/verification.js"/>"></script>
<script src="<c:url value="/libs/DataTables/datatables.min.js"/>"></script>
<script>
    $(document).ready(function () {
        const resDiv = $('#id-res');
        const tableDiv = $('#categoryTable');

        const prefixUrl = '<c:url value="/${pageContext.servletContext.getInitParameter('categoryProductImgsFolder')}/"/>';
        const CATEGORY_PRODUCT_MODAL_ID = '#categoryProductModal';

        const categoryProductModal = $(CATEGORY_PRODUCT_MODAL_ID);
        const categoryProductForm = categoryProductModal.find('#categoryProductForm');

        const modalUrlJson = '<c:url value="/admin/categoryProducts.json"/>';
        const modalResDiv = $('#id-modal-res');

        const modalUnknownErrorMessage = '<fmt:message key="generic.errors.unknownError"/>';

        function setModal(action, data) {
            const realData = {};
            if (action === 'modify') {
                realData.id = data.id;
                realData.name = data.name;
                realData.description = data.description;
                realData.img = data.img;

                categoryProductModal.find('#categoryProductFormSub').html('<fmt:message key="categoryProducts.label.modifyForm.submit"/>');
                categoryProductModal.find('.modal-title').html('<fmt:message key="categoryProducts.label.modifyForm.title"/>');
            } else if (action === 'create' || action === 'reset') {
                realData.id = '';
                realData.name = '';
                realData.description = '';
                realData.img = undefined;

                categoryProductModal.find('#categoryProductFormSub').html('<fmt:message key="categoryProducts.label.createForm.submit"/>');
                categoryProductModal.find('.modal-title').html('<fmt:message key="categoryProducts.label.createForm.title"/>');
            } else {
                console.error("Action not recognized");
                return;
            }

            categoryProductForm.find('input[name="action"]').val(action);

            categoryProductForm.find('input[name="id"]').val(realData.id);
            categoryProductForm.find('input[name="${CategoryProductValidator.NAME_KEY}"]').attr('placeholder', realData.name);
            categoryProductForm.find('textarea[name="${CategoryProductValidator.DESCRIPTION_KEY}"]').attr('placeholder', realData.description);

            const divImg1 = categoryProductForm.find('#divImg');

            divImg1.find('> img').attr("src", realData.img1 || "");

            // Trigger validation using event change
            categoryProductForm.find('input[name="${CategoryProductValidator.NAME_KEY}"]').trigger('change');
        }

        function format(row, data, index) {
            const map = {
                1: 'img',
            };

            $.each(map, function (key, val) {
                if (data[val] !== undefined) { // Se val non è una stringa vuota(non c'è una x-esima immagine)
                    $('td', row).eq(key).html(
                        $('<img/>', {
                            src: prefixUrl + data[val],
                            class: "img-responsive img-table"
                        })
                    )
                }
            });

            $('td', row).eq(4).html(
                $('<div/>', {
                    html: [
                        $('<button/>', {
                            class: 'btn btn-md btn-primary',
                            title: '<fmt:message key="categoryProducts.label.modifyCategoryProduct"/>',
                            html: $('<i/>', {class: 'far fa-edit'}),
                            'data-toggle': 'modal',
                            'data-target': CATEGORY_PRODUCT_MODAL_ID,
                            type: 'button',
                            click: function () {
                                setModal('modify', data);
                            }
                        }),
                        $('<button/>', {
                            class: 'btn btn-md btn-danger',
                            title: '<fmt:message key="categoryProducts.label.deleteCategoryProduct"/>',
                            html: $('<i/>', {class: 'far fa-trash-alt'}),
                            click: function () {
                                const btn = $(this);

                                btn.attr("disabled", true);

                                $.ajax({
                                    url: '<c:url value="/admin/categoryProducts.json"/>',
                                    type: 'POST',
                                    data: {'action': 'delete', 'id': data.id}
                                }).done(function () {
                                    row.remove();
                                }).fail(function (jqXHR) {
                                    const prevText = btn.html();

                                    if (typeof jqXHR.responseJSON === 'object' &&
                                        jqXHR.responseJSON !== null &&
                                        jqXHR.responseJSON['message'] !== undefined
                                    ) {
                                        btn.html(jqXHR.responseJSON['message']);

                                    } else {
                                        btn.html(unknownErrorMessage);
                                    }

                                    setTimeout(function () {
                                        btn.html(prevText);

                                        btn.attr("disabled", false);
                                    }, 2000);
                                });
                            }
                        }),
                    ]
                })
            );
        }

        tableDiv.on('xhr.dt', function (e, settings, json, xhr) {
            if (json === null) {
                const json = JSON.parse(xhr.responseText);
                resDiv.removeClass("d-none");

                if (json['message'] !== undefined && json['message'] !== null) {
                    resDiv.html(json['message']);
                } else {
                    resDiv.html('<fmt:message key="generic.errors.unknownError"/>');
                }
            } else {
                resDiv.addClass("d-none");
                resDiv.html("");
            }
        });

        const table = tableDiv.DataTable({
            ajax: {
                url: '<c:url value="/admin/categoryProducts.json"/>',
                dataType: "json",
                type: "get",
                cache: "false",
                data: function (d) {
                    return $('#filterForm').serialize();
                },
                dataSrc: ''
            },
            columns: [
                {
                    target: 0,
                    data: 'id'
                }, {
                    target: 1,
                    data: 'img',
                    orderable: false
                }, {
                    target: 2,
                    data: 'name',
                }, {
                    target: 3,
                    data: 'description',
                }, {
                    target: 4,
                    data: null,
                    orderable: false,
                    defaultContent: '',
                }
            ],
            createdRow: format,
            searching: false
        });

        tableDiv.find('tfoot').find('input,select').on('keyup change', function () {
            history.replaceState(undefined, undefined, "categoryProducts?" + $('#categoryTable tfoot').find('input,select').serialize());
            table.ajax.reload();
            table.draw();
        });

        $.fn.dataTable.ext.errMode = 'throw';

        {
            categoryProductModal.on("hidden.bs.modal", function () {
                categoryProductForm[0].reset();
                categoryProductForm.find('input').val("");
                categoryProductForm.find('input[type="text"]').html("");

                modalResDiv.addClass('d-none');
            });

            function add_file_errors(data, form, name, firstImgName) {
                // Se l'estensione per leggere i file è supportata faccio il controllo altrimenti no
                // (fatto successivamente dal server)
                if (!window.FileReader) {
                    return data;
                }

                const input = form.find('[type="file"][name="' + name + '"]');

                // Se il browser ha l'estensione che permette di accedere alla proprietà files continuo altrimenti no
                // (fatto successivamente dal server)
                if (!input[0].files) {
                    return data;
                }

                const fileToUpload = input[0].files[0];

                if (!fileToUpload) {
                    if (form.find('[name="action"]').val() === 'create' && name === firstImgName) {
                        data[name] = '<fmt:message key="validateCategoryProduct.errors.Img.FILE_EMPTY_OR_NULL"/>';
                    }
                } else if (fileToUpload.size > ${CategoryProductValidator.IMG_MAX_SIZE}) {
                    data[name] = '<fmt:message key="validateCategoryProduct.errors.Img.FILE_TOO_BIG"/>';
                } else if (window.Blob) {
                    if (!fileToUpload.type || !fileToUpload.type.trim()) {
                        data[name] = "<fmt:message key="validateCategoryProduct.errors.Img.FILE_CONTENT_TYPE_MISSING_OR_EMPTY"/>";
                    } else if (!(/.*(jpg|jpeg|png|gif|bmp).*/.test(fileToUpload.type))) {
                        data[name] = '<fmt:message key="validateCategoryProduct.errors.Img.FILE_OF_WRONG_TYPE"/>';
                    }
                }

                return data;
            }

            categoryProductForm.find('input,textarea').on('blur change', () => {
                const obj = {};

                const isCreate = categoryProductForm.find('input[name="action"]').val() === 'create';

                const name = categoryProductForm.find('input[name="${CategoryProductValidator.NAME_KEY}"]').val();
                if (!name) {
                    if (isCreate) {
                        obj['${CategoryProductValidator.NAME_KEY}'] = '<fmt:message key="validateCategoryProduct.errors.Name.STRING_EMPTY_OR_NULL"/>';
                    }
                } else if (name.length > ${CategoryProductValidator.NAME_MAX_LEN}) {
                    obj['${CategoryProductValidator.NAME_KEY}'] = '<fmt:message key="validateCategoryProduct.errors.Name.STRING_TOO_LONG"/>';
                }

                const descr = categoryProductForm.find('textarea[name="${CategoryProductValidator.DESCRIPTION_KEY}"]').val();
                if (!descr) {
                    if (isCreate) {
                        obj['${CategoryProductValidator.DESCRIPTION_KEY}'] = '<fmt:message key="validateCategoryProduct.errors.Description.STRING_EMPTY_OR_NULL"/>';
                    }
                } else if (descr.length > ${CategoryProductValidator.DESCRIPTION_MAX_LEN}) {
                    obj['${CategoryProductValidator.DESCRIPTION_KEY}'] = '<fmt:message key="validateCategoryProduct.errors.Description.STRING_TOO_LONG"/>';
                }

                // TODO: Ricontrollare se obj viene modificato
                add_file_errors(obj, categoryProductForm, '${CategoryProductValidator.IMG_KEY}', '${CategoryProductValidator.IMG_KEY}');

                updateVerifyMessages(categoryProductForm, obj);
            });

            $.each(categoryProductForm.find('.file-input'), function (index, value) {
                const k = $(value);
                k.find('+ div').find('>button.clear-btn').click(function () {
                    k.val("");
                });
            });

            categoryProductForm.find('.input-group  button.ins-btn').click(function () {
                const iGr = $(this).parent();

                iGr.find('> .input-group-append, > input').show();
                iGr.find('> button.ins-btn').hide();

                iGr.parent().find('input[type="hidden"]').val("");
            });

            categoryProductForm.find('.input-group button.del-btn').click(function () {
                const iGr = $(this).parent().parent();

                iGr.find('> .input-group-append, > input').val("");
                iGr.find('> .input-group-append, > input').hide();
                iGr.find('> button.ins-btn').show();

                iGr.parent().find('input[type="hidden"]').val("delete");
            });

            categoryProductForm.find('button.clear-btn, button.del-btn, button.ins-btn').click(function () {
                // Trigger update
                categoryProductForm.find('input[name="${CategoryProductValidator.NAME_KEY}"]').trigger('change');
            });


            categoryProductForm.submit(function (e) {
                e.preventDefault();

                formSubmit(
                    modalUrlJson,
                    categoryProductForm, {
                        'multipart': true,
                        'session': true,
                        'redirectUrl': null,
                        'unknownErrorMessage': modalUnknownErrorMessage,
                        'resDiv': modalResDiv,
                        'successCallback': function () {
                            table.ajax.reload();
                            table.draw();
                            categoryProductModal.modal('toggle');
                        }
                    }
                );
            });


            tableDiv.find('#btnNewCategoryProduct').click(function () {
                setModal("create", null);
            });
        }
    });
</script>
</body>
</html>
