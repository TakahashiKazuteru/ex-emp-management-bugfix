$(function () {
    $("#zipCode").on("keyup", function () {
        if($("#zipCode").val() == ""){
            $("#address").val("");
        }
        $.ajax({
            url: "https://zipcoda.net/api",
            dataType: 'jsonp',
            data: {
                zipcode: $("#zipCode").val()
            },
            async: true
        }).done(function (data) {
            $("#address").val(data.items[0].pref + data.items[0].address);
        }).fail(function () {
            console.log("通信エラー");
        });
    });
});