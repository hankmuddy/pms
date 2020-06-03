/*
 * Smoothzoom
 * http://kthornbloom.com/smoothzoom
 *
 * Copyright 2014, Kevin Thornbloom
 * Free to use and modify under the MIT license.
 * http://www.opensource.org/licenses/mit-license.php
 */

(function (jQuery) {
    jQuery.fn.extend({
        smoothZoom: function (options) {

            var defaults = {
                zoominSpeed: 800,
                zoomoutSpeed: 400,
                resizeDelay: 400,
                zoominEasing: 'easeOutExpo',
                zoomoutEasing: 'easeOutExpo'
            }

            var options = $.extend(defaults, options);


            // CLICKING AN IMAGE

            jQuery('img[rel="zoom"]').click(function (event) {

                var link = jQuery(this).attr('src'),
                    largeImg = jQuery(this).parent().attr('href'),
                    target = jQuery(this).parent().attr('target'),
                    offset = jQuery(this).offset(),
                    width = jQuery(this).width(),
                    height = jQuery(this).height(),
                    amountScrolled = jQuery(window).scrollTop(),
                    viewportWidth = jQuery(window).width(),
                    viewportHeight = jQuery(window).height();
                // IF THERE IS NO ANCHOR WRAP
                if ((!largeImg) || (largeImg == "#")) {

                    jQuery('body').append("<div id='lightwrap'><img src=" + link + "></div><div id='lightbg'></div><img id='off-screen' src=" + link + ">");
                    jQuery("#off-screen").load(function () {
                        jQuery('#lightwrap img').css({
                            width: width,
                            height: height,
                            top: (offset.top - amountScrolled),
                            left: offset.left
                        });
                        fitWidth();
                        jQuery('#lightbg').fadeIn();
                    });
                    jQuery(this).attr('id', 'lightzoomed');

                    // IF THERE IS AN ANCHOR, AND IT'S AN IMAGE
                } else if (largeImg.match("[jpg|png|gif]$")) {
                    jQuery('body').append("<div id='lightwrap'><img src=" + largeImg + "></div><div id='lightbg'></div><img id='off-screen' src=" + largeImg + ">");
                    jQuery("#off-screen").load(function () {
                        jQuery('#lightwrap img').css({
                            width: width,
                            height: height,
                            top: (offset.top - amountScrolled),
                            left: offset.left
                        });
                        fitWidth();
                        jQuery('#lightbg').fadeIn();
                    });
                    jQuery(this).attr('id', 'lightzoomed');

                    // IF THERE IS AN ANCHOR, BUT NOT AN IMAGE
                } else {
                    // SHOULD IT OPEN IN A NEW WINDOW?
                    if (target = '_blank') {
                        window.open(largeImg, '_blank');
                    } else {
                        window.location = largeImg;
                    }
                }
                event.preventDefault();
            });

            // CLOSE MODAL

            jQuery(document.body).on("click", "#lightwrap, #lightbg", function (event) {
                var offset = jQuery("#lightzoomed").offset(),
                    originalWidth = jQuery("#lightzoomed").width(),
                    originalHeight = jQuery("#lightzoomed").height(),
                    amountScrolled = jQuery(window).scrollTop();
                jQuery('#lightbg').fadeOut(500);
                jQuery('#lightwrap img').animate({
                    height: originalHeight,
                    width: originalWidth,
                    top: (offset.top - amountScrolled),
                    left: offset.left,
                    marginTop: '0',
                    marginLeft: '0'
                }, options.zoomoutSpeed, options.zoomoutEasing, function () {
                    jQuery('#lightwrap, #lightbg, #off-screen').remove();
                    jQuery('#lightzoomed').removeAttr('id');

                });
            });

            // DELAY FUNCTION FOR WINDOW RESIZE
            var delay = (function () {
                var timer = 0;
                return function (callback, ms) {
                    clearTimeout(timer);
                    timer = setTimeout(callback, ms);
                };
            })();

            // CHECK WINDOW SIZE EVERY _ MS
            jQuery(window).resize(function () {
                delay(function () {
                    fitWidth();
                }, options.resizeDelay);
            });


            // FIT IMAGE BASED ON HEIGHT
            function fitHeight() {

                var viewportHeight = $(window).height(),
                    viewportWidth = $(window).width(),
                    naturalWidth = $('#off-screen').width(),
                    naturalHeight = $('#off-screen').height(),
                    newHeight = (viewportHeight * 0.95),
                    ratio = (newHeight / naturalHeight),
                    newWidth = (naturalWidth * ratio);
                jQuery('#lightwrap img').show();
                if (newHeight > naturalHeight) {
                    jQuery('#lightwrap img').animate({
                        height: naturalHeight,
                        width: naturalWidth,
                        left: '50%',
                        top: '50%',
                        marginTop: -(naturalHeight / 2),
                        marginLeft: -(naturalWidth / 2)
                    }, options.zoominSpeed, options.zoominEasing);
                } else {
                    if (newWidth > viewportWidth) {
                        fitWidth();
                    } else {
                        jQuery('#lightwrap img').animate({
                            height: newHeight,
                            width: newWidth,
                            left: '50%',
                            top: '2.5%',
                            marginTop: '0',
                            marginLeft: -(newWidth / 2)
                        }, options.zoominSpeed, options.zoominEasing);
                    }
                }
            }

            // FIT IMAGE BASED ON WIDTH
            function fitWidth() {

                var naturalWidth = $('#off-screen').width(),
                    naturalHeight = $('#off-screen').height(),
                    viewportWidth = $(window).width(),
                    viewportHeight = $(window).height(),
                    newWidth = (viewportWidth * 0.95),
                    ratio = (newWidth / naturalWidth),
                    newHeight = (naturalHeight * ratio);
                jQuery('#lightwrap img').show();
                if (newHeight > naturalHeight) {
                    if (naturalHeight > viewportHeight) {
                        fitHeight();
                    } else {
                        jQuery('#lightwrap img').animate({
                            height: naturalHeight,
                            width: naturalWidth,
                            top: '50%',
                            left: '50%',
                            marginTop: -(naturalHeight / 2),
                            marginLeft: -(naturalWidth / 2)
                        }, options.zoominSpeed, options.zoominEasing);
                    }
                } else {
                    if (newHeight > viewportHeight) {
                        fitHeight();
                    } else {
                        jQuery('#lightwrap img').animate({
                            height: newHeight,
                            width: newWidth,
                            top: '50%',
                            left: '2.5%',
                            marginTop: -(newHeight / 2),
                            marginLeft: '0'
                        }, options.zoominSpeed, options.zoominEasing);
                    }
                }
            }


        }
    });
})(jQuery);
