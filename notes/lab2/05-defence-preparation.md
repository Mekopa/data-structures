# Lab 2 Defence Preparation

## Quick Reference Card

### Complexity Summary

| Operation | BstSet | AvlSet | TreeSet |
|-----------|--------|--------|---------|
| add() | O(h) | O(log n) | O(log n) |
| remove() | O(h) | O(log n) | O(log n) |
| contains() | O(h) | O(log n) | O(log n) |
| Worst case | O(n) | O(log n) | O(log n) |

**h = tree height, n = number of elements**

### BST Deletion Cases
```
Case 1: Leaf       → return null
Case 2: One child  → return child
Case 3: Two children → replace with predecessor, delete predecessor
```

### AVL Rotation Cases
```
balance > 1:  Left-heavy
  - LL: Right rotation
  - LR: Double right rotation

balance < -1: Right-heavy
  - RR: Left rotation
  - RL: Double left rotation
```

---

## Top 10 Defence Questions

### 1. "Explain the three BST deletion cases"

**Answer:**
- **Case 1 (Leaf):** No children → return null, parent's pointer becomes null
- **Case 2 (One child):** Return the child, it replaces the deleted node
- **Case 3 (Two children):** Find predecessor (max in left subtree), copy its value, delete the predecessor (which is now Case 1 or 2)

### 2. "Why does removeRecursive return BstNode instead of void?"

**Answer:**
To enable parent pointer updates. Pattern: `node.left = removeRecursive(...)`. When we delete a node, we return its replacement. Parent assigns this return value to update its child pointer.

### 3. "What's the time complexity of remove() and why?"

**Answer:**
- **O(h)** where h = tree height
- **Balanced tree:** O(log n) because h = log₂(n)
- **Unbalanced tree:** O(n) because h could equal n

### 4. "Why use predecessor instead of successor?"

**Answer:**
Both are valid! I used predecessor because `getMax()` and `removeMax()` helper methods were already provided. Successor would require `getMin()` and `removeMin()`.

### 5. "How do AVL rotations maintain balance?"

**Answer:**
- After deletion, calculate balance = height(left) - height(right)
- If |balance| > 1, tree is unbalanced
- Apply appropriate rotation based on which side is heavy
- Rotations restructure the tree to reduce height while maintaining BST property

### 6. "Why can't you remove while iterating?"

**Answer:**
The iterator tracks expected modification count. Direct removal changes structure and causes mismatch → ConcurrentModificationException. Solution: collect elements first, then remove in separate loop.

### 7. "What does BstSet.this mean?"

**Answer:**
Accesses the outer class instance from an inner class. `this` refers to the iterator, `BstSet.this` refers to the enclosing BstSet instance.

### 8. "Why is BstSet faster than TreeSet in your benchmarks?"

**Answer:**
- No rebalancing overhead (TreeSet is Red-Black tree)
- Simpler code path
- With random data, BstSet stays reasonably balanced
- But TreeSet guarantees O(log n) for any input pattern

### 9. "When would you use BST vs AVL vs TreeSet?"

**Answer:**
- **BST:** Simple use cases, random data, when you understand the code
- **AVL:** Need guaranteed O(log n), educational purposes
- **TreeSet:** Production code, need reliability, don't want to maintain custom code

### 10. "How do you ensure BST property is maintained after deletion?"

**Answer:**
- **Case 1:** Removing leaf doesn't affect other nodes
- **Case 2:** Child is already in correct position relative to parent
- **Case 3:** Predecessor is mathematically guaranteed to satisfy BST property (larger than left subtree, smaller than right subtree)

---

## Live Coding Tasks to Practice

### Task 1: Change from predecessor to successor
```java
// Current:
BstNode<E> max = getMax(node.left);
node.element = max.element;
node.left = removeMax(node.left);

// Change to:
BstNode<E> min = getMin(node.right);
node.element = min.element;
node.right = removeMin(node.right);
```

### Task 2: Make remove() return boolean
```java
public boolean remove(E element) {
    int oldSize = size;
    root = removeRecursive(element, root);
    return size < oldSize;
}
```

### Task 3: Implement removeMin()
```java
BstNode<E> removeMin(BstNode<E> node) {
    if (node == null) return null;
    if (node.left != null) {
        node.left = removeMin(node.left);
        return node;
    } else {
        return node.right;  // This is min, replace with right child
    }
}
```

### Task 4: Implement floor(E element)
```java
// Return greatest element <= given element
public E floor(E element) {
    BstNode<E> result = null;
    BstNode<E> current = root;
    while (current != null) {
        int cmp = c.compare(element, current.element);
        if (cmp == 0) return current.element;
        if (cmp > 0) {
            result = current;  // Could be answer
            current = current.right;
        } else {
            current = current.left;
        }
    }
    return result == null ? null : result.element;
}
```

---

## Visual Diagrams to Draw

### BST Deletion Case 3
```
Delete 50:
       50               Step 1: Find max(left)=40
      /  \                     ↓
    30    70            Step 2: Copy 40 to root
   /  \                        ↓
  20  40                Step 3: Delete old 40

Result:
       40
      /  \
    30    70
   /
  20
```

### AVL Right Rotation
```
Before:          After:
    30              20
   /               /  \
  20              10   30
 /
10
```

---

## Confidence Checklist

**Can I explain:**
- [ ] All three deletion cases with examples
- [ ] Why predecessor/successor works for Case 3
- [ ] Time and space complexity analysis
- [ ] Why we return BstNode instead of void
- [ ] How size is managed correctly
- [ ] All 4 AVL rotation cases
- [ ] Why BstSet outperformed TreeSet
- [ ] How JMH benchmarking works

**Can I modify:**
- [ ] Switch from predecessor to successor
- [ ] Add boolean return value
- [ ] Implement floor/ceiling
- [ ] Change to throw exception if not found

**Ready for defence!** 💪

---

## Key Files to Review

```
lab2/l2-assignment-binary-search-trees/
├── src/main/java/utils/
│   ├── BstSet.java       # Lines 154-233 (remove)
│   └── AvlSet.java       # Lines 73-204 (remove + rotations)
├── benchmarks/
│   └── results.csv       # Your benchmark data
└── report/
    └── benchmarking-report.md
```

---

## Last-Minute Review Points

1. **BST property:** left < node < right
2. **Predecessor:** max in left subtree (go left, then right as far as possible)
3. **Balance factor:** height(left) - height(right)
4. **AVL property:** |balance factor| ≤ 1
5. **Recursion pattern:** `node.left = recursive(...); return node;`
6. **Your benchmark result:** BstSet ~10% faster than TreeSet
7. **Why:** No rebalancing overhead in BstSet
