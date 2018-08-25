<%@ page import="it.unitn.webprogramming18.dellmm.util.FormValidator"%>

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
                                <div class="input-group-prepend"><span class="input-group-text currentInput" data-what="${FormValidator.FIRST_NAME_KEY}"></span></div>
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
                                <div class="input-group-prepend"><span class="input-group-text currentInput" data-what="${FormValidator.LAST_NAME_KEY}"></span></div>
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
                                <div class="input-group-prepend"><span class="input-group-text currentInput" data-what="${FormValidator.EMAIL_KEY}"></span></div>
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
                            <p data-errorName="${FormValidator.AVATAR_IMG_KEY}" id="span${FormValidator.AVATAR_IMG_KEY}"></p>
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
<script>
    function initUserModal(table) {
        const modifyUserModal = $('#modifyUserModal');
        const modifyUserForm = modifyUserModal.find('#modifyUserForm');

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

        const checkEmailLazy = validationUtils.user.validateEmail(true, false, () => false,  '<c:url value="/service/checkUserService"/>', {
            emptyOrNull: '<fmt:message key="validateUser.errors.EMAIL_MISSING"/>',
            tooLong: '<fmt:message key="validateUser.errors.EMAIL_TOO_LONG"/>',
            emailInvalid: '<fmt:message key="validateUser.errors.EMAIL_NOT_VALID"/>',
            emailNoExists: 'noExist', // TODO: To i18n
            emailAlreadyActivated: 'alreadyActivated' // TODO: To i18n
        });

        const checkEmailStrict = validationUtils.user.validateEmail(false, false, () => false,  '<c:url value="/service/checkUserService"/>', {
            emptyOrNull: '<fmt:message key="validateUser.errors.EMAIL_MISSING"/>',
            tooLong: '<fmt:message key="validateUser.errors.EMAIL_TOO_LONG"/>',
            emailInvalid: '<fmt:message key="validateUser.errors.EMAIL_NOT_VALID"/>',
            emailNoExists: 'noExist', // TODO: To i18n
            emailAlreadyActivated: 'alreadyActivated' // TODO: To i18n
        });

        const checkName = validationUtils.validateString(${FormValidator.FIRST_NAME_MAX_LEN},() => false, {
            emptyOrNull: '<fmt:message key="validateUser.errors.FIRST_NAME_MISSING"/>',
            tooLong: '<fmt:message key="validateUser.errors.FIRST_NAME_TOO_LONG"/>',
        });

        const checkDescription = validationUtils.validateString(${FormValidator.LAST_NAME_MAX_LEN}, () => false, {
            emptyOrNull: '<fmt:message key="validateUser.errors.LAST_NAME_MISSING"/>',
            tooLong: '<fmt:message key="validateUser.errors.LAST_NAME_TOO_LONG"/>',
        });

        const checkPassword = validationUtils.user.validatePassword(() => false, {
            minLength: 8,
            maxLength: 44,
            minLower: 1,
            minUpper: 1,
            minDigits: 1,
            minSymbol: 1,
        },{
            passwordMissingOrEmpty:'<fmt:message key="validateUser.errors.PASSWORD_MISSING"/>',
            passwordTooLong:'<fmt:message key="validateUser.errors.PASSWORD_TOO_LONG"/>',
            passwordTooShort:'<fmt:message key="validateUser.errors.PASSWORD_TOO_SHORT"/>',
            passwordInvalid:'<fmt:message key="validateUser.errors.PASSWORD_NOT_VALID"/>',
        });

        const checkAvatar = validationUtils.user.validateAvatar(
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

                checkAvatar(obj, modifyUserForm, '${FormValidator.AVATAR_KEY}', '${FormValidator.AVATAR_IMG_KEY}');

                return validationUtils.updateVerifyMessages(modifyUserForm, obj);
            }
        }

        formUtils.timedChange(modifyUserForm, validation(true));

        {
            const pwdInput = $('[name="${FormValidator.FIRST_PWD_KEY}"]', modifyUserForm);
            //visualizza la barra della valutazione di password
            pwdInput.focusin(function () {
                $(".progress-bar-div", modifyUserForm).show("slow");
            });
            //nasconde la barra della valutazione di password
            pwdInput.focusout(function () {
                $(".progress-bar-div", modifyUserForm).hide("slow");
            });

            pwdInput.on("keyup", function () {
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
        }


        const strictValidation = validation(false);

        modifyUserForm.submit(function (e) {
            e.preventDefault();

            if(strictValidation()) {
                validationUtils.formSubmitWithValidation(
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

        $('[name="${FormValidator.AVATAR_KEY}"]', modifyUserForm).change(function(e){
            if ($(this).val() === 'custom') {
                $(".custom-avatar-uploader", modifyUserForm).show("slow");
                $(".custom-file-input", modifyUserForm).attr("required", "required");
            } else {
                $(".custom-avatar-uploader", modifyUserForm).hide("slow");
                $(".custom-file-input", modifyUserForm).val("");
                $(".custom-file-input", modifyUserForm).removeAttr("required");
                $(".custom-file-label", modifyUserForm).html($(".custom-file-label-origin", modifyUserForm).html());
            }
        });

        modifyUserModal.on('show.bs.modal', function (event) {
            validationUtils.clearVerifyMessages(modifyUserForm);

            modifyUserForm[0].reset();
            modifyUserForm.find('#customImgLabel').remove();
            progressBar.css('width', '0');

            // Get the button that triggered the modal, get the whole row, get the data that created the row
            const data = $(event.relatedTarget).closest('tr').data('json');

            $('.custom-avatar-uploader', modifyUserForm).hide();
            $('#avatar-custom', modifyUserForm).removeClass('is-invalid');
            $('.error-messages [data-errorname="${FormValidator.AVATAR_IMG_KEY}"]', modifyUserForm).html('');


            $('input[name="id"]', modifyUserForm).val(data.id);
            $('.currentInput[data-what="${FormValidator.FIRST_NAME_KEY}"]', modifyUserForm).html(data.name);
            $('.currentInput[data-what="${FormValidator.LAST_NAME_KEY}"]', modifyUserForm).html(data.surname);
            $('.currentInput[data-what="${FormValidator.EMAIL_KEY}"]', modifyUserForm).html(data.email);

            if (/^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}\..*$/.test(data.img)) {
                $('#avatarDiv', modifyUserForm).prepend(

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
                $('input[name="${FormValidator.AVATAR_KEY}"][value="' + data.img + '"]', modifyUserForm).prop("checked", true);
            }
        });
    }
</script>

