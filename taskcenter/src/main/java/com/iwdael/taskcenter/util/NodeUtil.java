package com.iwdael.taskcenter.util;

import com.google.gson.reflect.TypeToken;
import com.iwdael.taskcenter.annotations.TaskFieldCreator;
import com.iwdael.taskcenter.core.Node;
import com.iwdael.taskcenter.exceptions.RunningTaskException;
import com.iwdael.taskcenter.interfaces.CloseCreator;
import com.iwdael.taskcenter.interfaces.DisperseCreator;
import com.iwdael.taskcenter.interfaces.Interceptor;
import com.iwdael.taskcenter.interfaces.Mapper;
import com.iwdael.taskcenter.interfaces.UnitCreator;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class NodeUtil {

    public static Pair nodeCreatorInfo(Node<Object, Object, Object> node) {
        Object creator = creatorOfInstance(node);
        Type[] types = typeOfCreator(creator.getClass());
        if (creator instanceof DisperseCreator) {
            return new Pair(types[0], TypeToken.getParameterized(ArrayList.class, types[1]).getType());
        } else {
            return types.length == 1 ? new Pair(types[0], Void.TYPE) : new Pair(types[0], types[1]);
        }
    }

    public static Type[] typeOfCreator(Class<?> clazz) {
        Type[] interfaceTypes = clazz.getGenericInterfaces();
        for (Type interfaceType : interfaceTypes) {
            if (interfaceType.toString().startsWith(CloseCreator.class.getName()) || interfaceType.toString().startsWith(UnitCreator.class.getName()) || interfaceType.toString().startsWith(DisperseCreator.class.getName()) || interfaceType.toString().startsWith(Mapper.class.getName()) || interfaceType.toString().startsWith(Interceptor.class.getName())) {
                ParameterizedType type = (ParameterizedType) interfaceType;
                return type.getActualTypeArguments();
            }
        }
        if (clazz == Object.class) throw new RunningTaskException("ParameterizedType not found");
        if (clazz.getSuperclass() == null) throw new RunningTaskException("ParameterizedType not found");
        return typeOfCreator(clazz.getSuperclass());

    }

    private static Object creatorOfInstance(Node<Object, Object, Object> node) {
        try {
            Field[] fields = Node.class.getDeclaredFields();
            for (Field field : fields) {
                if (field.getAnnotation(TaskFieldCreator.class) != null) {
                    field.setAccessible(true);
                    return field.get(node);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RunningTaskException(String.format("Field(creator) no found in:%s", node.getClass().getName()));
    }


}
