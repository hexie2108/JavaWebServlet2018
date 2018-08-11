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
    <title><fmt:message key="users.title"/></title>

    <script src="<c:url value="/libs/jquery/jquery-3.3.1.min.js"/>"></script>
    <script src="<c:url value="/libs/bootstrap-4.1.1-dist/js/bootstrap.bundle.min.js"/>"></script>
    <link rel="stylesheet" href="<c:url value="/libs/bootstrap-4.1.1-dist/css/bootstrap.min.css"/>">

    <link rel="stylesheet" href="<c:url value="/libs/fontawesome-free-5.1.1-web/css/all.min.css"/>" type="text/css"
          media="all">
    <link rel="stylesheet" href="<c:url value="/css/userPages.css"/>" type="text/css" media="all">


    <link rel="stylesheet" href="<c:url value="/css/adminPages.css"/>" type="text/css" media="all"/>
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
    <div class="alert alert-danger d-none" id="id-res">
    </div>
</div>
<div class="modal fade" id="modifyUserModal">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><fmt:message key="users.label.modifyUser"/></h4>
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="<fmt:message key="generic.label.close"/>">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="modifyUserForm" method="post" enctype="multipart/form-data">
                    <input class="form-control" type="hidden" name="id" value=""/>
                    <input class="form-control" type="hidden" name="action" value="modify"/>
                    <div class="form-group row">
                        <div class="col-sm-6">
                            <label for="inputFirstName" class="sr-only"><fmt:message key="user.label.name"/></label>
                            <div class="input-group ">
                                <div class="input-group-prepend"><i class="input-group-text fas fa-user"></i></div>
                                <div class="input-group-prepend"><span class="input-group-text"
                                                                       id="roSpanFirstName"></span></div>
                                <input id="inputFirstName" class="form-control"
                                       placeholder="<fmt:message key="user.label.name"/>" autofocus=""
                                       type="text" name="${RegistrationValidator.FIRST_NAME_KEY}">
                                <span id="spanFirstName"></span>
                            </div>
                        </div>

                        <div class="col-sm-6">
                            <label for="inputLastName" class="sr-only"><fmt:message key="user.label.surname"/></label>
                            <div class="input-group">
                                <div class="input-group-prepend"><i class="input-group-text fas fa-user"></i></div>
                                <div class="input-group-prepend"><span class="input-group-text"
                                                                       id="roSpanLastName"></span></div>
                                <input id="inputLastName" class="form-control"
                                       placeholder="<fmt:message key="user.label.surname"/>" autofocus=""
                                       type="text" name="${RegistrationValidator.LAST_NAME_KEY}">
                                <span id="spanLastName"></span>
                            </div>
                        </div>
                    </div>

                    <div class="form-group row">
                        <div class="col-sm-6">
                            <label for="inputEmail" class="sr-only"><fmt:message key="user.label.email"/></label>
                            <div class="input-group">
                                <div class="input-group-prepend"><i class="input-group-text fas fa-at"></i></div>
                                <div class="input-group-prepend"><span class="input-group-text" id="roSpanEmail"></span>
                                </div>
                                <input id="inputEmail" class="form-control"
                                       placeholder="<fmt:message key="user.label.email"/>" autofocus=""
                                       type="email" name="${RegistrationValidator.EMAIL_KEY}">
                                <span id="spanEmail"></span>
                            </div>
                        </div>

                        <div class="col-sm-6">
                            <label for="inputPassword" class="sr-only"><fmt:message key="user.label.password"/></label>
                            <div class="input-group">
                                <div class="input-group-prepend"><i class="input-group-text fas fa-key"></i></div>
                                <input id="inputPassword" class="form-control"
                                       placeholder="<fmt:message key="user.label.password"/>"
                                       type="password" name="${RegistrationValidator.FIRST_PWD_KEY}" value="">
                                <div class="input-group-append"><span class="input-group-text"
                                                                      id="strongPassword"><fmt:message
                                        key="user.label.passwordScore"/>: x/x</span></div>
                                <span id="spanPassword"></span>
                            </div>
                        </div>
                    </div>

                    <div class="form-group" id="avatarDiv">
                        <c:forEach items="${RegistrationValidator.DEFAULT_AVATARS}" var="av" varStatus="st">
                            <label>
                                <input class="d-none img-radio" type="radio" name="${RegistrationValidator.AVATAR_KEY}"
                                       value="${av}">
                                <img src="<c:url value="/${pageContext.servletContext.getInitParameter('avatarsFolder')}/${av}"/>"
                                     class="img-input"
                                ><i class="far fa-check-circle img-check"></i>
                            </label>
                        </c:forEach>
                        <label>
                            <input class="d-none img-radio" type="radio" name="${RegistrationValidator.AVATAR_KEY}"
                                   value="custom" id="customAvatar">
                            <img src="<c:url value="/libs/fontawesome-free-5.1.1-web/svgs/regular/plus-square.svg"/>"
                                 class="img-input"
                            ><i class="far fa-check-circle img-check"></i>
                            <input id="customAvatarImg"
                                   type="file" name="${RegistrationValidator.AVATAR_IMG_KEY}"
                                   accept="image/*">
                        </label>
                        <span id="spanAvatar"></span>
                        <span id="spanAvatarImg"></span>
                    </div>
                </form>

                <div class="alert d-none" id="id-modal-res">
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal"><fmt:message
                        key="generic.label.close"/></button>
                <button type="submit" form="modifyUserForm" class="btn btn-primary"><fmt:message
                        key="users.modifyUserForm.submit"/></button>
            </div>
        </div>
    </div>
</div>
<link rel="stylesheet" type="text/css" href="<c:url value="/libs/DataTables/datatables.min.css"/>"/>
<script src="<c:url value="/libs/DataTables/datatables.min.js"/>"></script>
<script src="<c:url value="/js/userValidate.js"/>"></script>
<script src="<c:url value="/libs/zxcvbn/zxcvbn.js"/>"></script>
<script>
    $(document).ready(function () {
        const tableDiv = $('#userTable');

        const resDiv = $('#id-res');
        const unknownError = '<fmt:message key="generic.errors.unknownError"/>';

        const modifyUserModal = $('#modifyUserModal');
        const modifyUserForm = modifyUserModal.find('#modifyUserForm');

        tableDiv.find('> thead, tfoot').find('> tr').prepend(
            $('<th/>', {
                text: ''
            })
        );


        tableDiv.on('xhr.dt', function (e, settings, json, xhr) {
            if (json === null) {
                const json = JSON.parse(xhr.responseText);
                resDiv.removeClass("d-none");

                if (json['message'] !== undefined && json['message'] !== null) {
                    resDiv.html(json['message']);
                } else {
                    resDiv.html(unknownError);
                }
            } else {
                resDiv.addClass("d-none");
                resDiv.html("");
            }
        });

        function ajaxButton(url, data, btn, successCallback) {
            btn.attr("disabled", true);

            $.ajax({
                url: url,
                type: 'POST',
                data: data
            }).done(function () {
                successCallback();
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
                click: function () {
                    modifyUserForm.find('input[name="id"]').val(data.id);
                    modifyUserForm.find('#roSpanFirstName').html(data.name);
                    modifyUserForm.find('#roSpanLastName').html(data.surname);
                    modifyUserForm.find('#roSpanEmail').html(data.email);

                    if (/^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$/.test(data.img)) {
                        modifyUserForm.find('#avatarDiv').prepend(
                            '<label id="customImgLabel">' +
                            '    <input class="d-none img-radio" required="" type="radio" name="${RegistrationValidator.AVATAR_KEY}" value="" checked>' +
                            '    <img src="<c:url value="${pageContext.servletContext.getInitParameter('avatarsFolder')}/"/>' + data.img + '" class="img-input"' +
                            '        ><i class="far fa-check-circle img-check"></i>' +
                            '</label>'
                        );
                    } else {
                        modifyUserForm.find('input[name="${RegistrationValidator.AVATAR_KEY}"][value="' + data.img + '"]').prop("checked", true);
                    }
                }
            });

            const upgradeUserButton = $('<button/>', {
                class: 'btn btn-md btn-danger',
                title: data.isAdmin ? '<fmt:message key="users.label.downgradeUser"/>' : '<fmt:message key="users.label.upgradeUser"/>',
                html: data.isAdmin ? '<i class="fas fa-level-down-alt"></i>' : '<i class="fas fa-level-up-alt"></i>',
                click: function () {
                    ajaxButton(
                        '<c:url value="/admin/upgradeUserToAdmin.json"/>',
                        {'email': data.email, 'admin': !data.isAdmin},
                        $(this),
                        (btn) => {
                            table.ajax.reload();
                            table.draw();
                        }
                    )
                }
            });

            const deleteUser = $('<button/>', {
                class: 'btn btn-md btn-danger',
                title: '<fmt:message key="users.label.deleteUser"/>',
                html: $('<i/>', {class: 'far fa-trash-alt'}),
                click: function () {
                    ajaxButton(
                        '<c:url value="/admin/users.json"/>',
                        {'action': 'delete', 'id': data.id},
                        $(this),
                        (btn) => row.remove())
                }
            });


            $('td', row).eq(2).html(imgAvatar);
            $('td', row).eq(6).html(admin);

            $('td', row).eq(7).html(
                $('<div/>', {
                    class: 'btn-group',
                    html: [modifyUserButton, upgradeUserButton, deleteUser]
                })
            );

        }


        const table = tableDiv.DataTable({
            ajax: {
                url: '<c:url value="/admin/users.json"/>',
                dataType: "json",
                type: "get",
                cache: "false",
                data: function (d) {
                    return tableDiv.find('#filterForm').serialize();
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
            createdRow: formatRow,
            searching: false
        });

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

        tableDiv.find('> tbody').on('click', 'td.details-control', function () {
            const tr = $(this).closest('tr');
            const row = table.row(tr);

            if (row.child.isShown()) {
                // This row is already open - close it
                row.child.hide();
                tr.removeClass('shown');

                tr.find('td.details-control i').removeClass("fa-minus-circle");
                tr.find('td.details-control i').addClass("fa-plus-circle");
            }
            else {
                // Open this row
                row.child(format(row.data())).show();
                tr.addClass('shown');

                tr.find('td.details-control i').removeClass("fa-plus-circle");
                tr.find('td.details-control i').addClass("fa-minus-circle");
            }
        });

        tableDiv.find('tfoot').find('input,select').on('keyup change', function () {
            history.replaceState(undefined, undefined, "users?" + $('#userTable tfoot').find('input,select').serialize());
            table.ajax.reload();
            table.draw();
        });

        $.fn.dataTable.ext.errMode = 'throw';

        {
            const URL = '<c:url value="/${PagePathsConstants.VALIDATE_REGISTRATION}"/>';
            const urlJSON = '<c:url value="/admin/users.json"/>';

            const resDiv = $('#id-modal-res');
            const strPwd = modifyUserForm.find('#strongPassword');

            const unknownErrorMessage = '<fmt:message key="generic.errors.unknownError"/>';
            const successMessage = '<fmt:message key="generic.success"/>';

            modifyUserModal.on("hidden.bs.modal", function () {
                modifyUserForm[0].reset();
                $('#customImgLabel').remove();
            });

            modifyUserForm.find('input').blur(() => {
                request_user_validation(modifyUserForm, true, URL).done((d) => updateVerifyMessages(modifyUserForm, add_file_errors(modifyUserForm, d)));
            });

            modifyUserForm.find('#inputPassword').on("keyup", function () {
                strPwd.text("<fmt:message key="user.label.passwordScore"/>: " + zxcvbn(this.value).score + "/4");
            });

            modifyUserForm.submit(function (e) {
                e.preventDefault();

                if (!$('#customAvatar').is(':checked')) {
                    $('#customAvatarImg').val("");
                }

                formSubmit(
                    urlJSON,
                    modifyUserForm, {
                        'multipart': true,
                        'session': false,
                        'redirectUrl': null,
                        'unknownErrorMessage': unknownErrorMessage,
                        'successMessage': successMessage,
                        'resDiv': resDiv,
                        'successCallback': function () {
                            table.ajax.reload();
                            table.draw();
                            modifyUserModal.modal('toggle');
                        }
                    }
                );
            });
        }
    });
</script>
</body>
</html>
