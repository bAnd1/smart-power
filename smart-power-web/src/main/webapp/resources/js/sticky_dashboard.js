$(function() {

    var $sidebar   = $("#dashboard-bar"),
        $window    = $(window),
        offset     = $sidebar.offset(),
        topPadding = 40;

    $window.scroll(function() {
        if ($window.scrollTop() > offset.top) {
            $sidebar.stop().animate({
                marginTop: $window.scrollTop() - offset.top + topPadding
            });
        } else {
            $sidebar.stop().animate({
                marginTop: 0
            });
        }
    });
    
});