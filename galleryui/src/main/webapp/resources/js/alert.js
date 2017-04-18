
jq.alert = function (msg, opt) {
    $(".modal-body #message").text( msg );
    $('#zkError').modal('show');
}

function modalWarning(msg) {
    $(".modal-body #message").text( msg );
    $('#zkError').modal('show');
}

function confirmationModal() {
    $('#confirmationModal').modal('show');
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

function openFullPicture(){
    $('#fullPicture').modal('show');
}