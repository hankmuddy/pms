<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:url value="/login" var="loginUrl" htmlEscape="true"/>
<spring:url value="/signup" var="signupUrl" htmlEscape="true"/>
<spring:url value="/recoverPassword" var="recoverPassword" htmlEscape="true"/>
<spring:url value="styles/bootstrap/bootstrap.min.css" var="bootstrapStylesUrl" htmlEscape="true"/>
<spring:url value="styles/app.css" var="appStylesUrl" htmlEscape="true"/>
<!DOCTYPE HTML>
<html>
<head>
    <title><spring:message code="login.pageTitle"/></title>
    <link rel="shortcut icon" href="themes/default/images/favicon.ico" type="image/vnd.microsoft.icon"/>
    <link href="${appStylesUrl}" rel="stylesheet" media="screen">
</head>
<body onload='document.user.hotel.focus();'>

<div class="login-form-bg" style="position: relative;">
    <div style="width:260px;height: 75px;position: absolute;top: 2px;left:160px;cursor: pointer"
         onclick="document.location.href='http://www.pmscloud.com/'"></div>
    <div class="login-form">
        <form name='user' class="form-horizontal center" method="POST" action=${loginUrl}>
            <input type="hidden" id="username" name="username"/>

            <div style="width:285px;border: 0 solid red;margin: 0 auto;">
                <table>
                    <tr>
                        <td>
                            <div style="margin-bottom: 10px;height: 20px;">
                                <c:choose>
                                    <c:when test="${param.error eq 'true'}">
                                        <div class="error">
                                            <spring:message code="login.invalidCredentials"/>
                                        </div>
                                    </c:when>
                                    <c:when test="${param.error eq 'blocked'}">
                                        <div class="error">
                                            <spring:message code="login.accountBlocked"/>
                                        </div>
                                    </c:when>
                                </c:choose>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input name="hotel" type="text" value="" class="hotel" placeholder="<spring:message code="login.hotel"/>" maxlength="60" id="hotel">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input name="login" type="text" value="" required class="login" placeholder="<spring:message code="login.login"/>" maxlength="60"
                                   id="login">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input type="password" name="password" required value="" class="password" placeholder="<spring:message code="login.password"/>"
                                   id="password">
                        </td>
                    </tr>
                </table>
            </div>
            <div style="border: 0 solid red; width: 285px;margin: 15px auto 0 auto;">
                <div style="float: left;color: #767a7d;margin-top: 15px;">
                    <label style="color:#767a7d;">
                        <%--<input type="checkbox"/>--%>
                        <%--Запомнить?--%>
                    </label>
                </div>
                <div style=" margin-left: 65px;">
                    <input id="submit" name="submit" class="idg-btn enter" type="submit" value="<spring:message code="login.submit"/>">
                </div>
                <div style="clear: both;"></div>
            </div>
        </form>
    </div>
    <%--<div style="border: 0px solid red; width: 260px;margin: 10px auto 0 auto; color: #c0c5c9; font-size: 14px;">--%>
    <%--<a href="#" style="color: #c0c5c9;">Зарегистрироваться</a> | <a href="#" style="color: #c0c5c9;">Забыли пароль?</a>--%>
    <%--</div>--%>
</div>
</body>
<script>
    var username = document.user.username;
    var login = document.user.login;
    var hotel = document.user.hotel;
    document.getElementById('submit').onclick = function () {
        username.value = login.value + "%%%" + hotel.value
    }
</script>
</html>