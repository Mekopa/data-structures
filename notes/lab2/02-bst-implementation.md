# BstSet Implementation Guide

## Overview

All 9 methods implemented in `BstSet.java`:

| Method | Lines | Purpose |
|--------|-------|---------|
| `remove(E element)` | 154-160 | Public wrapper for deletion |
| `removeRecursive(...)` | 189-233 | Recursive deletion logic |
| `containsAll(Set<E>)` | 90-101 | Check all elements exist |
| `addAll(Set<E>)` | 122-131 | Add all from another set |
| `retainAll(Set<E>)` | 167-187 | Keep only intersection |
| `headSet(E)` | 422-438 | Elements < limit |
| `tailSet(E)` | 474-490 | Elements >= limit |
| `subSet(E, E)` | 447-466 | Elements in range |
| `Iterator.remove()` | 558-566 | Remove during iteration |

---

## 1. BST Deletion - The Core Algorithm

### The Three Cases

#### Case 1: Leaf Node (No Children)
```
Delete 20:
   30           30
  /      →
 20          (null)
```
**Code:** `return null;`

#### Case 2: One Child
```
Delete 30:
   50              50
  /               /
 30        →     20
/
20
```
**Code:** `return node.left;` or `return node.right;`

#### Case 3: Two Children (Hardest!)
```
Delete 50:
       50                    40
      /  \                  /  \
    30    70      →       30    70
   /  \                  /
  20  40               20

Steps:
1. Find predecessor (max in left subtree): 40
2. Replace 50's value with 40
3. Delete old 40 (now a leaf - Case 1)
```

**Code:**
```java
BstNode<E> max = getMax(node.left);    // Find predecessor
node.element = max.element;             // Copy value
node.left = removeMax(node.left);       // Delete predecessor
```

---

### Implementation Pattern

**Public Wrapper + Private Recursive Helper:**

```java
// Public API
public void remove(E element) {
    if (element == null) throw new IllegalArgumentException(...);
    root = removeRecursive(element, root);  // Critical: root = ...
}

// Recursive helper
private BstNode<E> removeRecursive(E element, BstNode<E> node) {
    if (node == null) return null;  // Not found

    int cmp = c.compare(element, node.element);

    if (cmp < 0) {
        node.left = removeRecursive(element, node.left);
        return node;
    } else if (cmp > 0) {
        node.right = removeRecursive(element, node.right);
        return node;
    } else {
        // Found! Handle 3 cases...
    }
}
```

**Why return BstNode?**
- Allows parent to update child pointer
- Pattern: `node.left = removeRecursive(...)`
- When we delete, we return the replacement

---

## 2. Set Operations

### containsAll(Set<E> set)
```java
for (E element : set) {
    if (!this.contains(element)) {
        return false;  // Short-circuit on first miss
    }
}
return true;
```
**Complexity:** O(m log n) - check m elements in tree of size n

### addAll(Set<E> set)
```java
for (E element : set) {
    this.add(element);  // Duplicates handled by add()
}
```
**Complexity:** O(m log n)

### retainAll(Set<E> set)
```java
// Two-phase: collect then remove (can't modify while iterating!)
ArrayList<E> toRemove = new ArrayList<>();
for (E element : this) {
    if (!set.contains(element)) {
        toRemove.add(element);
    }
}
for (E element : toRemove) {
    this.remove(element);
}
```
**Why two phases?** Modifying during iteration causes ConcurrentModificationException

---

## 3. Range Queries

### headSet(E element) - Elements < limit
```java
BstSet<E> result = new BstSet<E>(c);  // Same comparator!
for (E e : this) {
    if (c.compare(e, element) < 0) {  // e < element
        result.add(e);
    }
}
return result;
```
**Example:** `headSet(30)` on {10, 20, 30, 40} → {10, 20}

### tailSet(E element) - Elements >= limit
```java
if (c.compare(e, element) >= 0) {  // e >= element (inclusive!)
    result.add(e);
}
```
**Example:** `tailSet(30)` on {10, 20, 30, 40} → {30, 40}

### subSet(E e1, E e2) - Range [e1, e2)
```java
if (c.compare(element1, element2) > 0) {
    throw new IllegalArgumentException("e1 must be <= e2");
}
// ...
if (c.compare(e, element1) >= 0 && c.compare(e, element2) < 0) {
    result.add(e);  // e1 <= e < e2
}
```
**Example:** `subSet(20, 40)` on {10, 20, 30, 40} → {20, 30}

---

## 4. Iterator.remove()

```java
@Override
public void remove() {
    if (last == null) {
        throw new IllegalStateException("next() must be called first");
    }
    BstSet.this.remove(last.element);  // Call outer class method
    last = null;  // Prevent double removal
}
```

**Key Syntax:** `BstSet.this` accesses the outer class instance from inner class.

---

## Complexity Summary

| Method | Time | Space |
|--------|------|-------|
| `remove()` | O(h) | O(h) stack |
| `containsAll()` | O(m log n) | O(1) |
| `addAll()` | O(m log n) | O(1) |
| `retainAll()` | O(n log m + k log n) | O(k) |
| `headSet/tailSet/subSet` | O(n + k log k) | O(k) |
| `Iterator.remove()` | O(log n) | O(1) |

Where: n = this set size, m = input set size, k = result size, h = tree height
