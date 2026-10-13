package com.faisal.backend.repository;

import com.faisal.backend.model.UserProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<UserProperty,Long>, JpaSpecificationExecutor<UserProperty> {

    UserProperty findById(long id);
    UserProperty findByGeneratedUUID(String uuid);
}
