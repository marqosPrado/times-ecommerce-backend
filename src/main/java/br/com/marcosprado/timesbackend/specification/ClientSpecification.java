package br.com.marcosprado.timesbackend.specification;

import br.com.marcosprado.timesbackend.aggregate.client.ClientAggregate;
import br.com.marcosprado.timesbackend.enums.Gender;
import org.springframework.data.jpa.domain.Specification;

public class ClientSpecification {

    public static Specification<ClientAggregate> hasName(String name) {
        return ((root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), "%" + name.toLowerCase() + "%"));
    }

    public static Specification<ClientAggregate> hasCpf(String cpf) {
        return ((root, query, criteriaBuilder) ->
                cpf == null ? null : criteriaBuilder.like(root.get("cpf"), cpf));
    }

    public static Specification<ClientAggregate> hasGender(Gender gender) {
        return ((root, query, criteriaBuilder) ->
                gender == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("gender")), gender.name().toLowerCase()));
    }

    public static Specification<ClientAggregate> hasEmail(String email) {
        return ((root, query, criteriaBuilder) ->
                email == null ? null : criteriaBuilder.equal(criteriaBuilder.lower(root.get("email")), email.toLowerCase()));
    }

    public static Specification<ClientAggregate> hasPhone(String phoneNumber) {
        return  ((root, query, criteriaBuilder) ->
                phoneNumber == null ? null : criteriaBuilder.equal(root.get("phoneNumber"), phoneNumber));
    }

    public static Specification<ClientAggregate> hasActive(Boolean active) {
        return ((root, query, criteriaBuilder) ->
                active == null ? null : criteriaBuilder.equal(root.get("active"), active));
    }
}
