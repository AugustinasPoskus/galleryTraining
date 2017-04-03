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
