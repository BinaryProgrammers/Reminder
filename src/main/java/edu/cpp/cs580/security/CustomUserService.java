package edu.cpp.cs580.security;

import edu.cpp.cs580.manager.UsersManager;
import edu.cpp.cs580.util.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by hardeepsingh on 2/17/17.
 */

@Service
public class CustomUserService implements UserDetailsService {
    private final UsersManager usersManager;
    private Users currentuser;

    @Autowired
    public CustomUserService(UsersManager usersManager) {
        this.usersManager = usersManager;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        currentuser = usersManager.findByEmail(username).get(0);
        if(currentuser.isVerified()){
        	 return new CustomUser(currentuser);
        }
        else 
        	return null;
    }

    public Users getCurrentuser() {
        return currentuser;
    }
}
