package br.com.marcosprado.timesbackend.service;

import br.com.marcosprado.timesbackend.aggregate.Voucher;
import br.com.marcosprado.timesbackend.dto.CreateVoucherRequest;
import br.com.marcosprado.timesbackend.dto.VoucherResponse;
import br.com.marcosprado.timesbackend.dto.voucher.VoucherSummaryResponse;
import br.com.marcosprado.timesbackend.exception.OperationNotAllowedException;
import br.com.marcosprado.timesbackend.exception.ResourceAlreadyExistsException;
import br.com.marcosprado.timesbackend.exception.ResourceNotFoundException;
import br.com.marcosprado.timesbackend.repository.VoucherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VoucherService {

    private final VoucherRepository repository;

    public VoucherService(VoucherRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public VoucherResponse create(CreateVoucherRequest input) {
        String identifier = input.identifier();
        Optional<Voucher> exists = this.repository.findVoucherByIdentifier(input.identifier());
        if (exists.isPresent()) {
            throw ResourceAlreadyExistsException.voucherAlreadyExists(identifier);
        }

        Voucher voucher = input.toEntity();
        Voucher saved = this.repository.save(voucher);

        return VoucherResponse.fromEntity(saved);
    }

    public Optional<Voucher> findVoucherByIdentifier(String identifier) {
        return repository.findVoucherByIdentifier(identifier);
    }

    public VoucherSummaryResponse getByIdentifier(String identifier) {
        var voucher = findVoucherByIdentifier(identifier)
                .orElseThrow(() -> ResourceNotFoundException.voucherNotFound(identifier));

        if (voucher.isExpired()) {
            throw OperationNotAllowedException.cannotUseExpiredVoucher();
        }

        if (!voucher.isValid()) {
            throw OperationNotAllowedException.cannotUseInvalidVoucher();
        }

        return VoucherSummaryResponse.fromEntity(voucher);
    }
}
