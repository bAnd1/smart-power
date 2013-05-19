$(function () {
    // Check the initial Poistion of the Sticky Header
    var stickyHeaderTop = $('#header-menubar').offset().top;

    $(window).scroll(function () {
        if ($(window).scrollTop() > stickyHeaderTop) {
            $('#header-menubar').css({position: 'fixed'});
            $('#stickyalias').css('display', 'block');
        } else {
            $('#header-menubar').css({position: 'static' });
            $('#stickyalias').css('display', 'none');
        }
    });
});