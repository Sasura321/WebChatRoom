package com.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

/**
 * LoginServlet：登录控制，用户名和密码再数据库中则跳转到聊天室主页面
 * Auther： zsm
 * Date： 2019/3/17 14:33
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            response.setContentType("text/html,charset=utf-8");
            PrintWriter out=response.getWriter();
            String userid = request.getParameter("userid");
            String password = request.getParameter("password");

            Class.forName("com.mysql.jdbc.Driver");
            String url="jdbc:mysql://localhost:3306/chatroom";
            Connection con=DriverManager.getConnection(url,"root","123456");
            String sql = "select * from user where userid=? and password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,userid);
            ps.setString(2,password);
            ResultSet rs = ps.executeQuery();

            if(!rs.next()) {
                rs.close();
                ps.close();
                con.close();
                String top="<script type=\"text/javascript\">\n"+"alert(\"用户名或密码错误，请重新登录\");\n"
                        +"open(\"../view/login.jsp\",\"_self\");\n"+"</script>";
                out.println(top);
            } else {
                rs.close();
                ps.close();
                con.close();
                request.setAttribute("userid",userid);
                response.sendRedirect("../view/index.jsp");
            }
        } catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
