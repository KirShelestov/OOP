package benchmarks;  // Указываем пакет

import org.openjdk.jmh.annotations.*;
import ru.nsu.shelestov.generator.DataGenerator;
import ru.nsu.shelestov.prime.PrimeChecker;
import ru.nsu.shelestov.prime.StreamPrimeChecker;
import ru.nsu.shelestov.prime.ParallelPrimeChecker;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(2)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class PrimeBenchmark {
    private int[] data;

    @Setup
    public void setup() {
        data = DataGenerator.generateTestData(1_000_000);
    }

    @Benchmark
    public boolean sequential() {
        return PrimeChecker.hasComposite(data);
    }

    @Benchmark
    public boolean parallelStream() {
        return StreamPrimeChecker.hasComposite(data);
    }

    @Benchmark
    public boolean parallelThreads_2() throws Exception {
        return ParallelPrimeChecker.hasComposite(data, 2); // 2 потока
    }

    @Benchmark
    public boolean parallelThreads_4() throws Exception {
        return ParallelPrimeChecker.hasComposite(data, 4); // 4 потока
    }

    @Benchmark
    public boolean parallelThreads_8() throws Exception {
        return ParallelPrimeChecker.hasComposite(data, 8); // 8 потоков
    }
}