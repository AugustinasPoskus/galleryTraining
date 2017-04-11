
jq.alert = function (msg) {
    $(".modal-body #msg").text( msg );
    $('#uploadError').modal('show');
}

function failedUpload(msg) {
    $(".modal-body #msg").text( msg );
    $('#uploadError').modal('show');
}

function failedToChangeFolder(message) {
    $(".modal-body #message").text( message );
    $('#nameNotChanged').modal('show');
}