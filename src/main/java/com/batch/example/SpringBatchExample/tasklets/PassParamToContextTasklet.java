package com.batch.example.SpringBatchExample.tasklets;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
@StepScope
@Slf4j
public class PassParamToContextTasklet implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("TEXT_KEY", "some value");
        return RepeatStatus.FINISHED;
    }
}
