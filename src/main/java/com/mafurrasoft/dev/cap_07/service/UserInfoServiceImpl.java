package com.mafurrasoft.dev.cap_07.service;

import com.mafurrasoft.dev.entity.User;
import com.mafurrasoft.dev.services.UserInfoService;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = "singleton", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserInfoServiceImpl implements UserInfoService {
    static protected List<User> userList = new ArrayList<User>();
    static{
        userList.add(new User("anonymous","1234","Anonymous","anonumous@your.com"));
        userList.add(new User("admin","1234","Admin","admin@your.com"));
        userList.add(new User("zkoss","1234","ZKOSS","info@zkoss.org"));
    }

    /** synchronized is just because we use static userList in this demo to prevent concurrent access **/
    public synchronized User findUser(String account){
        int s = userList.size();
        for(int i=0;i<s;i++){
            User u = userList.get(i);
            if(account.equals(u.getAccount())){
                return User.clone(u);
            }
        }
        return null;
    }

    /** synchronized is just because we use static userList in this demo to prevent concurrent access **/
    public synchronized User updateUser(User user){
        int s = userList.size();
        for(int i=0;i<s;i++){
            User u = userList.get(i);
            if(user.getAccount().equals(u.getAccount())){
                userList.set(i,u = User.clone(user));
                return u;
            }
        }
        throw new RuntimeException("user not found "+user.getAccount());
    }
}
