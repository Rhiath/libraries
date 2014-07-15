/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.persistence.implementation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import net.endofinternet.raymoon.lib.logging.Logging;
import net.endofinternet.raymoon.persistence.interfaces.Fetchable;
import net.endofinternet.raymoon.persistence.interfaces.TableDataGateway;

/**
 *
 * @author raymoon
 */
public class TableDataGatewayDecorator {

    public static <T extends TableDataGateway> T addLogging(Class<T> interfaceType, T toDecorate) {
        return (T) Proxy.newProxyInstance(TableDataGatewayDecorator.class.getClassLoader(),
                new Class[]{interfaceType},
                buildLoggingHandler(toDecorate));
    }

    public static <T extends TableDataGateway> T addFetching(Class<T> interfaceType, T toDecorate) {
        return (T) Proxy.newProxyInstance(TableDataGatewayDecorator.class.getClassLoader(),
                new Class[]{interfaceType},
                buildFetchingHandler(toDecorate));
    }

    private static <T extends TableDataGateway> InvocationHandler buildLoggingHandler(final T toDecorate) {
        return new InvocationHandler() {
            @Override
            public Object invoke(Object proxyObject, Method method, Object[] parameters) throws Throwable {
                long t0 = System.currentTimeMillis();
                try {
                    return method.invoke(toDecorate, parameters);
                } finally {
                    long t1 = System.currentTimeMillis();
                    Logging.debug(TableDataGatewayDecorator.class, "execution of method '" + method.getName() + "' took " + (t1 - t0) + " msec");
                }
            }
        };
    }

    private static <T extends Object> InvocationHandler buildFetchingHandler(final T toDecorate) {
        return new InvocationHandler() {
            @Override
            public Object invoke(Object proxyObject, Method method, Object[] parameters) throws Throwable {
                Object calculatedValue = method.invoke(toDecorate, parameters);

                if (calculatedValue instanceof Fetchable) {
                    ((Fetchable) calculatedValue).fetch();
                }

                return calculatedValue;
            }
        };
    }
}
