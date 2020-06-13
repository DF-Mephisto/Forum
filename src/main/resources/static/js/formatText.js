let formatText = function()
{
    let links = document.getElementsByClassName("last-comment-link");

    Array.prototype.forEach.call(links, function(link) {
        let text = link.innerHTML;

        if (text.length > 40)
        {
            text = text.substr(0, 40) + ' ...';
            link.innerHTML = text;
        }
    });
}