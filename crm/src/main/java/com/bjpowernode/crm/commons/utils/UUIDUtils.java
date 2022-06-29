package com.bjpowernode.crm.commons.utils;

import java.util.UUID;

/**
 * @BelongsProject: Code
 * @BelongsPackage: com.bjpowernode.crm.commons.utils
 * @Author: dushicheng
 * @CreateTime: 2022-06-17  00:31
 * @Description: TODO
 * @Version: 1.0
 * 獲取UUid的值
 */
public class UUIDUtils {
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
