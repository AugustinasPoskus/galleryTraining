//zk.afterMount(function(){
//$('.pagination').append('<x:li>' + 'a' + '</x:li>');
//    alert(x);
//});
var previousPagesCount=0; // pries tai buves puslapiu skaicius kai kreipesi viewmodelis
var elementsCount = 0; //kiek is viso yra li elementu
var prevDisabled = false;
var nextDisabled = false;
var activePage = 0; //kuris puslapis aktyvus, 0 - neaktyvus joks

function pagination (pagesCount, activePg){
    $('.pagination').append('<li class="page-item"><span id="prevbtn">' + 'Previous' + '</span></li>');
    for (var i = 1; i <= pagesCount; i++) {
        $('.pagination').append('<li class="page-item"><a class="page-link" href="?page=' + i + '">'+'<span>' + i +'</span></a></li>');
        elementsCount++;
    }
    $('.pagination').append('<li class="page-item"><span id="nextbtn">' + 'Next' + '</span></li>');
    elementsCount = elementsCount + 2;
    previousPagesCount = pagesCount;
    setActive(activePg);
    checkDisables();
}

function paginationChange (pagesCount){
    if(previousPagesCount != pagesCount){
        if(previousPagesCount>pagesCount){
            var removeIndex = pagesCount + 1;
            $('.pagination li:eq(' + removeIndex + ')').remove();
            previousPagesCount--;
            elementsCount--;
            if(!pagesCount == 0){
                setActive(pagesCount);
                window.location = "http://localhost:8080/?page=" + pagesCount;
            }
        }
        if(previousPagesCount<pagesCount){
            var addIndex = pagesCount - 1;
            $('.pagination li:eq(' + addIndex + ')').after('<li class="page-item"><a class="page-link">'+'<span>' + pagesCount +'</span></a></li>');
            elementsCount++;
            if(previousPagesCount == 0)
            {
                setActive(1);
            }
            previousPagesCount++;
        }
        checkDisables();
    }
}

function setActive(index){
    if((elementsCount-1 != index) && (0 != index)){
        $('.page-item active').attr('class', 'page-item');
        $('.pagination li:eq(' + index + ')').attr('class', 'page-item active');
        activePage = index;
    }
}

function checkDisables(){
    prevDisabled = false;
    nextDisabled = false;
    if(elementsCount == 2 || elementsCount == 3){
        prevDisabled = true;
        nextDisabled = true;
        $('.pagination li:last').attr('class', 'disabled');
        $('.pagination li:eq(0)').attr('class', 'disabled');
    }else if(activePage == 1){
        $('.pagination li:eq(0)').attr('class', 'disabled');
        $('.pagination li:last').attr('class', 'page-item');
        prevDisabled = true;
    } else if(elementsCount == activePage + 2){
        nextDisabled = true;
        $('.pagination li:last').attr('class', 'disabled');
        $('.pagination li:eq:eq[0]').attr('class', 'page-item');
    }
}

$("body").on('click','.prevbtn', function(){
    alert("called");
    activePage--;
    window.location = "http://localhost:8080/?page=" + activePage;
});

$("body").on('click','.nextbtn', function(){
    alert("called");
    activePage++;
    window.location = "http://localhost:8080/?page=" + activePage;
});