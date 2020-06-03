(function (jQuery) {

    jQuery.fn.extend({

        simpleGal: function (options) {

            var defaults = {
                mainImage: ".placeholder"
            };

            options = jQuery.extend(defaults, options);

            return this.each(function () {

                var thumbnail = jQuery(this).find("a"),
                    mainImage = jQuery(this).siblings().find(options.mainImage);

                thumbnail.on("click", function (e) {
                    e.preventDefault();
                    var galleryImage = jQuery(this).attr("href");
                    if(galleryImage != 'javascript:;')
                        mainImage.attr("src", galleryImage);
                });

            });

        }

    });

})(jQuery);
