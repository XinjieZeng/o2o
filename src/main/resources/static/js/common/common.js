function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return decodeURIComponent(r[2]);
    }
    return '';
}

function successSubmit() {
    alert("submit success!");
}

function failSubmit() {
    alert("submit fail!");
}

function invalidCode() {
    alert("Invalid Verification Code!");
}