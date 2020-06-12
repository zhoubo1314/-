package com.zb.ssm.controller;

import com.zb.ssm.dao.ISysLogDao;
import com.zb.ssm.domain.SysLog;
import com.zb.ssm.service.ISysLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;


@Component
@Aspect
public class LogAop {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ISysLogService sysLogService;

    private Date visitTime;//开始时间
    private Class clazz;//访问的类
    private Method method;//访问的方法
    //前置通知 主要获取开始时间，执行的类是哪一个，执行的方法是哪一个方法
    @Before("execution(* com.zb.ssm.controller.*.*(..))")
    public void doBefore(JoinPoint jp) throws NoSuchMethodException {
        visitTime = new Date();
        clazz=jp.getTarget().getClass();//具体要访问的类
        String methodName = jp.getSignature().getName();//获取访问的方法名称
        Object[] args = jp.getArgs();//获取访问的方法的参数

        //获取具体执行的方法的Method对象
        if(args==null||args.length==0) {
            method = clazz.getMethod(methodName);//只能获取无参的
        }else{
            Class[] classes=new Class[args.length];
            for(int i=0;i<args.length;i++){
                classes[i]=args[i].getClass();
            }
            clazz.getMethod(methodName,classes);
        }
    }
    //后置通知
    @After("execution(* com.zb.ssm.controller.*.*(..))")
    public void doAfter(JoinPoint jp) throws Exception {
        long time=new Date().getTime()-visitTime.getTime();//获取访问的时长

        //获取URL
        String URL="";
        if(clazz!=null&&method!=null&&clazz!=LogAop.class&&clazz!=SysLogController.class) {
            //1.获取类上的@RequestMapping("/orders")
            RequestMapping clazzAnnotation = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
            if (clazzAnnotation != null) {
                String[] classValue = clazzAnnotation.value();
                //2.获取方法上的@RequestMapping("xxx")
                RequestMapping methodAnnotation = (RequestMapping) method.getAnnotation(RequestMapping.class);
                if (methodAnnotation != null) {
                    String[] methodValue = methodAnnotation.value();
                    URL = classValue[0]+methodValue[0];
                }
            }
            //        获取访问的ip地址
            String ip = request.getRemoteHost();
//            获取当前用户
            SecurityContext context = SecurityContextHolder.getContext();//获取SecurityContext
            User user=(User)context.getAuthentication().getPrincipal();
            String username = user.getUsername();

            //将相关日志信息封装到SysLog对象
            SysLog sysLog = new SysLog();
            sysLog.setExecutionTime(time);
            sysLog.setIp(ip);
            sysLog.setUrl(URL);
            sysLog.setMethod("[类名]"+clazz.getName()+"[方法名]"+method.getName());
            sysLog.setUsername(username);
            sysLog.setVisitTime(visitTime);

            //调用Service完成操作
            sysLogService.save(sysLog);
        }
    }
}
