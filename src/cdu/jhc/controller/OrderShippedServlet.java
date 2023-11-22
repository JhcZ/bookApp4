package cdu.jhc.controller;

import cdu.jhc.model.User;
import cdu.jhc.service.OrderService;
import cdu.jhc.service.impl.OrderServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

//后台：管理员发货，仅已支付订单才更新为发货状态
@WebServlet("/admin/order/shipped")
public class OrderShippedServlet extends HttpServlet {
    OrderService orderService = new OrderServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //检查管理员登录状态，之后用过滤器处理
        HttpSession session = req.getSession();
        User admin = (User) session.getAttribute("admin");
        if(admin == null){
            //未登录管理员无法查询订单详情
            resp.sendRedirect("login.do");
            return;
        }

        //从客户端获取订单编号及物流单号
        String orderId = req.getParameter("orderId");
        String expressNumber = req.getParameter("expressNumber");
        //调用服务层方法更新订单状态为已发货状态
        orderService.shipped(orderId,expressNumber);
        //重定向到订单列表界面
        resp.sendRedirect("list");
    }
}
