package br.com.marcosprado.timesbackend.controller.creditcard;

import br.com.marcosprado.timesbackend.dto.CreditCardDto;
import br.com.marcosprado.timesbackend.service.CreditCardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class CreditCardController {
    private final CreditCardService service;

    public CreditCardController(CreditCardService service) {
        this.service = service;
    }

    @GetMapping("/credit-card/client/{id}/get")
    public ResponseEntity<CreditCardDto[]> getCreditCard(@PathVariable("id") String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getAllCreditCardsByClientId(id));
    }

    @PostMapping("/credit-card/client/{id}/new")
    public ResponseEntity<CreditCardDto> newCreditCard(
            @RequestBody CreditCardDto creditCardDto,
            @PathVariable("id") String id
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.registerCreditCard(creditCardDto, id));
    }
}
