package org.example.parallelstream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MonitoredParallelProcessor extends ParallelPermutationProcessor {

    @Autowired
    private PermutationMetrics metrics;

    @Override
    public List<String> process(String item) throws Exception {
        long startTime = System.currentTimeMillis();

        List<String> result = super.process(item);

        long endTime = System.currentTimeMillis();
        metrics.recordPermutationBatch(result.size(), endTime - startTime);

        return result;
    }
}