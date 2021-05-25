package com.batch.example.SpringBatchExample.configuration;

import com.batch.example.SpringBatchExample.entity.Contract;
import com.batch.example.SpringBatchExample.entity.ContractHistory;
import com.batch.example.SpringBatchExample.entity.User;
import com.batch.example.SpringBatchExample.tasklets.CsvReaderTasklet;
import com.batch.example.SpringBatchExample.tasklets.PassParamToContextTasklet;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@Slf4j
@AllArgsConstructor
public class BatchConfigurer extends DefaultBatchConfigurer {

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job startJob(
                        Step loadCsvToDBStep,
                        Step transferDataFromOneTableToAnother,
                        Step etlLoad,
                        TaskletStep passParamToContext) {
        return this.jobBuilderFactory.get("contractEffective")
                .incrementer(new RunIdIncrementer())
                .start(passParamToContext)
                .next(loadCsvToDBStep)
                .next(etlLoad)
                .next(transferDataFromOneTableToAnother)
                .build();
    }

    @Bean
    public TaskletStep passParamToContext(PassParamToContextTasklet passParamToContextTasklet)
    {
        return this.stepBuilderFactory.get("param context")
                .tasklet(passParamToContextTasklet)
                .build();
    }
    @Bean
    public Step transferDataFromOneTableToAnother(
                                                  ItemReader<Contract> itemReader,
                                                  ItemProcessor<Contract, ContractHistory> itemProcessor,
                                                  ItemWriter<ContractHistory> itemWriter) throws Exception {
        return this.stepBuilderFactory.get("transferDataFromOneTableToAnother")
                .<Contract, ContractHistory>chunk(500)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    public Step loadCsvToDBStep( CsvReaderTasklet csvReaderTasklet) throws Exception {
        return this.stepBuilderFactory.get("load-Csv-To-DB")
                .tasklet(csvReaderTasklet)
                .build();
    }

    @Bean
    public Step etlLoad(
                        ItemReader<User> fileItemReader,
                        ItemProcessor<User, User> csvProcessor,
                        ItemWriter<User> writer
    ) {
        return this.stepBuilderFactory.get("ETL-Load")
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
    public ItemWriter<User> writer() {
        return new DBWriter();
    }

    @Bean
    public ItemProcessor<User, User> csvProcessor() {
        return new CsvProcessor();
    }
}
