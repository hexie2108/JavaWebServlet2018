<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page import="it.unitn.webprogramming18.dellmm.util.RegistrationValidator" %>
<%@ page import="it.unitn.webprogramming18.dellmm.util.PagePathsConstants" %>

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
</head>
<body>
<%@ include file="../../jspf/i18n_switcher.jsp" %>

<form method="GET" id="filterForm"></form>
<div class="table-responsive">
    <table class="table dt-responsive nowrap w-100" id="categoryTable">
        <thead>
        <tr>
            <th><fmt:message key="categoryList.label.id"/></th>
            <th><fmt:message key="categoryList.label.name"/></th>
            <th><fmt:message key="categoryList.label.description"/></th>
            <th><fmt:message key="categoryList.label.img1"/></th>
            <th><fmt:message key="categoryList.label.img2"/></th>
            <th><fmt:message key="categoryList.label.img3"/></th>
            <th><fmt:message key="generic.label.actions"/></th>
        </tr>
        </thead>
        <tbody>
        </tbody>
        <tfoot>
        <tr>
            <td><input class="form-control" type="number" name="id" form="filterForm" value="${param['id']}"/></td>
            <td><input class="form-control" type="text" name="name" form="filterForm" value="${param['name']}"/></td>
            <td><input class="form-control" type="text" name="description" form="filterForm"
                       value="${param['description']}"/></td>
            <td></td>
            <td></td>
            <td></td>
            <td>
                <button class="btn btn-primary" id="btnNewCategoryList" data-toggle="modal"
                        data-target="#modifyCategoryListModal"><i class="fas fa-plus"></i></button>
            </td>
        </tr>
        </tfoot>
    </table>
    <div class="alert alert-danger d-none" id="id-res">
    </div>
</div>

<%@include file="modules/modifyCategoryList.jsp" %>

<link rel="stylesheet" type="text/css" href="<c:url value="/libs/DataTables/datatables.min.css"/>"/>
<script src="<c:url value="/js/verification.js"/>"></script>
<script src="<c:url value="/libs/DataTables/datatables.min.js"/>"></script>
<script>
    $(document).ready(function () {
        const resDiv = $('#id-res');
        const tableDiv = $('#categoryTable');

        const prefixUrl = '<c:url value="/${pageContext.servletContext.getInitParameter('categoryListImgsFolder')}/"/>';
        const CATEGORY_LIST_MODAL_ID = '#modifyCategoryListModal';

        const categoryListModal = $('#modifyCategoryListModal');
        const categoryListForm = categoryListModal.find('#modifyCategoryListForm');

        const modalUrlJson = '<c:url value="/admin/categoryLists.json"/>';
        const modalResDiv = $('#id-modal-res');

        const modalUnknownErrorMessage = '<fmt:message key="generic.errors.unknownError"/>';

        function setModal(action, data) {
            const realData = {};
            if (action === 'modify') {
                realData.id = data.id;
                realData.name = data.name;
                realData.description = data.description;
                realData.img1 = data.img1;
                realData.img2 = data.img2;
                realData.img3 = data.img3;

                categoryListModal.find('#modifyCategoryListFormSub').html('<fmt:message key="categoryLists.label.modifyForm.submit"/>');
                categoryListModal.find('.modal-title').html('<fmt:message key="categoryLists.label.modifyForm.title"/>');
            } else if (action === 'create' || action === 'reset') {
                realData.id = '';
                realData.name = '';
                realData.description = '';
                realData.img1 = undefined;
                realData.img2 = undefined;
                realData.img3 = undefined;

                categoryListModal.find('#modifyCategoryListFormSub').html('<fmt:message key="categoryLists.label.createForm.submit"/>');
                categoryListModal.find('.modal-title').html('<fmt:message key="categoryLists.label.createForm.title"/>');
            } else {
                console.error("Action not recognized");
                return;
            }

            categoryListForm.find('input[name="action"]').val(action);

            categoryListForm.find('input[name="id"]').val(realData.id);
            categoryListForm.find('input[name="${CategoryListValidator.NAME_KEY}"]').attr('placeholder', realData.name);
            categoryListForm.find('textarea[name="${CategoryListValidator.DESCRIPTION_KEY}"]').attr('placeholder', realData.description);

            const divImg1 = categoryListForm.find('#divImg1');
            const divImg2 = categoryListForm.find('#divImg2');
            const divImg3 = categoryListForm.find('#divImg3');

            divImg1.find('> img').attr("src", realData.img1 || "");
            divImg2.find('> img').attr("src", realData.img2 || "");
            divImg3.find('> img').attr("src", realData.img3 || "");

            const m = {
                'img2': divImg2.find('> div > .input-group'),
                'img3': divImg3.find('> div > .input-group')
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
            categoryListForm.find('input[name="${CategoryListValidator.NAME_KEY}"]').trigger('change');
        }

        function format(row, data, index) {
            const map = {
                3: 'img1',
                4: 'img2',
                5: 'img3'
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

            $('td', row).eq(6).html(
                $('<div/>', {
                    html: [
                        $('<button/>', {
                            class: 'btn btn-md btn-primary',
                            title: '<fmt:message key="categoryLists.label.modifyCategoryList"/>',
                            html: $('<i/>', {class: 'far fa-edit'}),
                            'data-toggle': 'modal',
                            'data-target': CATEGORY_LIST_MODAL_ID,
                            type: 'button',
                            click: function () {
                                setModal('modify', data);
                            }
                        }),
                        $('<button/>', {
                            class: 'btn btn-md btn-danger',
                            title: '<fmt:message key="categoryLists.label.deleteCategoryList"/>',
                            html: $('<i/>', {class: 'far fa-trash-alt'}),
                            click: function () {
                                const btn = $(this);

                                btn.attr("disabled", true);

                                $.ajax({
                                    url: '<c:url value="/admin/categoryLists.json"/>',
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
                url: '<c:url value="/admin/categoryLists.json"/>',
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
                    data: 'name'
                }, {
                    target: 2,
                    data: 'description',
                }, {
                    target: 3,
                    data: 'img1',
                    orderable: false,
                }, {
                    target: 4,
                    data: 'img2',
                    orderable: false,
                    defaultContent: '',
                }, {
                    target: 5,
                    data: 'img3',
                    orderable: false,
                    defaultContent: '',
                }, {
                    target: 6,
                    data: null,
                    orderable: false,
                    defaultContent: '',
                }
            ],
            createdRow: format,
            searching: false
        });

        tableDiv.find('tfoot').find('input,select').on('keyup change', function () {
            history.replaceState(undefined, undefined, "categoryLists?" + $('#categoryTable tfoot').find('input,select').serialize());
            table.ajax.reload();
            table.draw();
        });

        $.fn.dataTable.ext.errMode = 'throw';

        {
            categoryListModal.on("hidden.bs.modal", function () {
                categoryListForm[0].reset();
                categoryListForm.find('input').val("");
                categoryListForm.find('input[type="text"]').html("");

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
                        data[name] = '<fmt:message key="validateCategoryList.errors.IMG_MISSING"/>';
                    }
                } else if (fileToUpload.size > ${CategoryListValidator.MAX_LEN_FILE}) {
                    data[name] = '<fmt:message key="validateCategoryList.errors.IMG_TOO_BIG"/>';
                } else if (window.Blob && !fileToUpload.type.startsWith("image/")) {
                    data[name] = '<fmt:message key="validateCategoryList.errors.IMG_NOT_IMG"/>';
                }

                return data;
            }

            categoryListForm.find('input,textarea').on('blur change', () => {
                const obj = {};

                const isCreate = categoryListForm.find('input[name="action"]').val() === 'create';

                const name = categoryListForm.find('input[name="${CategoryListValidator.NAME_KEY}"]').val();
                if (!name) {
                    if (isCreate) {
                        obj['${CategoryListValidator.NAME_KEY}'] = '<fmt:message key="validateCategoryList.errors.NAME_MISSING"/>';
                    }
                } else if (name.length > ${CategoryListValidator.NAME_MAX_LEN}) {
                    obj['${CategoryListValidator.NAME_KEY}'] = '<fmt:message key="validateCategoryList.errors.NAME_TOO_LONG"/>';
                }

                const descr = categoryListForm.find('textarea[name="${CategoryListValidator.DESCRIPTION_KEY}"]').val();
                if (!descr) {
                    if (isCreate) {
                        obj['${CategoryListValidator.DESCRIPTION_KEY}'] = '<fmt:message key="validateCategoryList.errors.DESCRIPTION_MISSING"/>';
                    }
                } else if (descr.length > ${CategoryListValidator.DESCRIPTION_MAX_LEN}) {
                    obj['${CategoryListValidator.DESCRIPTION_KEY}'] = '<fmt:message key="validateCategoryList.errors.DESCRIPTION_TOO_LONG"/>';
                }

                [
                    '${CategoryListValidator.IMG1_KEY}',
                    '${CategoryListValidator.IMG2_KEY}',
                    '${CategoryListValidator.IMG3_KEY}'
                ].reduce(function (acc, curr) {
                        return add_file_errors(acc, categoryListForm, curr, '${CategoryListValidator.IMG1_KEY}');
                    },
                    obj
                );

                updateVerifyMessages(categoryListForm, obj);
            });

            $.each(categoryListForm.find('.file-input'), function (index, value) {
                const k = $(value);
                k.find('+ div').find('>button.clear-btn').click(function () {
                    k.val("");
                });
            });

            categoryListForm.find('.input-group  button.ins-btn').click(function () {
                const iGr = $(this).parent();

                iGr.find('> .input-group-append, > input').show();
                iGr.find('> button.ins-btn').hide();

                iGr.parent().find('input[type="hidden"]').val("");
            });

            categoryListForm.find('.input-group button.del-btn').click(function () {
                const iGr = $(this).parent().parent();

                iGr.find('> .input-group-append, > input').val("");
                iGr.find('> .input-group-append, > input').hide();
                iGr.find('> button.ins-btn').show();

                iGr.parent().find('input[type="hidden"]').val("delete");
            });

            categoryListForm.find('button.clear-btn, button.del-btn, button.ins-btn').click(function () {
                // Trigger update
                categoryListForm.find('input[name="${CategoryListValidator.NAME_KEY}"]').trigger('change');
            });


            categoryListForm.submit(function (e) {
                e.preventDefault();

                formSubmit(
                    modalUrlJson,
                    categoryListForm, {
                        'multipart': true,
                        'session': true,
                        'redirectUrl': null,
                        'unknownErrorMessage': modalUnknownErrorMessage,
                        'resDiv': modalResDiv,
                        'successCallback': function () {
                            table.ajax.reload();
                            table.draw();
                            categoryListModal.modal('toggle');
                        }
                    }
                );
            });


            tableDiv.find('#btnNewCategoryList').click(function () {
                setModal("create", null);
            });
        }
    });
</script>
</body>
</html>
