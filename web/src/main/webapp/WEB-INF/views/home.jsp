<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ page import="com.idgindigo.pms.channel.wubook.WubookImpl" %>
<%@ page import="com.idgindigo.pms.configuration.WebConfiguration" %>

<%@ page import="com.idgindigo.pms.security.SecurityUtils" %>
<%@ page import="com.idgindigo.pms.web.controller.extranet.DocumentController" %>
<%@ page import="org.joda.time.LocalDate" %>
<%@ page import="static com.idgindigo.pms.logins.domain.Hotel.WubookImportStatus.RESERVATIONS_IMPORTED" %>
<%@ page import="static com.idgindigo.pms.logins.domain.Hotel.WubookImportStatus.DATA_IMPORTED" %>
<%@ page import="java.util.Locale" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="shortcut icon" href="themes/default/images/favicon.ico" type="image/vnd.microsoft.icon"/>
    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <spring:url value="client/Ext/ext-all-debug.js" var="extJsLib" htmlEscape="true"/>
    <spring:url value="client/Pms/Pms.js" var="pmsVar" htmlEscape="true"/>
    <spring:url value="client/Sch/sch-all-debug.js" var="sch" htmlEscape="true"/>
    <spring:url value="client/Pms/core/Ajax.js" var="ajax" htmlEscape="true"/>
    <spring:url value="client/Pms/core/Print.js" var="print" htmlEscape="true"/>
    <spring:url value="client/Pms/overrides/Ext.data.proxy.Ajax.js" var="overrides" htmlEscape="true"/>
    <spring:url value="client/Pms/overrides/ComponentOverrides.js" var="componentOverrides" htmlEscape="true"/>
    <spring:url value="client/bootstrap.js" var="bootstrap" htmlEscape="true"/>
    <spring:eval expression="localeResolver.resolveLocale(pageContext.request).language" var="requestLang"/>
    <spring:url value='client/Ext/ext-lang-${requestLang}.js' var='lang' htmlEscape='true'/>

    <spring:url value="themes/default/css/ext-all-gray.css" var="extTheme" htmlEscape="true"/>
    <spring:url value="client/Sch/resources/css/sch-all.css" var="schTheme" htmlEscape="true"/>
    <spring:url value="client/Ext/src/ux/statusbar/css/statusbar.css" var="extStatusbar" htmlEscape="true"/>
    <spring:url value="themes/default/css/desktop.css" var="desktop" htmlEscape="true"/>
    <spring:url value="themes/default/images/preloader.gif" var="loading" htmlEscape="true"/>
    <spring:url value="themes/default/images/logo.svg" var="loading_logo" htmlEscape="true"/>
    <spring:url value="https://netdna.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.css" var="fontAwesome" htmlEscape="true"/>

    <spring:url value="client/JSON/countries.js" var="countries" htmlEscape="true"/>
    <spring:url value="client/websocket.js" var="websockets" htmlEscape="true"/>
    <spring:url value="client/version.js" var="version" htmlEscape="true"/>

    <%
        Locale locale = new WebConfiguration.CustomLocaleResolver().resolveLocale(request);
        String appLang = "ru";
        if (locale != null && ("en".equalsIgnoreCase(locale.getLanguage()) || "uk".equalsIgnoreCase(locale.getLanguage()))) {
            appLang = locale.getLanguage();
        }
    %>

    <title>PMS Cloud</title>
    <link rel="stylesheet" href="${extTheme}"/>
    <link rel="stylesheet" href="${schTheme}">
    <link rel="stylesheet" href="${extStatusbar}"/>
    <link rel="stylesheet" href="${fontAwesome}"/>
    <link rel="stylesheet" href="${desktop}"/>
    <script>
        var lang = '<%=appLang%>';
        var g_aLabels = {};

        var l = function (sCode, role) {
            if (role) {
                var sCodeRole = sCode + '.' + role;
                if (g_aLabels[sCodeRole] != null) {
                    sCode = sCodeRole;
                }
            }
            return g_aLabels[sCode] == null ? sCode : g_aLabels[sCode];
        };
    </script>
</head>
<body>

<div id="loading-mask"></div>
<!-- <div id="pms-loading-module"></div> -->
<div id="loading">
    <img width="194" src="${loading_logo}"/>

    <div id="loaderImage"><img src="${loading}" alt="Загрузка..."/></div>
    <div id="example-container"></div>
</div>

<script type="text/javascript">
    function _(p) {
        var context = {};
        context.user =<%=SecurityUtils.isTenantUser() ? SecurityUtils.getCurrentUser().getId() : null%>;
        context.name = "<%=SecurityUtils.isTenantUser() ? SecurityUtils.getCurrentUser().getFirstName() + ' ' + SecurityUtils.getCurrentUser().getLastName() : SecurityUtils.getUserDetails().getAuthentication().getUsername()%>";
        context.hotel = <%=new ObjectMapper().writeValueAsString(SecurityUtils.getHotel().getInfo())%>;
        context.maxRooms = <%=SecurityUtils.getHotel().getMaxRooms()%>;
        context.timeZone = "<%=SecurityUtils.getTimeZone().getOffset(LocalDate.now(SecurityUtils.getTimeZone()).toDate().getTime())%>";
        context.channelsEnabled = <%=WubookImpl.ENABLED && SecurityUtils.isWubookConfigured()%>;
        context.hotelId = "<%=SecurityUtils.getCurrentTenantId()%>";
        //deprecated - start
        context.channelsPaused = <%=WubookImpl.ENABLED && SecurityUtils.isWubookConfigured() && SecurityUtils.getHotel().getImportStatus() == DATA_IMPORTED%>;
        //deprecated - end

        context.importStatus = "<%=SecurityUtils.getHotel().getImportStatus()%>";
        context.imagesUrlPrefix = '<%=DocumentController.getImagesUrlPrefix()%>';
        context.extranet = <%=SecurityUtils.getHotel().isExtranet()%>;

        function parsePermissions(s) {
            return s.replace("[", "").replace("]", "").split(", ");
        }

        context.isAdmin = <%=!SecurityUtils.isTenantUser()%>;
        context.permissions = parsePermissions("<%=SecurityUtils.isTenantUser() ? SecurityUtils.getCurrentUser().getRole().getPermissions() : "ADMIN"%>");
        return context[p];
    }

</script>
<c:set var="hotelId" value="<%=SecurityUtils.getCurrentTenantId()%>"></c:set>
<c:set var="demoId" value="266478"></c:set>
<c:if test="${hotelId == demoId}">
    <!-- Yandex.Metrika counter -->
    <script type="text/javascript">
        (function (d, w, c) {
            (w[c] = w[c] || []).push(function () {
                try {
                    w.yaCounter25190681 = new Ya.Metrika({id: 25190681,
                        webvisor: true,
                        clickmap: true});
                } catch (e) {
                }
            });

            var n = d.getElementsByTagName("script")[0],
                    s = d.createElement("script"),
                    f = function () {
                        n.parentNode.insertBefore(s, n);
                    };
            s.type = "text/javascript";
            s.async = true;
            s.src = (d.location.protocol == "https:" ? "https:" : "http:") + "//mc.yandex.ru/metrika/watch.js";

            if (w.opera == "[object Opera]") {
                d.addEventListener("DOMContentLoaded", f, false);
            } else {
                f();
            }
        })(document, window, "yandex_metrika_callbacks");
    </script>
    <noscript>
        <div><img src="//mc.yandex.ru/watch/25190681" style="position:absolute; left:-9999px;" alt=""/></div>
    </noscript>
    <!-- /Yandex.Metrika counter -->
</c:if>
<script src="${pmsVar}"></script>
<script>
    Pms.BASE_PATH = "/";
</script>
<script src="${extJsLib}"></script>
<script src="${lang}"></script>
<script src="${sch}"></script>
<script src="${ajax}"></script>
<script src="${print}"></script>
<script src="${overrides}"></script>
<script src="${componentOverrides}"></script>
<script src="${bootstrap}"></script>
<script src="${countries}"></script>
<script src="${websockets}"></script>
<script src="${version}"></script>

</body>
</html>