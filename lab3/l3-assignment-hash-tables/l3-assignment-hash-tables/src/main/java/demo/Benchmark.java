package demo;

import utils.HashManager;
import utils.HashMap;
import utils.Map;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

// Variant #6: Compare HashMap.remove() vs java.util.HashMap.remove()

@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(time = 1, timeUnit = TimeUnit.SECONDS)
public class Benchmark {

    // State for our custom HashMap (Separate Chaining)
    @State(Scope.Benchmark)
    public static class FullMap {

        List<String> ids;
        List<Car> cars;
        HashMap<String, Car> carsMap;

        @Setup(Level.Iteration)
        public void generateIdsAndCars(BenchmarkParams params) {
            ids = Benchmark.generateIds(Integer.parseInt(params.getParam("elementCount")));
            cars = Benchmark.generateCars(Integer.parseInt(params.getParam("elementCount")));
        }

        @Setup(Level.Invocation)
        public void fillCarMap(BenchmarkParams params) {
            carsMap = new HashMap<>(HashManager.HashType.DIVISION);
            putMappings(ids, cars, carsMap);
        }
    }

    // State for Java's built-in HashMap (Variant #6)
    @State(Scope.Benchmark)
    public static class FullJavaMap {

        List<String> ids;
        List<Car> cars;
        java.util.HashMap<String, Car> javaMap;

        @Setup(Level.Iteration)
        public void generateIdsAndCars(BenchmarkParams params) {
            ids = Benchmark.generateIds(Integer.parseInt(params.getParam("elementCount")));
            cars = Benchmark.generateCars(Integer.parseInt(params.getParam("elementCount")));
        }

        @Setup(Level.Invocation)
        public void fillJavaMap(BenchmarkParams params) {
            javaMap = new java.util.HashMap<>();
            for (int i = 0; i < cars.size(); i++) {
                javaMap.put(ids.get(i), cars.get(i));
            }
        }
    }

    @Param({"10000", "20000", "40000", "80000"})
    public int elementCount;

    List<String> ids;
    List<Car> cars;

    @Setup(Level.Iteration)
    public void generateIdsAndCars() {
        ids = generateIds(elementCount);
        cars = generateCars(elementCount);
    }

    static List<String> generateIds(int count) {
        return new ArrayList<>(CarsGenerator.generateShuffleIds(count));
    }

    static List<Car> generateCars(int count) {
        return new ArrayList<>(CarsGenerator.generateShuffleCars(count));
    }

    @org.openjdk.jmh.annotations.Benchmark
    public Map<String, Car> putMap() {
        Map<String, Car> carsMap = new HashMap<>(HashManager.HashType.DIVISION);
        putMappings(ids, cars, carsMap);
        return carsMap;
    }

    // Benchmark: Our custom HashMap.remove() (Variant #6 - Method 1)
    @org.openjdk.jmh.annotations.Benchmark
    public void removeCustomHashMap(FullMap fullMap) {
        fullMap.ids.forEach(id -> fullMap.carsMap.remove(id));
    }

    // Benchmark: Java's built-in HashMap.remove() (Variant #6 - Method 2)
    @org.openjdk.jmh.annotations.Benchmark
    public void removeJavaHashMap(FullJavaMap fullJavaMap) {
        fullJavaMap.ids.forEach(id -> fullJavaMap.javaMap.remove(id));
    }

    public static void putMappings(List<String> ids, List<Car> cars, Map<String, Car> carsMap) {
        for (int i = 0; i < cars.size(); i++) {
            carsMap.put(ids.get(i), cars.get(i));
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Benchmark.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}
