package com.kong.yygh.hosp.repository;

import com.kong.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalRepository extends MongoRepository<Hospital,String> {
    Hospital getHospitalByHoscode(String hospCode);

    List<Hospital> findHospitalByHosnameLike(String hosname);
}
