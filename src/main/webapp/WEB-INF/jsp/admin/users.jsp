<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="/WEB-INF/jspf/i18n.jsp"%>

<%@ include file="header.jsp"%>

<link rel="stylesheet" href="<c:url value="/css/user-system-style.css"/>" type="text/css" media="all"/>

<div class="content">
    <form method="GET" id="filterForm"></form>
    <div class="table-responsive">
        <table class="table dt-responsive w-100 user-list" id="userTable">
            <thead>
            <tr>
                <th><fmt:message key="user.label.id"/></th>
                <th><fmt:message key="user.label.image"/></th>
                <th><fmt:message key="user.label.name"/></th>
                <th><fmt:message key="user.label.surname"/></th>
                <th><fmt:message key="user.label.email"/></th>
                <th><fmt:message key="user.label.password"/></th>
                <th><fmt:message key="user.label.isAdmin"/></th>
                <th><fmt:message key="user.label.verifyEmailLink"/></th>
                <th><fmt:message key="user.label.resetPasswordLink"/></th>
                <th><fmt:message key="generic.label.actions"/></th>
            </tr>
            </thead>
            <tbody>
            </tbody>
            <tfoot>
            <tr>
                <td><input class="form-control" size="6" type="number" name="id" form="filterForm" value="${param['id']}"/>
                </td>
                <td></td>
                <td><input class="form-control" type="text" name="name" form="filterForm" value="${param['name']}"/></td>
                <td><input class="form-control" type="text" name="surname" form="filterForm" value="${param['surname']}"/>
                </td>
                <td><input class="form-control" type="text" name="email" form="filterForm" value="${param['email']}"/></td>
                <td></td>
                <td>
                    <select name="admin" form="filterForm">
                        <option value="" ${param['admin'].equals("")?'selected':''}>--</option>
                        <option value="true" ${param['admin'].equals("true")?'selected':''}><fmt:message
                                key="users.label.true"/></option>
                        <option value="false" ${param['admin'].equals("false")?'selected':''}><fmt:message
                                key="users.label.false"/></option>
                    </select>
                </td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            </tfoot>
        </table>
    </div>
    <%@ include file="modules/modifyUser.jsp"%>
</div>

<!-- Custom verification utilities -->
<link rel="stylesheet" type="text/css" href="<c:url value="/css/validation.css"/>"/>
<script src="<c:url value="/js/verification.js"/>"></script>

<!-- Custom datatables utilities -->
<link rel="stylesheet" type="text/css" href="<c:url value="/libs/DataTables/datatables.min.css"/>"/>
<script src="<c:url value="/libs/DataTables/datatables.min.js"/>"></script>

<script src="<c:url value="/libs/zxcvbn/zxcvbn.js"/>"></script>
<script>
    $(document).ready(function () {
        const tableDiv = $('#userTable');

        const unknownError = '<fmt:message key="generic.errors.unknownError"/>';

        function modifyUserClass(event) {
            const data = $(event.currentTarget).closest('tr').data('json');

            formUtils.ajaxButton(
                '<c:url value="/admin/upgradeUserToAdmin.json"/>',
                {'email': data.email, 'admin': !data.isAdmin},
                () => {
                    table.ajax.reload();
                    table.draw();
                },  {
                    title: "<fmt:message key="generic.modal.waitOperation.title"/>",
                    message: "<fmt:message key="generic.modal.waitOperation.message"/>",
                }, {
                    title: "<fmt:message key="generic.label.errorTitle"/>",
                    message: "<fmt:message key="generic.errors.unknownError"/>",
                    closeLabel: "<fmt:message key="generic.label.close"/>"
                }
            );
        }

        function deleteUser(event) {
            const data = $(event.currentTarget).closest('tr').data('json');

            formUtils.ajaxButton(
                '<c:url value="/admin/users.json"/>', {
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
                    message: "<fmt:message key="generic.errors.unknownError"/>",
                    closeLabel: "<fmt:message key="generic.label.close"/>"
                }
            );
        }

        function formatRow(row, data, index) {
            const imgAvatar = $('<img/>', {
                src: '<c:url value="/${pageContext.servletContext.getInitParameter('avatarsFolder')}/"/>' + data.img,
                class: "img-responsive img-table"
            });

            const admin = data.isAdmin ? "<fmt:message key="users.label.true"/>" : "<fmt:message key="users.label.false"/>";

            const modifyUserButton = $('<button/>', {
                class: 'btn btn-md btn-primary',
                title: '<fmt:message key="users.label.modifyUser"/>',
                html: '<i class="far fa-edit"></i>',
                'data-toggle': 'modal',
                'data-target': '#modifyUserModal'
            });

            const upgradeUserButton = $('<button/>', {
                class: 'btn btn-md btn-danger',
                title: data.isAdmin ? "<fmt:message key="users.label.downgradeUser"/>" : "<fmt:message key="users.label.upgradeUser"/>",
                html: data.isAdmin ? '<i class="fas fa-level-down-alt"></i>' : '<i class="fas fa-level-up-alt"></i>',
                click: modifyUserClass
            });

            const deleteUserButton = $('<button/>', {
                class: 'btn btn-md btn-danger',
                title: '<fmt:message key="users.label.deleteUser"/>',
                html: $('<i/>', {class: 'far fa-trash-alt'}),
                click: deleteUser
            });


            $('td', row).eq(2).html(imgAvatar);
            $('td', row).eq(6).html(admin);

            $('td', row).eq(7).html(
                $('<div/>', {
                    class: 'btn-group',
                    html: [modifyUserButton, upgradeUserButton, deleteUserButton]
                })
            );

            $(row).data('json', data);
        }

        function format(d) {
            const ul = $('<ul/>', {
                class: 'list-unstyled',
                html: [
                    $('<li/>', {
                        html: [$('<b/>', {text: '<fmt:message key="user.label.password"/>: '}), document.createTextNode(d.password)]
                    })
                ]
            });

            if (d.verifyEmailLink !== undefined) {
                ul.append(
                    $('<li/>', {
                        html: [$('<b/>', {text: '<fmt:message key="user.label.verifyEmailLink"/>:  '}), document.createTextNode(d.verifyEmailLink)]
                    })
                );
            }

            if (d.resetPwdEmailLink !== undefined) {
                ul.append(
                    $('<li/>', {
                        html: [$('<b/>', {text: '<fmt:message key="user.label.resetPasswordLink"/>:  '}), document.createTextNode(d.resetPwdEmailLink)]
                    })
                );
            }

            return ul;
        }

        $('> thead > tr, tfoot > tr', tableDiv).prepend('<th></th>');

        tableDiv.on('xhr.dt', function (e, settings, json, xhr) {
            if (json !== null) {
                return;
            }

            let err = unknownError;

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
                url: '<c:url value="/admin/users.json"/>',
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
                }
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
                    data: 'img',
                    orderable: false
                }, {
                    target: 3,
                    data: 'name',
                    name: 'name'
                }, {
                    target: 4,
                    data: 'surname',
                    name: 'surname'
                }, {
                    target: 5,
                    data: 'email',
                    name: 'email'
                }, {
                    target: 6,
                    data: 'password',
                    visible: false
                }, {
                    target: 7,
                    data: 'isAdmin',
                    name: 'admin'
                }, {
                    target: 8,
                    data: 'verifyEmailLink',
                    defaultContent: '',
                    visible: false
                }, {
                    target: 9,
                    data: 'resetPwdEmailLink',
                    defaultContent: '',
                    visible: false
                }, {
                    target: 10,
                    data: null,
                    defaultContent: '',
                    orderable: false
                }
            ],
            order: [[1, 'asc']],
            createdRow: formatRow,
            searching: false
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
                row.child(format(row.data())).show();
                tr.addClass('shown');

                $('td.details-control i', tr).removeClass("fa-plus-circle");
                $('td.details-control i', tr).addClass("fa-minus-circle");
            }
        });

        formUtils.timedChange(
            $('tfoot', tableDiv),
            function () {
                history.replaceState(undefined, undefined, "users?" + tableDiv.find('tfoot').find('input,select').serialize());
                table.ajax.reload();
                table.draw();
            }
        );

        $.fn.dataTable.ext.errMode = 'throw';

        initUserModal(table);
    });
</script>

<%@include file="footer.jsp"%>
