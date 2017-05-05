
jq.alert = function (msg, opt) {
    $(".modal-body #message").text( msg );
    $('#zkError').modal('show');
}