<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="../../jspf/i18n.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><fmt:message key="users.title"/></title>

    <script src="<c:url value="/libs/jquery/jquery-3.3.1.min.js"/>"></script>
    <script src="<c:url value="/libs/bootstrap-4.1.1-dist/js/bootstrap.bundle.min.js"/>"></script>
    <link rel="stylesheet" href="<c:url value="/libs/bootstrap-4.1.1-dist/css/bootstrap.min.css"/>">

    <link rel="stylesheet" href="<c:url value="/libs/fontawesome-free-5.1.1-web/css/all.min.css"/>" type="text/css" media="all">
</head>
<body>
<%@ include file="../../jspf/i18n_switcher.jsp"%>

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
            <th><fmt:message key="users.label.actions"/></th>
        </tr>
        </thead>
        <tbody>
        </tbody>
        <tfoot>
        <tr>
            <td><input class="form-control" size="6" type="number" name="id" form="filterForm" value="${param['id']}"/></td>
            <td></td>
            <td><input class="form-control" type="text" name="name" form="filterForm" value="${param['name']}"/></td>
            <td><input class="form-control" type="text" name="surname" form="filterForm" value="${param['surname']}"/></td>
            <td><input class="form-control" type="text" name="email" form="filterForm" value="${param['email']}"/></td>
            <td></td>
            <td>
                <select name="admin" form="filterForm">
                    <option value="" ${param['admin'].equals("")?'selected':''}>--</option>
                    <option value="true" ${param['admin'].equals("true")?'selected':''}><fmt:message key="users.label.true"/></option>
                    <option value="false" ${param['admin'].equals("false")?'selected':''}><fmt:message key="users.label.false"/></option>
                </select>
            </td>
            <td></td>
            <td></td>
            <td><button class="btn btn-dark" type="submit" form="filterForm"><i class="fas fa-search"></i></button></td>
        </tr>
        </tfoot>
    </table>
    <div class="alert alert-danger d-none" id="id-res">
    </div>
</div>
<link rel="stylesheet" type="text/css" href="<c:url value="/libs/DataTables/datatables.min.css"/>"/>
<script src="<c:url value="/libs/DataTables/datatables.min.js"/>"></script>
<script>
    $(document).ready( function (key, value) {
        $('#userTable > thead, tfoot').find('> tr').prepend(
            $('<th/>', {
                text: ''
            })
        );

        const resDiv = $('#id-res');
        const unknownError = '<fmt:message key="generic.errors.unknownError"/>';

        const table = $('#userTable').on('xhr.dt', function (e, settings, json, xhr){
            if (json === null) {
                const json = JSON.parse(xhr.responseText);
                resDiv.removeClass("d-none");

                if(json['message'] !== undefined && json['message'] !== null) {
                    resDiv.html(json['message']);
                } else {
                    resDiv.html(unknownError);
                }
            } else {
                resDiv.addClass("d-none");
                resDiv.html("");
            }
        }).DataTable({
            ajax: {
                url: '<c:url value="/admin/users.json"/>',
                dataType: "json",
                type: "get",
                cache: "false",
                data: function(d) {
                    return $('#filterForm').serialize();
                },
                dataSrc: ''
            },
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
                }, {
                    target: 2,
                    data: 'img',
                    orderable: false,
                }, {
                    target: 3,
                    data: 'name'
                }, {
                    target: 4,
                    data: 'surname'
                }, {
                    target: 5,
                    data: 'email',
                }, {
                    target: 6,
                    data: 'password',
                    visible: false
                }, {
                    target: 7,
                    data: 'isAdmin',
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
                    defaultContent: ''
                }
            ],
            order: [[1, 'asc']],
            createdRow: function(row, data, index) {
                $('td', row).eq(2).html(
                    $('<img/>',{
                        src: '<c:url value="/${pageContext.servletContext.getInitParameter('avatarsFolder')}/"/>' + data.img,
                        class: "img-responsive"
                    })
                );

                $('td', row).eq(6).html(data.isAdmin?'<fmt:message key="users.label.true"/>':'<fmt:message key="users.label.false"/>');

                $('td', row).eq(7).html(
                    $('<div/>', {
                        html: [
                            $('<button/>', {
                                class: 'btn btn-md btn-primary',
                                title: '<fmt:message key="users.label.modifyUser"/>',
                                html: $('<i/>',{class: 'far fa-edit'}),
                            }),
                            $('<button/>', {
                                class: 'btn btn-md btn-danger',
                                title: '<fmt:message key="users.label.deleteUser"/>',
                                html: $('<i/>',{class: 'far fa-trash-alt'}),
                                click: function(){
                                    const btn = $(this);

                                    btn.attr("disabled", true);

                                    $.ajax({
                                        url: '<c:url value="/admin/users.json"/>',
                                        type: 'POST',
                                        data: {'action': 'delete', 'id': data.id}
                                    }).done(function(){
                                        row.remove();
                                    }).fail(function(jqXHR){
                                        const prevText = btn.html();

                                        if (typeof jqXHR.responseJSON === 'object' &&
                                            jqXHR.responseJSON !== null &&
                                            jqXHR.responseJSON['message'] !== undefined
                                        ) {
                                            btn.html(jqXHR.responseJSON['message']);

                                        } else {
                                            btn.html(unknownErrorMessage);
                                        }

                                        setTimeout(function(){
                                            btn.html(prevText);

                                            btn.attr("disabled", false);
                                        }, 2000);
                                    });
                                }
                            }),
                        ]
                    })
                );

            },
            searching: false
        });

        function format ( d ) {
            const ul = $('<ul/>', {
                class: 'list-unstyled',
                html: [
                    $('<li/>',{
                        html: [$('<b/>',{ text: '<fmt:message key="user.label.password"/>: '}), document.createTextNode(d.password)]
                    })
                ]
            });

            if (d.verifyEmailLink !== undefined) {
                ul.append(
                    $('<li/>',{
                        html: [$('<b/>',{ text: '<fmt:message key="user.label.verifyEmailLink"/>:  '}), document.createTextNode(d.verifyEmailLink)]
                    })
                );
            }

            if (d.resetPwdEmailLInk !== undefined) {
                ul.append(
                    $('<li/>',{
                        html: [$('<b/>',{ text: '<fmt:message key="user.label.resetPasswordLink"/>:  '}), document.createTextNode(d.resetPwdEmailLink)]
                    })
                )
            }

            return ul;
        }

        $('#userTable > tbody').on('click','td.details-control', function () {
            const tr = $(this).closest('tr');
            const row = table.row( tr );

            if ( row.child.isShown() ) {
                // This row is already open - close it
                row.child.hide();
                tr.removeClass('shown');

                tr.find('td.details-control i').removeClass("fa-minus-circle");
                tr.find('td.details-control i').addClass("fa-plus-circle");
            }
            else {
                // Open this row
                row.child( format(row.data()) ).show();
                tr.addClass('shown');

                tr.find('td.details-control i').removeClass("fa-plus-circle");
                tr.find('td.details-control i').addClass("fa-minus-circle");
            }
        });

        $('#userTable tfoot input').on('keyup', function () {
            table.ajax.reload();
            table.draw();
        });

        $.fn.dataTable.ext.errMode = 'throw';
    });
</script>
</body>
</html>
