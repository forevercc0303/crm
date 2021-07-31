package com.cc.crm.web.filter;

import com.cc.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String path = request.getServletPath();
        if ("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){
            filterChain.doFilter(req,resp);
        }else {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            if (user != null){
                filterChain.doFilter(req,resp);
            }else {
                //重定向到首页
            /*

                    重定向的路径怎么写？
                        在实际项目开发中，对于路径的使用，不论是前端还是后端，应该一律使用绝对路径
                        关于转发和重定向的路径的写法如下
                        转发：
                            使用的是一种特殊的绝对路径的使用方式，这种绝对路径的前面不加/项目名，这种路径也叫作内部路径
                            /login.jsp
                        重定向：
                             使用的是传统绝对路径的写法，前面必须以/项目名开头，后面跟具体的资源路径
                             /crm/login.jsp

                    为什么使用重定向，使用请求转发不行吗？
                        转发之后，路径会停在老路径上，而不是跳转之后的最新路径
                        我们应该在为用户跳转到登录页的同时，将浏览器的地址栏自动设置为当前登录页的路径



             */
                response.sendRedirect(request.getContextPath()+"/login.jsp");
            }
        }

        }


}
