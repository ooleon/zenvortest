package org.example.parallelstream;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class PermutationMetrics {

    private final MeterRegistry meterRegistry;
    private final Counter permutationCounter;
    private final Timer permutationTimer;

    public PermutationMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.permutationCounter = meterRegistry.counter("permutations.generated");
        this.permutationTimer = meterRegistry.timer("permutations.processing.time");
    }

    public void recordPermutationBatch(int count, long processingTime) {
        permutationCounter.increment(count);
        permutationTimer.record(processingTime, TimeUnit.MILLISECONDS);
    }
}