package com.iwdael.taskcenter.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class Generic {
    public final Class<?> clazz;
    public final Type type;
    public final Generic subGeneric;
    public final Generic[] superGenerics;

    public Generic(Class<?> clazz) {
        this(clazz, null, null);
    }

    private Generic(Class<?> clazz, Type type, Generic subGeneric) {
        this.clazz = clazz;
        this.type = type;
        this.subGeneric = subGeneric;
        this.superGenerics = superGenerics();
    }

    private Generic[] superGenerics() {
        Class<?> superClass = clazz.getSuperclass();
        Type superType = clazz.getGenericSuperclass();
        Class<?>[] interfaces = clazz.getInterfaces();
        Type[] interfaceTypes = clazz.getGenericInterfaces();
        List<Generic> generics = new ArrayList<>();
        if (superClass != null) generics.add(new Generic(superClass, superType, this));
        for (Class<?> clazz : interfaces) {
            for (Type type : interfaceTypes) {
                if (type.toString().startsWith(clazz.getName())) generics.add(new Generic(clazz, type, this));
            }
        }
        return generics.toArray(new Generic[0]);
    }

    public Type getTypeArgument(Class<?> clazz, String typeName) {
        Generic generic = findGeneric(clazz);
        if (generic == null) return null;
        return generic.findTypeArgument(typeName);
    }

    private Type findTypeArgument(String typeName) {
        TypeVariable<? extends Class<?>>[] typeVariables = clazz.getTypeParameters();
        if (this.type == null) {
            for (TypeVariable<? extends Class<?>> typeParameter : typeVariables) {
                if (typeParameter.getName().equals(typeName)) return typeParameter;
            }
            return null;
        } else {
            int typeVariablesCount = typeVariables.length;
            int typeIndex = -1;
            for (int index = 0; index < typeVariablesCount; index++) {
                if (typeVariables[index].getName().equals(typeName)) {
                    typeIndex = index;
                    break;
                }
            }
            if (typeIndex == -1) return null;
            if (this.type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) this.type;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                Type typeArgument = actualTypeArguments[typeIndex];
                if (typeArgument instanceof Class) return typeArgument;
                return subGeneric.findTypeArgument(typeArgument.toString());
            }
        }
        return null;
    }

    private Generic findGeneric(Class<?> clazz) {
        if (this.clazz == clazz) return this;
        for (Generic superGeneric : superGenerics) {
            Generic generic = superGeneric.findGeneric(clazz);
            if (generic != null) return generic;
        }
        return null;
    }
}
