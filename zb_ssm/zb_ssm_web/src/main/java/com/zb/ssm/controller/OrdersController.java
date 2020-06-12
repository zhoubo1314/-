package com.zb.ssm.controller;


import com.github.pagehelper.PageInfo;
import com.zb.ssm.domain.Orders;
import com.zb.ssm.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private IOrderService orderService;
    //查询全部订单，未分页
   /* @RequestMapping("/findAll.do")
    public ModelAndView findAll() throws Exception {
        ModelAndView mv = new ModelAndView();
        List<Orders> list = orderService.findAll();
        mv.addObject("ordersList",list);
        mv.setViewName("orders-list");
        return mv;
    }*/

   @RequestMapping("/findAll.do")
   @Secured("ROLE_ADMIN")
    public ModelAndView findAll(@RequestParam(name="page",required = true,defaultValue = "1") Integer page,@RequestParam(name = "size",required = true,defaultValue = "4") Integer size) throws Exception {
       ModelAndView mv = new ModelAndView();
       List<Orders> ordersList = orderService.findAll(page, size);
       //PageInfo就是分页bean
       PageInfo pageInfo = new PageInfo(ordersList);
       mv.addObject("pageinfo",pageInfo);
       mv.setViewName("orders-page-list");
        return mv;
   }

   @RequestMapping("/findById.do")
    public ModelAndView findById(@RequestParam(name = "id",required = true)String ordersId) throws Exception {
       ModelAndView mv = new ModelAndView();
       Orders orders = orderService.findById(ordersId);
       mv.addObject("orders",orders);
       mv.setViewName("orders-show");
       return mv;
   }
}
