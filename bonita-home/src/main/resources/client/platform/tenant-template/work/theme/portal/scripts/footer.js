/**
* 
* This is script it is used to display/hide the form footer according to mode 
* value specified in the URL.
* 
*/
 
window.onload = function(){
    "use strict";

    if($(window.top.document).find("html").is(".ui-mobile") === true) {
        $(".footer").hide();
    }
};