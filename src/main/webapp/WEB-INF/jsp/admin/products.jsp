<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="/WEB-INF/jspf/i18n.jsp"%>

<%@ include file="header.jsp" %>

<div class="content">
    <form method="GET" id="filterForm"></form>
    <div class="table-responsive">
        <table class="table dt-responsive w-100 prodoct-page" id="categoryTable">
            <thead>
            <tr>
                <th><fmt:message key="product.label.id"/></th>
                <th><fmt:message key="product.label.name"/></th>
                <th><fmt:message key="product.label.description"/></th>
                <th><fmt:message key="product.label.img"/></th>
                <th><fmt:message key="product.label.logo"/></th>
                <th><fmt:message key="product.label.categoryName"/></th>
                <th><fmt:message key="product.label.privateListId"/></th>
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
                <td><input class="form-control" type="text" name="privateListId" form="filterForm"
                           value="${param['privateListId']}"/></td>
                <td>
                    <button class="btn btn-primary" id="btnNewCategoryList" data-toggle="modal"
                            data-target="#productModal" data-action="create"><i class="fas fa-plus"></i></button>
                </td>
            </tr>
            </tfoot>
        </table>
    </div>

    <%@include file="modules/modifyProduct.jsp"%>
</div>

<!-- Custom verification utilities -->
<link rel="stylesheet" type="text/css" href="<c:url value="/css/validation.css"/>"/>
<script src="<c:url value="/js/verification.js"/>"></script>

<!-- Custom datatables utilities -->
<link rel="stylesheet" type="text/css" href="<c:url value="/libs/DataTables/datatables.min.css"/>"/>
<script src="<c:url value="/libs/DataTables/datatables.min.js"/>"></script>

<script>
    $(document).ready(function () {
        const tableDiv = $('#categoryTable');

        const PRODUCT_MODAL_ID = '#productModal';

        const unknownErrorMessage = "<fmt:message key="generic.errors.unknownError"/>";

        function deleteProduct(e) {
            const data = $(e.currentTarget).closest('tr').data('json');

            formUtils.ajaxButton(
                '<c:url value="/admin/products.json"/>', {
                    'action': 'delete',
                    'id': data.id
                }, function(){
                    table.ajax.reload();
                    table.draw();
                }, {
                    title: "<fmt:message key="generic.label.errorTitle"/>",
                    message: unknownErrorMessage,
                    closeLabel: "<fmt:message key="generic.label.close"/>"
                }
            );
        }

        function format(row, data, index) {
            $(row).data('json', data);

            $('td', row).eq(6).html(
                $('<div/>', {
                    class: 'btn-group',
                    html: [
                        $('<button/>', {
                            class: 'btn btn-md btn-primary',
                            title: "<fmt:message key="products.label.modifyProduct"/>",
                            html: $('<i/>', {class: 'far fa-edit'}),
                            'data-toggle': 'modal',
                            'data-target': PRODUCT_MODAL_ID,
                            'data-action': 'modify',
                            type: 'button',
                        }),
                        $('<button/>', {
                            class: 'btn btn-md btn-danger',
                            title: "<fmt:message key="products.label.deleteProduct"/>",
                            html: $('<i/>', {class: 'far fa-trash-alt'}),
                            click: deleteProduct,
                            type: 'button'
                        }),
                    ]
                })
            );
        }

        function formatChild(d) {
            const prefix = "<c:url value="/${pageContext.servletContext.getInitParameter('productImgsFolder')}"/>";
            console.log(prefix);

            return $('<ul/>', {
                class: 'list-unstyled',
                html: [
                    $('<li/>', {
                        html: [
                            $('<b/>', {text: '<fmt:message key="product.label.img"/>:  '}),
                            $('<img/>', {src: prefix + '/' + d.img, class: 'img-fluid limited-image'})
                        ]
                    }),
                    $('<li/>', {
                        html: [
                            $('<b/>', {text: '<fmt:message key="product.label.logo"/>:  '}),
                            $('<img/>', {src: prefix + '/' + d.logo, class: 'img-fluid limited-image'})
                        ]
                    })
                ]
            });
        }

        tableDiv.on('xhr.dt', function (e, settings, json, xhr) {
            if (json !== null) {
                return;
            }

            let err = unknownErrorMessage;

            try{
                const errJSON = JSON.parse(xhr.responseText);

                if (errJSON['message'] !== undefined && errJSON['message'] !== null) {
                    err = errJSON['message'];
                }
            } catch(e) {
                // Se errore durante parse o errore mal formato lascia default
            }

            modalAlert.error('<fmt:message key="generic.label.errorTitle"/>', err, '<fmt:message key="generic.label.close"/>');
        });

        $('> thead > tr, tfoot > tr', tableDiv).prepend('<th></th>');

        const table = tableDiv.DataTable({
            <c:if test="${language == 'it'}">
            language: {
                url: '<c:url value="/libs/DataTables/Languages/it.json"/>'
            },
            </c:if>
            ajax: {
                url: '<c:url value="/admin/products.json"/>',
                dataType: "json",
                type: "get",
                cache: "false",
                data: function (d) {
                    return $.extend( {}, d,
                        $('#filterForm')
                            .serializeArray()
                            .reduce(
                                function(accumulator,pair){
                                    accumulator[pair.name] = pair.value;
                                    return accumulator;
                                }, {})
                    );
                },
            },
            serverSide: true,
            columns: [
                {
                    target: 0,
                    className: 'details-control',
                    orderable: false,
                    data: null,
                    defaultContent: '<i class="fas fa-plus-circle"></i>'
                }, {
                    target: 1,
                    data: 'id',
                    name: 'id'
                }, {
                    target: 2,
                    data: 'name',
                    name: 'name'
                }, {
                    target: 3,
                    data: 'description',
                    name: 'description'
                }, {
                    target: 4,
                    data: 'img',
                    orderable: false,
                    visible: false,
                }, {
                    target: 5,
                    data: 'logo',
                    orderable: false,
                    visible: false,
                }, {
                    target: 6,
                    data: 'categoryName',
                    name: 'categoryName',
                }, {
                    target: 7,
                    data: 'privateListId',
                    name: 'privateListId',
                    defaultContent: ''
                }, {
                    target: 8,
                    data: null,
                    orderable: false,
                    defaultContent: '',
                }
            ],
            createdRow: format,
            searching: false,
            order: [[1,'asc']]
        });

        tableDiv.find('> tbody').on('click', 'td.details-control', function () {
            const tr = $(this).closest('tr');
            const row = table.row(tr);

            if (row.child.isShown()) {
                // This row is already open - close it
                row.child.hide();
                tr.removeClass('shown');

                $('td.details-control i', tr).removeClass("fa-minus-circle");
                $('td.details-control i', tr).addClass("fa-plus-circle");
            } else {
                // Open this row
                row.child(formatChild(row.data())).show();
                tr.addClass('shown');

                $('td.details-control i', tr).removeClass("fa-plus-circle");
                $('td.details-control i', tr).addClass("fa-minus-circle");
            }
        });

        formUtils.timedChange(
            $('tfoot', tableDiv),
            function () {
                history.replaceState(undefined, undefined, "products?" + $('tfoot', tableDiv).find('input,select').serialize());
                table.ajax.reload();
                table.draw();
            }
        );

        $.fn.dataTable.ext.errMode = 'throw';

        initProductModal(table);
    });
</script>

<%@ include file="footer.jsp"%>
