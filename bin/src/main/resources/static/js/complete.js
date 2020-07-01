$(function () {
    var availableTags = ["渡辺三郎", "佐藤次郎", "山本八郎", "小林九子", "佐藤次子", "高橋五郎", "山田花子", "吉田四子", "吉田四郎", "山田太郎", "伊藤七子", "伊藤七郎", "田中六子", "高橋五子", "加藤十郎", "田中六郎", "小林九郎", "鈴木一朗", "山本八子", "渡辺三子", "鈴木一子", "加藤十子"];
    $("#searchName").autocomplete({
        source: availableTags
    });
    // $("#searchName").on("keyup", function () {
    //     $.ajax({
    //         url: "http://localhost:8080/employee/complete",
    //         type: "POST",
    //         dataType: "json",
    //         data: {
    //             inputData: $("#searchName").val()
    //         },
    //         async: true
    //     }).done(function (data) {
    //         $("#searchName").autocomplete({
    //             source: data.suggestions
    //         });
    //     }).fail(function () {
    //         console.log("エラー");
    //     });
    // });
});