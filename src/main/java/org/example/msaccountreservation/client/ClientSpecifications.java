package org.example.msaccountreservation.client;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class ClientSpecifications {

   private ClientSpecifications() {
    }


    public static Specification<Client> hasFullName(String fullName) {
        return (root, query, criteriaBuilder) -> {
            if(!StringUtils.hasText(fullName)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("fullName"), fullName);
        };
    }

    public static Specification<Client> hasMdmId(Long mdmId) {
       return (root, query, criteriaBuilder) -> {
            if (mdmId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("mdmId"), mdmId);
       };
    }
}
