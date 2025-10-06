package br.com.marcosprado.timesbackend.controller.address;

import br.com.marcosprado.timesbackend.dto.AddressDto;
import br.com.marcosprado.timesbackend.service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class AddressController {
    private final AddressService service;

    public AddressController(AddressService service) {
        this.service = service;
    }

    @GetMapping("/address/client/{id}/get")
    public ResponseEntity<AddressDto[]> getAddress(@PathVariable("id") String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getAllAddressByClientId(id));
    }
}
