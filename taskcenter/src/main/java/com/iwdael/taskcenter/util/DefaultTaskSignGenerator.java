package com.iwdael.taskcenter.util;

import com.google.gson.Gson;
import com.iwdael.taskcenter.interfaces.TaskSignGenerator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class DefaultTaskSignGenerator implements TaskSignGenerator {
    @Override
    public String generate(Object object) {
        String json = new Gson().toJson(object);
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(json.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) i += 256;
                if (i < 16) buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return result;
    }
}
