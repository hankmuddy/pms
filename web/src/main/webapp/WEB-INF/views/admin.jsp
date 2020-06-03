<%@ page import="com.idgindigo.pms.security.SecurityUtils" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="shortcut icon" href="themes/default/images/favicon.ico" type="image/vnd.microsoft.icon"/>
    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
    <spring:url value="/client/Ext/ext-all-debug.js" var="extJsLibUrl" htmlEscape="true"/>
    <spring:eval expression="localeResolver.resolveLocale(pageContext.request).language" var="requestLang"/>
    <spring:url value='/client/Ext/ext-lang-${requestLang}.js' var='lang' htmlEscape='true'/>
    <%--<spring:url value="/client/Admin/Admin.js" var="adminVar" htmlEscape="true"/>--%>
    <spring:url value="/themes/default/css/admin.css" var="adminStyleUrl" htmlEscape="true"/>
    <spring:url value="client/bootstrap.js" var="bootstrap" htmlEscape="true"/>
    <spring:url value="themes/default/css/ext-all-gray.css" var="extTheme" htmlEscape="true"/>
    <spring:url value="https://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" var="fontAwesome" htmlEscape="true"/>
    <%--<spring:url value="client/Admin/generic/app/Application.js" var="appUrl" htmlEscape="true"/>--%>
    <sec:authorize access="hasAuthority('admin')">
    <spring:url value="client/Admin/adminApp/Application.js" var="appUrl" htmlEscape="true"/>
    </sec:authorize>
    <sec:authorize access="hasAuthority('manager')">
    <spring:url value="client/Admin/managerApp/Application.js" var="appUrl" htmlEscape="true"/>
    </sec:authorize>
    <sec:authorize access="hasAuthority('managerSupervisor')">
    <spring:url value="client/Admin/supervisorApp/Application.js" var="appUrl" htmlEscape="true"/>
    </sec:authorize>
    <spring:url value="client/JSON/countries.js" var="countries" htmlEscape="true"/>
    <spring:url value="client/websocket.js" var="websockets" htmlEscape="true"/>
    <spring:url value="client/JSON/countries.js" var="countries" htmlEscape="true"/>
    <spring:url value="client/Admin/generic/ux/ColorField.js" var="colorField" htmlEscape="true"/>
    <link rel="stylesheet" href="${adminStyleUrl}"/>
    <link rel="stylesheet" href="${extTheme}"/>
    <link rel="stylesheet" href="${fontAwesome}"/>
    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?libraries=places&sensor=true"></script>
    <title>PMS Cloud - admin</title>
</head>
<body>
<script type="text/javascript" src="${extJsLibUrl}"></script>
<script type="text/javascript" src="${colorField}"></script>
<script src="${countries}"></script>
<script>
    var g_aLabels = {};

    var l = function (sCode, role) {
        if (role) {
            var sCodeRole = sCode + '.' + role;
            if (g_aLabels[sCodeRole] != null) {
                sCode = sCodeRole;
            }
        }
        return g_aLabels[sCode] == null
                ? sCode
                : g_aLabels[sCode];
    };
</script>
<script type="text/javascript">
    function _(p) {
        var context = {};
        context.userId =<%=SecurityUtils.getUserDetails().getAuthentication().getId()%>;
        return context[p];
    }

</script>
<%--<script src="${adminVar}"></script>--%>
<script src="${lang}"></script>
<script type="text/javascript">
    //    Ext.Loader.setPath('Ext.ux', 'resources/client/Ext/ux');
    Ext.Loader.setPath('Admin', 'client/Admin');
    Ext.Ajax.request({
        url: 'labels',
        async: false,
        success: function (response) {
            g_aLabels = JSON.parse(response.responseText);
        }
    });
</script>

<script type="text/javascript" src="${appUrl}"></script>
<script src="${countries}"></script>
<script src="${websockets}"></script>
</body>
</html>