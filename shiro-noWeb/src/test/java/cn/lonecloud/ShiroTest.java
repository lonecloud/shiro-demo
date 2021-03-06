package cn.lonecloud;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**ˇ
 * Created by lonecloud on 17/3/30.
 */
public class ShiroTest {
    @Test
    public void testHelloWorld(){
        checkAuth("classpath:shiro.ini","zhang","123");
    }
    @Test
    public void testMyRelam(){
        checkAuth("classpath:shiro-realm.ini","1","2");
    }
    @Test
    public void testMySqlRelam(){
        checkAuth("classpath:shiro-mysql.ini","1","1");
    }

    /**
     * 多Realmd的判断
     */
    @Test
    public void testMutiRelam(){
        checkAuth("classpath:shiro-mutiRealm.ini","1","2");
    }

    /**
     * 判断有没有权限
     */
    @Test
    public void testRole(){
        Subject subject = checkAuth("classpath:role/shiro-role.ini", "wang", "123");
        boolean isAdmin = subject.hasRole("admin");
        Assert.assertEquals(true,isAdmin);
    }

    /**
     * 判断是否拥有多个用户组
     */
    @Test
    public void testRoleList(){
        Subject subject = checkAuth("classpath:role/shiro-role.ini", "zhang", "123");
        boolean allRoles = subject.hasAllRoles(Arrays.asList("admin", "admin2", "admin32"));
        Assert.assertEquals(true,allRoles);
    }

    /**
     * 判断有没有操作权限
     */
    @Test
    public void testHasPerssion(){
        Subject subject = checkAuth("classpath:role/shiro_role_perssion.ini", "zhang", "123");
        boolean permitted = subject.isPermitted("users:crate");
        boolean permittedAll = subject.isPermittedAll("users:crate", "users:delete");
        Assert.assertEquals(true,permitted);
        Assert.assertEquals(true,permittedAll);
    }
    /**
     * 登录
     * @param iniPath
     * @param username
     * @param password
     * @return
     */
    private Subject checkAuth(String iniPath,String username,String password){
        //1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
        Factory<SecurityManager> factory =
                new IniSecurityManagerFactory(iniPath);
        //2、得到SecurityManager实例 并绑定给SecurityUtils
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        //3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            System.out.printf("测试前");
            //4、登录，即身份验证
            subject.login(token);
            //5.获取认证信息
            PrincipalCollection principals = subject.getPrincipals();
            System.out.println("认证信息:"+principals==null?0:principals.asList());
        } catch (AuthenticationException e) {
            //5、身份验证失败
            e.printStackTrace();
            System.out.println("程序出错!");
        }
        Assert.assertEquals(true, subject.isAuthenticated()); //断言用户已经登录
        return subject;
    }

    /**
     * 退出
     * @param subject
     */
    public void logout(Subject subject){
        //6、退出
        subject.logout();
    }
}
