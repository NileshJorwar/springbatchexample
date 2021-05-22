package com.batch.example.SpringBatchExample.configuration;

import com.batch.example.SpringBatchExample.entity.User;
import com.batch.example.SpringBatchExample.repo.IUserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
@Slf4j
public class DBWriter implements ItemWriter<User> {
    @Autowired
    private IUserRepo iUserRepo;

    @Override
    public void write(List<? extends User> users) throws Exception {
        log.info("Data Saved for Users: {} " , users);
        iUserRepo.saveAll(users);
    }
}
