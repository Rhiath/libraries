/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.datanode.protocolHandlers.invocation;

import com.google.gson.Gson;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.endofinternet.raymoon.datanode.MessageHandler;
import net.endofinternet.raymoon.datanode.ProtocolHandler;
import net.endofinternet.raymoon.datanode.messages.exceptions.InvalidMessageTypeException;

/**
 *
 * @author raymoon
 */
public class InvocationHandler implements ProtocolHandler {

    private Map<Class, Object> instances = new HashMap<>();

    public <T> void register(Class<T> classObject, T instance) {
        synchronized (instances) {
            instances.put(classObject, instance);
        }
    }

    public void unregister(Class classObject) {
        synchronized (instances) {
            instances.remove(classObject);
        }
    }

    @Override
    public void handle(MessageHandler messageHandler) throws IOException, InvalidMessageTypeException {
        Invocation invocation = messageHandler.getMessage(Invocation.class);

        synchronized (instances) {
            for (Class classObject : instances.keySet()) {
                if (classObject.getCanonicalName().equals(invocation.interfaceType)) {
                    handleInvocation(messageHandler, instances.get(classObject), invocation);
                }
            }
        }
    }

    @Override
    public boolean responsibleForNextMessage(MessageHandler messageHandler) throws IOException, InvalidMessageTypeException {
        return messageHandler.getNextMessageType().equals(Invocation.class.getCanonicalName());
    }

    private void handleInvocation(MessageHandler messageHandler, Object target, Invocation invocation) throws IOException {
        for (Method method : target.getClass().getMethods()) {
            if (method.getName().equals(invocation.method)) {
                if (method.getParameterTypes().length == invocation.parameters.length) {
                    Class[] parameterTypes = method.getParameterTypes();
                    boolean matches = true;
                    for (int i = 0; i < parameterTypes.length; i++) {
                        if (!parameterTypes[i].getCanonicalName().equals(invocation.parameters[i].parameterType)) {
                            matches = false;
                        }
                    }

                    if (matches) {
                        invoke(messageHandler, target, method, invocation);
                    }
                }
            }
        }
    }

    private void invoke(MessageHandler messageHandler, Object target, Method method, Invocation invocation) throws IOException {
        try {
            Object[] parameters = new Object[invocation.parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = new Gson().fromJson(invocation.parameters[i].value, method.getParameterTypes()[i]);
            }

            Object retValue;
            try {
                retValue = method.invoke(target, parameters);
            } catch (InvocationTargetException ex) {
                InvocationException result = new InvocationException();
                result.type = ex.getCause().getClass().getCanonicalName();
                result.message = new Gson().toJson(ex.getCause().getMessage());
                messageHandler.writeMessage(result);
                return;
            } catch (Throwable ex) {
                InvocationException result = new InvocationException();
                result.type = ex.getClass().getCanonicalName();
                result.message = new Gson().toJson(ex.getLocalizedMessage());
                messageHandler.writeMessage(result);
                return;
            }

            InvocationResult result = new InvocationResult();
            result.type = method.getReturnType().getCanonicalName();
            result.value = new Gson().toJson(retValue);
            messageHandler.writeMessage(result);
        } catch (IllegalArgumentException ex) {
            throw new IOException("failed to invoke operation", ex);
        }
    }

    public static class Invocation {

        String interfaceType;
        String method;
        InvocationParameter[] parameters;
    }

    public static class InvocationResult {

        String type;
        String value;
    }

    public static class InvocationParameter {

        String parameterType;
        String value;
    }

    public static class InvocationException {

        String type;
        String message;
    }
}
