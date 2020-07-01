$(function () {
    $("#searchName").on("keyup", function () {
        $.ajax({
            url: "http://localhost:8080/employee/complete",
            type: "POST",
            dataType: "json",
            data: {
                inputData: $("#searchName").val()
            },
            async: true
        }).done(function (data) {
            $("#searchName").autocomplete({
                source: data.suggestions
            });
        }).fail(function () {
            console.log("エラー");
        });
    });
});