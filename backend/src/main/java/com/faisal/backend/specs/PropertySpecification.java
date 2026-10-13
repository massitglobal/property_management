package com.faisal.backend.specs;

import com.faisal.backend.model.UserProperty;
import org.springframework.data.jpa.domain.Specification;

public class PropertySpecification {
    public static Specification<UserProperty> hasState(String state) {
        return (root, query, cb)
                -> state == null ? null : cb.equal(root.get("state"), state);
    }
    public static Specification<UserProperty> hasDistrict(String district) {
        return (root, query, cb)
                -> district == null ? null : cb.equal(root.get("district"), district);
    }

}
