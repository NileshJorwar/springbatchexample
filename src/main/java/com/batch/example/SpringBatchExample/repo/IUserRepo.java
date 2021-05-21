package com.batch.example.SpringBatchExample.repo;

import com.batch.example.SpringBatchExample.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepo extends JpaRepository<User, Integer> {
}
