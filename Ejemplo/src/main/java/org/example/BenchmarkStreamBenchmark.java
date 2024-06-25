package org.example;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BenchmarkStreamBenchmark {
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchmarkStreamBenchmark.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(10)
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    List<Integer> list = new ArrayList<>();

    @Setup(Level.Trial)
    public void setUp() {
        int size = 10000000;
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            list.add(random.nextInt(50000) + 1);
        }
    }

    @Benchmark
    public void resultSecuencial(Blackhole bh) {
        bh.consume(list.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList()));
    }

    @Benchmark
    public void resultParalelo(Blackhole bh) {
        bh.consume(list.parallelStream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList()));
    }
}