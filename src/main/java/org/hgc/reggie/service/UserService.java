package org.hgc.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.hgc.reggie.entity.User;

public interface UserService extends IService<User> {
    /**
     * 发送邮箱
     * @param to
     * @param subject
     * @param context
     */
    void sendMsg(String to,String subject,String context);
}
