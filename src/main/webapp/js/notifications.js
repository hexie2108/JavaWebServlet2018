if(typeof jQuery === "undefined") {
    console.error("Jquery needed");
}

function markNotification(url,id, btn, empty, read, del, count){

    btn.attr("disabled", true);

    $.ajax({
        dataType: "json",
        url : url,
        type: "get",
        async: true,
        data: {'id': id}
    }).done(function(data) {
        const li = btn.closest('li');
        const ul = li.closest('ul');

        li.children().first().children().first().html(read);


        if (del) {
            li.remove();

            if(count !== null && count !== undefined) {
                count.html(" " + Number(count.html())-1);

            }
        }

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

function createDivNotification(notification, read, notRead, mark, urlMark, empty, del, count){
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

    const markSpan = jQuery('<span/>', {
        class: "float-right",
        html: jQuery('<button/>', {
            class: ['btn', 'btn-primary'].join(' '),
            text: mark,
            click: function(){markNotification(urlMark, notification.id, $(this), empty, read, del, count);},
            disabled: notification.status
        })
    });


    return jQuery('<li/>', {
        class: "list-group-item",
        html: [header, textDiv, markSpan]
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

function updateNotificationList(list, url, async, status, count, empty, read, notRead, mark, urlMark, del){
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
        list.append(data.map(v => createDivNotification(v, read, notRead, mark, urlMark, empty, del, count)));

        updateEmptyNotificationList(empty, list);

        if(count !== null && count !== undefined) {
            count.html(" "+ data.length);
        }
    }).fail(function(){
        // Fail silently
    });
}
