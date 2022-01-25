function loadtransactions(){
    $("#appwindow").load("transactions.html");
}

function loadaccount(){
    $("#appwindow").load("account.html");
}

$(document).ready(function(){
    loadtransactions();
    updateUserInfo("merchant");
})