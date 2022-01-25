function loadtransactions(){
    $("#appwindow").load("transactions.html");
}

function loadaccount(){
    $("#appwindow").load("account.html");
}

function loadbuy(){
    $("#appwindow").load("buy.html");
}

function loadpay(){
    $("#appwindow").load("paydebt.html");
}

$(document).ready(function(){
    loadtransactions();
    updateUserInfo("individual");
})