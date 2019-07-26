<%--
  Created by IntelliJ IDEA.
  User: zsm
  Date: 2019/3/18
  Time: 21:44
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>

<div class="all">
    <br><br>
    <h1 style="text-align: center">WebChatRoom</h1>
    <div class="chat_index">
        <div id="console-container">
            <div id="console"/>
        </div>
        <div id="tips">
            <p>
                <input type="text" placeholder="输入文字，回车发送" id="chat" /><br>
                温馨提示：<br>
                私聊：输入【消息】" to "【用户名】将消息发送给你指定的用户。比如：你好！ to User1<br>
                群聊：输入消息，直接回车，则发送给全体用户。比如：Hello!【回车】<br>
                修改名字："nserName:"+要修改的名字。比如：nserName:张三 <br>
            </p>
        </div>
    </div>
</div>

</body>
</html>
