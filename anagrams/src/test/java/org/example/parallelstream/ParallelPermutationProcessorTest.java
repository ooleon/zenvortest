package org.example.parallelstream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.HashSet;
import java.util.List;


@SpringBootTest
class ParallelPermutationProcessorTest {


    @Autowired
    ValidationUtil vu;

    @Autowired
    ParallelPermutationProcessor parallelPermutationProcessor;

    @Test
    void process05Chart() throws Exception {
        var sw = new StopWatch();
        var stringToApplay = "a,b,c,d,e";
//        vu.validations(stringToApplay);

        String title = String.format("process %d Charts to permutation, ", stringToApplay.split(",").length);

        sw.start(title + stringToApplay);
        IO.println(stringToApplay);

//        PermutationItemReader reader = new PermutationItemReader(stringToApplay);

        List<String> result = parallelPermutationProcessor.process(stringToApplay);


        sw.stop();
        IO.println("result: " + result.size());
        IO.println(result);
        HashSet<String> hs = new HashSet<>();
        result.forEach((s) -> hs.add(s));
        IO.println("HashSet: " + hs.size());
        IO.println(hs.toString());

        IO.println(sw.prettyPrint());


    }


    @Test
    void process10Chart() throws Exception {
        var sw = new StopWatch();

        var stringToApplay = "a,b,c,d,e,f,g,h,i,j";
        String title = String.format("process %d Charts to permutation, ", stringToApplay.split(",").length);

        sw.start(title + stringToApplay);
        IO.println(stringToApplay);

//        PermutationItemReader reader = new PermutationItemReader(stringToApplay);

        List<String> result = parallelPermutationProcessor.process(stringToApplay);

        sw.stop();
        IO.println(sw.prettyPrint());
        IO.println("result: " + result.size());

    }

    /**
     * No use 11 or more without configure the parameters of heap memory space, it casue an exception.
     * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
     *
     */
    //    @Test
    void process11Chart() throws Exception {
        var sw = new StopWatch();
        sw.start("process 11 Charts to permutation");

        var stringToApplay = "a,b,c,d,e,f,g,h,i,j,k";
        PermutationItemReader reader = new PermutationItemReader(stringToApplay);

        List<String> result = parallelPermutationProcessor.process(stringToApplay);


        sw.stop();
        IO.println("result: " + result.size());
        IO.println(result);
        IO.println(sw.prettyPrint());
    }

    @Test
    void t1(){
        Thread.startVirtualThread(() -> {
            System.out.println(Thread.currentThread());
        });
    }


}