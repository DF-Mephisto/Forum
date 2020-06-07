window.onload = function()
{
    var input = document.querySelector('.inputfile');
    addAvatarInputListener(input);
}

function addAvatarInputListener(input){
    input.addEventListener('change', function(e){
        const selectedFile = e.target.files[0];
        //alert(selectedFile.name);
        document.getElementById("ava").src = URL.createObjectURL(selectedFile);

    }, false);
}