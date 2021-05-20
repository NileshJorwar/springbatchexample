package com.batch.example.SpringBatchExample.configuration;

import com.batch.example.SpringBatchExample.entity.ContractHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class ItemWriterConfiguration {
    @Bean
    public ItemWriter<ContractHistory> itemWriter(NamedParameterJdbcTemplate jdbcTemplate) {
        final String INSERT_QUERY = "INSERT INTO contract_history (contract_id, holder_name, duration, amount, creation_date, status) VALUES (:contractId, :holderName, :duration, :amount, :creationDate, 'EFFECTIVE')";

        JdbcBatchItemWriter<ContractHistory> itemWriter =
                new JdbcBatchItemWriter<ContractHistory>() {

                    @Override
                    public void write(List<? extends ContractHistory> items) throws Exception {
                        super.write(items);
                        log.info("items processed: " + items.size());
//                        log.info("item : " + items.stream().map(ContractHistory::getHolderName).forEach(x-> System.out.println(x)));
                        delete(items.stream().map(ContractHistory::getContractId).collect(Collectors.toList()), jdbcTemplate);
                    }

                };
        itemWriter.setSql(INSERT_QUERY);
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        itemWriter.setJdbcTemplate(jdbcTemplate);
        itemWriter.setAssertUpdates(false);

        return itemWriter;
    }

    public void delete(final List<String> contractList, NamedParameterJdbcTemplate jdbcTemplate) {
        final String DELETE_QUERY = "DELETE FROM contract WHERE contract_id in (:contract_id)";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("contract_id", contractList);
        jdbcTemplate.update(DELETE_QUERY, parameterSource);
    }
}
