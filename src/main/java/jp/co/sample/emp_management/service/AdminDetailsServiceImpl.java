package jp.co.sample.emp_management.service;

import jp.co.sample.emp_management.domain.Administrator;
import jp.co.sample.emp_management.domain.LoginAdmin;
import jp.co.sample.emp_management.repository.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class AdminDetailsServiceImpl  implements UserDetailsService {
    @Autowired
    private AdministratorRepository administratorRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Administrator administrator = administratorRepository.findByMailAddress(email);
        if(administrator == null){
            throw new UsernameNotFoundException("そのメールアドレスは登録されていません");
        }
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new LoginAdmin(administrator,authorities);
    }
}
