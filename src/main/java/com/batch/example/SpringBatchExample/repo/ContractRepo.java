package com.batch.example.SpringBatchExample.repo;

import com.batch.example.SpringBatchExample.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepo extends JpaRepository<Contract, String> {
}
