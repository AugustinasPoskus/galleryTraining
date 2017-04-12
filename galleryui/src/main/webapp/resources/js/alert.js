
jq.alert = function (msg, opt) {
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

function dismissFormModal() {
   $('#myModal').modal('toggle');
}

function dismissChangeNameModal() {
   $('#changeFolderNameModal').modal('toggle');
}

function dismissAddFolderModal() {
   $('#addFolder').modal('toggle');
}