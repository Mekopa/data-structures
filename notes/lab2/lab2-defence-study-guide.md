# Lab 2 Defence Study Guide - Binary Search Trees

## Quick Reference Card

### Complexity Summary

| Operation | BstSet | AvlSet | TreeSet |
|-----------|--------|--------|---------|
| add() | O(h) | O(log n) | O(log n) |
| remove() | O(h) | O(log n) | O(log n) |
| contains() | O(h) | O(log n) | O(log n) |
| Worst case | O(n) | O(log n) | O(log n) |

**h = tree height, n = number of elements**

---

## BST Property

For any node with value X:
- ALL nodes in the **left subtree** have values **less than X**
- ALL nodes in the **right subtree** have values **greater than X**

---

## BST Deletion - 3 Cases

```
Case 1: Leaf       -> return null
Case 2: One child  -> return that child
Case 3: Two children -> replace with predecessor, delete predecessor
```

### Predecessor vs Successor

| Term | Definition | How to Find |
|------|------------|-------------|
| **Predecessor** | Largest value SMALLER than node | Go LEFT once, then RIGHT all the way |
| **Successor** | Smallest value LARGER than node | Go RIGHT once, then LEFT all the way |

---

## AVL Rotations

### Balance Factor
```
Balance Factor = height(left subtree) - height(right subtree)
Allowed values: -1, 0, +1
```

### Rotation Cases

| Tree Shape | Case | Rotation |
|------------|------|----------|
| Falls left-left (straight) | LL | Single RIGHT |
| Falls right-right (straight) | RR | Single LEFT |
| Zig-zag left-right | LR | Double RIGHT |
| Zig-zag right-left | RL | Double LEFT |

**Rule:** Rotation is OPPOSITE of first letter!

### Visual Examples

**LL Case -> Right Rotation:**
```
BEFORE:           AFTER:
      30            20
     /             /  \
    20            10   30
   /
  10
```

**RR Case -> Left Rotation:**
```
BEFORE:           AFTER:
  10                20
    \              /  \
     20           10   30
       \
        30
```

**LR Case -> Double Right Rotation:**
```
BEFORE:           STEP 1:           STEP 2:
    30              30                20
   /               /                 /  \
  10              20                10   30
    \            /
     20         10
```

**RL Case -> Double Left Rotation:**
```
BEFORE:           STEP 1:           STEP 2:
  10              10                  20
    \               \                /  \
     30              20             10   30
    /                  \
   20                   30
```

---

## Methods You Implemented - Visualized

### 1. remove(30) - Delete From Tree

```
STEP 1: Find 30 in tree

        50
       /  \
     [30]  70    <- Found it! Has 2 children
     /  \
    20   40

STEP 2: Has 2 children -> use predecessor (max of left subtree)

        50
       /  \
      30   70
     /  \
   [20]  40      <- Predecessor = 20 (go left, then right till end)

STEP 3: Copy predecessor value, delete predecessor

        50
       /  \
      20   70    <- 30 replaced with 20
        \
         40

DONE!
```

**One sentence:** "Find the node, handle 3 cases based on number of children"

---

### 2. addAll(inputSet) - Add All Elements

```
OURS (tree):          INPUT (set):
      30                {15, 45}
     /  \
    20   40

LOOP through INPUT:

Step 1: Add 15
      30
     /  \
    20   40
   /
  15  <- added!

Step 2: Add 45
      30
     /  \
    20   40
   /       \
  15        45  <- added!

DONE! OURS = {15, 20, 30, 40, 45}
```

**One sentence:** "Loop through input set, add each element"

---

### 3. containsAll(inputSet) - Check All Exist

```
OURS (tree):          INPUT (set):
      30                {20, 40}
     /  \
    20   40

LOOP through INPUT:

Step 1: Is 20 in OURS?
      30
     /  \
   [20]  40     <- Found! Continue...

Step 2: Is 40 in OURS?
      30
     /  \
    20  [40]    <- Found! Continue...

All found -> return TRUE
```

```
Example with FALSE:

OURS:                 INPUT:
      30               {20, 99}
     /  \
    20   40

Step 1: Is 20 in OURS? YES
Step 2: Is 99 in OURS? NO -> return FALSE immediately!
```

**One sentence:** "Loop through input set, return false if ANY is missing"

---

### 4. retainAll(inputSet) - Keep Only Common

```
OURS (tree):          INPUT (set):
      30                {20, 50}
     /  \
    20   40

STEP 1: Loop OURS, find what's NOT in INPUT

      30  -> 30 in INPUT {20,50}? NO -> toRemove
     /  \
    20   40 -> 40 in INPUT? NO -> toRemove
    ^
    20 in INPUT? YES -> keep!

toRemove = {30, 40}

STEP 2: Remove collected elements

Remove 30:           Remove 40:
      20                  20
        \          ->
         40

DONE! OURS = {20}
```

**One sentence:** "Collect elements NOT in input set, then remove them"

---

### 5. headSet(35) - Elements LESS Than 35

```
OURS (tree):
      30
     /  \
    20   40
   /       \
  10        50

LOOP through OURS (in-order: 10, 20, 30, 40, 50):

NEW SET = {}

  10 < 35? YES -> add    NEW = {10}
  20 < 35? YES -> add    NEW = {10, 20}
  30 < 35? YES -> add    NEW = {10, 20, 30}
  40 < 35? NO  -> skip
  50 < 35? NO  -> skip

RESULT: headSet(35) = {10, 20, 30}
```

**Memory:** HEAD = beginning = smaller values

---

### 6. tailSet(30) - Elements GREATER OR EQUAL To 30

```
OURS (tree):
      30
     /  \
    20   40
   /       \
  10        50

LOOP through OURS (in-order: 10, 20, 30, 40, 50):

NEW SET = {}

  10 >= 30? NO  -> skip
  20 >= 30? NO  -> skip
  30 >= 30? YES -> add    NEW = {30}
  40 >= 30? YES -> add    NEW = {30, 40}
  50 >= 30? YES -> add    NEW = {30, 40, 50}

RESULT: tailSet(30) = {30, 40, 50}
```

**Memory:** TAIL = end = larger values

---

### 7. subSet(20, 45) - Elements In Range [20, 45)

```
OURS (tree):
      30
     /  \
    20   40
   /       \
  10        50

LOOP through OURS (in-order: 10, 20, 30, 40, 50):

NEW SET = {}

  10: 10 >= 20? NO -> skip
  20: 20 >= 20 AND 20 < 45? YES -> add    NEW = {20}
  30: 30 >= 20 AND 30 < 45? YES -> add    NEW = {20, 30}
  40: 40 >= 20 AND 40 < 45? YES -> add    NEW = {20, 30, 40}
  50: 50 >= 20 AND 50 < 45? NO -> skip (50 >= 45!)

RESULT: subSet(20, 45) = {20, 30, 40}
```

**Memory:** [start, end) - start INCLUDED, end EXCLUDED

---

### 8. Iterator.remove() - Safe Remove While Looping

```
OURS:
      30
     /  \
    20   40
   /
  10

Task: Remove all elements > 25

WRONG WAY (crashes!):
for (E e : ourSet) {
    if (e > 25) {
        ourSet.remove(e);  // BOOM! ConcurrentModificationException
    }
}

RIGHT WAY:
Iterator<E> it = ourSet.iterator();
while (it.hasNext()) {
    E e = it.next();
    if (e > 25) {
        it.remove();       // Safe!
    }
}

RESULT: OURS = {10, 20}
```

**One sentence:** "Call outer class remove using BstSet.this.remove(), then set last to null"

---

### 9. AvlSet.remove(40) - Delete + Rebalance

```
BEFORE:
        30
       /  \
      20   40
     /
    10

STEP 1: Delete 40 (normal BST delete - it's a leaf)

        30
       /
      20      <- Tree is now UNBALANCED! (balance = 2)
     /
    10

STEP 2: It's LL case (falls left-left) -> RIGHT rotation

        20
       /  \
      10   30    <- Balanced now!

DONE!
```

**One sentence:** "Same as BstSet remove, but then check balance and rotate if needed"

---

## Method Patterns Summary

### Which Loop Pattern?

| Method | Loop Through | Check Against |
|--------|--------------|---------------|
| addAll | INPUT set | - (just add) |
| containsAll | INPUT set | OUR tree |
| retainAll | OUR tree | INPUT set |
| headSet(x) | OUR tree | value x |
| tailSet(x) | OUR tree | value x |
| subSet(a,b) | OUR tree | values a, b |

**Simple rule:**
- Want to ADD from input? -> Loop INPUT
- Want to FILTER our data? -> Loop OURS

---

## Summary Picture

```
+-------------------------------------------------------------+
|  YOUR METHODS                                               |
+-------------------------------------------------------------+
|                                                             |
|  addAll:      INPUT --add--> OURS                          |
|                                                             |
|  containsAll: INPUT --check--> OURS --> true/false         |
|                                                             |
|  retainAll:   OURS --check--> INPUT --remove--> OURS       |
|                                                             |
|  headSet:     OURS --filter(<)--> NEW SET                  |
|                                                             |
|  tailSet:     OURS --filter(>=)--> NEW SET                 |
|                                                             |
|  subSet:      OURS --filter([a,b))--> NEW SET              |
|                                                             |
|  remove:      Find node --> 3 cases --> delete             |
|                                                             |
|  AVL remove:  remove + check balance + rotate if needed    |
|                                                             |
+-------------------------------------------------------------+
```

---

## Benchmark Results (Variant #6)

### Your Finding

| Data Type | BstSet | TreeSet | Winner |
|-----------|--------|---------|--------|
| Random | ~10% faster | slower | BstSet |
| Sorted (40k+) | CRASH! | stable | TreeSet |

### Key Points for Defence

1. **Random data:** BstSet wins by 7-11% (no rebalancing overhead)
2. **Sorted data:** BstSet CRASHES with StackOverflowError at 40,000 elements
3. **Why crash?** Tree becomes linked list, height = 40,000, recursive calls overflow stack
4. **Conclusion:** TreeSet is safer for production (guaranteed O(log n))

---

## What is a Set?

**Set** = Collection of unique elements (no duplicates)

```
Set         = The RULES (interface)
BstSet      = Container using Binary Search Tree inside
TreeSet     = Container using Red-Black Tree inside
HashSet     = Container using Hash Table inside
```

They all store unique elements, just different internal organization!

---

## Defence One-Liners

| If Asked | Say This |
|----------|----------|
| "What's BST property?" | "Left subtree < node < right subtree" |
| "3 deletion cases?" | "Leaf->null, one child->return child, two children->use predecessor" |
| "What's predecessor?" | "Max of left subtree - go left once, then right till end" |
| "LL rotation?" | "Tree falls left-left, rotate RIGHT to fix" |
| "Why BstSet crashed?" | "Sorted data makes tree a linked list, 40k recursive calls overflow stack" |
| "Which for production?" | "TreeSet - guaranteed O(log n), BstSet can crash" |
| "What's BstSet.this?" | "Access outer class instance from inner class" |
| "What's retainAll?" | "Keep only elements that exist in BOTH sets" |

---

Good luck on your defence!
