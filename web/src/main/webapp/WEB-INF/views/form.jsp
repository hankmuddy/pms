<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="UTF-8"/>

<title>Online reception widget</title>

<link rel="stylesheet" href="pbb/css/bootstrap.css">
<link rel="stylesheet" href="pbb/css/jquery-ui-1.10.4.custom.min.css">
<script src="pbb/js/pmsbook.js"></script>
<style>
    @import url(//fonts.googleapis.com/css?family=Open+Sans:400,400italic,300italic,300,600,600italic,700,700italic&subset=latin,cyrillic);

    .ui-widget {
        font-family: 'Open Sans', sans-serif !important;
    }

    body {
        font-family: 'Open Sans', sans-serif;
        font-size: 15px;
        color: <%= request.getParameter("textColor") %>;
    }

    .formWrapper {
        padding: 30px;
        text-align: center;
        background-color: <%= request.getParameter("backgroundColor") %>;
    }

    .title {
        text-transform: uppercase;
        font-size: 16px;
        margin-top: 0;
    }

    .formWrapper input[type="text"].date-picker {
        padding: 10px;
        margin-top: 9px;
        width: 98%;
        color: #333333;
        font-size: 13px;
        border: 1px solid #163347;
        background: url(pbb/img/calendar.jpg) no-repeat 96% #fff;
        cursor: pointer;
    }

    .formWrapper input[type="text"] {
        padding: 10px;
        margin-top: 9px;
        width: 98%;
        color: #333333;
        font-size: 13px;
        border: 1px solid #163347;
    }

    .formWrapper hr {
        background: url(pbb/img/border2_03.png) no-repeat 50% 0;
        height: 2px;
        margin: 15px 0 10px;
        border: none;
    }

    button.submit, .submit {
        display: block;
        text-transform: uppercase;
        font-weight: 500;
        margin: 10px 0 10px;
        border: 1px solid #8DC961;
        outline: none;
        cursor: pointer;
        width: 100%;
        padding: 9px 0;
        color: #fff;
        font-size: 15px;
        background: rgb(160, 221, 115); /* Old browsers */
        background: -moz-linear-gradient(top, rgba(160, 221, 115, 1) 0%, rgba(119, 178, 77, 1) 100%); /* FF3.6+ */
        background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, rgba(160, 221, 115, 1)), color-stop(100%, rgba(119, 178, 77, 1))); /* Chrome,Safari4+ */
        background: -webkit-linear-gradient(top, rgba(160, 221, 115, 1) 0%, rgba(119, 178, 77, 1) 100%); /* Chrome10+,Safari5.1+ */
        background: -o-linear-gradient(top, rgba(160, 221, 115, 1) 0%, rgba(119, 178, 77, 1) 100%); /* Opera 11.10+ */
        background: -ms-linear-gradient(top, rgba(160, 221, 115, 1) 0%, rgba(119, 178, 77, 1) 100%); /* IE10+ */
        background: linear-gradient(to bottom, rgba(160, 221, 115, 1) 0%, rgba(119, 178, 77, 1) 100%); /* W3C */
        filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#a0dd73', endColorstr='#77b24d', GradientType=0); /* IE6-9 */
    }

    .submit:hover, .submit:active, .submit:focus {
        border: 1px solid #8eca62;
        background: rgb(143, 202, 100); /* Old browsers */
        background: -moz-linear-gradient(top, rgba(143, 202, 100, 1) 0%, rgba(119, 178, 77, 1) 100%); /* FF3.6+ */
        background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, rgba(143, 202, 100, 1)), color-stop(100%, rgba(119, 178, 77, 1))); /* Chrome,Safari4+ */
        background: -webkit-linear-gradient(top, rgba(143, 202, 100, 1) 0%, rgba(119, 178, 77, 1) 100%); /* Chrome10+,Safari5.1+ */
        background: -o-linear-gradient(top, rgba(143, 202, 100, 1) 0%, rgba(119, 178, 77, 1) 100%); /* Opera 11.10+ */
        background: -ms-linear-gradient(top, rgba(143, 202, 100, 1) 0%, rgba(119, 178, 77, 1) 100%); /* IE10+ */
        background: linear-gradient(to bottom, rgba(143, 202, 100, 1) 0%, rgba(119, 178, 77, 1) 100%); /* W3C */
        filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#8fca64', endColorstr='#77b24d', GradientType=0); /* IE6-9 */
    }

    #ui-datepicker-div {
        box-shadow: 0 0 9px rgba(204, 204, 204, 0.93);
        padding: 10px 25px 15px 25px;
        width: 320px;
    }

    .ui-datepicker-calendar thead tr {
        border-bottom: 1px solid #dfdfdf;
        border-top: 1px solid #dfdfdf;
        color: #d2ad68;
    }

    .ui-state-default {
        width: 32px;
        padding: 4px !important;
        border: 1px solid #ffffff;
        text-align: center !important;
    }

    .ui-state-active, .ui-widget-content .ui-state-active, .ui-widget-header .ui-state-active {
        background: #d2ad68;
        border: 1px solid #d2ad68;
        text-align: center;
        width: 32px;
        padding: 4px;
    }

    .ui-widget-header {
        font-weight: 600;
    }

    .ui-state-hover, .ui-widget-content .ui-state-hover {
        border: 1px solid #d2ad68;
        width: 32px;
        padding: 4px;
    }

    .ui-datepicker th {
        padding: 0.4em 0.3em;
        font-weight: 600;
    }

    .ui-state-hover .ui-icon-circle-triangle-e {
        background: url(pbb/img/design/right-circle-arrow.png) center no-repeat !important;
    }

    .ui-icon-circle-triangle-e {
        background: url(pbb/img/design/right-arrow.gif) center no-repeat !important;
        margin-top: -20px !important;
        height: 31px;
        width: 31px;
    }

    .ui-state-hover .ui-icon-circle-triangle-w {
        background: url(pbb/img/design/left-circle-arrow.png) center no-repeat !important;
    }

    .ui-icon-circle-triangle-w {
        background: url(pbb/img/design/left-arrow.gif) center no-repeat !important;
        margin-top: -20px !important;
        height: 31px;
        width: 31px;
    }

    .ui-widget-header .ui-state-hover {
        border: none !important;
        background: #fff;
    }

    .ui-datepicker .ui-datepicker-next-hover {
        right: 2px !important;
        top: 2px !important;
    }

    .ui-datepicker .ui-datepicker-prev-hover {
        left: 2px !important;
        top: 3px !important;
    }

    .ui-widget-header .ui-state-disabled {
        background: #fff;
    }
</style>


<script>

var __lcode__ = '<%= request.getParameter("hotelId") %>';
var _openparams_ = '';
var _wbhostname_ = '' || false;
var __lang__ = 'ru';
var height = '0';

function WbDel() {
    jQuery('#__wbor_reservation__').fadeOut(400, function () {
        jQuery('#__wbor_cancel__').fadeIn(100);
        set_height();
    });
}

function WbRes() {
    jQuery('#__wbor_cancel__').fadeOut(400, function () {
        jQuery('#__wbor_reservation__').fadeIn(100);
        set_height();
    });
}

function WbMail() {
    jQuery('#__wbor_reservation__').fadeOut(400, function () {
        jQuery('#__wbor_mail__').fadeIn(100);
        set_height();
    });
}

function WbResFromMail() {
    jQuery('#__wbor_mail__').fadeOut(400, function () {
        jQuery('#__wbor_reservation__').fadeIn(100);
        set_height();
    });
}

function IsEmail(email) {
    var regex = /^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    return regex.test(email);
}

function WbCheckMessage() {
    var name = jQuery('#sendmail-wbrname').val();
    var email = jQuery('#sendmail-wbrmail').val();
    var subject = jQuery('#sendmail-wbrsubject').val();
    var text = jQuery('#sendmail-wbrrequest').val();
    var status = true;

    jQuery('#sendmail-wbrname').removeClass('werror');
    jQuery('#sendmail-wbrmail').removeClass('werror');
    jQuery('#sendmail-wbrsubject').removeClass('werror');
    jQuery('#sendmail-wbrrequest').removeClass('werror');

    if (name == '') {
        jQuery('#sendmail-wbrname').addClass('werror');
        status = false;
    }

    if (email == '' || !IsEmail(email)) {
        jQuery('#sendmail-wbrmail').addClass('werror');
        status = false;
    }

    if (subject == '') {
        jQuery('#sendmail-wbrsubject').addClass('werror');
        status = false;
    }

    if (text == '') {
        jQuery('#sendmail-wbrrequest').addClass('werror');
        status = false;
    }

    return status;
}

function WbSendMessage() {
    if (WbCheckMessage()) {
        jQuery('#send-image-row').show();
        jQuery('#send-button-row').hide();

        var par = {
            'cname': jQuery('#sendmail-wbrname').val(),
            'email': jQuery('#sendmail-wbrmail').val(),
            'subject': jQuery('#sendmail-wbrsubject').val(),
            'text': jQuery('#sendmail-wbrrequest').val()
        };

        function myCallback(error) {
            if (!error) {
                jQuery('#send-image-row').hide();
                jQuery('#sent-message-row').show();
                jQuery('#send-button-row').hide();
            }
            else {
                jQuery('#send-image-row').hide();
                jQuery('#send-button-row').show();
                alert('An error occurred: ' + error);
            }
        }

        Pms.sendmail(par, myCallback);
    }
}

function WbCheckCancel() {
    var code = jQuery('#cancel-wbrcode').val();
    var email = jQuery('#cancel-wbrmail').val();
    var status = true;

    jQuery('#cancel-wbrcode').removeClass('werror');
    jQuery('#cancel-wbrmail').removeClass('werror');

    if (code == '') {
        jQuery('#cancel-wbrcode').addClass('werror');
        status = false;
    }

    if (email == '' || !IsEmail(email)) {
        jQuery('#cancel-wbrmail').addClass('werror');
        status = false;
    }

    return status;
}

function WbDelReservation() {
//    if (WbCheckCancel()) {
//        jQuery('#cancel-image-row').show();
//        jQuery('#cancel-button-row').hide();

    var par = {
        'rcode': jQuery('#cancel-wbrcode').val(),
        'email': jQuery('#cancel-wbrmail').val()
    };

    function myCallback(error) {
        if (!error) {
            jQuery('#cancel-image-row').hide();
            jQuery('#cancel-message-row').show();
            jQuery('#cancel-button-row').hide();
        }
        else {
            jQuery('#cancel-image-row').hide();
            jQuery('#cancel-button-row').show();
            alert('An error occurred: ' + error);
        }
    }

    Pms.cancel(par, myCallback);
//    }
}

function set_width(w) {
    /*
     if ( window.parent ) {
     var frame_id = window.frameElement.attributes.id.value;
     parent.document.getElementById(frame_id).style.width = (parseInt(w.replace('px', ''))+12).toString()+'px';
     }
     */
    if (window.parent) {
        set_iframe_width(w);
    }
}

function set_height(h) {
    if (window.parent) {
        if (h || height == 0) {
//             set_iframe_height(h);
        }
    }
}

// var Pms, $, jQuery = null;

function lc() {
    Pms = new _Pms(__lcode__, true, _wbhostname_, false, false, _openparams_);
    $ = jQuery = Pms.jQuery;
    var lang = Pms.jQuery('#lang').val();

    if (lang == 'ru') {

        jQuery(".date-picker").datepicker({
            inline: true,
            dateFormat: "dd/mm/yy",
            minDate: new Date(),
            firstDay: 1,
            dayNames: ["Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье", "Понедельник"],
            dayNamesMin: ["Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб"],
            dayNamesShort: ["Пон", "Втр", "Срд", "Чет", "Пят", "Суб", "Вск"],
            monthNames: ["Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"],
            onSelect: function (selectedDate) {
                Pms.jQuery("#dto").datepicker("option", "minDate", selectedDate);
            },
            beforeShow: function (e, o) {
                try {
                    var w = WuBook.jQuery('body').width();
                    //<![CDATA[
                    if (w < 350) {
                        set_width('350px');
                    }
                    if (height > 0 && height < 300) {
                        set_height(300);
                    }
                    //]]>
                }
                catch (e) {
                }
            },
            onClose: function (e, o) {
                try {
                    set_width('250px');
                    //<![CDATA[
                    if (height > 0) {
                        //]]>
                        set_height('0');
                        //<![CDATA[
                    }
                    //]]>
                }
                catch (e) {

                }
            }
        });
    } else {
        jQuery(".date-picker").datepicker({
            inline: true,
            dateFormat: "dd/mm/yy",
            firstDay: 1,
            minDate: new Date(),
            onSelect: function (selectedDate) {
                Pms.jQuery("#dto").datepicker("option", "minDate", selectedDate);
            },
            beforeShow: function (e, o) {
                try {
                    var w = Pms.jQuery('body').width();
                    //<![CDATA[
                    if (w < 350) {
                        set_width('350px');
                    }
                    if (height > 0 && height < 300) {
                        set_height(300);
                    }
                    //]]>
                }
                catch (e) {
                    console.log('____ before show error ___ ' + e)
                }
            },
            onClose: function (e, o) {
                try {
                    set_width('250px');
                    //<![CDATA[
                    if (height > 0) {
                        //]]>
                        set_height('0');
                        //<![CDATA[
                    }
                    //]]>
                }
                catch (e) {
                }
            }
        });
    }

    if (height == 0) {
        set_height();
    }
}
;

function wbopen(form) {
    try {
        var params = {
            'startD': Pms.jQuery('#dfrom').val(),
            'endD': Pms.jQuery('#dto').val(),
//            'nights': Pms.jQuery('#nights').val(),
            'button': Pms.jQuery('#button').val(),
            'lang': Pms.jQuery('#lang').val()
//            'layout': Pms.jQuery('#layout').val()
        };

        var diff,
                sDate,
                eDate,
                currentDate = new Date();

        currentDate.setHours(0, 0, 0, 0);

        sDate = params.startD.split("/");
        sDate = new Date(sDate[2] + "," + sDate[1] + "," + sDate[0]);
        eDate = params.endD.split("/");
        eDate = new Date(eDate[2] + "," + eDate[1] + "," + eDate[0]);
        diff = Math.round((eDate.getTime() - sDate.getTime()) / 86400000);

//        currentDate = currentDate.getTime();

//        console.log(sDate);
//        console.log(currentDate);

        if (diff > 0 && sDate.getTime() >= currentDate) {
            Pms.open(params);
        } else {
            alert('Ошибка');
        }


        return false;
    }
    catch (e) {
        var fwind = window.open('', 'fwind', 'height=600,width=1100,scrollbars=yes,toolbar=yes,location=1,resizable=1');
        fwind.focus();
        form.submit();
    }
}
</script>
</head>
<body onload="lc()">
<div class="formWrapper">
    <div id="__wbor_reservation__">
        <form action="" onsubmit="wbopen(this);return false;" method="GET">
            <p class="title">онлайн бронирование</p>
            <hr>
            <input type="hidden" name="hotelId" id="hotelId" value="<%= request.getParameter("hotelId") %>">
            <input type="hidden" id="button" name="button" value="<%= request.getParameter("button") %>">
            <input type="hidden" id="lang" value="<%= request.getParameter("lang") %>" name="lang">
            <input type="text" class="field date-picker" id="dfrom" value="" required name="startD" placeholder="Дата заезда">
            <input type="text" class="field date-picker" id="dto" value="" required name="endD" placeholder="Дата выезда">
            <hr>
            <input class="submit" type="submit" value="Забронировать">
        </form>
        <a href="javascript:void(0)" class="wb-link" onclick="WbDel()" id="cancel-link">Отменить бронирование</a>
    </div>
    <div style="display: none;" class="wb-content_block" id="__wbor_cancel__">
        <div class="wb-header" id="cancel-header">
            Отмена бронирования
        </div>

        <div class="wb-row" id="code-row">
            <input type="text" required placeholder="Код" id="cancel-wbrcode">
        </div>
        <div class="wb-row" id="mail-row">
            <input type="text" required placeholder="E-mail" id="cancel-wbrmail">
        </div>
        <div class="wb-button" id="cancel-button-row">
            <button type="submit" class="wb-submit submit" onclick="WbDelReservation()" id="cancel-button">Отменить</button>
        </div>
        <div style="display: none" class="wb-row" id="cancel-image-row">
            <%--<img src="/wbkd/renimgs/ajax-loader.gif" alt="Loading...">--%>
        </div>
        <div style="display: none" class="wb-row" id="cancel-message-row">
            Ваше бронирование было отменено
        </div>
        <div class="wb-row" id="check-link-row">
            <a href="javascript:void(0)" class="wb-link" onclick="WbRes()" id="check-link">Цены и наличие</a>
        </div>

    </div>
</div>
</body>
</html>