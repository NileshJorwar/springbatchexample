package com.batch.example.SpringBatchExample.configuration;

import com.batch.example.SpringBatchExample.entity.Contract;
import com.batch.example.SpringBatchExample.entity.ContractHistory;
import com.batch.example.SpringBatchExample.entity.User;
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
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
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

    @Bean
    public Step etlLoad(StepBuilderFactory stepBuilderFactory,
                        ItemReader<User> fileItemReader,
                        ItemProcessor<User, User> csvProcessor,
                        ItemWriter<User> writer
    ) throws Exception {
        return stepBuilderFactory.get("ETL-Load")
                .<User, User>chunk(100)
                .reader(fileItemReader)
                .processor(csvProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    public FlatFileItemReader<User> fileItemReader() {

        FlatFileItemReader<User> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new FileSystemResource("src/main/resources/users.csv"));
        flatFileItemReader.setName("CSV-Reader");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }

    @Bean
    public LineMapper<User> lineMapper() {

        DefaultLineMapper<User> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "name", "dept", "salary");

        BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(User.class);

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }

    @Bean
    public ItemWriter<User> writer(){
        return new DBWriter();
    }

    @Bean
    public ItemProcessor<User,User> csvProcessor(){
        return new CsvProcessor();
    }
}
