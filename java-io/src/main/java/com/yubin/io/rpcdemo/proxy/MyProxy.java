package com.yubin.io.rpcdemo.proxy;

import com.yubin.io.rpcdemo.rpc.Dispatcher;
import com.yubin.io.rpcdemo.rpc.protocol.MyContent;
import com.yubin.io.rpcdemo.rpc.transport.ClientFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;

/**
 * 代理类
 *
 * @author YUBIN
 * @create 2021-01-17
 */
public class MyProxy {
    public static <T> T proxyGet(Class<T>  interfaceClass){
        // 实现各个版本的动态代理...(该案例使用的是JDK自带的jdk动态代理)
        ClassLoader loader = interfaceClass.getClassLoader();
        Class<?>[] interfaces = {interfaceClass};
        // LOCAL服务 REMOTE服务  思考实现用到dispatcher是直接给你返回，还是本地调用的时候也代理一下
        // 这里还走一下代理主要是方便扩展
        Dispatcher dis = Dispatcher.getInstance();
        return (T) Proxy.newProxyInstance(loader, interfaces, new InvocationHandler() {

            /**
             * 通过该代理如何设计我们的Consumer对于Provider的调用过程?
             */
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 类的全限定名
                String serviceName = interfaceClass.getName();
                Object obj = dis.get(serviceName);
                Object result = null;
                if (obj == null) {
                    // 1、需要封装的数据是调用的服务、方法、参数  将其 封装成message  [content]
                    // 方法名称
                    String methodName = method.getName();
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    //TODO  rpc  就像小火车拉货  content是service的具体数据，但是还需要header层完成IO传输的控制
                    MyContent content = new MyContent();
                    content.setArgs(args);
                    content.setName(serviceName);
                    content.setMethodName(methodName);
                    content.setParameterTypes(parameterTypes);
                    //TODO 未来的小火车可能会变

                    /**
                     * 1,缺失了注册发现，zk
                     * 2,第一层负载(面向的provider)
                     * 3，consumer  线程池  面向 service；并发就有木桶效应，倾斜
                     * serviceA
                     *      ipA:port
                     *          socket1
                     *          socket2
                     *      ipB:port
                     */

                    CompletableFuture<Object> res = ClientFactory.transport(content);

                    // 5、？如果从IO，未来有响应了, 怎么将代码执行到这里(执行完发送之后,线程处于阻塞状态,有响应了唤醒)(睡眠/回调)
                    //countDownLatch.await();
                    result = res.get();//阻塞的
                } else { // 本机存在服务,则直接调用本机的(这里也走代理)
                    //就是local
                    //插入一些插件的机会，做一些扩展
                    System.out.println("lcoal FC....");
                    Class<?> clazz = obj.getClass();
                    try {
                        Method m = clazz.getMethod(method.getName(), method.getParameterTypes());
                        result = m.invoke(obj, args);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                return result;
            }
        });


    }
}
