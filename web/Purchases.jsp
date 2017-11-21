<%-- 
    Document   : Purchases
    Created on : Nov 14, 2017, 9:14:32 PM
    Author     : kmne6
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Purchases</title>
    </head>
    <body>
        <h1>Member Purchases</h1>
        
        <h2>${m.memid}</h2>
        <h2>${m.firstnm} ${m.lastnm}</h2>
        <table border="1">
            <tr>
            <th>Purchase Dt</th>
            <th>Purchase Type</th>
            
            
        </tr>
        <c:forEach var="p" items="${pur}">
            <tr>
                <td align="left">${p.purchdt}</td>
                <td align="left">${p.purchtype}</td>
                <td align="left">${p.transcd}</td>
                <td align="left">${p.transdesc}</td>
                <td align="right">${p.amount}</td>
            </tr>
        </c:forEach>
                    
        </table>
        <br>
        <p>${msg}</p>
        <br>
        <a href=""MemberScreen.jsp">Back to Member Screen</a>
        
    </body>
</html>
