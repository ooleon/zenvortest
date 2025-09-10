package org.example.parallelstream;

import org.springframework.batch.core.*;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/permutations")
public class PermutationController {

    /**
     * Deprecated, for removal: This API element is subject to removal in a future version.
     since 6.0 in favor of JobOperator. Scheduled for removal in 6.2 or later.

     https://docs.spring.io/spring-batch/docs/6.0.x/api/org/springframework/batch/core/launch/JobLauncher.html
     **/
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobOperator jobOperator;


    @Autowired
    private Job parallelPermutationJob;

    @PostMapping("/generate")
    public ResponseEntity<String> generatePermutations() {
        try {
            JobParameters jobParameters = new org.springframework.batch.core.JobParametersBuilder()
                    .addLong("startTime", System.currentTimeMillis())
                    .toJobParameters();


//            JobExecution execution = jobOperator.run(parallelPermutationJob, jobParameters);
            JobExecution execution = jobLauncher.run(parallelPermutationJob, jobParameters);


            return ResponseEntity.accepted()
                    .body("Job started with ID: " + execution.getJobId());

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error starting job: " + e.getMessage());
        }
    }
}