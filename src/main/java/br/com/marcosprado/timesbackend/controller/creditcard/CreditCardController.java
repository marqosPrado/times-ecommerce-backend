package br.com.marcosprado.timesbackend.controller.creditcard;

import br.com.marcosprado.timesbackend.dto.CreditCardDto;
import br.com.marcosprado.timesbackend.service.CreditCardService;
import br.com.marcosprado.timesbackend.util.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/credit-card")
@CrossOrigin(origins = "*")
public class CreditCardController {
    private final CreditCardService service;

    public CreditCardController(CreditCardService service) {
        this.service = service;
    }

    @GetMapping("/client/get")
    public ResponseEntity<CreditCardDto[]> getCreditCard() {
        Integer clientId = SecurityUtils.getCurrentUserId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getAllCreditCardsByClientId(clientId));
    }

    @PostMapping("/client/new")
    public ResponseEntity<CreditCardDto> newCreditCard(
            @RequestBody CreditCardDto creditCardDto
    ) {
        Integer clientId = SecurityUtils.getCurrentUserId();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.registerCreditCard(creditCardDto, clientId));
    }
}
