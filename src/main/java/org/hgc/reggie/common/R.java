package org.hgc.reggie.common;

/**
 * @author hgc
 * @version 1.0
 * @date 2023/4/28/0028 0:14
 */

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果
 *
 * @param <T>
 */
@Data
public class R<T> {
    private Integer code; // 编码：1成功，

    private String msg; // 错误信息

    private T data; // 数据
    private Map map = new HashMap();// 动态数据

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }


}
