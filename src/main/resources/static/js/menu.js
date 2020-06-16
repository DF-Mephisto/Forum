const removeComment = function(id) {

    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let headers = {};
    headers[header] = token;

    $.ajax({
        type: 'DELETE',
        url: '/comment/' + id,
        async: true,
        headers: headers,
        success: function() {
            let comment = document.getElementById("com" + id);
            comment.parentElement.removeChild(comment);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            alert(jqXHR.status + ' ' + jqXHR.responseText);
        }
    });
};