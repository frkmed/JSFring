package fr.pinguet62.jsfring.batch.user;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.CompositeJobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.pinguet62.jsfring.model.sql.User;

@Configuration
public class UserImportBatch {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private JobExecutionListener listener;

    @Autowired
    private UserProcessor processor;

    @Autowired
    private UserReader reader;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private UserWriter writer;

    @Bean
    public Job importUserJob() {
        return jobBuilderFactory.get("importUserJob").incrementer(new RunIdIncrementer()).listener(listener)
                .flow(step()).end().build();
    }

    @Bean
    public JobExecutionListener listener() {
        return new CompositeJobExecutionListener();
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get("step1").<UserRow, User> chunk(10).reader(reader).processor(processor)
                .writer(writer).build();
    }

}