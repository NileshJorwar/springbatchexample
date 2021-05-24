package com.batch.example.SpringBatchExample.controller;

import com.batch.example.SpringBatchExample.repo.ContractRepo;
import com.batch.example.SpringBatchExample.entity.Contract;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
@AllArgsConstructor
public class BatchController {

    ContractRepo repo;
    JobLauncher jobLauncher;
    private Job job;

    @GetMapping("/insert")
    public String saveDummyData() {
        List<Contract> contractList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Contract contract = new Contract();
            contract.setHolderName("name-" + i);
            contract.setAmount(new Random().nextInt(500000));
            contract.setDuration(new Random().nextInt());
            Date date = new Date();
            date.setDate(new Random().nextInt(30));
            date.setMonth(new Random().nextInt(12));
            date.setYear(new Random().nextInt(2020));
            contract.setCreationDate(date);
            contract.setStatus("InProgress");
            contractList.add(contract);
        }
        repo.saveAll(contractList);
        return "Saved Successfully";
    }

    @SneakyThrows
    @GetMapping("/start-batch")
    public ResponseEntity<?> startBatch() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis()).toJobParameters();

        JobExecution jobExecution = jobLauncher.run(job, jobParameters);

        ResponseEntity<String> responseEntity;

        if (jobExecution.getStatus().isRunning())
            responseEntity = ResponseEntity.status(HttpStatus.OK).body("Job Running");
        else
            responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        return responseEntity;
    }
}
