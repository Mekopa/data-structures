# Benchmarking Guide

## Your Assignment: Variant #6

| Method 1 | Method 2 |
|----------|----------|
| `BstSet.remove()` | `java.util.TreeSet.remove()` |

---

## What is JMH?

**JMH** = Java Microbenchmark Harness

- Official OpenJDK benchmarking tool
- Handles JVM warmup, garbage collection, JIT compilation
- Produces statistically reliable results
- Prevents common benchmarking mistakes

---

## How JMH Works

```
1. SETUP: Create data structures with N elements
2. WARMUP: Run the method several times (JVM optimization)
3. MEASURE: Run the method many times, measure average time
4. REPEAT: Test with different sizes (10000, 20000, 40000, 80000)
5. OUTPUT: Results in microseconds per operation
```

---

## Your Benchmark Configuration

```java
@BenchmarkMode(Mode.AverageTime)           // Measure average time per operation
@State(Scope.Benchmark)                     // Shared state across iterations
@OutputTimeUnit(TimeUnit.MICROSECONDS)      // Results in μs
@Warmup(time = 1, timeUnit = TimeUnit.SECONDS)   // 1 second warmup
@Measurement(time = 1, timeUnit = TimeUnit.SECONDS) // 1 second measurement
@Param({"10000", "20000", "40000", "80000"})  // Test these sizes
```

---

## Benchmark Code Structure

### State Classes (Setup)
```java
// For BstSet
@State(Scope.Benchmark)
public static class BstFullSet {
    Car[] cars;
    BstSet<Car> carSet;

    @Setup(Level.Iteration)
    public void generateElements(BenchmarkParams params) {
        cars = generateElements(Integer.parseInt(params.getParam("elementCount")));
    }

    @Setup(Level.Invocation)
    public void fillCarSet() {
        carSet = new BstSet<>(Car.byPrice);
        for (Car car : cars) carSet.add(car);
    }
}

// Similar for TreeSet
@State(Scope.Benchmark)
public static class TreeSetFullSet {
    Car[] cars;
    TreeSet<Car> treeSet;
    // ... same setup pattern
}
```

### Benchmark Methods
```java
@Benchmark
public void removeBst(BstFullSet state) {
    for (Car car : state.cars) {
        state.carSet.remove(car);
    }
}

@Benchmark
public void removeTreeSet(TreeSetFullSet state) {
    for (Car car : state.cars) {
        state.treeSet.remove(car);
    }
}
```

---

## How to Run Benchmarks

### Step 1: Build the JAR
```bash
cd lab2/l2-assignment-binary-search-trees
mvn clean package -DskipTests
```

### Step 2: Run Benchmarks
```bash
# Run only remove benchmarks
java -jar target/benchmarks.jar ".*remove.*" -f 1 -wi 3 -i 5

# Options:
# -f 1     : 1 fork (JVM instance)
# -wi 3    : 3 warmup iterations
# -i 5     : 5 measurement iterations
```

### Step 3: View Results
Results appear at the end:
```
Benchmark                (elementCount)  Mode  Cnt      Score     Error  Units
Benchmark.removeBst               10000  avgt    5   1028.026 ±  95.886  us/op
Benchmark.removeTreeSet           10000  avgt    5   1155.936 ±  60.977  us/op
```

---

## Your Results (Variant #6)

| Input Size | BstSet (μs) | TreeSet (μs) | BstSet Faster By |
|------------|-------------|--------------|------------------|
| 10,000 | 1,028 ± 96 | 1,156 ± 61 | **12%** |
| 20,000 | 2,433 ± 117 | 2,693 ± 13 | **11%** |
| 40,000 | 5,837 ± 74 | 6,385 ± 300 | **9%** |
| 80,000 | 14,232 ± 1,118 | 15,311 ± 652 | **8%** |

### Key Finding
**BstSet.remove() is ~10% faster than TreeSet.remove()!**

---

## Analysis

### Why is BstSet Faster?

1. **No Rebalancing Overhead**
   - BstSet: Just delete the node
   - TreeSet (Red-Black): Must recolor + rotate to maintain balance

2. **Simpler Code Path**
   - BstSet: Straightforward 3-case algorithm
   - TreeSet: Complex color rules, multiple rotation checks

3. **Random Data Advantage**
   - Random shuffled data creates reasonably balanced BSTs
   - BstSet rarely hits worst-case O(n)

4. **JVM Optimization**
   - Simple recursive pattern in BstSet may be better optimized by JIT

### When Would TreeSet Win?

- Sorted input data (BstSet degrades to O(n))
- Sequential insertions/deletions
- Long-running applications (consistent performance matters)

---

## Complexity Verification

**Scaling Analysis:**
```
n → 2n should take slightly more than 2x time for O(n log n)

10k → 20k: 2.37x (expected ~2.15x for O(n log n))
20k → 40k: 2.40x
40k → 80k: 2.44x

Conclusion: Consistent with O(n log n) behavior
```

---

## Files Created

```
benchmarks/
├── results.csv              # Raw data
├── benchmark_graph.png      # Line graph
├── benchmark_bar_chart.png  # Bar chart
└── generate_graph.py        # Python script

report/
└── benchmarking-report.md   # Full analysis
```

---

## Regenerating Graphs

```bash
cd benchmarks
python3 generate_graph.py
```

Outputs: `benchmark_graph.png`, `benchmark_bar_chart.png`

---

## Defence Questions

**Q: "Why did you use JMH instead of System.currentTimeMillis()?"**
A: JMH handles JVM warmup, prevents dead-code elimination, manages garbage collection pauses, and provides statistical error margins. Manual timing is unreliable for microbenchmarks.

**Q: "What does the ± value mean in the results?"**
A: It's the 99.9% confidence interval. We're 99.9% confident the true mean is within that range. Smaller error = more reliable result.

**Q: "Why is BstSet faster even though TreeSet is 'optimized'?"**
A: TreeSet (Red-Black tree) has overhead from rebalancing after every deletion. With random data, BstSet stays reasonably balanced without any overhead. TreeSet's advantage is guaranteed O(log n) worst-case, which matters for sorted input.

**Q: "Does this mean you should always use BstSet?"**
A: No! BstSet is faster for random data, but TreeSet provides consistent performance regardless of input pattern. For production code where you can't guarantee random input, TreeSet is safer.
