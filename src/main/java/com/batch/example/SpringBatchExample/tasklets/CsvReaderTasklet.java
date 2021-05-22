package com.batch.example.SpringBatchExample.tasklets;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@StepScope
public class CsvReaderTasklet implements Tasklet {
    private final Long timeInMilliSecs ;

    public CsvReaderTasklet(
            @Value("#{jobParameters[time]}") Long time){
        this.timeInMilliSecs = time;
    }
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        log.info("TaskLet called for CSV Reading:  {}", timeInMilliSecs);
        return RepeatStatus.FINISHED;
    }
}
