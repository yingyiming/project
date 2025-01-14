package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.prefs.BackingStoreException;

import static com.sky.constant.AutoFillConstant.*;


/*
* 实现自定义切面，实现公共字段自动填充
* */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    //切入点,这段代码的用途是在com.sky.mapper包下的所有方法上应用特定的切面逻辑，前提是这些方法被@AutoFill注解标记
    @Pointcut("execution(* com.sky.mapper.*.*(..))&&@annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}

    //前置通知，为公共字段赋值
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("自动填充公共字段");

        //获取被拦截的方法的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//方法对象签名
        AutoFill autofill=signature.getMethod().getAnnotation(AutoFill.class);//获取方法上的注解
        OperationType operationType = autofill.value();//获取操作类型

        //获取到当前被拦截到的方法的参数-实体对象
        Object[] args = joinPoint.getArgs();
        if(args==null||args.length==0){
            return;
        }

        Object arg = args[0];

        //准备赋值数据
        LocalDateTime now= LocalDateTime.now();
        Long currentId=BaseContext.getCurrentId();

        //为实体对象的公共字段赋值
        if(operationType==OperationType.INSERT){
            //插入操作
            try{
                //这段Java代码的作用是获取一个名为setCreateTime的方法，该方法接受一个LocalDateTime类型的参数，并设置给arg对象
                Method setCreateTime = arg.getClass().getDeclaredMethod(SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = arg.getClass().getDeclaredMethod(SET_CREATE_USER, Long.class);
                Method setUpdateTime = arg.getClass().getDeclaredMethod(SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = arg.getClass().getDeclaredMethod(SET_UPDATE_USER, Long.class);

                //通过对象属性来赋值
                setCreateTime.invoke(arg,now);
                setCreateUser.invoke(arg,currentId);
                setUpdateTime.invoke(arg,now);
                setUpdateUser.invoke(arg,currentId);

            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(operationType == OperationType.UPDATE){
            //更新操作
            try{
                Method setUpdateTime = arg.getClass().getDeclaredMethod(SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = arg.getClass().getDeclaredMethod(SET_UPDATE_USER, Long.class);

                setUpdateTime.invoke(arg,now);
                setUpdateUser.invoke(arg,currentId);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
