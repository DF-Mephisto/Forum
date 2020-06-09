window.onload = function()
{
    var input = document.querySelector('.inputfile');
    addAvatarInputListener(input);
}

function addAvatarInputListener(input){
    input.addEventListener('change', function(e){
        const selectedFile = e.target.files[0];

        document.getElementById("ava").src = URL.createObjectURL(selectedFile);

    }, false);
}