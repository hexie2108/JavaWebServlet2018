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
                                <div class="input-group-prepend"><span class="input-group-text"><i class="fas fa-user"></i></span></div>
                                <div class="input-group-prepend"><span class="input-group-text" id="roSpanFirstName"></span></div>
                                <input id="inputFirstName" class="form-control" placeholder="<fmt:message key="user.label.name"/>" autofocus=""
                                       type="text" name="${FormValidator.FIRST_NAME_KEY}" >
                            </div>
                            <div class="error-messages">
                                <p id="span${FormValidator.FIRST_NAME_KEY}"></p>
                            </div>
                        </div>

                        <div class="col-sm-6">
                            <label for="inputLastName" class="sr-only"><fmt:message key="user.label.surname"/></label>
                            <div class="input-group">
                                <div class="input-group-prepend"><span class="input-group-text"><i class="fas fa-user"></i></span></div>
                                <div class="input-group-prepend"><span class="input-group-text" id="roSpanLastName"></span></div>
                                <input id="inputLastName" class="form-control" placeholder="<fmt:message key="user.label.surname"/>" autofocus=""
                                       type="text" name="${FormValidator.LAST_NAME_KEY}">
                            </div>
                            <div class="error-messages">
                                <p id="span${FormValidator.LAST_NAME_KEY}"></p>
                            </div>
                        </div>
                    </div>

                    <div class="form-group row">
                        <div class="col-sm-6">
                            <label for="inputEmail" class="sr-only"><fmt:message key="user.label.email"/></label>
                            <div class="input-group">
                                <div class="input-group-prepend"><span class="input-group-text"><i class="fas fa-at"></i></span></div>
                                <div class="input-group-prepend"><span class="input-group-text" id="roSpanEmail"></span></div>
                                <input id="inputEmail" class="form-control" placeholder="<fmt:message key="user.label.email"/>" autofocus=""
                                       type="email" name="${FormValidator.EMAIL_KEY}">
                            </div>
                            <div class="error-messages">
                                <p id="span${FormValidator.EMAIL_KEY}"></p>
                            </div>
                        </div>

                        <div class="col-sm-6">
                            <label for="inputPassword" class="sr-only"><fmt:message key="user.label.password"/></label>
                            <div class="input-group">
                                <div class="input-group-prepend"><span class="input-group-text"><i class="fas fa-key"></i></span></div>
                                <input id="inputPassword" class="form-control" placeholder="<fmt:message key="user.label.password"/>"
                                       type="password" name="${FormValidator.FIRST_PWD_KEY}" value="">
                            </div>
                            <div class="progress progress-bar-div">
                                <div class="progress-bar progress-bar-striped progress-bar-animated"></div>
                            </div>
                            <div class="error-messages">
                                <p id="span${FormValidator.FIRST_PWD_KEY}"></p>
                            </div>
                        </div>
                    </div>

                    <div class="form-group" id="avatarDiv">
                            <c:forEach items="${FormValidator.DEFAULT_AVATARS}" var="av" varStatus="status">
                                <div class="avatar-box custom-control custom-radio custom-control-inline">
                                    <input type="radio" class="custom-control-input  img-radio default-avatar" id="avatar-${status.index}" name="${FormValidator.AVATAR_KEY}" value="${av}" ${status.index==0?"checked":""} required="required" />
                                    <label class="custom-control-label" for="avatar-${status.index}">
                                        <img class="img-input img-fluid" src="<c:url value="/image/user/${av}"/>" />
                                        <span class="img-check">
                                                <i class="far fa-check-circle "></i>
                                        </span>
                                    </label>

                                </div>
                            </c:forEach>

                            <div class="avatar-box custom-control custom-radio custom-control-inline">
                                <input type="radio" class="custom-control-input  img-radio" id="avatar-custom" name="${FormValidator.AVATAR_KEY}" value="custom"  required="required" />
                                <label class="custom-control-label" for="avatar-custom">
                                    <img class="img-input img-fluid" src="<c:url value="/image/base/custom-avatar.svg"/>" />
                                    <span class="img-check">
                                            <i class="far fa-check-circle "></i>
                                    </span>
                                </label>

                            </div>
                        <div class="error-messages">
                            <p id="span${FormValidator.AVATAR_KEY}"></p>
                        </div>
                    </div>

                    <div class="form-group custom-avatar-uploader">
                        <div class=" custom-file input-group mb-3">
                            <input type="file" class="custom-file-input"  id="customAvatarImg" name="${FormValidator.AVATAR_IMG_KEY}" accept="image/jpeg, image/png, image/gif, image/bmp">
                            <label class="custom-file-label input-box" for="customAvatarImg">seleziona file</label>
                            <%-- serve per ripristinare il segnaposto --%>
                            <label class="custom-file-label-origin d-none">seleziona file</label>
                        </div>
                        <%--parte di suggerimenti --%>
                        <div class="form-group">
                            <label>accetta solo file *.jpg, *.png, *.gif, *.bmp</label> <%-- TODO: To i18n --%>
                        </div>
                        <div class="error-messages">
                            <p id="span${FormValidator.AVATAR_IMG_KEY}"></p>
                        </div>
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
<link rel="stylesheet" type="text/css" href="<c:url value="/css/validation.css"/>"/>
<script src="<c:url value="/libs/DataTables/datatables.min.js"/>"></script>
<!-- <script src="<c:url value="/js/userValidate.js"/>"></script> -->
<script src="<c:url value="/js/verification.js"/>"></script>
<script src="<c:url value="/libs/zxcvbn/zxcvbn.js"/>"></script>
<script>
    $(document).ready(function () {
        const tableDiv = $('#userTable');

        const unknownError = '<fmt:message key="generic.errors.unknownError"/>';

        const modifyUserModal = $('#modifyUserModal');
        const modifyUserForm = modifyUserModal.find('#modifyUserForm');

        tableDiv.find('> thead, tfoot').find('> tr').prepend(
            $('<th/>', {
                text: ''
            })
        );

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

            showErrorAlert('<fmt:message key="generic.label.errorTitle"/>', err, '<fmt:message key="generic.label.close"/>');
        });

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
                    showErrorAlert(
                        '<fmt:message key="generic.label.errorTitle"/>',
                        jqXHR.responseJSON['message'],
                        '<fmt:message key="generic.label.close"/>'
                    );

                } else {
                    showErrorAlert(
                        '<fmt:message key="generic.label.errorTitle"/>',
                        unknownError,
                        '<fmt:message key="generic.label.close"/>'
                    );
                }
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

                    if (/^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}\..*$/.test(data.img)) {
                        modifyUserForm.find('#avatarDiv').prepend(

                            '<div class="avatar-box custom-control custom-radio custom-control-inline" id="customImgLabel">' +
                            '   <input type="radio" class="custom-control-input  img-radio" name="${FormValidator.AVATAR_KEY}" value="" checked id="customImgInput"/>' +
                            '   <label class="custom-control-label" for="customImgInput">' +
                            '       <img class="img-input img-fluid" src="<c:url value="/${pageContext.servletContext.getInitParameter('avatarsFolder')}/"/>' + data.img + '" />' +
                            '       <span class="img-check">' +
                            '           <i class="far fa-check-circle "></i>' +
                            '       </span>' +
                            '   </label>' +
                            '</div>'
                        );
                    } else {
                        modifyUserForm.find('input[name="${FormValidator.AVATAR_KEY}"][value="' + data.img + '"]').prop("checked", true);
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
                        () => {
                            table.ajax.reload();
                            table.draw();
                        }
                    );
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
                        () => {
                            table.ajax.reload();
                            table.draw();
                        }
                    );
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

        timedChange(
            tableDiv.find('tfoot'),
            function () {
                history.replaceState(undefined, undefined, "users?" + tableDiv.find('tfoot').find('input,select').serialize());
                table.ajax.reload();
                table.draw();
            }
        );

        $.fn.dataTable.ext.errMode = 'throw';

        {
            //elimina gli spazi di input
            $(".input-group input.input-box", modifyUserForm).change(function () {
                $(this).val($.trim($(this).val()));
            });

            //visualizza il nome file nel cutom-file-input di form
            $(".custom-file-input", modifyUserForm).on("change", function () {
                //get il nome di file
                var fileName = $(this)[0].files[0].name;
                //sostituisce il contenuto del "custom-file-label" label
                $(this).next(".custom-file-label").html(fileName);

            });

            const progressBar = modifyUserForm.find(".progress-bar");

            modifyUserModal.on("hidden.bs.modal", function () {
                clearVerifyMessages(modifyUserForm);

                modifyUserForm[0].reset();
                modifyUserForm.find('#customImgLabel').remove();
                progressBar.css('width', '0');
            });


            modifyUserForm.find('[name="${FormValidator.FIRST_PWD_KEY}"]').focusin(function () {
                $(".progress-bar-div").show("slow");
            });
            //nasconde la barra della valutazione di password
            modifyUserForm.find('[name="${FormValidator.FIRST_PWD_KEY}"]').focusout(function () {
                $(".progress-bar-div").hide("slow");
            });

            function validateEmail(lazy, required, url, errors) {
                const emptyOrNull = errors.emptyOrNull;
                const tooLong = errors.tooLong;
                const emailInvalid = errors.emailInvalid;
                const emailNoExists = errors.emailNoExists;
                const emailAlreadyActivated = errors.emailAlreadyActivated;

                return function(obj, form, name) {
                    const emailVal = form.find('[name="'+name+'"]').val();

                    validateString(${FormValidator.EMAIL_MAX_LEN}, required, {
                        emptyOrNull,
                        tooLong
                    });

                    if (!lazy && obj[name] === undefined && emailVal !== '') {
                        //espressione per controllare il formatto di email
                        const reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
                        //se è vuoto o se supera la lunghezza massima o non rispetta il formatto di email
                        if (!reg.test(emailVal)) {
                            obj[name] = emailInvalid;
                        } else {
                            //poi bisogna verificare se tale email esiste o no
                            //e verifica lo stato di attivazione dell'account con tale email

                            //get valore di email
                            var result;
                            $.ajax({
                                url: '<c:url value="/service/checkUserService"/>',
                                data: {action: "checkActivationStatus", email: emailVal},
                                type: 'POST',
                                dataType: "text",
                                async: false,
                                cache: false,
                                error: function () {
                                    alert('error to check email existence, retry submit');
                                },
                                success: function (data) {
                                    //se email non esiste
                                    if (data === "0") {
                                        obj[name] = emailNoExists;
                                    } else if (data === "1") {
                                        obj[name] = emailAlreadyActivated;
                                    }
                                }
                            });
                        }
                    }
                };
            }

            const checkEmailLazy = validateEmail(true, () => false,  '<c:url value="/service/checkUserService"/>', {
                emptyOrNull: '<fmt:message key="validateUser.errors.EMAIL_MISSING"/>',
                tooLong: '<fmt:message key="validateUser.errors.EMAIL_TOO_LONG"/>',
                emailInvalid: '<fmt:message key="validateUser.errors.EMAIL_NOT_VALID"/>',
                emailNoExists: 'noExist', // TODO: To i18n
                emailAlreadyActivated: 'alreadyActivated' // TODO: To i18n
            });

            const checkEmailStrict = validateEmail(false, () => false,  '<c:url value="/service/checkUserService"/>', {
                emptyOrNull: '<fmt:message key="validateUser.errors.EMAIL_MISSING"/>',
                tooLong: '<fmt:message key="validateUser.errors.EMAIL_TOO_LONG"/>',
                emailInvalid: '<fmt:message key="validateUser.errors.EMAIL_NOT_VALID"/>',
                emailNoExists: 'noExist', // TODO: To i18n
                emailAlreadyActivated: 'alreadyActivated' // TODO: To i18n
            });

            const checkName = validateString(${FormValidator.FIRST_NAME_MAX_LEN},() => false, {
                emptyOrNull: '<fmt:message key="validateUser.errors.FIRST_NAME_MISSING"/>',
                tooLong: '<fmt:message key="validateUser.errors.FIRST_NAME_TOO_LONG"/>',
            });

            const checkDescription = validateString(${FormValidator.LAST_NAME_MAX_LEN}, () => false, {
                emptyOrNull: '<fmt:message key="validateUser.errors.LAST_NAME_MISSING"/>',
                tooLong: '<fmt:message key="validateUser.errors.LAST_NAME_TOO_LONG"/>',
            });

            function validatePassword(required) {
                return function (obj, form, name) {
                    var password = form.find('[name="' + name + '"]').val();

                    //check password
                    //se è vuoto o se supera la lunghezza massima o se è troppo corta
                    if (password === "") {
                        if (required(form, name)) {
                            obj[name] = '<fmt:message key="validateUser.errors.PASSWORD_MISSING"/>';
                        }
                    } else if (password.length > 44) {
                        obj[name] = '<fmt:message key="validateUser.errors.PASSWORD_TOO_LONG"/>';
                    } else if (password.length < 8) {
                        obj[name] = '<fmt:message key="validateUser.errors.PASSWORD_TOO_SHORT"/>';
                    } else {
                        let lower = 0;
                        let upper = 0;
                        let number = 0;
                        let symbol = 0;

                        for (let i = 0; i < password.length; i++) {
                            const letter = password.charAt(i);

                            if (letter === letter.toLowerCase() && letter !== letter.toUpperCase()) {
                                lower++;
                            } else if (letter !== letter.toLowerCase() && letter === letter.toUpperCase()) {
                                upper++;
                            } else if (/\d/.test(letter)) {
                                number++;
                            } else if (/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(letter)) {
                                symbol++;
                            }
                        }

                        //se password non ha un carattere minuscolo, un maiuscolo, un numero e un simbolo, è invalido
                        if ((lower < 1) || (upper < 1) || (number < 1) || (symbol < 1)) {
                            obj[name] = '<fmt:message key="validateUser.errors.PASSWORD_NOT_VALID"/>';
                        }
                    }
                };
            }

            const checkPassword = validatePassword(() => false);

            function validatePassword2(errors) {
                const password2Missing = errors.password2Missing;
                const password2NotSame = errors.password2NotSame;

                if (password2Missing === undefined) {
                    console.error('errors.password2Missing is undefined');
                    return;
                }

                if (password2NotSame === undefined) {
                    console.error('errors.password2NotSame is undefined');
                    return;
                }

                return function(obj, form, namePwd1, namePwd2) {
                    const password = form.find('[name="' + namePwd1 + '"]').val();
                    const password2 = form.find('[name="' + namePwd2 + '"]').val();

                    if (password2 === "") {
                        obj[namePwd2] = "<fmt:message key="validateUser.errors.PASSWORD2_MISSING"/>";
                    } else if (password2 !== password) {
                        obj[namePwd2] = "<fmt:message key="validateUser.errors.PASSWORD2_NOT_SAME"/>";
                    }
                };
            }

            /*
            TODO: move
            const checkPassword2 = validatePassword2({
                password2Missing:"<fmt:message key="validateUser.errors.PASSWORD2_MISSING"/>",
                password2NotSame:"<fmt:message key="validateUser.errors.PASSWORD2_NOT_SAME"/>",
            });
            */


            function validateAvatar(regexContentType, arrayOfDefaultAvatar, maxFileLen, required, errors)  {
                if (maxFileLen === undefined) {
                    console.error('maxFileLen is undefined');
                    return;
                }
                if (errors === undefined) {
                    console.error("errors is undefined");
                    return;
                }

                const fileEmptyOrNull = errors.fileEmptyOrNull;
                const fileTooBig = errors.fileTooBig;
                const fileContentTypeMissingOrType = errors.fileContentTypeMissingOrType;
                const fileOfWrongType = errors.fileOfWrongType;
                const selectValMissing = errors.selectValMissing;
                const selectValInvalid = errors.selectValInvalid;

                if ( fileEmptyOrNull === undefined ) {
                    console.error('fileEmptyOrNull is undefined');
                    return;
                }

                if ( fileTooBig === undefined ) {
                    console.error('fileTooBig is undefined');
                    return;
                }

                if ( fileContentTypeMissingOrType === undefined ) {
                    console.error('fileContentTypeMissingOrType is undefined');
                    return;
                }

                if ( fileOfWrongType === undefined ) {
                    console.error('fileOfWrongType is undefined');
                    return;
                }

                if ( selectValMissing === undefined ) {
                    console.error('selectValMissing is undefined');
                    return;
                }

                if ( selectValInvalid === undefined ) {
                    console.error('selectValInvalid is undefined');
                    return;
                }

                const checkFile = add_file_errors(regexContentType, maxFileLen, () => true, {
                    fileEmptyOrNull,
                    fileTooBig,
                    fileContentTypeMissingOrType,
                    fileOfWrongType,
                });

                return function(obj, form, selectName, fileName) {
                    const select = form.find('[name="' + selectName + '"]:checked').val();

                    if (select === undefined) {
                        // if selected value is empty
                        if (required(form, name)) {
                            obj[selectName] = selectValMissing;
                        }
                    } else if (select === "custom") {
                        // if selected value is custom than check file
                        checkFile(obj, form, fileName);
                    } else if (arrayOfDefaultAvatar.indexOf(select) === -1) {
                        // if selected value is not one of the recognized one
                        obj[selectName] = selectValInvalid;
                    }
                }
            }

            const checkAvatar = validateAvatar(
                /.*(jpg|jpeg|png|gif|bmp).*/,
                ["","user.svg", "user-astronaut.svg", "user-ninja.svg", "user-secret.svg"],
                ${FormValidator.MAX_LEN_FILE},
                () => false, {
                    fileEmptyOrNull: '<fmt:message key="validateUser.errors.AVATAR_IMG_MISSING"/>',
                    fileTooBig: '<fmt:message key="validateUser.errors.AVATAR_IMG_TOO_BIG"/>',
                    fileContentTypeMissingOrType: "<fmt:message key="validateUser.errors.AVATAR_FILE_CONTENT_MISSING_OR_EMTPY"/>",
                    fileOfWrongType: '<fmt:message key="validateUser.errors.AVATAR_IMG_NOT_IMG"/>',
                    selectValMissing: '<fmt:message key="validateUser.errors.AVATAR_MISSING"/>',
                    selectValInvalid: '<fmt:message key="validateUser.errors.AVATAR_NOT_VALID"/>',
                }
            );

            function validation(lazy) {
                return function(){
                    const obj = {};

                    if(lazy) {
                        checkEmailLazy(obj, modifyUserForm, '${FormValidator.EMAIL_KEY}');
                    } else {
                        checkEmailStrict(obj, modifyUserForm, '${FormValidator.EMAIL_KEY}');
                    }
                    checkName(obj, modifyUserForm, '${FormValidator.FIRST_NAME_KEY}');
                    checkDescription(obj, modifyUserForm, '${FormValidator.LAST_NAME_KEY}');

                    checkPassword(obj, modifyUserForm, '${FormValidator.FIRST_PWD_KEY}');
                    //checkPassword2(obj, modifyUserForm, '${FormValidator.FIRST_PWD_KEY}', '${FormValidator.SECOND_PWD_KEY}');

                    checkAvatar(obj, modifyUserForm, '${FormValidator.AVATAR_KEY}', '${FormValidator.AVATAR_IMG_KEY}');

                    updateVerifyMessages(modifyUserForm, obj);

                    return $.isEmptyObject(obj);
                }
            }

            timedChange(modifyUserForm, validation(true));

            //visualizza la barra della valutazione di password
            modifyUserForm.find('[name="${FormValidator.FIRST_PWD_KEY}}"]').focusin(function () {
                $(".progress-bar-div").show("slow");
            });
            //nasconde la barra della valutazione di password
            modifyUserForm.find('[name="${FormValidator.FIRST_PWD_KEY}}"]').focusout(function () {
                $(".progress-bar-div").hide("slow");
            });

            modifyUserForm.find('[name="${FormValidator.FIRST_PWD_KEY}"]').on("keyup", function () {
                const score = zxcvbn(this.value).score;

                progressBar.css("width", (score+(this.value === ''?0:1))*20.0 + "%");

                progressBar.removeClass("bg-secondary");
                progressBar.removeClass("bg-danger");
                progressBar.removeClass("bg-warning");
                progressBar.removeClass("bg-info");
                progressBar.removeClass("bg-success");

                switch (score) {
                    case 0: progressBar.addClass("bg-secondary"); break;
                    case 1: progressBar.addClass("bg-danger"); break;
                    case 2: progressBar.addClass("bg-warning"); break;
                    case 3: progressBar.addClass("bg-info"); break;
                    case 4: progressBar.addClass("bg-success"); break;
                }
            });


            const strictValidation = validation(false);

            modifyUserForm.submit(function (e) {
                e.preventDefault();

                if (!$('#customAvatar').is(':checked')) {
                    $('#customAvatarImg').val("");
                }

                if(strictValidation()) {
                    formSubmit(
                        '<c:url value="/admin/users.json"/>',
                        modifyUserForm, {
                            multipart: true,
                            session: false,
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
                            successCallback: function () {
                                table.ajax.reload();
                                table.draw();
                                modifyUserModal.modal('toggle');
                            }
                        }
                    );
                }
            });

            modifyUserForm.find('[name="${FormValidator.AVATAR_KEY}"]').change((e) => {
                if (e.target.value === 'custom') {
                    $(".custom-avatar-uploader", modifyUserForm).show("slow");
                    $(".custom-file-input", modifyUserForm).attr("required", "required");
                } else {
                    $(".custom-avatar-uploader", modifyUserForm).hide("slow");
                    $(".custom-file-input", modifyUserForm).val("");
                    $(".custom-file-input", modifyUserForm).removeAttr("required");
                    $(".custom-file-label", modifyUserForm).html($(".custom-file-label-origin").html());
                }
            })
        }
    });
</script>
</body>
</html>
