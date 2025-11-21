# Lab 2: BST Deletion - Defence Preparation Notes

**Student:** Mekopa
**Topic:** Binary Search Tree Implementation
**Completed:** BST Deletion (remove methods)
**Date:** 2025-11-21

---

## What I Implemented

### Methods Completed:
1. **`remove(E element)`** - Public API for deletion (lines 140-145)
2. **`removeRecursive(E element, BstNode<E> node)`** - Recursive deletion logic (lines 154-198)

**Status:** Fully functional, handles all three deletion cases

---

## Core Concept: BST Deletion Algorithm

### The Three Cases

#### **Case 1: Leaf Node (No Children)**
**Condition:** `node.left == null && node.right == null`

**What I do:**
```java
size--;      // Decrement size
return null; // Remove node by returning null to parent
```

**Why it works:**
- Parent was calling: `node.left = removeRecursive(...)`
- When we return `null`, parent sets its child pointer to null
- Node is effectively removed from the tree

**Time Complexity:** O(h) to find + O(1) to delete = **O(h)**

**Example:**
```
Delete 20:
   30           30
  /      →     /
 20          (null)
```

---

#### **Case 2: One Child**
**Condition:** One child is null, the other is not

**What I do:**
```java
// Right child is null, only left exists
if (node.right == null) {
    size--;
    return node.left;  // Replace this node with left child
}

// Left child is null, only right exists
if (node.left == null) {
    size--;
    return node.right; // Replace this node with right child
}
```

**Why it works:**
- We bypass the deleted node by returning its child
- Parent updates its pointer to skip the deleted node
- The child "moves up" to take deleted node's place

**Time Complexity:** O(h) to find + O(1) to replace = **O(h)**

**Example:**
```
Delete 30 (only has left child):
   50              50
  /               /
 30        →     20
/
20
```

---

#### **Case 3: Two Children (Most Complex)**
**Condition:** Both `node.left` and `node.right` exist

**What I do:**
```java
BstNode<E> max = getMax(node.left);    // 1. Find predecessor
node.element = max.element;             // 2. Copy predecessor's value
node.left = removeMax(node.left);       // 3. Delete predecessor
size--;                                 // 4. Decrement size
return node;                            // 5. Return modified node
```

**Why it works:**
1. **Predecessor** = maximum value in left subtree (rightmost node going left)
2. Predecessor is **guaranteed** to be larger than everything else in left subtree
3. Predecessor is **guaranteed** to be smaller than everything in right subtree
4. **BST property is maintained** after replacement
5. Predecessor has **at most one child** (the left one), so removing it is Case 1 or 2

**Time Complexity:**
- O(h) to find node to delete
- O(h) to find predecessor (getMax)
- O(h) to remove predecessor (removeMax)
- Total: **O(h)** where h = tree height

**Example:**
```
Delete 50:
       50                    40
      /  \                  /  \
    30    70      →       30    70
   /  \                  /
  20  40               20

Steps:
1. Find max in left subtree: 40
2. Replace 50's value with 40
3. Delete the old 40 node (leaf node - Case 1)
```

---

## Design Decisions & Justifications

### 1. Why Use Predecessor Instead of Successor?

**Decision:** Use predecessor (max of left subtree)

**Reason:**
- The codebase already provides `getMax()` and `removeMax()` helper methods
- Using successor would require `getMin()` and `removeMin()`
- Both approaches are equally valid, chose the one with existing infrastructure

**Alternative:** Could use successor (min of right subtree) - same result

---

### 2. Why Check Cases in This Order?

**Order:**
1. Check leaf first (`both null`)
2. Check one child (`one null`)
3. Otherwise, two children (both exist)

**Reason:**
- If both children are null and we checked `node.left == null` first, we'd incorrectly treat it as one-child case
- Must handle most specific case (leaf) before more general cases

---

### 3. Why Return BstNode<E> Instead of void?

**Decision:** `removeRecursive()` returns `BstNode<E>`

**Reason:**
- Allows parent to update its child pointer: `node.left = removeRecursive(...)`
- When we delete a node, we return its replacement (or null)
- Parent automatically updates its pointer to the new subtree root
- This is the standard pattern for recursive tree modifications

**Contrast:**
- If it returned void, we'd need to manually track parent nodes
- Current approach is cleaner and less error-prone

---

### 4. Why Use Public Wrapper + Private Recursive Helper?

**Pattern:**
```java
// Public wrapper
public void remove(E element) {
    if (element == null) throw new IllegalArgumentException(...);
    root = removeRecursive(element, root);
}

// Private recursive helper
private BstNode<E> removeRecursive(E element, BstNode<E> node) {
    // ... actual implementation
}
```

**Reasons:**
1. **Encapsulation:** Users don't need to know about `BstNode` internals
2. **Clean API:** Simple interface: `set.remove(50)` - no node references needed
3. **Validation:** Wrapper handles null checking and edge cases
4. **Consistency:** Same pattern used in `add()` + `addRecursive()`

---

## Complexity Analysis

### Time Complexity

| Operation | Best Case | Average Case | Worst Case | Explanation |
|-----------|-----------|--------------|------------|-------------|
| **remove()** | O(log n) | O(log n) | **O(n)** | Depends on tree balance |

**Why O(n) worst case?**
- In unbalanced tree (linked list shape), height h = n
- Must traverse entire tree to find element
- Example: Delete 50 from: 10→20→30→40→50

**Why O(log n) average?**
- In balanced tree, height h = log₂(n)
- Each comparison eliminates half the remaining tree
- Example: Balanced tree with 1000 nodes, height ≈ 10

**This motivates AVL trees!** They guarantee O(log n) by maintaining balance.

---

### Space Complexity

| Aspect | Complexity | Explanation |
|--------|------------|-------------|
| **Auxiliary Space** | O(h) | Recursive call stack depth = tree height |
| **Balanced Tree** | O(log n) | Stack depth = log₂(n) |
| **Worst Case** | O(n) | Completely unbalanced tree |

**Could we do O(1) space?**
- Yes, with iterative implementation using while loop
- But code becomes much more complex
- Recursion is preferred for clarity

---

## Questions I Expect During Defence

### Q1: "Walk me through deleting a node with two children"

**My Answer:**
1. We can't just remove the node because it has two children - we can only replace with one
2. Find the predecessor: maximum value in left subtree (rightmost node going left)
3. Copy the predecessor's value into the node we're "deleting"
4. Delete the predecessor from the left subtree (it becomes a simpler case)
5. The predecessor is guaranteed to have at most one child, so it's Case 1 or 2

**Visual I can draw:**
```
       50
      /  \
    30    70    Delete 50 → Find max in left (40)
   /  \          → Replace 50 with 40
  20  40         → Delete old 40 (leaf)
```

---

### Q2: "Why does removeRecursive return a BstNode instead of void?"

**My Answer:**
- It returns the new root of the subtree after deletion
- This allows the parent to update its child pointer automatically
- Pattern: `node.left = removeRecursive(element, node.left)`
- When we delete a node, we return its replacement (child, null, or itself with new value)
- The parent assigns this return value to update its child pointer
- Same pattern used in `addRecursive()` for consistency

---

### Q3: "What's the time complexity of remove() and why?"

**My Answer:**
- **O(h)** where h is tree height
- For balanced tree: **O(log n)** because h = log₂(n)
- For unbalanced tree: **O(n)** because h could equal n

**Breakdown:**
1. Search for element: O(h) - navigate down the tree
2. Case 1 or 2: O(1) - just return null or child
3. Case 3: O(h) for getMax + O(h) for removeMax, but these are in the same subtree, so still O(h)
4. Total: **O(h)**

---

### Q4: "What happens if you try to delete an element that doesn't exist?"

**My Answer:**
- The recursive search reaches a null node (base case)
- `if (node == null) return null;`
- Size is NOT decremented (element wasn't found)
- Tree remains unchanged
- No exception is thrown (following Set semantics - remove is idempotent)

---

### Q5: "Why use predecessor instead of successor?"

**My Answer:**
- Both are valid! Predecessor or successor both maintain BST property
- Predecessor = max in left subtree (go left, then right as far as possible)
- Successor = min in right subtree (go right, then left as far as possible)
- I used predecessor because `getMax()` and `removeMax()` were already provided
- Either choice gives a correct BST after deletion

---

### Q6: "Can you implement this iteratively instead of recursively?"

**My Answer:**
- Yes, but it's more complex
- Need to manually track parent nodes
- Need to handle pointer updates explicitly
- Code becomes harder to read and maintain
- Recursive version is preferred because:
  * Mirrors the recursive structure of trees naturally
  * Cleaner code
  * Stack depth = O(h), which is acceptable for balanced trees
  * Modern JVMs handle 10,000+ recursive calls

---

### Q7: "What edge cases did you consider?"

**My Answer:**
1. **Deleting from empty tree:** Base case returns null, tree stays empty
2. **Deleting root node:** Wrapper updates `root = removeRecursive(...)`, handles correctly
3. **Deleting only node:** Returns null, root becomes null, size = 0
4. **Null element:** Wrapper throws `IllegalArgumentException` before recursion
5. **Element not in tree:** Base case returns null, size unchanged
6. **All three deletion cases:** Leaf, one child, two children all tested

---

### Q8: "How do you ensure the BST property is maintained after deletion?"

**My Answer:**
- **Case 1 (Leaf):** Removing a leaf doesn't affect other nodes, property maintained
- **Case 2 (One Child):** Child is already in correct position (left child < parent, right child > parent), moving it up maintains property
- **Case 3 (Two Children):** Predecessor is mathematically guaranteed to satisfy:
  * Larger than everything in left subtree (it's the max)
  * Smaller than everything in right subtree (it came from left side)
  * Therefore BST property holds after replacement

---

### Q9: "Show me how size is managed"

**My Answer:**
- Size is decremented exactly once per deletion
- Case 1: `size--` then return null
- Case 2: `size--` then return child
- Case 3: `size--` when removing predecessor (not when copying value!)
- Important: In Case 3, we only decrement once even though we're conceptually deleting and replacing

---

### Q10: "What if I ask you to modify this to return a boolean indicating success?"

**My Answer:**
Current implementation is void (follows Java Set interface).

To return boolean:
```java
public boolean remove(E element) {
    if (element == null) throw new IllegalArgumentException(...);
    int oldSize = size;
    root = removeRecursive(element, root);
    return size < oldSize;  // True if size changed
}
```

Or track in recursive method with a wrapper boolean[1] or AtomicBoolean.

---

## Code I Should Be Able to Modify During Defence

### Modification 1: "Change to use successor instead of predecessor"

**Current (predecessor):**
```java
BstNode<E> max = getMax(node.left);
node.element = max.element;
node.left = removeMax(node.left);
```

**Changed (successor):**
```java
BstNode<E> min = getMin(node.right);
node.element = min.element;
node.right = removeMin(node.right);
```

Would also need to implement `removeMin()`:
```java
BstNode<E> removeMin(BstNode<E> node) {
    if (node == null) return null;
    else if (node.left != null) {
        node.left = removeMin(node.left);
        return node;
    } else {
        return node.right;  // This is the min, replace with right child
    }
}
```

---

### Modification 2: "Add a method to count how many nodes were deleted"

```java
private int deleteCount = 0;

public void remove(E element) {
    if (element == null) throw new IllegalArgumentException(...);
    int oldSize = size;
    root = removeRecursive(element, root);
    if (size < oldSize) deleteCount++;
}

public int getDeleteCount() {
    return deleteCount;
}
```

---

### Modification 3: "Make removal fail-fast if element doesn't exist"

```java
public void remove(E element) {
    if (element == null) throw new IllegalArgumentException(...);
    if (!contains(element)) {
        throw new NoSuchElementException("Element not found: " + element);
    }
    root = removeRecursive(element, root);
}
```

---

## Testing Strategy I Used

### Test Cases:
1. **Delete leaf node** - simplest case, verify size decreases, contains() returns false
2. **Delete node with left child only** - verify child moves up correctly
3. **Delete node with right child only** - verify child moves up correctly
4. **Delete node with two children** - verify predecessor replacement, BST property maintained
5. **Delete root node** - verify root is updated correctly for all cases
6. **Delete from single-node tree** - verify tree becomes empty
7. **Delete non-existent element** - verify tree unchanged, size unchanged
8. **Visualize tree before/after** - use `toVisualizedString()` to verify structure

### How to Test:
```java
BstSet<Integer> set = new BstSet<>();
set.add(50); set.add(30); set.add(70);
set.add(20); set.add(40); set.add(60); set.add(80);

System.out.println(set.toVisualizedString("")); // Visualize before
set.remove(30); // Delete two-child node
System.out.println(set.toVisualizedString("")); // Visualize after
System.out.println("Size: " + set.size()); // Should be 6
System.out.println("Contains 30: " + set.contains(30)); // Should be false
```

---

## Connection to Other Concepts

### 1. Relates to Add Operation
- Both use public wrapper + private recursive pattern
- Both return BstNode for pointer updates
- Deletion is inverse of addition

### 2. Enables Other Operations
- `retainAll()` will use `remove()` to delete unwanted elements
- `Iterator.remove()` will call `remove()` during iteration

### 3. Motivates AVL Trees
- Worst-case O(n) deletion in unbalanced trees is unacceptable
- AVL trees maintain balance, guaranteeing O(log n)
- Next step: Implement deletion with rebalancing in AvlSet

---

## Key Insights to Articulate

1. **Case 3 is recursive reduction** - We reduce the complex two-child case to simpler Case 1 or 2 by deleting the predecessor instead

2. **Predecessor/successor have special property** - They're guaranteed to have at most one child because they're the rightmost/leftmost node in a subtree

3. **Return values enable elegant parent updates** - Returning nodes instead of void allows `node.left = removeRecursive(...)` pattern

4. **Pattern consistency** - Same wrapper+helper pattern used throughout the codebase (add, clone, etc.)

5. **Trade-off awareness** - Recursive implementation is clearer but uses O(h) stack space vs iterative O(1) space

---

## Confidence Check ✅

**Can I explain:**
- [x] All three deletion cases with examples
- [x] Why predecessor works for Case 3
- [x] Time and space complexity analysis
- [x] Why we return BstNode instead of void
- [x] How size is managed correctly
- [x] Edge cases handled
- [x] Design decisions and alternatives
- [x] How BST property is maintained

**Can I modify:**
- [x] Switch from predecessor to successor
- [x] Add boolean return value
- [x] Add deletion count tracking
- [x] Change to throw exception if not found

**Ready for defence!** 💪
