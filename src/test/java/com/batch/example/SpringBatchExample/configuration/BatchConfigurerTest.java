package com.batch.example.SpringBatchExample.configuration;

import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;

import static org.mockito.Mockito.spy;

//@Import(TestConfiguration.class)
@SpringBatchTest
@RunWith(SpringRunner.class)
@ContextConfiguration
public class BatchConfigurerTest {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    JobExecution jobExecution;
    @Autowired
    Job job;

//    @Autowired
//    private JobBuilderFactory jobBuilderFactory;
//
//    @Autowired
//    private StepBuilderFactory stepBuilderFactory;

//    private BatchConfigurer batchConfigurer ;

//    @BeforeEach
//    public void setup(){
//        MockitoAnnotations.openMocks(this);
////        batchConfigurer = spy(new BatchConfigurer(jobBuilderFactory,stepBuilderFactory));
//    }

    @Test
    @SneakyThrows
    public void test(){

        jobExecution = jobLauncherTestUtils.launchJob();
    }

//    @TestConfiguration
//    static class Config{
//
//        @Bean
//        JobBuilderFactory jobBuilderFactory(JobRepository jobRepository){
//            return new JobBuilderFactory(jobRepository);
//        }
//        @Bean
//        StepBuilderFactory stepBuilderFactory(JobRepository jobRepository, PlatformTransactionManager transactionManager){
//            return new StepBuilderFactory(jobRepository, transactionManager);
//        }
//    }
}
