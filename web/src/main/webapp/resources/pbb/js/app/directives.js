//oApp.directive('chosenselect', function () {
//    return {
//        link: function (oScope, oElement, aAttrs) {
//
//
//            angular.element(oElement).chosen({
//                placeholder: aAttrs.placeholder,
//                allow_single_deselect: aAttrs.allowDeselect
//            });
//
//            var aProperties = aAttrs.dependencies.split(',');
//
//            aProperties.forEach(function (sProperty) {
//                oScope.$watch(sProperty.trim(), function () {
//                    jQuery(oElement).chosen({
//                        placeholder: aAttrs.placeholder,
//                        allow_single_deselect: aAttrs.allowDeselect
//                    }).trigger("liszt:updated");
//                });
//            });
//        }
//    };
//});


oApp.directive('match', function ($parse) {
    return {
        require: 'ngModel',
        link: function (scope, elem, attrs, ctrl) {
            scope.$watch(function () {
                return $parse(attrs.match)(scope) === ctrl.$modelValue;
            }, function (currentValue) {
                ctrl.$setValidity('mismatch', currentValue);
            });
        }
    };
});

oApp.directive('bdatepicker', function () {
    return {
        restrict: "A",
        scope: true,

        link: function (oScope, oElement, aAttrs) {
            var nowTemp = new Date();
            var now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0);

            var options = {
                onRender: function (date) {
                    return date.valueOf() < now.valueOf() ? 'disabled' : '';
                },
                onChangeDate: function (ev) {
                    if (ev.date.valueOf() > checkout.date.valueOf()) {
                        var newDate = new Date(ev.date);
                        newDate.setDate(newDate.getDate() + 1);
                        checkout.setValue(newDate);
                    }
                }
            };
            oElement.datepicker(options);
        }
    }
});

oApp.directive('tinycarousel', function () {
    return {
        restrict: 'A',
        scope: true,

        link: function (oScope, oElement, aAttrs) {
            var options = {
                infinite: false
            };
            oScope.$watch(aAttrs.field, function (newVal, oldVal) {
                oElement.tinycarousel(options);
                oElement.simpleGal();
            });
        }
    }
});

oApp.directive('hoverzoom', function () {
    return {
        restrict: 'A',
        scope: true,

        link: function (oScope, oElement, aAttrs) {

//            oScope.$watch(aAttrs.field, function (newValue, oldValue) {
//                oElement.simpleGal();
//            }, true);

//            $('.thumb img').hoverZoom({speedView:600, speedRemove:400, showCaption:true, speedCaption:600, debug:true, hoverIntent: true, loadingIndicatorPos: 'center'});
            var options = {
                speedView: 400,
                speedRemove: 400,
                showCaption: true,
                speedCaption: 600,
                debug: true,
                hoverIntent: true,
                loadingIndicatorPos: 'center'
            };

            oScope.$watch(aAttrs.field, function (newVal, oldVal) {
                oElement.hoverZoom(options);
            });


        }
    }
});

oApp.directive('smoothzoom', function () {
    return {
        restrict: 'A',
        scope: true,

        link: function (oScope, oElement, aAttrs) {

//            oScope.$watch(aAttrs.field, function (newValue, oldValue) {
//                oElement.simpleGal();
//            }, true);

//            $('.thumb img').hoverZoom({speedView:600, speedRemove:400, showCaption:true, speedCaption:600, debug:true, hoverIntent: true, loadingIndicatorPos: 'center'});
            var options = {
//                speedView:400,
//                speedRemove:400,
//                showCaption:true,
//                speedCaption:600,
//                debug:true,
//                hoverIntent: true,
//                loadingIndicatorPos: 'center'
            };


            oScope.$watch(aAttrs.field, function (newVal, oldVal) {
                oElement.smoothZoom(options);
            });


        }
    }
});


oApp.directive('styling', function () {

});

oApp.directive('bxslider', function () {
    return {
        restrict: 'A',
        scope: true,

        link: function (oScope, oElement, aAttrs) {
            var options = {
                pagerCustom: '#bx-pager'
            };
            oScope.$watch(aAttrs.field, function (newValue, oldValue) {
                oElement.bxSlider(options);
            }, true);
        }
    }
});

oApp.directive('facilities', function () {
    return {
        restrict: 'A',
//        template: '<li><i class="icon-plus-sign"></i><a href="/"><i class="icon-folder-close"></i>{{item.text}}</a></li>',
        replace: true,
//        scope: {
//            item: "="
//        },
        link: function (scope, element, attrs) {
//            console.log(element.facilities);
//            element.append($compile('<ul><treeview-item ng-repeat="childitem in item.childitems" item="childitem"></treeview-item></ul>')(scope));
//
//            console.log("treeview item directive loaded");
        }
    };
});

oApp.directive('simplegal', function () {
    return {
        restrict: 'A',
        scope: true,

        link: function (oScope, oElement, aAttrs) {
            oScope.$watch(aAttrs.field, function (newValue, oldValue) {
                oElement.simpleGal();
            }, true);
//            oScope.watch()

        }
    }
});

oApp.directive('roomImage', function () {
    return {
        restrict: "A",
        link: function (oScope, oElement, aAttrs) {

////            $('.thumb img').hoverZoom({speedView:600, speedRemove:400, showCaption:true, speedCaption:600, debug:true, hoverIntent: true, loadingIndicatorPos: 'center'});
//            var options = {
//                speedView:600,
//                speedRemove:400,
//                showCaption:true,
//                speedCaption:600,
//                debug:true,
//                hoverIntent: true,
//                loadingIndicatorPos: 'center'
//            };
//
//            oElement.hoverZoom(options);
        }
    };
});


/**
 * animations
 * show, slideDown, fadeIn, blind, bounce, clip, drop, fold, slide
 *
 * showButtonPanel: true/false
 *
 * Display month & year menus
 * changeMonth: true, changeYear: true
 *
 * Display multiple months
 * numberOfMonths: 3,
 */
oApp.directive('juidatepicker', function ($parse, dateUtils) {
    return {
        restrict: "A",
        scope: true,

        link: function (oScope, oElement, aAttrs) {

            var options = {
                inline: true,
                firstDay: 1,
                dateFormat: aAttrs.dateFormat,
                showAnim: aAttrs.animate,
                showButtonPanel: aAttrs.buttonPanel,
                changeMonth: aAttrs.changeMonth,
                changeYear: aAttrs.changeYear,
                numberOfMonths: aAttrs.monthCount,
                dayNames: ["Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье", "Понедельник"],
                dayNamesMin: ["Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб"],
                dayNamesShort: ["Пон", "Втр", "Срд", "Чет", "Пят", "Суб", "Вск"],
                monthNames: ["Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"],

            };

            oElement.datepicker(options);

//            oScope.$watch(aAttrs.value, function (val) {
//                var fromDate = jQuery('input[name="dfrom"]').datepicker('getDate'),
//                    toDate = jQuery('input[name="dto"]').datepicker('getDate');
//
//                if (toDate && fromDate) {
//                    toDate = new Date(toDate).getTime();
//                    fromDate = new Date(fromDate).getTime();
//                    oScope.model.daysDifference = Math.floor((toDate - fromDate) / (1000 * 60 * 60 * 24));
//                }
//            });

            oScope.$watch(aAttrs.minDate, function (val) {
                if (!val) return;

                var newMin = new Date(dateUtils.toDate(val).getTime() + 86400000);
                var model = $parse(aAttrs.ngModel);
                var current = dateUtils.toDate(model(oScope));
                if (current < newMin) {
                    model.assign(oScope, dateUtils.toDateString(newMin));
                }
                oElement.datepicker("option", "minDate", newMin);
            });
        }
    };
});

oApp.directive('collapse', function () {
    return {
        restrict: "A",

        link: function (oScope, oElement, aAttrs) {
            var options = {

            };

            oElement.collapse();
        }
    };
});

oApp.directive('optionsDisabled', function ($parse) {
    var disableOptions = function (scope, attr, element, data, fnDisableIfTrue) {
        // refresh the disabled options in the select element.
        var options = element.find("option");
        for (var pos = 0, index = 0; pos < options.length; pos++) {
            var elem = angular.element(options[pos]);
            if (elem.val() != "") {
                var locals = {};
                locals[attr] = data[index];
                elem.attr("disabled", fnDisableIfTrue(scope, locals));
                index++;
            }
        }
    };
    return {
        priority: 0,
        require: 'ngModel',
        link: function (scope, iElement, iAttrs, ctrl) {
            // parse expression and build array of disabled options
            var expElements = iAttrs.optionsDisabled.match(/^\s*(.+)\s+for\s+(.+)\s+in\s+(.+)?\s*/),
                attrToWatch = expElements[3],
                fnDisableIfTrue = $parse(expElements[1]);

            scope.$watch(attrToWatch, function (newValue, oldValue) {
                if (newValue)
                    disableOptions(scope, expElements[2], iElement,
                        newValue, fnDisableIfTrue);
            }, true);

            // handle model updates properly
            scope.$watch(iAttrs.ngModel, function (newValue, oldValue) {
                var disOptions = $parse(attrToWatch)(scope);
                if (newValue)
                    disableOptions(scope, expElements[2], iElement, disOptions, fnDisableIfTrue);
            });

            var aProperties = iAttrs.dependencies.split(',');

            aProperties.forEach(function (sProperty) {
                scope.$watch(sProperty.trim(), function (newValue, oldValue) {
                    if (newValue)
                        disableOptions(scope, expElements[2], iElement, newValue, fnDisableIfTrue);
                });
            });
        }
    };
});

oApp.directive('bbColor', function (customizationFactory) {
    return customizationFactory.getColorAdjustDirective("color", "bbColor");
});

oApp.directive('sliderColor', function (customizationFactory) {
    return {
        restrict: "A",

        link: function (oScope, oElement, aAttrs) {
            oScope.$watch(aAttrs.sliderColor, function (v) {
                oElement.hover(
                    function () {
                        oElement.oldColor = oElement.css("color");
                        oElement.css("color", v);
                    },
                    function () {
                        oElement.css("color", oElement.oldColor);
                    }
                );
            });
        }
    };
});


oApp.directive('bbBackgrnd', function (customizationFactory) {
    return customizationFactory.getColorAdjustDirective("background-color", "bbBackgrnd");
});

oApp.directive('bbHover', function () {
    return {
        restrict: "A",

        link: function (oScope, oElement, aAttrs) {
            oScope.$watch(aAttrs.bbHover, function (v) {
                oElement.hover(
                    function () {
                        oElement.oldColor = oElement.css("background-color");
                        oElement.css("background-color", v);
                    },
                    function () {
                        oElement.css("background-color", oElement.oldColor);
                    }
                );
            });
        }
    };
});

oApp.directive('bbDisabled', function () {
    return {
        restrict: "A",

        link: function (oScope, oElement, aAttrs) {
            oScope.$watch('aAttrs.bbDisabled', function (v) {
                oElement.enabledColor = oElement.css("background-color");//TODO This will only work if bb-disabled attr change while disabled attr is false. Figure out how to init enabled color reliably
            });
            aAttrs.$observe('disabled', function (v) {
                if (v) {
                    oElement.css("background-color", aAttrs.bbDisabled);
                    oElement.css("cursor", "default");
                } else {
                    oElement.css("background-color", oElement.enabledColor);
                    oElement.css("cursor", "pointer");
                }
            });
        }
    };
});

oApp.directive('bbBorder', function (customizationFactory) {
    return customizationFactory.getColorAdjustDirective("border-color", "bbBorder");
});

oApp.directive('bbDate', function (dateUtils) {
    return {
        restrict: "E",
        template: "{{day}}, {{date}} {{month}} {{year}}",
        scope: {
            dateString: "=date"
        },

        link: function (oScope) {
            var date = oScope.dateString.split("/");
            date = new Date(date[2] + "," + date[1] + "," + date[0]);
            oScope.day = dateUtils.getDay(date.getDay() || 7);
            oScope.date = date.getDate();
            oScope.month = dateUtils.getMonth(date.getMonth());
            oScope.year = date.getFullYear();
        }
    };
});

oApp.directive('imageExpand', function () {
    return {
        restrict: "A",

        link: function (oScope, oElement, aAttrs) {
            var xOffset = 5,
                yOffset = 5;
            var Mx = jQuery(document).width();
            var My = window.innerHeight + jQuery(document).scrollTop();

            /* END CONFIG */
            var callback = function (event) {
                var $img = jQuery("#image-preview");

                // top-right corner coords' offset
                var trc_x = xOffset + $img.width();
                var trc_y = yOffset + $img.height();

                trc_x = Math.min(trc_x + event.pageX, Mx);
                trc_y = Math.min(trc_y + event.pageY, My);

                $img
                    .css("top", (trc_y - $img.height()) + "px")
                    .css("left", (trc_x - $img.width()) + "px");
            };
            oElement.hover(
                function (e) {
                    Mx = jQuery(document).width();
                    My = window.innerHeight + jQuery(document).scrollTop();
                    jQuery("body").append("<p id='image-preview'><img width='500' src='" + this.src + "' alt='Image preview' /></p>");
                    callback(e);
                    jQuery("#image-preview").fadeIn("fast");
                },
                function () {
                    jQuery("#image-preview").remove();
                }
            ).mousemove(callback);
        }
    };
});

oApp.directive('declineNights', function (customizationFactory) {
    return {
        restrict: "A",

        link: function (oScope, oElement, aAttrs) {
            oScope.$watch(aAttrs.declineNights, function (number) {
                var cases = [2, 0, 1, 1, 1, 2],
                    titles = ['ночь', 'ночи', 'ночей'];
                oElement.append(number + ' ' + titles[ (number % 100 > 4 && number % 100 < 20) ? 2 : cases[(number % 10 < 5) ? number % 10 : 5] ]);
            });
        }
    };
});

oApp.directive('bedDetails', function () {
    return {
        restrict: "A",
        link: function (oScope, oElement, aAttrs) {
            var str = '',
                beds = oScope.beds;
            oScope.$watch(aAttrs.bedDetails, function (details) {
                for (var i in beds) {
                    if (details[beds[i]]) {
                        str += details[beds[i]] + ' ' + oScope.labels['roomType.details.' + beds[i]] + ', ';
                    }
                }
                if (str.length) {
                    oElement.append(str.slice(0, str.length - 2));
                }
                else {
                    oElement.remove();
                }
            });
        }
    };
});

