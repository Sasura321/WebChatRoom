package com.chat;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * ChatWebSocket类描述:简单的聊天室
 * Auther： zsm
 * Date： 2019/3/18 8:11
 */
@ServerEndpoint(value = "/chat")
public class ChatWebSocket {

    private static final String GUEST_PREFIX = "User";

    // AtomicInteger：通过一种线程安全的加减操作接口。
    private static final AtomicInteger connectionIdCount = new AtomicInteger(1);
    // CopyOnWriteArraySet：线程安全的无序的集合，可以将它理解成线程安全的HashSet
    private static final Set<ChatWebSocket> connections = new CopyOnWriteArraySet<>();

    private String nickname; //用户名
    private Session session; //会话

    public ChatWebSocket() {
        nickname = GUEST_PREFIX + connectionIdCount.getAndIncrement();
    }

    /**
     * 创建连接
     * @param session
     */
    @OnOpen
    public void start(Session session) {
        this.session = session;
        connections.add(this);
        // format进行字符串拼接
        String message = String.format("* 系统：%s %s", nickname, "加入聊天室");

        //上线通知（以广播形式）
        radioBroadcast(message);
        //系统问候语 （广播形式）
        sendHello(this.nickname);
        //返回在线用户（广播形式）
        onlineList();

    }

    /**
     * 连接关闭
     */
    @OnClose
    public void end() {
        connections.remove(this);
        String message = String.format("* 系统：%s %s", nickname, "退出聊天室");
        radioBroadcast(message);
    }

    /**
     * 传输信息过程中调用方法
     * @param message
     */
    @OnMessage
    public void sendMessage(String message) {
        String m = String.format("@%s：%s", nickname, message);

        // 修改用户名
        if(m.contains("userName:")){
            nickname = m.split("\\:")[1];
        }

        if(m.contains(" to ")){
            // 私聊
            broadcastOneToOne(m,nickname);
        }else{
            // 群发
            radioBroadcast(m);
        }
    }

    /**
     * 发生错误是调用方法
     * @param t
     * @throws Throwable
     */
    @OnError
    public void onError(Throwable t) throws Throwable {
        System.out.println("错误: " + t.toString());
    }

    /**
     * 点对点发送消息（私聊）
     * 通过connections，对所有其他用户推送信息的方法
     * @param message
     */
    private static void broadcastOneToOne(String message, String nickName) {
        String[] arr = message.split(" to ");
        for (ChatWebSocket client : connections) {
            try {
                // 只对指定的用户发消息
                if(arr[1].equals(client.nickname) || nickName.equals(client.nickname)){
                    synchronized (client) {
                        // 发送消息
                        client.session.getBasicRemote().sendText(arr[0]);
                    }
                }
            } catch (IOException e) {
                // 处理发送失败得消息
                failInSend(client);
            }
        }
    }

    /**
     * 消息广播（群聊）
     * 通过connections，对所有其他用户推送信息的方法
     * @param message
     */
    private static void radioBroadcast(String message) {
        for (ChatWebSocket client : connections) {
            try {
                synchronized (client) {
                    // 发送文本消息
                    client.session.getBasicRemote().sendText(message);
                }
            } catch (IOException e) {
                // 处理发送失败得消息
                failInSend(client);
            }
        }
    }

    /**
     * 发送消息失败
     * @param client
     */
    private static void failInSend(ChatWebSocket client) {
        System.out.println("错误:向客户端发送消息失败");
        connections.remove(client);
        try {
            // 关闭连接
            client.session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String message = String.format("* 系统：%s %s", client.nickname,"退出聊天室...");
        radioBroadcast(message);
    }

    /**
     * 系统问候语
     * @param nickName
     */
    private static void sendHello(String nickName){
        String m = String.format("* 系统：%s %s", nickName, "你好^O^");
        for (ChatWebSocket client : connections) {
            if(client.nickname.equals(nickName)){
                try {
                    client.session.getBasicRemote().sendText(m);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 当前在线用户
     */
    private static void onlineList() {
        String online = "";
        for (ChatWebSocket client : connections) {
            if(online.equals("")){
                online = client.nickname;
            }else{
                online += ","+client.nickname;
            }
        }

        String m = String.format("* 系统：%s %s", "当前在线用户", online);
        for (ChatWebSocket client : connections) {
            try {
                client.session.getBasicRemote().sendText(m);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}