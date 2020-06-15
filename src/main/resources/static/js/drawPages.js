function drawPages(cur, count, url) {

    if (count <= 1)
    {
        document.getElementById("pages").style.display = "none";
        return;
    }

    let minPage = document.createElement('a');
    minPage.className = "page-btn";
    minPage.innerHTML = "«";
    minPage.setAttribute("href", url + "?page=0")
    document.getElementById('pages').appendChild(minPage);

    let start = 0;
    if (cur >= 2 && count > 5)
    {
        if (count - cur - 1 < 2) start = count - 5;
        else start = cur - 2;
    }

    for (let i = start; i < (count > 5 ? start + 5 : count); i++) {
        let page = document.createElement('a');
        page.className = "page-btn";
        if (cur == i) page.className += " page-btn-sel";
        page.innerHTML = i + 1;

        let link = url + "?page=" + i;

        page.setAttribute("href", link);
        document.getElementById('pages').appendChild(page);
    }

    let maxPage = document.createElement('a');
    maxPage.className = "page-btn";
    maxPage.innerHTML = "»";
    maxPage.setAttribute("href", url + "?page=" + (count - 1));
    document.getElementById('pages').appendChild(maxPage);
}