

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="/WEB-INF/jspf/i18n.jsp"%>


<%@ include file="header.jsp" %>

<div class="content">
    <form method="GET" id="filterForm"></form>
    <div class="table-responsive ">
        <table class="table dt-responsive  w-100 list-category" id="categoryTable">
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
                            data-target="#modifyCategoryListModal" data-action="create"><i class="fas fa-plus"></i></button>
                </td>
            </tr>
            </tfoot>
        </table>
        <div class="alert alert-danger d-none" id="id-res">
        </div>
    </div>

    <%@include file="modules/modifyCategoryList.jsp" %>
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

        const prefixUrl = '<c:url value="/${pageContext.servletContext.getInitParameter('categoryListImgsFolder')}/"/>';
        const CATEGORY_LIST_MODAL_ID = '#modifyCategoryListModal';

        const unknownErrorMessage = "<fmt:message key="generic.errors.unknownError"/>";

        function deleteCategoryList(e) {
            const data = $(e.currentTarget).closest('tr').data('json');

            formUtils.ajaxButton(
                '<c:url value="/admin/categoryLists.json"/>', {
                    'action': 'delete',
                    'id': data.id
                }, function(){
                    table.ajax.reload();
                    table.draw();
                },  {
                    title: "<fmt:message key="generic.modal.waitOperation.title"/>",
                    message: "<fmt:message key="generic.modal.waitOperation.message"/>",
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
                    );
                }
            });

            $('td', row).eq(6).html(
                $('<div/>', {
                    class: 'btn-group',
                    html: [
                        $('<button/>', {
                            class: 'btn btn-md btn-primary',
                            title: "<fmt:message key="categoryLists.label.modifyCategoryList"/>",
                            html: $('<i/>', {class: 'far fa-edit'}),
                            'data-toggle': 'modal',
                            'data-target': CATEGORY_LIST_MODAL_ID,
                            'data-action': 'modify',
                            type: 'button',
                        }),
                        $('<button/>', {
                            class: 'btn btn-md btn-danger',
                            title: "<fmt:message key="categoryLists.label.deleteCategoryList"/>",
                            html: $('<i/>', {class: 'far fa-trash-alt'}),
                            click: deleteCategoryList,
                            type: 'button'
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
             <c:if test="${cookie.language.value == 'it' || pageContext.request.locale.language == 'it'}">
            language: {
                url: '<c:url value="/libs/DataTables/Languages/it.json"/>'
            },
            </c:if>
            ajax: {
                url: '<c:url value="/admin/categoryLists.json"/>',
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
                    data: 'name',
                    name: 'name'
                }, {
                    target: 2,
                    data: 'description',
                    name: 'description'
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

        formUtils.timedChange(
            $('tfoot', tableDiv),
            function () {
                history.replaceState(undefined, undefined, "categoryLists?" + $('tfoot', tableDiv).find('input,select').serialize());
                table.ajax.reload();
                table.draw();
            }
        );

        $.fn.dataTable.ext.errMode = 'throw';

        initCategoryListModal(table);
    });
</script>

<%@ include file="footer.jsp"%>
