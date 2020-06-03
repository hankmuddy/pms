<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html ng-app="pms">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="themes/default/images/favicon.ico" type="image/vnd.microsoft.icon"/>
    <%--<title>{{model.hotelInfo.name}}</title>--%>
    <title ng-bind="header"></title>
    <%--<title ng-bind="'MyApp - ' + $root.title">MyApp - Welcome</title>--%>

    <!-- Stylesheets -->
    <link rel="stylesheet" href="https://netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">

    <link href="//netdna.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css" rel="stylesheet">

    <link rel="stylesheet" href="pbb/css/jquery-ui-1.10.4.custom.min.css"/>
    <link rel="stylesheet" href="pbb/css/lightbox.css"/>
    <link rel="stylesheet" href="pbb/css/jquery.chosen.css" type="text/css"/>
    <link rel="stylesheet" href="pbb/css/jquery.hoverZoom.min.css" type="text/css"/>
    <link rel="stylesheet" href="pbb/css/smoothzoom.css" type="text/css"/>
    <link rel="stylesheet" href="pbb/css/jquery.bxslider.css" type="text/css"/>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min.js"></script>
    <script src="pbb/js/jquery-ui-1.10.4.custom.min.js"></script>
    <script src="https://netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>

    <!-- Chosen -->
    <script src="pbb/js/chosen.jquery.js"></script>
    <script src="pbb/js/lightbox/lightbox-2.6.min.js"></script>
    <script src="pbb/js/jquery.simpleGal.js"></script>
    <script src="pbb/js/jquery.hoverZoom.js"></script>
    <script src="pbb/js/easing.js"></script>
    <script src="pbb/js/smoothzoom.js"></script>
    <script src="pbb/js/jquery.bxslider.js"></script>
    <script src="pbb/js/jquery.tinycarousel.min.js"></script>

    <!-- Angular related -->
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.14/angular.js"></script>
    <%--<script src="https://code.angularjs.org/1.2.14/angular-route.min.js"></script>--%>
    <script src="//cdnjs.cloudflare.com/ajax/libs/angular.js/1.2.14/angular-route.min.js"></script>
    <script src="pbb/js/ui-bootstrap-tpls-0.9.0.min.js"></script>
    <script src="client/JSON/countries.js"></script>
    <style>
        @import url(//fonts.googleapis.com/css?family=Open+Sans:400,400italic,300italic,300,600,600italic,700,700italic&subset=latin,cyrillic);
        @media print {
            .not-print {
                display: none !important;
            }
        }
        table.align-top td{
            vertical-align: top !important;
        }
        .ui-widget {
            font-family: 'Open Sans', sans-serif !important;
        }
        span.hidden-span{
            position: absolute;
            opacity: 0;
            visibility: hidden;
        }
        span.error{
            margin-top:-55px;
            margin-left: -28px;
            opacity: 1 !important;
            visibility: visible !important;
            /*display: inline;*/
            position: absolute;
            border: 1px solid #8c8c8c;
            background: #fbfbfb;
            width: 200px;
            /*padding: 10px 12px;*/
            opacity: 1;
            visibility: visible;
            overflow: visible;
            /*z-index: 10;*/
            position: absolute;
            font-size: 12px;
            font-style: normal;
            -webkit-border-radius: 3px;
            -moz-border-radius: 3px; -o-border-radius: 3px;
            border-radius: 3px;
            color: #000000;
            background: #fbfbfb;
            background: -moz-linear-gradient(top, #f4f4f4 0%, #fbfbfb 100%);
            background: -webkit-gradient(linear, top, bottom, color-stop(0%,#f4f4f4), color-stop(100%,#fbfbfb));
            filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#f4f4f4', endColorstr='#fbfbfb',GradientType=0 );
            -webkit-box-shadow: 0px 1px 5px 0px #ececec;
            -moz-box-shadow:    0px 1px 5px 0px #ececec;
            box-shadow:         0px 1px 5px 0px #ececec;
            border: 1px solid #ececec;
            line-height: 28px;
            height: 40px;
            padding: 5px;
            display: block;
        }
        span.error b{
            width: 10px;
            height: 10px;
            margin-left:90px;
            margin-top: 29px;
            display: block;
            position: absolute;
            -webkit-transform: rotate(-45deg);
            -moz-transform: rotate(-45deg);
            -o-transform: rotate(-45deg);
            transform: rotate(-45deg);;
            display: none\0/; *display: none;
            background: #fbfbfb;
            border-left: 1px solid #ececec;
            border-bottom: 1px solid #ececec;
        }

        a.back {
            margin: 0;
            font-weight: 500 !important;
            color: #424242 !important;
            text-decoration: underline;
            font-size: 13px;
        }
    </style>

    <script>
        var url = 'app/rest/bbs/2';

        function getQueryVariable(variable) {
            var query = window.location.hash.substring(3);

            query = query.split('?');

            if (query.length > 1) {
                var vars = query[1].split("&");
            } else {
                var vars = query[0].split("&");
            }

            for (var i = 0; i < vars.length; i++) {
                var pair = vars[i].split("=");
                if (pair[0] == variable) {
                    return decodeURIComponent(pair[1]);
                }
            }
            return(false);
        }

        var today = new Date();
        startDate = getQueryVariable('startD'),
                endDate = getQueryVariable('endD'),
                hotelId = getQueryVariable('hotelId');

        if (!startDate && !endDate && !hotelId) {
//            alert('error');
            throw new Error("Неправильный формат данных: ");
        }

        //    window.addEventListener("beforeunload", function (e) {
        //        var confirmationMessage = "\o/";
        //
        //        (e || window.event).returnValue = confirmationMessage; //Gecko + IE
        //        return confirmationMessage;                            //Webkit, Safari, Chrome
        //    });

    </script>
    <style>
        .clearfix {
            clear: both;
        }
    </style>
</head>
<body ng-view bb-border="settings.textColor">

<%--http://stackoverflow.com/questions/3846132/jquery-get-height-of-iframe-content-when-loaded--%>

<%--<div class="container" style="margin-top: 20px; width: 760px;">--%>

<%--</div>--%>

<script src="pbb/js/app/app.js"></script>
<script src="pbb/js/app/controllers.js"></script>
<script src="pbb/js/app/services.js"></script>
<script src="pbb/js/app/factories.js"></script>
<script src="pbb/js/app/directives.js"></script>
</body>
</html>