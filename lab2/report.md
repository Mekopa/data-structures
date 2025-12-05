# Lab 2 Benchmarking Report: Binary Search Tree Remove Operation

**Variant:** #6
**Comparison:** `BstSet.remove()` vs `java.util.TreeSet.remove()`
**Author:** Mekopa
**Date:** December 2025

---

## 1. Description of Benchmarked Methods

### 1.1 BstSet.remove(E element)

A custom implementation of a Binary Search Tree (BST) remove operation. This method removes an element from an unbalanced BST using the standard three-case deletion algorithm:

- **Case 1 (Leaf):** Node has no children - simply remove the node
- **Case 2 (One Child):** Node has one child - replace node with its child
- **Case 3 (Two Children):** Node has two children - replace with in-order predecessor (maximum value in left subtree), then remove the predecessor

**Implementation Characteristics:**
- Recursive implementation
- Uses predecessor approach for two-children case
- No self-balancing mechanism
- Custom comparator support

### 1.2 java.util.TreeSet.remove(Object o)

Java's standard library implementation based on a Red-Black Tree (a self-balancing BST). TreeSet is backed by a TreeMap which uses Red-Black tree internally.

**Implementation Characteristics:**
- Self-balancing (maintains O(log n) height)
- Uses Red-Black tree rotations after deletion
- Highly optimized JDK implementation
- Thread-safe considerations in design

---

## 2. Computational Complexity Analysis

### 2.1 Theoretical Complexity

| Operation | BstSet (Unbalanced) | TreeSet (Red-Black) |
|-----------|---------------------|---------------------|
| Single remove() | O(h) where h = height | O(log n) guaranteed |
| Best case | O(log n) - balanced | O(log n) |
| Worst case | O(n) - degenerate/skewed | O(log n) |
| Average case | O(log n) - random data | O(log n) |

### 2.2 Total Complexity for Removing All Elements

When removing all n elements from a tree:

- **BstSet:** O(n * h) where h varies
  - Best: O(n log n) for balanced tree
  - Worst: O(n^2) for skewed tree

- **TreeSet:** O(n log n) guaranteed due to self-balancing

### 2.3 Space Complexity

Both implementations use O(n) space for storing n elements. The recursive remove operation uses O(h) stack space.

---

## 3. Benchmark Methodology

### 3.1 Benchmarking Framework

**Tool:** JMH (Java Microbenchmark Harness) version 1.25.2
**JVM:** OpenJDK 17.0.16, 64-Bit Server VM

### 3.2 Benchmark Configuration

```
Benchmark Mode:     Average Time (avgt)
Output Unit:        Microseconds (us/op)
Warmup:             3 iterations, 1 second each
Measurement:        5 iterations, 1 second each
Forks:              1
Threads:            1 (single-threaded)
```

### 3.3 Test Data

- **Data Type:** Car objects with price-based comparison
- **Generation:** Random shuffled data using CarsGenerator
- **Input Sizes:** 10,000 / 20,000 / 40,000 / 80,000 elements

### 3.4 Benchmark Procedure

For each measurement:
1. **Setup (per iteration):** Generate array of n random Car objects
2. **Setup (per invocation):** Create fresh tree and add all elements
3. **Measure:** Remove all elements one by one, measure total time
4. **Repeat:** 5 measurement iterations after 3 warmup iterations

This ensures each benchmark starts with a fully populated tree.

---

## 4. Results

### 4.1 Raw Data

| Input Size (n) | BstSet.remove() (μs) | Error (±) | TreeSet.remove() (μs) | Error (±) |
|----------------|----------------------|-----------|----------------------|-----------|
| 10,000 | 1,028.03 | 95.89 | 1,155.94 | 60.98 |
| 20,000 | 2,432.71 | 116.86 | 2,693.42 | 12.65 |
| 40,000 | 5,837.39 | 74.42 | 6,384.91 | 300.40 |
| 80,000 | 14,232.25 | 1,118.10 | 15,311.07 | 652.15 |

### 4.2 Performance Comparison Graphs

#### Line Graph with Error Bars
![Benchmark Line Graph](../benchmarks/benchmark_graph.png)

#### Bar Chart Comparison
![Benchmark Bar Chart](../benchmarks/benchmark_bar_chart.png)

### 4.3 Scaling Analysis

| n₁ → n₂ | Ratio | BstSet Time Ratio | TreeSet Time Ratio | Expected O(n log n) |
|---------|-------|-------------------|--------------------|--------------------|
| 10k → 20k | 2x | 2.37x | 2.33x | ~2.15x |
| 20k → 40k | 2x | 2.40x | 2.37x | ~2.14x |
| 40k → 80k | 2x | 2.44x | 2.40x | ~2.13x |

---

## 5. Conclusions

### 5.1 Consistency with Theoretical Complexity

**Finding:** The experimental results are **consistent** with O(n log n) complexity for both implementations.

**Evidence:**
- When input size doubles, execution time increases by approximately 2.3-2.4x
- For pure O(n) complexity, we would expect exactly 2x increase
- For O(n log n), the expected ratio is ~2.15x (accounting for the log factor)
- The observed ratios (2.3-2.4x) are slightly higher due to:
  - Cache effects at larger sizes
  - Memory allocation overhead
  - JVM garbage collection

### 5.2 Why BstSet is Faster Than TreeSet

**Surprising Result:** Custom BstSet outperforms Java's optimized TreeSet by ~7-10%

**Reasons:**
1. **No Rebalancing Overhead:** BstSet doesn't perform rotations after deletion, while TreeSet (Red-Black) must rebalance to maintain height guarantees

2. **Simpler Code Path:** BstSet's straightforward recursive deletion has fewer branches and operations than Red-Black tree's complex deletion with color adjustments

3. **Test Data Characteristics:** Random shuffled data creates reasonably balanced BSTs, minimizing BstSet's worst-case scenarios

4. **JVM Optimization:** The simple recursive pattern in BstSet may be better optimized by JIT compiler

### 5.3 When Each Would Be Better

| Scenario | Better Choice | Reason |
|----------|---------------|--------|
| Random data, performance-critical | BstSet | Lower constant factors |
| Sorted/sequential data | TreeSet | Guaranteed O(log n) height |
| Unknown data patterns | TreeSet | Consistent performance |
| Simple use cases | BstSet | Easier to understand/modify |

### 5.4 Final Verdict

For **Variant #6** comparison:

- **BstSet.remove()** is approximately **10% faster** than **TreeSet.remove()** with random data
- Both exhibit O(n log n) behavior when removing all elements
- TreeSet provides better **worst-case guarantees** but at the cost of rebalancing overhead
- BstSet is faster for average cases but vulnerable to degenerate inputs

---

## Appendix: Benchmark Configuration

```java
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(time = 1, timeUnit = TimeUnit.SECONDS)
@Param({"10000", "20000", "40000", "80000"})
```

**Full benchmark code available in:** `src/main/java/demo/Benchmark.java`
