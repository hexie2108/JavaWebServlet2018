"use strict";

function showErrorAlert(title, message, closeLabel) {
    const x = $(
        '<div class="modal fade modal-danger" id="errorAlertBox" role="dialog">' +
        '     <div class="modal-dialog">' +
        '        <div class="modal-content">' +
        '            <div class="modal-header">' +
        '                <h4 class="modal-title"><i class="fas fa-exclamation-triangle"></i> ' + title + '</h4>' +
        '                <button type="button" class="close" data-dismiss="modal">&times;</button>' +
        '            </div>' +
        '            <div class="modal-body">' + message +'</div>' +
        '            <div class="modal-footer">' +
        '                <button type="button" class="btn btn-secondary" data-dismiss="modal">' + closeLabel + '</button>' +
        '            </div>' +
        '        </div>' +
        '    </div>' +
        '</div>'
    );

    $('body').append(x);
    x.on('hidden.bs.modal', function(){
        x.remove();
    });
    x.modal('toggle');
}
