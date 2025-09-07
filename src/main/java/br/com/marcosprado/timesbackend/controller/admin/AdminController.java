package br.com.marcosprado.timesbackend.controller.admin;

import br.com.marcosprado.timesbackend.dto.admin.ClientFilterDto;
import br.com.marcosprado.timesbackend.dto.admin.UpdateClientStatusDto;
import br.com.marcosprado.timesbackend.dto.client.response.ClientResponseDto;
import br.com.marcosprado.timesbackend.service.AdminService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController()
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("admin/client/search")
    public ResponseEntity<Page<ClientResponseDto>> filterClient(
            @ModelAttribute ClientFilterDto filter,
            Pageable pageable
    ) throws Exception {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(adminService.search(filter, pageable));
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PatchMapping("admin/client/{id}/status")
    public ResponseEntity<?> updateClientStatus(
            @PathVariable("id") Integer id,
            @RequestBody UpdateClientStatusDto dto) {
        try {
            ClientResponseDto updatedClient = adminService.updateStatus(id, dto);
            return ResponseEntity.ok(updatedClient);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar o status do cliente.");
        }
    }
}
