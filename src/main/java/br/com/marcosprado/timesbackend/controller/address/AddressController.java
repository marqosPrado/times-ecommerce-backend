package br.com.marcosprado.timesbackend.controller.address;

import br.com.marcosprado.timesbackend.dto.AddressDto;
import br.com.marcosprado.timesbackend.service.AddressService;
import br.com.marcosprado.timesbackend.util.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/address")
@CrossOrigin(origins = "*")
public class AddressController {
    private final AddressService service;

    public AddressController(AddressService service) {
        this.service = service;
    }

    @GetMapping("/client/get")
    public ResponseEntity<AddressDto[]> getAddress() {
        Integer clientId = SecurityUtils.getCurrentUserId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getAllAddressByClientId(clientId));
    }

    @PostMapping("/client/new")
    public ResponseEntity<AddressDto> newAddress(
            @RequestBody AddressDto addressDto
    ) {
        Integer clientId = SecurityUtils.getCurrentUserId();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.registerAddress(addressDto, clientId));
    }
}
