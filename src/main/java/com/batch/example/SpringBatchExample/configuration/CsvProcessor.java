package com.batch.example.SpringBatchExample.configuration;

import com.batch.example.SpringBatchExample.entity.Contract;
import com.batch.example.SpringBatchExample.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//@Component
@Slf4j
public class CsvProcessor implements ItemProcessor<User, User> {

    private static final Map<String, String> DEPT_NAMES =
            new HashMap<>();

    public CsvProcessor() {
        DEPT_NAMES.put("01", "IT");
        DEPT_NAMES.put("02", "MEC");
        DEPT_NAMES.put("03", "COMP");
    }

    @Override
    public User process(User user) throws Exception {
        String deptCode = user.getDept();
        String dept = DEPT_NAMES.get(deptCode);
        user.setDept(dept);
        user.setTime(new Date());
        log.info("Converted from {} to {}", deptCode, dept);
        return user;
    }
}
