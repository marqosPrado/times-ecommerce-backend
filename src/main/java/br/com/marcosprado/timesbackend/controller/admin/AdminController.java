package br.com.marcosprado.timesbackend.controller.admin;

import br.com.marcosprado.timesbackend.dto.admin.ClientFilterDto;
import br.com.marcosprado.timesbackend.dto.client.ClientResponseDto;
import br.com.marcosprado.timesbackend.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("admin/client/search")
    public ResponseEntity<List<ClientResponseDto>> filterClient(@ModelAttribute ClientFilterDto filter) throws Exception {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(adminService.search(filter));
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
