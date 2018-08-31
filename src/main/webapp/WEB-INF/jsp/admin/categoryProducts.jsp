<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="header.jsp"%>

<div class="content">
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
                            data-target="#categoryProductModal" data-action="create"><i class="fas fa-plus"></i></button>
                </td>
            </tr>
            </tfoot>
        </table>
        <div class="alert alert-danger form-error-alert" id="id-res">
        </div>
    </div>

    <%@include file="modules/modifyCategoryProduct.jsp" %>
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

        const prefixUrl = '<c:url value="/${pageContext.servletContext.getInitParameter('categoryProductImgsFolder')}/"/>';
        const CATEGORY_PRODUCT_MODAL_ID = '#categoryProductModal';

        const unknownErrorMessage = "<fmt:message key="generic.errors.unknownError"/>";

        function deleteCategoryProduct(e) {
            const data = $(e.currentTarget).closest('tr').data('json');

            formUtils.ajaxButton(
                '<c:url value="/admin/categoryProducts.json"/>', {
                    action: 'delete',
                    id: data.id
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
                    class: 'btn-group',
                    html: [
                        $('<button/>', {
                            class: 'btn btn-md btn-primary',
                            title: "<fmt:message key="categoryProducts.label.modifyCategoryProduct"/>",
                            html: $('<i/>', {class: 'far fa-edit'}),
                            'data-toggle': 'modal',
                            'data-target': CATEGORY_PRODUCT_MODAL_ID,
                            'data-action': 'modify',
                            type: 'button',
                        }),
                        $('<button/>', {
                            class: 'btn btn-md btn-danger',
                            title: "<fmt:message key="categoryProducts.label.deleteCategoryProduct"/>",
                            html: $('<i/>', {class: 'far fa-trash-alt'}),
                            click: deleteCategoryProduct
                        }),
                    ]
                })
            );
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

        const table = tableDiv.DataTable({
            <c:if test="${language == 'it'}">
            language: {
                url: '<c:url value="/libs/DataTables/Languages/it.json"/>'
            },
            </c:if>
            ajax: {
                url: '<c:url value="/admin/categoryProducts.json"/>',
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
                    data: 'id',
                    name: 'id'
                }, {
                    target: 1,
                    data: 'img',
                    orderable: false
                }, {
                    target: 2,
                    data: 'name',
                    name: 'name',
                }, {
                    target: 3,
                    data: 'description',
                    name: 'description'
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


        formUtils.timedChange(
            tableDiv.find('tfoot'),
            function () {
                history.replaceState(undefined, undefined, "categoryProducts?" + tableDiv.find('tfoot').find('input,select').serialize());
                table.ajax.reload();
                table.draw();
            }
        );

        $.fn.dataTable.ext.errMode = 'throw';

        initCategoryProductModal(table);
    });
</script>

<%@include file="footer.jsp"%>
