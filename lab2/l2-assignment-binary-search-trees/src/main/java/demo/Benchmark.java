package demo;

import utils.AvlSet;
import utils.BstSet;
import utils.BstSetIterative;
import utils.SortedSet;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(time = 1, timeUnit = TimeUnit.SECONDS)
public class Benchmark {

    // State for BstSet remove benchmark
    @State(Scope.Benchmark)
    public static class BstFullSet {

        Car[] cars;
        BstSet<Car> carSet;

        @Setup(Level.Iteration)
        public void generateElements(BenchmarkParams params) {
            cars = Benchmark.generateElements(Integer.parseInt(params.getParam("elementCount")));
        }

        @Setup(Level.Invocation)
        public void fillCarSet(BenchmarkParams params) {
            carSet = new BstSet<>(Car.byPrice);
            addElements(cars, carSet);
        }
    }

    // State for TreeSet remove benchmark (Variant #6)
    @State(Scope.Benchmark)
    public static class TreeSetFullSet {

        Car[] cars;
        TreeSet<Car> treeSet;

        @Setup(Level.Iteration)
        public void generateElements(BenchmarkParams params) {
            cars = Benchmark.generateElements(Integer.parseInt(params.getParam("elementCount")));
        }

        @Setup(Level.Invocation)
        public void fillTreeSet(BenchmarkParams params) {
            treeSet = new TreeSet<>(Car.byPrice);
            for (Car car : cars) {
                treeSet.add(car);
            }
        }
    }

    @Param({"10000", "20000", "40000", "80000"})
    public int elementCount;

    Car[] cars;

    @Setup(Level.Iteration)
    public void generateElements() {
        cars = generateElements(elementCount);
    }

    static Car[] generateElements(int count) {
        return new CarsGenerator().generateShuffle(count, 1.0);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public BstSet<Car> addBstRecursive() {
        BstSet<Car> carSet = new BstSet<>(Car.byPrice);
        addElements(cars, carSet);
        return carSet;
    }

    @org.openjdk.jmh.annotations.Benchmark
    public BstSetIterative<Car> addBstIterative() {
        BstSetIterative<Car> carSet = new BstSetIterative<>(Car.byPrice);
        addElements(cars, carSet);
        return carSet;
    }

    @org.openjdk.jmh.annotations.Benchmark
    public AvlSet<Car> addAvlRecursive() {
        AvlSet<Car> carSet = new AvlSet<>(Car.byPrice);
        addElements(cars, carSet);
        return carSet;
    }

    // Variant #6: BstSet.remove() benchmark
    @org.openjdk.jmh.annotations.Benchmark
    public void removeBst(BstFullSet state) {
        for (Car car : state.cars) {
            state.carSet.remove(car);
        }
    }

    // Variant #6: java.util.TreeSet.remove() benchmark
    @org.openjdk.jmh.annotations.Benchmark
    public void removeTreeSet(TreeSetFullSet state) {
        for (Car car : state.cars) {
            state.treeSet.remove(car);
        }
    }
    public static void addElements(Car[] carArray, SortedSet<Car> carSet) {
        for (Car car : carArray) {
            carSet.add(car);
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
