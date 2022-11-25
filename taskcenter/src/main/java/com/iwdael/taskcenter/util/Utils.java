package com.iwdael.taskcenter.util;

import com.google.gson.Gson;
import com.iwdael.taskcenter.exceptions.RunningTaskException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class Utils {

    public static <S, T> Collection<T> convert(Collection<S> sources, Converter<S, T> converter) {
        List<T> collection = new ArrayList<>(sources.size());
        for (S source : sources) {
            collection.add(converter.convert(source));
        }
        return collection;
    }

    public interface Converter<S, T> {
        T convert(S s);
    }

    public static Object objectConvertObject(Object o, Type target) {
        try {
            return new Gson().fromJson(new Gson().toJson(o), target);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RunningTaskException("json can not convert: " + new Gson().toJson(o));
        }
    }
}
