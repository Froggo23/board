function go() {
        var number = document.getElementById("numberSelect").value;
        var incrementedNumber = parseInt(number) + 1;
        var url = "/other?number=" + incrementedNumber;
        window.location.href = url;
}