window.onload = function() {
    String.prototype.lines = function() { return this.split(/\r*\n/); };
    String.prototype.lineCount = function() { return this.lines().length; };
    document.getElementById("create-btn").disabled = true;

    let inputs = document.getElementsByClassName("create-topic-input");

    for (let i = 0; i < inputs.length; i++)
    {
        addInpEvents(inputs[i], inputs[i].rows);
    }
};

function addInpEvents(input, rows) {
    input.onfocus = function() {
        input.style.backgroundColor = "rgba(37,18,0,0.6)";
        document.getElementById("create-btn").style.display = 'block';
        document.getElementById("create-comment").style.display = 'block';
    };

    input.addEventListener('focusout', function(){
        input.style.backgroundColor = "rgba(0,0,0,0.9)";
    });

    input.addEventListener('input', function(){
        let lineCount = input.value.toString().lineCount();
        if (lineCount >= rows)
            input.rows = lineCount;
        else
            input.rows = rows;

        let inputs = document.getElementsByClassName("create-topic-input");

        let notEmptyContent = true;

        Array.prototype.forEach.call(inputs, function(inp) {
            if (inp.value.toString().length === 0) notEmptyContent = false;
        });

        let btn = document.getElementById("create-btn");
        if (notEmptyContent){
            if (btn.disabled)
            {
                btn.disabled = false;
                btn.className = btn.className.replace("btn-dis", "btn");
            }
        }
        else
        {
            if (!btn.disabled)
            {
                btn.disabled = true;
                btn.className = btn.className.replace("btn", "btn-dis");
            }
        }
    });
}