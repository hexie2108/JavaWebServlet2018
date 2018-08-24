<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page import="it.unitn.webprogramming18.dellmm.util.FormValidator"%>

<%@ include file="../../jspf/i18n.jsp" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><fmt:message key="users.title"/></title>

    <script src="<c:url value="/libs/jquery/jquery-3.3.1.min.js"/>"></script>
    <script src="<c:url value="/libs/bootstrap-4.1.1-dist/js/bootstrap.bundle.min.js"/>"></script>
    <link rel="stylesheet" href="<c:url value="/libs/bootstrap-4.1.1-dist/css/bootstrap.min.css"/>">

    <link rel="stylesheet" href="<c:url value="/libs/fontawesome-free-5.1.1-web/css/all.min.css"/>" type="text/css"
          media="all">

    <link rel="stylesheet" href="<c:url value="/css/adminPages.css"/>" type="text/css" media="all"/>
    <link rel="stylesheet" href="<c:url value="/css/user-system-style.css"/>" type="text/css" media="all"/>
</head>
<body>
<%@ include file="../../jspf/i18n_switcher.jsp" %>

<form method="GET" id="filterForm"></form>
<div class="table-responsive">
    <table class="table dt-responsive nowrap w-100" id="userTable">
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
<link rel="stylesheet" type="text/css" href="<c:url value="/libs/DataTables/datatables.min.css"/>"/>
<script src="<c:url value="/libs/DataTables/datatables.min.js"/>"></script>

<link rel="stylesheet" type="text/css" href="<c:url value="/css/validation.css"/>"/>
<script src="<c:url value="/js/verification.js"/>"></script>

<script src="<c:url value="/libs/zxcvbn/zxcvbn.js"/>"></script>
<script>
    $(document).ready(function () {
        const tableDiv = $('#userTable');

        const unknownError = '<fmt:message key="generic.errors.unknownError"/>';

        function ajaxButton(url, data, successCallback) {
            $.ajax({
                url: url,
                type: 'POST',
                data: data,
                async: false
            }).done(function () {
                successCallback();
            }).fail(function (jqXHR) {
                if (typeof jqXHR.responseJSON === 'object' &&
                    jqXHR.responseJSON !== null &&
                    jqXHR.responseJSON['message'] !== undefined
                ) {
                    modalAlert.error(
                        '<fmt:message key="generic.label.errorTitle"/>',
                        jqXHR.responseJSON['message'],
                        '<fmt:message key="generic.label.close"/>'
                    );

                } else {
                    modalAlert.error(
                        '<fmt:message key="generic.label.errorTitle"/>',
                        unknownError,
                        '<fmt:message key="generic.label.close"/>'
                    );
                }
            });
        }

        function modifyUserClass(event) {
            const data = $(event.currentTarget).closest('tr').data('json');

            ajaxButton(
                '<c:url value="/admin/upgradeUserToAdmin.json"/>',
                {'email': data.email, 'admin': !data.isAdmin},
                () => {
                    table.ajax.reload();
                    table.draw();
                }
            );
        }

        function deleteUser(event) {
            const data = $(event.currentTarget).closest('tr').data('json');

            ajaxButton(
                '<c:url value="/admin/users.json"/>',
                {'action': 'delete', 'id': data.id},
                () => {
                    table.ajax.reload();
                    table.draw();
                }
            );
        }

        function formatRow(row, data, index) {
            const imgAvatar = $('<img/>', {
                src: '<c:url value="/${pageContext.servletContext.getInitParameter('avatarsFolder')}/"/>' + data.img,
                class: "img-responsive img-table"
            });

            const admin = data.isAdmin ? '<fmt:message key="users.label.true"/>' : '<fmt:message key="users.label.false"/>';

            const modifyUserButton = $('<button/>', {
                class: 'btn btn-md btn-primary',
                title: '<fmt:message key="users.label.modifyUser"/>',
                html: '<i class="far fa-edit"></i>',
                'data-toggle': 'modal',
                'data-target': '#modifyUserModal',
            });

            const upgradeUserButton = $('<button/>', {
                class: 'btn btn-md btn-danger',
                title: data.isAdmin ? '<fmt:message key="users.label.downgradeUser"/>' : '<fmt:message key="users.label.upgradeUser"/>',
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
                )
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
                    name: 'id',
                }, {
                    target: 2,
                    data: 'img',
                    orderable: false,
                }, {
                    target: 3,
                    data: 'name',
                    name: 'name',
                }, {
                    target: 4,
                    data: 'surname',
                    name: 'surname',
                }, {
                    target: 5,
                    data: 'email',
                    name: 'email',
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
                    orderable: false,
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

                tr.find('td.details-control i').removeClass("fa-minus-circle");
                tr.find('td.details-control i').addClass("fa-plus-circle");
            } else {
                // Open this row
                row.child(format(row.data())).show();
                tr.addClass('shown');

                tr.find('td.details-control i').removeClass("fa-plus-circle");
                tr.find('td.details-control i').addClass("fa-minus-circle");
            }
        });

        formUtils.timedChange(
            tableDiv.find('tfoot'),
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
</body>
</html>
