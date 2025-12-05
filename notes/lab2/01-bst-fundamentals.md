# BST Fundamentals

## What is a Binary Search Tree?

A **Binary Search Tree (BST)** is a binary tree with a special ordering property:

> **BST Property:** For every node:
> - All elements in the **left subtree** are **smaller**
> - All elements in the **right subtree** are **larger**

```
       50
      /  \
    30    70
   /  \   / \
  20  40 60  80
```

---

## Why BSTs Are Powerful

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

---

## Time Complexity

| Operation | Balanced BST | Unbalanced BST (Worst) |
|-----------|--------------|------------------------|
| Search    | **O(log n)** | O(n)                   |
| Insert    | **O(log n)** | O(n)                   |
| Delete    | **O(log n)** | O(n)                   |

**Why O(log n)?** Each comparison eliminates half the search space. Tree height h ≈ log₂(n) for balanced trees.

---

## The Problem: Unbalanced Trees

Inserting sorted data creates a linked list:

```
Insert: 10, 20, 30, 40, 50

Result:
10
 \
  20
   \
    30
     \
      40
       \
        50  ← Height = 5, search is O(n)!
```

**This is why AVL trees exist!** They self-balance to maintain O(log n).

---

## BST vs AVL Comparison

| Aspect | BST | AVL |
|--------|-----|-----|
| **Balance** | Not guaranteed | Always balanced |
| **Height** | O(n) worst case | O(log n) guaranteed |
| **Insert** | Simple | + rotations |
| **Delete** | Simple | + rotations |
| **Best for** | Random data | Any data pattern |

---

## Key Terminology

- **Root:** Top node of the tree
- **Leaf:** Node with no children
- **Height:** Longest path from node to leaf
- **Balance Factor:** height(left) - height(right)
- **In-order Predecessor:** Largest value in left subtree
- **In-order Successor:** Smallest value in right subtree

---

## Traversal Orders

```
       50
      /  \
    30    70
   /  \
  20  40
```

| Order | Pattern | Result |
|-------|---------|--------|
| **In-order** | Left → Node → Right | 20, 30, 40, 50, 70 (sorted!) |
| **Pre-order** | Node → Left → Right | 50, 30, 20, 40, 70 |
| **Post-order** | Left → Right → Node | 20, 40, 30, 70, 50 |

**In-order traversal of BST = sorted order!**
