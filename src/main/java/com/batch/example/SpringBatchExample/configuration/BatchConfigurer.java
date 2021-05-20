package com.batch.example.SpringBatchExample.configuration;

import com.batch.example.SpringBatchExample.entity.Contract;
import com.batch.example.SpringBatchExample.entity.ContractHistory;
import com.batch.example.SpringBatchExample.tasklets.CsvReaderTasklet;
import lombok.Value;
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
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@Slf4j
public class BatchConfigurer extends DefaultBatchConfigurer {

    @Bean
    public Job startJob(JobBuilderFactory jobBuilderFactory, Step step1, Step loadCsvToDBStep) {
        return jobBuilderFactory.get("contractEffective")
                .incrementer(new RunIdIncrementer())
                .start(loadCsvToDBStep)
                .next(step1)
                .build();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory,
                      ItemReader<Contract> itemReader,
                      ItemProcessor<Contract, ContractHistory> itemProcessor,
                      ItemWriter<ContractHistory> itemWriter) throws Exception {
        return stepBuilderFactory.get("step1")
                .<Contract, ContractHistory>chunk(500)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    public Step loadCsvToDBStep(StepBuilderFactory stepBuilderFactory) throws Exception {
        return stepBuilderFactory.get("load-Csv-To-DB")
                .tasklet(new CsvReaderTasklet())
                .build();
    }

}
