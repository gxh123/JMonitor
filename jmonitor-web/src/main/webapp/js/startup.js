/**
 * Created by gxh on 2016/12/14.
 */

(function ($) {
    $(function () {
        nav();
        tip();
    });

    function nav() {
        var $liCur = $(".nav>li.cur"),
            $target = $(".nav>li");
        $liCur.css({
            "background-color":"#ffffff",
            "color":"black",
            "border-right":"4px solid #19aa8d"
        });
        $target.mouseenter(function () {
            $liCur.css({
                "background-color":"#ebebeb",
                "color":"none",
                "border-right":"none"
            });
        });
        $target.mouseleave(function () {
            $liCur.css({
                "background-color":"#ffffff",
                "color":"black",
                "border-right":"4px solid #19aa8d"
            });
        });
    };

    function tip() {
        $('#tipLastMonth').tipso({
            useTitle: false
        });
        $('#tipLastWeek').tipso({
            useTitle: false
        });
        $('#tipLastDay').tipso({
            useTitle: false
        });
        $('#tipLastHour').tipso({
            useTitle: false
        });
        $('#tipNow').tipso({
            useTitle: false
        });
        $('#tipNextMonth').tipso({
            useTitle: false
        });
        $('#tipNextWeek').tipso({
            useTitle: false
        });
        $('#tipNextDay').tipso({
            useTitle: false
        });
        $('#tipNextHour').tipso({
            useTitle: false
        });
    }
})(jQuery);
