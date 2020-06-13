function tuneCommentBlocks() {
    let avas = document.querySelectorAll(".user-main-info img");

    for (let i = 0; i < avas.length; i++)
    {
        avas[i].height = avas[i].width;
    }

    let uBlocks = document.querySelectorAll(".user-block");
    let cBlocks = document.querySelectorAll(".comment-block");
    for (let i = 0; i < uBlocks.length; i++)
    {
        if (uBlocks[i].clientHeight  > cBlocks[i].clientHeight )
        {
            cBlocks[i].style.height  = uBlocks[i].clientHeight.toString() + 'px' ;
        }
    }
}

function vscroll()
{
    let url_string = window.location.href ;
    let url = new URL(url_string);
    let scroll = url.searchParams.get("vscroll");
    if (scroll)
        window.scrollTo(0,document.body.scrollHeight);
}