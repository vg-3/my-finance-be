package com.dev.my_finance.controller;

import com.dev.my_finance.exceptions.ResourceNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class User {

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('admin:read', 'user:read')")
    public String get(){
       throw new ResourceNotFoundException("Resource Not Found");
    }
    @PutMapping()
    @PreAuthorize("hasAuthority('user:update')")
    public String put(){
        return "user put";
    }
    @PostMapping()
    @PreAuthorize("hasAuthority('user:create')")
    public String post(){
        return "user post";
    }
    @DeleteMapping()
    @PreAuthorize("hasAuthority('user:delete')")
    public String delete(){
        return "user delete";
    }

}
