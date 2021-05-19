package com.batch.example.SpringBatchExample.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.batch.example.SpringBatchExample.entity.Contract;
import com.batch.example.SpringBatchExample.entity.ContractHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@Slf4j
public class ItemProcessorConfiguration {

    private AtomicInteger count = new AtomicInteger();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public ItemProcessor<Contract, ContractHistory> itemProcessor() {
        return new ItemProcessor<Contract, ContractHistory>() {
            @Override
            public ContractHistory process(Contract contract) throws Exception {
                log.info("Contract in Processing: "+ contract);
                log.info("Processing Data: " + contract.getContractId() + "Record No: " + count.incrementAndGet());
//                ContractHistory contractHistory = objectMapper.convertValue(contract, ContractHistory.class);

                return objectMapper.convertValue(contract, ContractHistory.class);
            }
        };
    }
}
