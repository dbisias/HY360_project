function loadtransactions(){
    $("#appwindow").load("transactions.html");
}

function loadbuy(){
    $("#appwindow").load("buy.html");
}

function loadpay(){
    $("#appwindow").load("paydebt.html");
}

$(document).ready(function(){
    loadtransactions();
    checkUserLoggedIn("company");
})