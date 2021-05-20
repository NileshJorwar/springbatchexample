package com.batch.example.SpringBatchExample.configuration;

import com.batch.example.SpringBatchExample.entity.Contract;
import com.batch.example.SpringBatchExample.entity.ContractHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class BatchConfigurer extends DefaultBatchConfigurer {

    @Bean
    public Job startJob(JobBuilderFactory jobBuilderFactory, Step step1) {
        return jobBuilderFactory.get("contractEffective")
                .incrementer(new RunIdIncrementer())
                .start(step1)

                .build();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory,
                      ItemReader<Contract> itemReader,
                      ItemProcessor<Contract, ContractHistory> itemProcessor,
                      ItemWriter<ContractHistory> itemWriter) throws Exception {
//        log.info("Holder from: " + itemReader.read().getHolderName());
        return stepBuilderFactory.get("step1")
                .<Contract, ContractHistory>chunk(500)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }
}
