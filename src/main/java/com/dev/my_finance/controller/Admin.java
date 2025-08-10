package com.dev.my_finance.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class Admin {
    @GetMapping()
    @PreAuthorize("hasAuthority('admin:read')")
    public String get(){
        return "admin get";
    }
    @PutMapping()
    @PreAuthorize("hasAuthority('admin:update')")
    public String put(){
        return "admin put";
    }
    @PostMapping()
    @PreAuthorize("hasAuthority('admin:create')")
    public String post(){
        return "admin post";
    }
    @DeleteMapping()
    @PreAuthorize("hasAuthority('admin:delete')")
    public String delete(){
        return "admin delete";
    }
}
