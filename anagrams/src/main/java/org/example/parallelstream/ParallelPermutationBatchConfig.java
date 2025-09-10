package org.example.parallelstream;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;


import java.util.List;

@Configuration
@EnableBatchProcessing
public class ParallelPermutationBatchConfig {
    @Autowired
    private PermutationItemReader reader;
    @Autowired
    private ParallelPermutationProcessor processor;
    @Autowired
    private PermutationItemWriter writer;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    private JobBuilder jobBuilder;

    @Autowired
    private JobStepBuilder jobStepBuilder;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job parallelPermutationJob() {
        return jobBuilderFactory.get("parallelPermutationJob")
                .incrementer(new RunIdIncrementer())
                .start(permutationStep())
                .build();
    }

    @Bean
    public Step permutationStep() {

        return stepBuilderFactory.get("permutationStep")
                .<String, List<String>>chunk(1000)
//                .reader(permutationItemReader())
//                .processor(parallelPermutationProcessor())
//                .writer(permutationItemWriter())
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .taskExecutor(taskExecutor())
                .throttleLimit(8)
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 2);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("permutation-worker-");
        executor.initialize();
        return executor;
    }
}