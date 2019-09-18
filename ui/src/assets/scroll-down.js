/* Class is used to make a smooth scroll for the welcome page to show the blurb section. */

$(document).ready(function(){
    $(document).on('click', "#down-arrow-button", function(event) {
        if (this.hash !== "") {
           // event.preventDefault();
            var hash = this.hash;
            $('html, body').animate({
                scrollTop: $(hash).offset().top
            }, 600);
        }
    });

    $(document).on('click', "#get-started", function(event) {
        $([document.documentElement, document.body]).animate({
            scrollTop: $("#start-page").offset().top
        }, 600);
    });
});