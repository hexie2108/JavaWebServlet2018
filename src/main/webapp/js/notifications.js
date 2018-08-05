if(typeof jQuery === "undefined") {
    console.error("Jquery needed");
}

function createDivNotification(notification, read, notRead, mark){
    const lettoSm = jQuery('<small/>', {
        text: notification.status? read: notRead
    });

    const dateSm = jQuery('<small/>', {
        class: "float-right",
        title: notification.date,
        text: (new Date(notification.date)).toLocaleString() // La data è in UTC quindi la converto secondo la timezone locale
    });

    const header = jQuery('<div/>', {
        class: ["d-flex","w-100","justify-content-between"].join(' '),
        html: [lettoSm, dateSm]
    });

    const textDiv = jQuery('<div/>', {
        text: notification.text
    });

    /*
<span class="float-right"><button class="btn btn-primary" onclick="deleteNotification(${notification.id}, this)"><fmt:message key="notifications.label.markAsRead"/></button></span>
    */


    return jQuery('<li/>', {
        class: "list-group-item",
        html: [header, textDiv]
    });
}

function updateEmptyNotificationList(empty, list){
    if (empty !== null && empty !== undefined) {
        if(list.children().length > 0) {
            list.show();
            empty.hide();
        } else {
            list.hide();
            empty.show();
        }
    }
}

function updateNotificationList(list, url, async, status, count, empty, read, notRead, mark){
    if (status === true || status === false) {
        status = "" + status;
    } else if (status === undefined || status === null) {
        status = "";
    } else {
        status = "";
        console.warn("filter must be true, false, null or undefined");
    }

    const request = $.ajax({
        dataType: "json",
        url : url,
        type: "get",
        async: async,
        data: "status="+status
    }).done(function(data){
        list.empty();
        list.append(data.map(v => createDivNotification(v, read, notRead)));

        updateEmptyNotificationList(empty, list);

        if(count !== null && count !== undefined) {
            count.html(" "+ data.length);
        }
    }).fail(function(){
        // Fail silently
    });
}

function deleteNotification(id, btn, empty) {
    btn.attr("disabled", true);

    $.ajax({
        dataType: "json",
        url : 'nonesisto',
        type: "delete",
        async: true,
    }).done(function(data) {
        const li = btn.closest('li');
        const ul = li.closest('ul');

        li.remove();
        btn.attr("disabled", false);

        updateEmptyNotificationList(empty, ul)
    }).fail(function(){
        btn.removeClass("btn-primary");
        btn.addClass("btn-danger");

        setTimeout(function(){
            btn.removeClass("btn-danger");
            btn.addClass("btn-primary");

            btn.attr("disabled", false);

        }, 2000);
    });



}

