# AVL Tree Implementation Guide

## What is an AVL Tree?

AVL = **A**delson-**V**elsky and **L**andis (inventors' names)

An AVL tree is a **self-balancing BST** that maintains height balance after every operation.

**AVL Property:** For every node, the heights of left and right subtrees differ by **at most 1**.

```
Balance Factor = height(left subtree) - height(right subtree)

Valid balance factors: -1, 0, +1
```

---

## Why AVL Trees?

Regular BST with sorted input:
```
Insert: 10, 20, 30, 40, 50

BST Result (O(n) operations!):
10
 \
  20
   \
    30
     \
      40
       \
        50
```

Same data in AVL tree:
```
       30
      /  \
    20    40
   /        \
  10        50    ← Height = 3, O(log n) guaranteed!
```

---

## AVL Deletion Algorithm

**AVL deletion = BST deletion + Rebalancing**

### Step-by-Step Process

```java
private AVLNode<E> removeRecursive(E element, AVLNode<E> node) {
    // 1. Standard BST deletion (same 3 cases)
    if (node == null) return null;

    int cmp = c.compare(element, node.element);

    if (cmp < 0) {
        node.setLeft(removeRecursive(element, node.getLeft()));
    } else if (cmp > 0) {
        node.setRight(removeRecursive(element, node.getRight()));
    } else {
        // Case 1: Leaf
        if (node.getLeft() == null && node.getRight() == null) {
            size--;
            return null;
        }
        // Case 2: One child
        if (node.getLeft() == null) { size--; return node.getRight(); }
        if (node.getRight() == null) { size--; return node.getLeft(); }
        // Case 3: Two children - use predecessor
        AVLNode<E> predecessor = getMaxNode(node.getLeft());
        node.element = predecessor.element;
        node.setLeft(removeMaxNode(node.getLeft()));
        size--;
    }

    // 2. Update height
    node.height = Math.max(height(node.getLeft()), height(node.getRight())) + 1;

    // 3. Check balance and rotate if needed
    int balance = height(node.getLeft()) - height(node.getRight());

    // 4. Apply rotations based on balance factor
    if (balance > 1) {  // Left-heavy
        if (height(node.getLeft().getLeft()) >= height(node.getLeft().getRight())) {
            return rightRotation(node);      // Left-Left case
        } else {
            return doubleRightRotation(node); // Left-Right case
        }
    }

    if (balance < -1) {  // Right-heavy
        if (height(node.getRight().getRight()) >= height(node.getRight().getLeft())) {
            return leftRotation(node);       // Right-Right case
        } else {
            return doubleLeftRotation(node); // Right-Left case
        }
    }

    return node;
}
```

---

## The 4 Rotation Cases

### Case 1: Left-Left (LL) - Single Right Rotation

When: balance > 1 AND left child is left-heavy or balanced

```
Before:              After rightRotation(z):
        z                      y
       / \                   /   \
      y   T4                x      z
     / \         →        /  \    /  \
    x   T3               T1  T2  T3  T4
   / \
  T1  T2
```

### Case 2: Right-Right (RR) - Single Left Rotation

When: balance < -1 AND right child is right-heavy or balanced

```
Before:              After leftRotation(z):
    z                          y
   / \                       /   \
  T1   y                    z      x
      / \        →         / \    / \
     T2   x               T1  T2 T3  T4
         / \
        T3  T4
```

### Case 3: Left-Right (LR) - Double Rotation

When: balance > 1 AND left child is right-heavy

```
Before:         After doubleRightRotation(z):
     z                        x
    / \                      /  \
   y   T4                   y      z
  / \           →          / \    / \
 T1   x                   T1  T2 T3  T4
     / \
   T2   T3
```
**Steps:** Left rotate y, then right rotate z

### Case 4: Right-Left (RL) - Double Rotation

When: balance < -1 AND right child is left-heavy

```
Before:          After doubleLeftRotation(z):
   z                           x
  / \                        /   \
 T1   y                     z      y
     / \         →         / \    / \
    x   T4                T1  T2 T3  T4
   / \
  T2  T3
```
**Steps:** Right rotate y, then left rotate z

---

## How to Determine Which Rotation

```
Calculate balance = height(left) - height(right)

if (balance > 1):        // Left-heavy
    Check left child's balance:
    - Left child left-heavy or balanced → Right Rotation (LL)
    - Left child right-heavy → Double Right Rotation (LR)

if (balance < -1):       // Right-heavy
    Check right child's balance:
    - Right child right-heavy or balanced → Left Rotation (RR)
    - Right child left-heavy → Double Left Rotation (RL)
```

---

## Helper Methods

### getMaxNode - Find Predecessor
```java
private AVLNode<E> getMaxNode(AVLNode<E> node) {
    while (node.getRight() != null) {
        node = node.getRight();
    }
    return node;
}
```

### removeMaxNode - Remove with Rebalancing
```java
private AVLNode<E> removeMaxNode(AVLNode<E> node) {
    if (node.getRight() == null) {
        return node.getLeft();  // This is the max
    }

    node.setRight(removeMaxNode(node.getRight()));

    // Update height and rebalance (same as removeRecursive)
    node.height = Math.max(height(node.getLeft()), height(node.getRight())) + 1;
    int balance = height(node.getLeft()) - height(node.getRight());

    if (balance > 1) {
        // Apply rotations...
    }

    return node;
}
```

---

## Key Differences: BstSet vs AvlSet

| Aspect | BstSet | AvlSet |
|--------|--------|--------|
| Node type | `BstNode<E>` | `AVLNode<E>` |
| Accessors | `node.left` | `node.getLeft()` |
| After deletion | Just return | Update height + rebalance |
| Height tracking | None | `node.height = ...` |
| Rotations | None | 4 cases |
| Guarantee | O(n) worst case | O(log n) always |

---

## Complexity Analysis

| Operation | Time | Why |
|-----------|------|-----|
| remove() | **O(log n)** | Tree always balanced, height = log n |
| Single rotation | O(1) | Just pointer updates |
| Rebalancing | O(log n) | At most O(log n) rotations up the tree |

**Space:** O(log n) for recursive call stack

---

## Defence Questions

**Q: "Why does AVL guarantee O(log n)?"**
A: The balance property ensures height ≤ 1.44 × log₂(n). Since operations depend on height, they're always O(log n).

**Q: "When do you need a double rotation?"**
A: When the imbalance zigzags - left child is right-heavy (LR) or right child is left-heavy (RL). Single rotation won't fix this.

**Q: "Why update height before checking balance?"**
A: The balance factor depends on accurate heights. After deletion, child heights may have changed, so we must recalculate before checking balance.

**Q: "What's the difference between your BstSet and AvlSet remove?"**
A: Same 3 deletion cases, but AvlSet adds: height update after each modification, balance factor check, and rotations when |balance| > 1.
