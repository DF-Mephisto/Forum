const removeItem = function(type, id) {

    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let headers = {};
    headers[header] = token;

    $.ajax({
        type: 'DELETE',
        url: '/' + type + '/' + id,
        async: true,
        headers: headers,
        success: function() {
            let item = document.getElementById("item" + id);
            item.parentElement.removeChild(item);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            alert(jqXHR.status + ' ' + jqXHR.responseText);
        }
    });
};