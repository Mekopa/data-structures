# HashMap Methods Implementation (Separate Chaining)

## Overview

Three methods implemented in `HashMap.java`:

| Method | Purpose | Time Complexity |
|--------|---------|-----------------|
| `remove(K key)` | Delete key-value pair | O(1) avg, O(n) worst |
| `containsValue(Object value)` | Check if value exists | O(n) |
| `replace(K key, V oldValue, V newValue)` | Conditional replace | O(1) avg, O(n) worst |

---

## Method 1: remove(K key)

### What It Does
Removes a key-value pair and returns the value (or null if not found).

### Three Cases

```
Case 1: Empty slot
table[index] = null  →  return null

Case 2: Key at head
table[3] → [KEY] → [B] → null
           ↓
table[3] → [B] → null
Solution: table[index] = current.next

Case 3: Key in middle/end
table[3] → [A] → [KEY] → [C] → null
                  ↓
table[3] → [A] → [C] → null
Solution: current.next = current.next.next
```

### Code Pattern
```java
// Find slot
int index = HashManager.hash(key.hashCode(), table.length, ht);

// Check head
if (current.key.equals(key)) {
    table[index] = current.next;  // Remove head
}

// Check rest of chain
while (current.next != null) {
    if (current.next.key.equals(key)) {
        current.next = current.next.next;  // Skip over
    }
}
```

---

## Method 2: containsValue(Object value)

### What It Does
Checks if ANY key has this value. Returns true/false.

### Why O(n)?
- Cannot use hash function (searching by VALUE, not key)
- Must check EVERY slot and EVERY node

### The Pattern: Two Nested Loops

```java
for (int i = 0; i < table.length; i++) {    // Each slot
    Node<K, V> node = table[i];
    while (node != null) {                   // Each node in chain
        if (node.value.equals(value)) {
            return true;                      // Found!
        }
        node = node.next;
    }
}
return false;                                 // Not found
```

### Visual
```
Looking for Car2:

table[0] → null              ← skip
table[1] → [Car1] → [Car5]   ← no, no
table[2] → [Car2] → [Car3]   ← YES! return true
```

---

## Key vs Value Search

| Search Type | Method | Can Use Hash? | Complexity |
|-------------|--------|---------------|------------|
| By Key | `get()`, `remove()`, `contains()` | YES | O(1) avg |
| By Value | `containsValue()` | NO | O(n) |

**Why the difference?**
- Keys are hashed → go directly to slot
- Values are not hashed → must check everything

---

## Linked List Operations Review (from Lab 1)

These hash table methods use the same linked list skills:

| Operation | How |
|-----------|-----|
| Walk chain | `while (node != null) { node = node.next; }` |
| Remove head | `table[index] = head.next` |
| Remove middle | `prev.next = current.next` |
| Check each node | `for` loop + `while` loop |
