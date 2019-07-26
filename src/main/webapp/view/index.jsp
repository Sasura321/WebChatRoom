<%--
  Created by IntelliJ IDEA.
  User: zsm
  Date: 2019/3/18
  Time: 19:08
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">
    <link href="<%=path%>/source/css/index.css" rel='stylesheet' type='text/css' />
    <title>WebChatRoom</title>

    <script type="application/javascript">
        "use strict";

        var Chat = {};
        Chat.socket = null;

        Chat.connect = (function(host) {

            // 判断当前浏览器是否支持websocket协议
            if ('WebSocket' in window) {
                Chat.socket = new WebSocket(host);
            } else if ('MozWebSocket' in window) {
                Chat.socket = new MozWebSocket(host);
            } else {
                Console.log('Error: 当前浏览器不支持');
                return;
            }

            // 建立连接
            Chat.socket.onopen = function () {
                Console.log('* 系统：连接打开...');
                // document.onkeydown来对用户敲击键盘事件进行监听
                document.getElementById('chat').onkeydown = function(event) {
                    // event.keyCode == 13：回车
                    if (event.keyCode == 13) {
                        Chat.sendMessage();
                    }
                };
            };

            // 关闭连接
            Chat.socket.onclose = function () {
                document.getElementById('chat').onkeydown = null;
                Console.log('* 系统：连接关闭.');
            };

            Chat.socket.onmessage = function (message) {
                Console.log(message.data);
            };
        });

        Chat.initialize = function() {
            if (window.location.protocol == 'http:') {
                Chat.connect('ws://' + window.location.host + '/chat');
            } else {
                Chat.connect('wss://' + window.location.host + '/chat');
            }
        };

        Chat.sendMessage = (function() {
            var message = document.getElementById('chat').value;
            if (message != '') {
                Chat.socket.send(message);
                document.getElementById('chat').value = '';
            }
        });

        var Console = {};

        Console.log = (function(message) {
            var console = document.getElementById('console');
            var p = document.createElement('p');
            p.style.wordWrap = 'break-word';
            p.innerHTML = message;
            console.appendChild(p);
            while (console.childNodes.length > 25) {
                console.removeChild(console.firstChild);
            }
            console.scrollTop = console.scrollHeight;
        });

        Chat.initialize();

        document.addEventListener("DOMContentLoaded", function() {
            var noscripts = document.getElementsByClassName("noscript");
            for (var i = 0; i < noscripts.length; i++) {
                noscripts[i].parentNode.removeChild(noscripts[i]);
            }
        }, false);

    </script>
</head>

    <jsp:include page="chat.jsp" />
</html>