/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.netcoms.protocolHandlers.invocation;

import com.google.gson.Gson;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import net.endofinternet.raymoon.netcoms.MessageHandler;
import net.endofinternet.raymoon.netcoms.protocolHandlers.invocation.InvocationHandler.Invocation;
import net.endofinternet.raymoon.netcoms.protocolHandlers.invocation.InvocationHandler.InvocationException;
import net.endofinternet.raymoon.netcoms.protocolHandlers.invocation.InvocationHandler.InvocationParameter;
import net.endofinternet.raymoon.netcoms.protocolHandlers.invocation.InvocationHandler.InvocationResult;

/**
 *
 * @author raymoon
 */
public class ProxyBuilder {

    public static <T> T invoke(final Class<T> classObject, final MessageHandler handler) {
        return (T) Proxy.newProxyInstance(ProxyBuilder.class.getClassLoader(), new Class<?>[]{classObject}, new java.lang.reflect.InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] os) throws Throwable {
                Invocation invocation = new Invocation();

                invocation.interfaceType = classObject.getCanonicalName();
                invocation.method = method.getName();
                if (os == null) {
                    invocation.parameters = new InvocationParameter[0];
                } else {
                    invocation.parameters = new InvocationParameter[os.length];
                }

                for (int i = 0; i < invocation.parameters.length; i++) {
                    invocation.parameters[i] = new InvocationParameter();
                    invocation.parameters[i].parameterType = os[i].getClass().getCanonicalName();
                    invocation.parameters[i].value = new Gson().toJson(os[i]);
                }

                handler.writeMessage(invocation);

                String type = handler.getNextMessageType();

                if (type.equals(InvocationHandler.InvocationResult.class.getCanonicalName())) {
                    InvocationResult result = handler.getMessage(InvocationHandler.InvocationResult.class);
                    if (method.getReturnType().getCanonicalName().equals(result.type)) {
                        if (method.getReturnType().equals(Void.TYPE)) {
                            return null;
                        } else {
                            return new Gson().fromJson(result.value, method.getReturnType());
                        }
                    } else {
                        throw new Exception("failed to invoke ... ");
                    }
                } else if (type.equals(InvocationHandler.InvocationException.class.getCanonicalName())) {
                    InvocationException result = handler.getMessage(InvocationHandler.InvocationException.class);
                    throw (Throwable) Class.forName(result.type).getConstructor(String.class).newInstance(result.message);
                } else {
                    throw new Exception("failed to invoke ... ");
                }
            }
        });
    }
}
