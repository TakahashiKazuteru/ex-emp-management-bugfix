package jp.co.sample.emp_management.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class LoginAdmin extends User {
    private static final long serialVersionUID = 1L;
    public final Administrator admin;
    
    public LoginAdmin(Administrator admin, Collection<GrantedAuthority> authorities){
        super(admin.getMailAddress(),admin.getPassword(),authorities);
        this.admin = admin;
    }
    public Administrator getAdministrator(){
        return admin;
    }
}
