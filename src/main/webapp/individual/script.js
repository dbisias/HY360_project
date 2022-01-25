function loadtransactions(){
    $("#appwindow").load("transactions.html");
}

function loadbuy(){
    $("#appwindow").load("buy.html");
}

function loadpay(){
    $("#appwindow").load("paydebt.html");
}

function loadaccount(){
    $("#appwindow").load("account.html");
}

$(document).ready(function(){
    loadtransactions();
    checkUserLoggedIn("individual");
})