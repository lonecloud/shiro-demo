package cn.lonecloud.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.Realm;

/**
 * Created by lonecloud on 17/3/31.
 */
public class MyRealm implements Realm {

    private static final String RELAM_NAME = "MyRealm";

    @Override
    public String getName() {
        return RELAM_NAME;
    }

    /**
     * 仅支持用户名密码的Token
     *
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        //坑爹的地方
        String password = new String((char[]) token.getCredentials()) ;
        System.out.printf("获取到用户名"+username+"密码"+password);
        if (!"1".equals(username)) {
            throw new UnknownAccountException("没有找到该用户");
        }
        if (!"2".equals(password)) {
            throw new IncorrectCredentialsException("密码错误");
        }
        return new SimpleAuthenticationInfo(username, password, getName());
    }
}
