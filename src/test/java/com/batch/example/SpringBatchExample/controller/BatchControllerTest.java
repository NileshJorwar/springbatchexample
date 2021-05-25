package com.batch.example.SpringBatchExample.controller;

import com.batch.example.SpringBatchExample.repo.ContractRepo;
import lombok.SneakyThrows;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BatchControllerTest {
    ContractRepo contractRepo;
    @Mock
    JobLauncher jobLauncher;
    @Mock
    Job job;
    @Mock
    JobExecution jobExecution;
    @Mock
    JobInstance jobInstance;

    BatchController batchControllerTest;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        contractRepo = mock(ContractRepo.class);
        batchControllerTest = spy(new BatchController(contractRepo, jobLauncher, job));
    }

    @After
    public void tearDown(){
        batchControllerTest = null;
    }

    @Test
    public void contextLoads(){
        assertNotNull(batchControllerTest);
    }

    @SneakyThrows
    @Test
    public void startBatch_shouldReturnResponse(){
        BatchStatus batchStatus = BatchStatus.STARTED;
        doReturn(jobExecution).when(jobLauncher).run(any(),any());
        doReturn(jobInstance).when(jobExecution).getJobInstance();
        doReturn(batchStatus).when(jobExecution).getStatus();
        ResponseEntity<String> expectedRes = new ResponseEntity<>(HttpStatus.OK);

        ResponseEntity<?> actualRes = batchControllerTest.startBatch();

        ArgumentCaptor<JobParameters> argumentCaptor = ArgumentCaptor.forClass(JobParameters.class);
        verify(jobLauncher, times(1)).run(any(),argumentCaptor.capture());
        assertNotNull(actualRes);
        assertEquals(actualRes.getStatusCode(),expectedRes.getStatusCode());
        assertEquals(actualRes.getBody(),"Job Running");
    }

    @SneakyThrows
    @Test
    public void startBatch_shouldReturnErrorResponse(){
        BatchStatus batchStatus = BatchStatus.FAILED;
        doReturn(jobExecution).when(jobLauncher).run(any(),any());
        doReturn(jobInstance).when(jobExecution).getJobInstance();
        doReturn(batchStatus).when(jobExecution).getStatus();
        ResponseEntity<String> expectedRes = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        ResponseEntity<?> actualRes = batchControllerTest.startBatch();

        ArgumentCaptor<JobParameters> argumentCaptor = ArgumentCaptor.forClass(JobParameters.class);
        verify(jobLauncher, times(1)).run(any(),argumentCaptor.capture());
        assertNotNull(actualRes);
        assertEquals(actualRes.getStatusCode(),expectedRes.getStatusCode());
        assertEquals(actualRes.getBody(),"Error");
    }
}
