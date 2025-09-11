package org.example.parallelstream;

//import org.springframework.batch.core.Job;

import org.springframework.batch.core.job.Job;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.ResourcelessJobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

import java.util.List;

/**
 * Configuration class for parallel permutation processing using Spring Batch.
 *
 * This class configures a batch job that generates permutations of strings in parallel
 * using Spring Batch's chunk-oriented processing with multi-threading capabilities.
 *
 * The batch job is designed to:
 * 1. Read input data using a custom ItemReader
 * 2. Process permutations in parallel using a custom ItemProcessor
 * 3. Write results using a custom ItemWriter
 * 4. Utilize multi-threading for improved performance
 *
 * @Configuration Indicates that this class provides Spring configuration
 * @EnableBatchProcessing Enables Spring Batch features and provides default configuration
 */
@Configuration
@EnableBatchProcessing
public class ParallelPermutationBatchConfig {

    /*
    public ParallelPermutationBatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager=transactionManager;
    }
    */

    /**
     * Custom ItemReader responsible for providing input data for permutation generation.
     * Injected via Spring's dependency injection.
     */
    @Autowired
    private PermutationItemReader reader;

    /**
     * Custom ItemProcessor that handles parallel permutation generation logic.
     * Injected via Spring's dependency injection.
     */
    @Autowired
    private ParallelPermutationProcessor processor;

    /**
     * Custom ItemWriter responsible for persisting or outputting generated permutations.
     * Injected via Spring's dependency injection.
     */
    @Autowired
    private PermutationItemWriter writer;

    /**
     * JobRepository for managing batch job metadata and state.
     * Injected via Spring's dependency injection from @EnableBatchProcessing.
     */
//    @Autowired
    private JobRepository jobRepository;

    /**
     * Transaction manager for handling batch transaction boundaries.
     * Will be configured programmatically for this batch job.
     */
//    @Autowired
//    private final PlatformTransactionManager transactionManager;

    /**
     * Defines the main batch job for parallel permutation processing.
     *
     * This method:
     * - Creates an in-memory JobRepository for job metadata storage
     * - Configures a custom transaction manager for batch operations
     * - Sets up the job with a single step for permutation processing
     * - Uses a unique job name for identification
     *
     * @return Configured Job instance for parallel permutation processing
     */
    @Bean
    public Job parallelPermutationJob() {
        // Initialize an in-memory job repository (suitable for testing/simple scenarios)
        jobRepository = new ResourcelessJobRepository();

        /*
        // Configure a custom transaction manager for batch operations
        transactionManager = new AbstractPlatformTransactionManager() {
            @Override
            protected Object doGetTransaction() throws TransactionException {
                return null; // No transaction object needed for this implementation
            }

            @Override
            protected void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException {
                // No-op: Custom implementation may not require explicit transaction begin
            }

            @Override
            protected void doCommit(DefaultTransactionStatus status) throws TransactionException {
                // No-op: Custom implementation may not require explicit commit
            }

            @Override
            protected void doRollback(DefaultTransactionStatus status) throws TransactionException {
                // No-op: Custom implementation may not require explicit rollback
            }
        };
*/

        // Build and return the job configuration
        return new JobBuilder("parallelPermutationJob", jobRepository)
//                .start(permutationStep()) // Define the processing step
                .start((Step) processor) // Define the processing step
                .build();
    }
/*
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
 */
    /**
     * Configures the main processing step for permutation generation.
     *
     * This step uses:
     * - Chunk-oriented processing with a chunk size of 1000 items
     * - Custom reader, processor, and writer components
     * - Multi-threading via TaskExecutor for parallel processing
     * - Start limit of 4, restricting the number of step executions
     *
     * @return Configured Step instance for permutation processing
     */
//    @Bean
//    public Step permutationStep() {
//
//        return new StepBuilder("permutationStep", jobRepository)
//                // Configure chunk processing: 1000 items per chunk, String input → List<String> output
//                .<String, List<String>>chunk(1000, transactionManager)
//                .reader(reader)          // Set custom item reader
//                .processor(processor)    // Set custom item processor for parallel processing
//                .writer(writer)          // Set custom item writer
//                .taskExecutor(taskExecutor()) // Enable parallel processing with custom executor
//                .startLimit(4)           // Maximum of 4 step executions (restart protection)
//                .build();
//    }

    /**
     * Configures the TaskExecutor for parallel processing in the batch step.
     *
     * This executor:
     * - Uses a thread pool sized according to available processors
     * - Scales up to twice the available processors under load
     * - Maintains a queue capacity of 1000 tasks
     * - Uses descriptive thread names for monitoring and debugging
     *
     * @return Configured ThreadPoolTaskExecutor for parallel batch processing
     */
    @Bean
    public TaskExecutor taskExecutor() {

        try {
            processor.process("s");
            System.out.println();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Set core pool size to number of available processors for optimal CPU utilization
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());

        // Allow expansion to twice the core size under heavy load
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 2);

        // Queue capacity to handle bursts of processing requests
        executor.setQueueCapacity(1000);

        // Descriptive thread names for easier monitoring and debugging
        executor.setThreadNamePrefix("permutation-worker-");

        // Initialize the executor
        executor.initialize();

        return executor;
    }


}