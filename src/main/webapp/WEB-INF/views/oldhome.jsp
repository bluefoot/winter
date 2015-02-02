<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>index</title>
</head>
<body>
<div id="usuario-logado">
Olá, 
<c:if test="${namePerson != null}">
  <strong><c:out value="${namePerson}" /></strong>
</c:if>
<c:if test="${namePerson == null}">
  <strong><sec:authentication property="principal.username"/></strong>
</c:if>
</div>
<h1>Página inicial</h1>
<p>
<a href="j_spring_security_logout">logout</a>
</p>

<c:forEach items="${playlists }" var="pl" > ${pl.name } <br /> </c:forEach>

</body>
</html>