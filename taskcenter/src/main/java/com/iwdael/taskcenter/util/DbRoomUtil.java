package com.iwdael.taskcenter.util;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.iwdael.dbroom.annotations.DbRoomCreator;
import com.iwdael.taskcenter.core.TaskPersistence;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class DbRoomUtil {
    @DbRoomCreator("com.iwdael.taskcenter.TaskRecorder")
    public static <T extends RoomDatabase> T taskRecorder(Context context, Class<T> room) {
        return Room.databaseBuilder(context, room, "TaskRecorder.db").build();
    }

    @TypeConverter
    public static TaskPersistence jsonConvertTaskPersistence(String json) {
        return new Gson().fromJson(json, TaskPersistence.class);
    }
    @TypeConverter
    public static String taskPersistenceConvertJson(TaskPersistence persistence){
        return  new Gson().toJson(persistence);
    }

}
