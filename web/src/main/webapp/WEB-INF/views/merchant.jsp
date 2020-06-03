<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
  User: vomel
  Date: 09.07.14
  Time: 17:23
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Proceed to payment</title>
</head>
<body>

<form:form method="POST" action="https://pay.fidobank.ua/EquPlace/pay" name="PayToFido">
    <label>Go to bank payment page</label>

    <form:input type="hidden" path="OrderID"/>
    <form:input type="hidden" path="OrderDate"/>
    <c:forEach items="${command.getSaleDetal()}" var="saleDetal">
        <input type="hidden" name="SaleDetal" value="${saleDetal}"/>
    </c:forEach>
    <c:forEach items="${command.getSalePrice()}" var="salePrice">
        <input type="hidden" name="SalePrice" value="${salePrice}"/>
    </c:forEach>
    <form:input type="hidden" path="SaleCurr"/>
    <form:input type="hidden" path="SaleSum"/>
    <form:input type="hidden" path="ReturnUrl"/>
    <input type="hidden" name="HashCode" value="${command.signature}"/>
    <form:input type="hidden" path="MerchantId"/>
    <form:input type="hidden" path="TerminalId"/>
    <p/>
    <button type="submit">Proceed to payment</button>
</form:form>

<%--
<form name="PayToFido" action="https://pay.fidobank.ua/EquPlace/pay" method="POST">
    <input type="hidden" name="OrderID" value="'.$p_OrderId.'">
    <input type="hidden" name="OrderDate" value="'.$p_OrderDate.'">
    for ($i = 0; $i < count($p_SaleDetal); $i++) {
    <input type="hidden" name="SaleDetal" value="'. $p_SaleDetal[$i].'">
    <input type="hidden" name="SalePrice" value="'.$p_SalePrice[$i].'">
    }
    <input type="hidden" name="SaleCurr" value="'.$p_SaleCurr.'">
    <input type="hidden" name="SaleSum" value="'.$p_SaleSum.'">
    <input type="hidden" name="ReturnUrl" value="'.$p_ReturnUrl.'">
    <input type="hidden" name="HashCode" value="'.$signature.'">
    <button type="submit">Оплатить</button>
    <input type="hidden" name="MerchantId" value="'.$p_MerchantId.'">
    <input type="hidden" name="TerminalId" value="'.$p_TerminalId.'">
</form>--%>
</body>
</html>
