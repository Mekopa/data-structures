# Lab 2: Binary Search Trees - Learning Notes

**Status:** In Progress
**Branch:** L2-assignment
**Started:** 2025-11-21

---

## Overview

Lab 2 focuses on implementing and benchmarking Binary Search Trees (BST) and AVL Trees (self-balancing BST variant). The goal is to understand tree-based data structures, recursive algorithms, and performance analysis through hands-on implementation.

### Mandatory Requirements (10 points)
- **Implementation (7 points):** 9 methods in `BstSet` + 2 methods in `AvlSet`
- **Benchmarking (3 points):** Variant #6 - Compare `BstSet.remove()` vs `java.util.TreeSet.remove()`

### Project Structure
```
lab2/l2-assignment-binary-search-trees/
├── src/main/java/
│   ├── utils/          # Core data structures
│   │   ├── BstSet.java      # Binary Search Tree implementation
│   │   ├── AvlSet.java      # AVL Tree (self-balancing)
│   │   ├── SortedSet.java   # Interface
│   │   └── Set.java         # Base interface
│   ├── demo/           # Testing and benchmarking
│   │   ├── ManualTest.java  # Interactive testing
│   │   ├── Benchmark.java   # JMH performance tests
│   │   └── Car.java         # Domain model
│   └── gui/            # GUI visualization (provided)
├── data/
│   └── ban.txt         # Sample car data
└── pom.xml             # Maven config with JMH dependencies
```

---

## Part 1: Binary Search Tree Fundamentals

### What is a Binary Search Tree?

A **Binary Search Tree (BST)** is a binary tree with a special ordering property:

> **BST Property:** For every node:
> - All elements in the **left subtree** are **smaller**
> - All elements in the **right subtree** are **larger**

**Example:**
```
       50
      /  \
    30    70
   /  \   / \
  20  40 60  80
```

Verify:
- Node 50: left (20, 30, 40) < 50 ✓, right (60, 70, 80) > 50 ✓
- Node 30: left (20) < 30 ✓, right (40) > 30 ✓
- Node 70: left (60) < 70 ✓, right (80) > 70 ✓

### Why BSTs Are Powerful

The BST property enables **binary search** on a tree structure:

**Searching for 40:**
```
       50     ← 40 < 50, go LEFT
      /  \
    30    70  ← 40 > 30, go RIGHT
   /  \
  20  40      ← Found! 40 == 40
```

**Steps:** 3 comparisons (not 5!)

### Time Complexity

| Operation | Balanced BST | Unbalanced BST (Worst Case) |
|-----------|--------------|------------------------------|
| Search    | **O(log n)** | O(n)                         |
| Insert    | **O(log n)** | O(n)                         |
| Delete    | **O(log n)** | O(n)                         |

**Why log n?** Each comparison eliminates half the search space. Tree height h ≈ log₂(n) for balanced trees.

**Worst Case - Linked List Structure:**

Inserting sorted data (10, 20, 30, 40, 50):
```
10
 \
  20
   \
    30
     \
      40
       \
        50  ← Height = 5, search is O(n)
```

**This is why AVL trees exist!** They self-balance to maintain O(log n) performance.

---

## Part 2: BST Deletion - The Core Challenge

Deletion is the **most complex BST operation** with three distinct cases:

### Case 1: Deleting a Leaf Node (No Children)

**Delete 20:**
```
       50              50
      /  \            /  \
    30    70   →    30    70
   /  \              \
  20  40             40
```

**Algorithm:** Simply remove it (set parent's pointer to null).

**Complexity:** O(h) to find + O(1) to delete

---

### Case 2: Deleting a Node with One Child

**Delete 70 (only has right child 80):**
```
       50              50
      /  \            /  \
    30    70   →    30    80
   /  \     \      /  \
  20  40    80    20  40
```

**Algorithm:** Replace the node with its child. The child "moves up" to take its place.

**Complexity:** O(h) to find + O(1) to replace

---

### Case 3: Deleting a Node with Two Children (Hardest!)

**Delete 30:**
```
       50
      /  \
    30    70
   /  \
  20  40
```

**Problem:** We have TWO children (20 and 40), but can only replace with ONE node.

**Solution:** Use the **in-order predecessor** or **in-order successor**:
- **Predecessor:** Largest value in left subtree (go left once, then right until null)
- **Successor:** Smallest value in right subtree (go right once, then left until null)

**Using Predecessor (40):**
1. Find max in left subtree → 40
2. Replace 30's value with 40
3. Delete the old 40 node (which is now a duplicate)

```
Step 1-2: Replace value
       50
      /  \
    40    70      ← Value changed
   /  \
  20  40          ← Duplicate!

Step 3: Delete duplicate (leaf node - Case 1)
       50
      /  \
    40    70
   /
  20              ✓ BST property maintained!
```

**Alternative - Using Successor (from right subtree):**

If we deleted 70 instead:
```
       50
      /  \
    30    70
   /  \   / \
  20  40 60  80

Predecessor approach: Replace 70 with 60
       50
      /  \
    30    60
   /  \     \
  20  40    80

Successor approach: Replace 70 with 80
       50
      /  \
    30    80
   /  \   /
  20  40 60
```

Both are valid! The codebase uses **predecessor** (provides `getMax()` and `removeMax()` helpers).

**Why this works:**
- The predecessor is **guaranteed** to be larger than everything in the left subtree (it's the max)
- It's **guaranteed** to be smaller than everything in the right subtree
- It has **at most one child** (the left child, since it's the rightmost node) → recursive deletion is Case 1 or 2!

**Complexity:** O(h) to find node + O(h) to find predecessor + O(1) to swap = **O(h)**

---

## Part 3: Recursion Pattern in Tree Algorithms

### The Two-Method Design Pattern

BST operations use a **public wrapper + private recursive helper** pattern:

1. **Public wrapper:** `remove(E element)`
   - User-facing API
   - Input validation
   - Hides implementation details
   - Initiates recursion with root

2. **Private helper:** `removeRecursive(E element, BstNode<E> node)`
   - Actual recursive implementation
   - Navigates tree structure
   - Returns new subtree root

**Why both?**

Users want simple API:
```java
set.remove(30);  // Simple!
```

But recursion needs to track current node:
```java
removeRecursive(30, currentNode);  // Internal detail
```

### How They Work Together

**Public wrapper (facade):**
```java
@Override
public void remove(E element) {
    if (element == null) {
        throw new IllegalArgumentException("Element is null");
    }
    root = removeRecursive(element, root);  // ← Start recursion, update root
}
```

**Recursive helper:**
```java
private BstNode<E> removeRecursive(E element, BstNode<E> node) {
    if (node == null) return null;  // Not found

    int cmp = c.compare(element, node.element);

    if (cmp < 0) {
        node.left = removeRecursive(element, node.left);  // ← Update left pointer
        return node;
    } else if (cmp > 0) {
        node.right = removeRecursive(element, node.right); // ← Update right pointer
        return node;
    } else {
        // Found it! Handle 3 cases...
        // Return new subtree root
    }
}
```

### Why Return BstNode<E>?

The recursive method **returns the new subtree root** so parents can update their child pointers.

**Example: Delete leaf node 20**
```
       50
      /  \
    30    70
   /
  20

Recursive calls:
1. removeRecursive(20, 50)  → 20 < 50, go left
   node.left = removeRecursive(20, 30)  ← Updates pointer!
   return node (50)

2. removeRecursive(20, 30)  → 20 < 30, go left
   node.left = removeRecursive(20, 20)  ← Updates pointer!
   return node (30)

3. removeRecursive(20, 20)  → Found! Leaf node
   return null  ← Goes back up the chain

Unwinding:
- Call 2: node.left = null  (30's left is now null)
- Call 1: node.left = node  (50's left is still 30)

Result:
       50
      /  \
    30    70
```

**Key insight:** When we delete a node, we return its replacement (or null). The parent updates its pointer to this replacement.

### Same Pattern in `add()`

The codebase already uses this pattern for insertion (lines 100-132):

```java
public void add(E element) {
    if (element == null) throw new IllegalArgumentException(...);
    root = addRecursive(element, root);  // ← root = ...
}

private BstNode<E> addRecursive(E element, BstNode<E> node) {
    if (node == null) {
        size++;
        return new BstNode<>(element);  // ← New node
    }

    int cmp = c.compare(element, node.element);
    if (cmp < 0) {
        node.left = addRecursive(element, node.left);   // ← Update
    } else if (cmp > 0) {
        node.right = addRecursive(element, node.right); // ← Update
    }
    return node;  // ← Return current node
}
```

### Recursion vs. Iteration Trade-offs

| Aspect | Recursion | Iteration |
|--------|-----------|-----------|
| **Code clarity** | ✅ Clean, mirrors tree structure | ❌ Complex pointer manipulation |
| **Readability** | ✅ Easy to understand | ❌ Harder to follow |
| **Stack usage** | ❌ O(h) call stack | ✅ O(1) call stack |
| **Stack overflow risk** | ❌ Possible if h is huge | ✅ No risk |
| **Extensibility** | ✅ Easy to add balancing (AVL) | ❌ Harder to extend |
| **Performance** | ⚠️ Small overhead | ✅ Slightly faster |

**For BSTs, recursion is preferred:**
- Balanced trees: log₂(1,000,000) ≈ 20 recursive calls → no problem
- Modern JVMs handle 10,000+ call depth
- Code clarity is worth the small stack cost
- AVL balancing is much easier with recursion

---

## Part 4: AVL Trees - Self-Balancing BSTs

### The Problem with Regular BSTs

Inserting sorted data creates a linked list:
```
Insert: 10, 20, 30, 40, 50

Result:
10
 \
  20
   \
    30    ← Height = 5, O(n) operations!
     \
      40
       \
        50
```

### AVL Tree Solution

**AVL Property:** For every node, the heights of left and right subtrees differ by **at most 1**.

**Balance Factor** = height(left subtree) - height(right subtree)

AVL maintains: **-1 ≤ balance factor ≤ 1**

**Same data in AVL tree:**
```
       30
      /  \
    20    40
   /        \
  10        50    ← Height = 3, O(log n) operations!
```

**How?** Through **rotations** after insertion/deletion.

The codebase already implements rotations for `add()`. Your task includes implementing rotations for `remove()` in `AvlSet`.

---

## Part 5: Helper Methods Already Provided

The instructor provided useful helper methods in `BstSet.java`:

### `getMax(BstNode<E> node)` - Line 181
Finds the **maximum** (rightmost) element in a subtree.

```java
BstNode<E> getMax(BstNode<E> node) {
    return get(node, true);  // true = find max
}

// Internal helper
private BstNode<E> get(BstNode<E> node, boolean findMax) {
    BstNode<E> parent = null;
    while (node != null) {
        parent = node;
        node = findMax ? node.right : node.left;  // Go right for max
    }
    return parent;
}
```

**Usage:** Finding in-order predecessor (max in left subtree)

---

### `getMin(BstNode<E> node)` - Line 191
Finds the **minimum** (leftmost) element in a subtree.

```java
BstNode<E> getMin(BstNode<E> node) {
    return get(node, false);  // false = find min
}
```

**Usage:** Finding in-order successor (min in right subtree)

---

### `removeMax(BstNode<E> node)` - Line 164
**Removes** the maximum element from a subtree and returns the new subtree root.

```java
BstNode<E> removeMax(BstNode<E> node) {
    if (node == null) {
        return null;
    } else if (node.right != null) {
        node.right = removeMax(node.right);  // Recursively remove from right
        return node;
    } else {
        return node.left;  // This is the max, replace with left child
    }
}
```

**This is gold for deletion!** Use it when implementing Case 3 (two children):
1. Find predecessor: `max = getMax(node.left)`
2. Replace value: `node.element = max.element`
3. Remove predecessor: `node.left = removeMax(node.left)`

---

## Part 6: Set Operations to Implement

BSTs are often used to implement mathematical **Sets** - collections of unique elements.

### `addAll(Set<E> set)`
Add all elements from another set.
- **Algorithm:** Iterate through input set, add each element
- **Complexity:** O(m log n) where m = size of input set, n = size of this set

### `containsAll(Set<E> set)`
Check if all elements from another set exist in this set.
- **Algorithm:** Iterate through input set, check if each exists
- **Complexity:** O(m log n)
- **Return:** true if all exist, false if any missing

### `retainAll(Set<E> set)`
Keep only elements that exist in both sets (intersection).
- **Algorithm:** Iterate through this set, remove elements not in input set
- **Complexity:** O(n log m) where we check each of our n elements against the input set
- **Tricky:** Don't modify while iterating! Use a temporary collection or multiple passes

### Range Query Methods

#### `headSet(E element)`
Returns subset of elements **less than** element (exclusive).
```java
Set: [10, 20, 30, 40, 50]
headSet(30) → [10, 20]
```

#### `tailSet(E element)`
Returns subset of elements **greater than or equal to** element (inclusive).
```java
Set: [10, 20, 30, 40, 50]
tailSet(30) → [30, 40, 50]
```

#### `subSet(E element1, E element2)`
Returns subset from element1 (inclusive) to element2 (exclusive).
```java
Set: [10, 20, 30, 40, 50]
subSet(20, 50) → [20, 30, 40]
```

**Implementation approach:** Use in-order traversal, collect elements matching the range condition.

---

## Part 7: Iterator with Remove Support

The `IteratorBst` inner class (lines 399-450) already implements tree traversal using a stack. You need to implement the `remove()` method.

**Challenge:** Iterator is currently visiting nodes. When `remove()` is called, you need to:
1. Delete the last element returned by `next()`
2. Maintain iterator state correctly
3. Handle the case where tree structure changes

**Fields available:**
- `last` - last node returned by `next()`
- `lastInStack` - parent reference
- `stack` - current iteration state

**Strategy:** Call the outer class's `remove()` method and handle iterator state.

---

## Part 8: Implementation Checklist

### BstSet Methods to Implement (9 total)
- [ ] `remove(E element)` - Public wrapper
- [ ] `removeRecursive(E element, BstNode<E> node)` - Recursive helper with 3 cases
- [ ] `Iterator.remove()` - Remove during iteration
- [ ] `addAll(Set<E> set)` - Add all from another set
- [ ] `containsAll(Set<E> set)` - Check if all exist
- [ ] `retainAll(Set<E> set)` - Keep only intersection
- [ ] `headSet(E e)` - Elements less than e
- [ ] `tailSet(E e)` - Elements >= e
- [ ] `subSet(E e1, E e2)` - Elements in range [e1, e2)

### AvlSet Methods to Implement (2 total)
- [ ] `remove(E element)` - Override with balancing
- [ ] `removeRecursive(E element, BstNode<E> node)` - Override with rotations

### Benchmarking (Variant 6)
- [ ] Run JMH benchmark: `BstSet.remove()` vs `TreeSet.remove()`
- [ ] Collect data for different input sizes (10k, 20k, 40k, 80k)
- [ ] Create graphs of execution time vs input size
- [ ] Write report with complexity analysis and conclusions

---

## Part 9: Key Insights Learned

### 1. BST Property Creates Efficiency
The ordering property (left < parent < right) enables O(log n) search by eliminating half the search space at each step. This is binary search in tree form.

### 2. Deletion is Complex Because Structure Must Be Maintained
Unlike arrays or linked lists, we can't just remove a node. We must maintain the BST property, which requires different strategies based on the node's children.

### 3. In-Order Predecessor/Successor Are Guaranteed Replacements
The max of left subtree (or min of right subtree) is mathematically guaranteed to maintain the BST property when replacing a two-child node. It also has at most one child, making recursive deletion possible.

### 4. Public Wrapper + Private Recursive is a Design Pattern
This pattern (Facade Pattern) hides implementation complexity while providing a clean API. The recursive method returns nodes so parents can update their pointers - this is essential for tree modifications.

### 5. Recursion Models Tree Structure Naturally
Tree operations are inherently recursive - a tree is defined recursively (a node with left and right subtrees). Recursive code mirrors this structure, making it clearer than iterative approaches.

### 6. Unbalanced BSTs Degrade to O(n)
Sorted insertion creates linked-list structure with O(n) operations. This motivates self-balancing trees like AVL.

### 7. Helper Methods Reduce Code Duplication
Methods like `getMax()`, `getMin()`, and `removeMax()` are reusable across different operations. Good design isolates common subtasks.

---

## Part 10: Testing Strategy

### Manual Testing
Use `ManualTest.java` to interactively test operations:
1. Add elements to build a tree
2. Visualize with `toVisualizedString()`
3. Test deletion on different cases (leaf, one child, two children)
4. Verify BST property is maintained

### Automated Testing
Create test cases for:
- Edge cases: empty tree, single node, deleting root
- All three deletion cases
- Set operations with various input sizes
- Iterator remove during traversal
- Range queries with boundary conditions

### Visualization
The GUI tool provides visual tree representation - use it to:
- See tree structure before/after deletion
- Verify balancing in AVL trees
- Understand rotation effects

---

## Next Steps

1. ✅ Understand BST deletion algorithm (three cases)
2. ✅ Understand recursion pattern (wrapper + helper)
3. ⏭️ Implement `removeRecursive()` with three cases
4. ⏭️ Implement `remove()` wrapper
5. ⏭️ Test with ManualTest and visualization
6. ⏭️ Implement remaining BstSet methods
7. ⏭️ Implement AvlSet deletion with balancing
8. ⏭️ Run benchmarks and write report

---

## Questions for Review

1. What are the three cases for BST deletion and how is each handled?
2. Why do we use in-order predecessor/successor for two-child deletion?
3. Why does `removeRecursive()` return `BstNode<E>` instead of `void`?
4. What is the time complexity of deletion in a balanced vs unbalanced BST?
5. How do AVL trees maintain O(log n) performance?
6. When would you choose iteration over recursion for tree operations?
7. What is the purpose of the public wrapper + private recursive helper pattern?
8. How does `removeMax()` work and why is it useful?

---

**Ready to implement!** 🚀
