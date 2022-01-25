function loadtransactions(){
    $("#appwindow").load("transactions.html");
}

function loadpay(){
    $("#appwindow").load("paydebt.html");
}

$(document).ready(function(){
    loadtransactions();
    updateUserInfo("merchant");
})