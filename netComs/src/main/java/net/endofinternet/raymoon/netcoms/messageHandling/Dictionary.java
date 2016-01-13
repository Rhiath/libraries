/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.netcoms.messageHandling;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author raymoon
 */
public class Dictionary {

    private final Map<Class, Integer> classToTypeCode = new HashMap<>();
    private final Map<Integer, Class> typeCodeToClass = new HashMap<>();

    public void register(int typeCode, Class type) {
        classToTypeCode.put(type, typeCode);
        typeCodeToClass.put(typeCode, type);
    }

    public int getTypeCode(Class type) {
        return classToTypeCode.get(type);
    }

    public Class getClass(int typeCode) {
        return typeCodeToClass.get(typeCode);
    }
}
