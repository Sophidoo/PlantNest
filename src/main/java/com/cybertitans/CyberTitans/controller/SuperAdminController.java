package com.cybertitans.CyberTitans.controller;

import com.cybertitans.CyberTitans.dto.AdminDTO;
import com.cybertitans.CyberTitans.dto.UpdateProfileDTO;
import com.cybertitans.CyberTitans.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("**")
@RestController
@RequestMapping("/api/v1/super-admin")
public class SuperAdminController {
    private final UserDetailsService userDetailsService;
    private final AdminService adminService;

    public SuperAdminController(UserDetailsService userDetailsService, AdminService adminService) {
        this.userDetailsService = userDetailsService;
        this.adminService = adminService;
    }

    @PostMapping("/add-admin")
    public ResponseEntity<String> addAdmin(@RequestBody AdminDTO adminDTO){
        String admin = adminService.addAdmin(adminDTO);
        return new ResponseEntity<>(admin, HttpStatus.CREATED);
    }

    @PatchMapping("/edit-admin/{adminId}")
    public ResponseEntity<String> editAdmin(@RequestBody UpdateProfileDTO adminDTO, @PathVariable Long adminId){
        String admin = adminService.editAdmin(adminDTO, adminId);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    @DeleteMapping("/delete-admin/{adminId}")
    public ResponseEntity<String> deleteAdmin( @PathVariable Long adminId){
        String admin = adminService.deleteAdmin(adminId);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    @PatchMapping("/update")
    public ResponseEntity<UpdateProfileDTO> updateDetails(@RequestBody UpdateProfileDTO adminDTO){
        UpdateProfileDTO updateProfileDTO = adminService.updateSuperAdminDetails(adminDTO);
        return new ResponseEntity<>(updateProfileDTO, HttpStatus.OK);
    }

}
