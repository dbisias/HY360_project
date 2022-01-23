function loadtransactions(){
    $("#appwindow").load("transactions.html");
}

function loadbuy(){
    $("#appwindow").load("buy.html");
}

function loadpay(){
    $("#appwindow").load("pay.html");
}

$(document).ready(function(){
    loadtransactions();
    checkUserLoggedIn("individual");
})