# Benchmark Results - Variant #6

## Task

Compare `HashMap.remove()` (custom implementation) vs `java.util.HashMap.remove()` (Java built-in)

---

## Methodology

**Tool:** JMH (Java Microbenchmark Harness) v1.25.2

**Setup:**
- Warmup: 2 iterations, 1 second each
- Measurement: 3 iterations, 1 second each
- Fork: 1
- Mode: Average time per operation

**Test procedure:**
1. Fill map with N elements (Car objects with String keys)
2. Remove ALL N elements
3. Measure total time
4. Repeat for different sizes

**Input sizes:** 10,000 | 20,000 | 40,000 | 80,000 elements

---

## Results

### Raw Data

| Benchmark | Elements | Score (μs) | Error (±μs) |
|-----------|----------|------------|-------------|
| removeCustomHashMap | 10,000 | 164.703 | 159.832 |
| removeCustomHashMap | 20,000 | 612.357 | 299.610 |
| removeCustomHashMap | 40,000 | 974.472 | 1321.127 |
| removeCustomHashMap | 80,000 | 1662.817 | 1563.213 |
| removeJavaHashMap | 10,000 | 99.630 | 25.210 |
| removeJavaHashMap | 20,000 | 223.154 | 126.543 |
| removeJavaHashMap | 40,000 | 483.432 | 313.546 |
| removeJavaHashMap | 80,000 | 1058.088 | 889.833 |

### Comparison Table

| Elements | Custom HashMap (μs) | Java HashMap (μs) | Ratio (Custom/Java) |
|----------|--------------------:|------------------:|--------------------:|
| 10,000 | 164.7 | 99.6 | 1.65x slower |
| 20,000 | 612.4 | 223.2 | 2.74x slower |
| 40,000 | 974.5 | 483.4 | 2.02x slower |
| 80,000 | 1662.8 | 1058.1 | 1.57x slower |

---

## Graph Data (for plotting)

```
Elements,CustomHashMap,JavaHashMap
10000,164.703,99.630
20000,612.357,223.154
40000,974.472,483.432
80000,1662.817,1058.088
```

---

## Computational Complexity

### Custom HashMap.remove()

- **Average case:** O(1) per operation, O(n) for n removals
- **Worst case:** O(n) per operation if all keys hash to same bucket

### java.util.HashMap.remove()

- **Average case:** O(1) per operation, O(n) for n removals
- **Worst case:** O(log n) per operation (uses tree bins for long chains since Java 8)

**Both methods have the same theoretical complexity**, but Java's implementation has better constants.

---

## Analysis

### Why is Java's HashMap faster?

1. **Tree bins (Java 8+)**
   - When a bucket exceeds 8 elements, it converts to a red-black tree
   - Worst-case lookup/remove becomes O(log n) instead of O(n)

2. **Optimized hash function**
   - Java uses additional bit spreading to reduce collisions
   - Better distribution = shorter chains = faster operations

3. **JIT compiler optimizations**
   - Java's HashMap is heavily used, so HotSpot JIT optimizes it aggressively
   - Method inlining, loop unrolling, etc.

4. **Memory layout**
   - Optimized for CPU cache performance
   - Better locality of reference

5. **Decades of engineering**
   - Java's HashMap has been optimized since Java 1.2 (1998)
   - Millions of developers have tested and improved it

### Our Implementation

- Simple linked list for collision resolution
- No tree conversion for long chains
- Basic division hash function
- Still correct and functional, just not as optimized

---

## Conclusions

1. **Experimental results match theoretical complexity**
   - Both implementations show linear scaling (O(n) for removing n elements)
   - Doubling input roughly doubles execution time

2. **Java's HashMap is consistently faster**
   - Average speedup: ~2x faster
   - Due to optimizations, not algorithmic improvements

3. **Our implementation is correct**
   - Passes all tests
   - Same O(1) average time complexity
   - Trade-off: simpler code vs. raw performance

4. **When to use which?**
   - **Production code:** Use `java.util.HashMap` (faster, battle-tested)
   - **Learning:** Custom implementation helps understand internals
   - **Special requirements:** Custom implementation can be modified for specific needs

---

## Environment

- JDK: 17.0.16, OpenJDK 64-Bit Server VM
- OS: Linux
- JMH: 1.25.2
