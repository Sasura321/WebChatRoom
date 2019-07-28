<%--
  Created by IntelliJ IDEA.
  User: zsm
  Date: 2019/3/17
  Time: 10:40
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%String path = request.getContextPath();%>

<!DOCTYPE html>
<html>
<head>
  <title>WebChat(登陆页面)</title>
  <link href="<%=path%>/source/css/login.css" rel='stylesheet' type='text/css' />
</head>
<body>

<h1>WebChatRoom</h1>
<div class="login-form">
  <div class="close"> </div>
  <div class="head-info">
    <label class="lbl-1"></label>
    <label class="lbl-2"></label>
    <label class="lbl-3"></label>
  </div>
  <div class="clear"> </div>
  <div class="avtar"><img src="<%=path%>/source/img/wei.jpg" /></div>
  <form id="login-form" action="<%=path%>/login" method="post">
    <div class="key">
      <input type="text" id="username" name="userid" placeholder="请输入账号" >
    </div>

    <div class="key">
      <input type="password" id="password" name="password" placeholder="请输入密码">
    </div>
    <div class="signin">
      <input type="submit" id="submit" value="LOGIN" >
    </div>
  </form>
</div>
</body>
</html>