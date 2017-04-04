$(document).ready(function(){
    $('#target').append('target edit 1<x:br/>');
    var foldersCount = 20;
    alert("A")
    var pagesCount = 2;
    $('.pagination').append('<x:li class="page-item">'+'<x:a class="page-link" href="#">' + 'Previous' +'</x:a>' + '</x:li>');
    for (var i = 1; i &lt;= pagesCount; i++) {
        $('.pagination').append('<x:li class="page-item">'+'<x:a class="page-link" href="#">' + i +'</x:a>' + '</x:li>');
    }
    $('.pagination').append('<x:li class="page-item">'+'<x:a class="page-link" href="#">' + 'Next' +'</x:a>' + '</x:li>');
});
function myFunction() {
    alert("1");
    var ul = document.getElementsByClassName('pagination');
    alert("2");
    var li = document.createElementNS("x","li");
    alert("3");
    li.innerHTML = 'Jeff';
    alert("4");
    document.getElementsByClassName('pagination').appendChild(li)
    alert("5");
}

jQuery(document).ready(function test(){
            alert("omg");
        });

$( document ).ready(function() {
  alert("A");
});
//$('body').on('change', '#chapters-select', function(){
//    alert('changed');
//});