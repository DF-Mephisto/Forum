window.onload = function() {
    String.prototype.lines = function() { return this.split(/\r*\n/); };
    String.prototype.lineCount = function() { return this.lines().length; };

    let inputs = document.getElementsByClassName("create-topic");

    for (let i = 0; i < inputs.length; i++)
    {
        addInpEvents(inputs[i]);
    }
};

function addInpEvents(input) {
    input.onfocus = function() {
        input.style.backgroundColor = "rgba(37,18,0,0.6)";
        document.getElementById("create-btn").style.display = 'block';
    };

    input.addEventListener('focusout', function(){
        input.style.backgroundColor = "rgba(0,0,0,0.9)";
    });

    input.addEventListener('input', function(){
        input.rows = input.value.toString().lineCount();
    });
}