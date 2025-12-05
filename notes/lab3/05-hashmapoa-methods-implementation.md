# HashMapOa Methods Implementation (Open Addressing)

## Overview

Three methods implemented in `HashMapOa.java`:

| Method | Purpose | Time Complexity |
|--------|---------|-----------------|
| `remove(K key)` | Delete key-value pair (mark DELETED) | O(1) avg, O(n) worst |
| `containsValue(Object value)` | Check if value exists | O(n) |
| `replace(K key, V oldValue, V newValue)` | Conditional replace | O(1) avg, O(n) worst |

---

## Key Difference from HashMap

| Aspect | HashMap (Separate Chaining) | HashMapOa (Open Addressing) |
|--------|----------------------------|----------------------------|
| Delete | Remove node from linked list | Mark as `DELETED` |
| Helper | `getInChain()` | `findPosition()` |
| Slot | Linked list | Single Entry |

---

## Method 1: remove(K key)

### What It Does
Finds the key and marks the slot as `DELETED` (not null!).

### Why DELETED, Not Null?

```
If we use null:
[5] John
[6] null    ← was Jane, now empty
[7] Jim     ← can't find this anymore!

Search for Jim stops at [6] because it's empty.

If we use DELETED:
[5] John
[6] DELETED ← "keep searching!"
[7] Jim     ← found!
```

### Code Pattern

```java
int position = findPosition(key, false);  // Find where key is

if (position != -1 && table[position] != null && !DELETED.equals(table[position])) {
    V value = table[position].value;
    table[position] = DELETED;  // Mark, don't null!
    size--;
    return value;
}

return null;
```

### Visual

```
remove("Jane")

Before:
[5] John
[6] Jane   ← position = 6
[7] Jim

table[6] = DELETED;

After:
[5] John
[6] DELETED
[7] Jim

return: Jane's value
```

### The Three Checks Explained

```java
if (position != -1 && table[position] != null && !DELETED.equals(table[position]))
```

| Check | Why |
|-------|-----|
| `position != -1` | findPosition found something |
| `table[position] != null` | Slot is not empty |
| `!DELETED.equals(table[position])` | Slot is not already deleted |

---

## Helper Method: findPosition()

```java
private int findPosition(K key, boolean stopAtDeleted)
```

| Parameter | Meaning |
|-----------|---------|
| `key` | What to search for |
| `stopAtDeleted = true` | Stop at DELETED slots (for inserting) |
| `stopAtDeleted = false` | Skip DELETED slots (for searching/removing) |

**For remove:** Use `false` - we need to find the actual key, not stop at deleted slots.

---

## Method 2: containsValue(Object value)

### What It Does
Checks if ANY key has this value. Returns true/false.

### Simpler Than HashMap!

No chains to walk through - just one loop:

```java
for (int i = 0; i < table.length; i++) {
    if (table[i] != null && !DELETED.equals(table[i])) {
        if (table[i].value.equals(value)) {
            return true;
        }
    }
}
return false;
```

### Skip These Slots

| Slot State | Action |
|------------|--------|
| `null` | Skip - empty |
| `DELETED` | Skip - no data |
| `Entry` | Check the value |

### Visual

```
containsValue(Car2)

[0] → Car1      ← check: no
[1] → null      ← skip
[2] → DELETED   ← skip
[3] → Car2      ← check: YES! return true
[4] → Car3
```

### Comparison: HashMap vs HashMapOa

| HashMap | HashMapOa |
|---------|-----------|
| Two loops (slots + chains) | One loop (slots only) |
| `while (node != null)` | `if (not null && not DELETED)` |

**Time complexity:** O(n) for both - must check all values.

---

## Method 3: replace(K key, V oldValue, V newValue)

### What It Does
Replace value **only if** current value matches `oldValue`.

### Same Logic as HashMap

1. Find the key
2. Check if current value equals oldValue
3. If yes → replace, return true
4. If no → return false

### Code Pattern

```java
int position = findPosition(key, false);

if (position != -1 && table[position] != null && !DELETED.equals(table[position])) {
    if (table[position].value.equals(oldValue)) {
        table[position].value = newValue;
        return true;
    }
}

return false;
```

### The Checks

| Check | Why |
|-------|-----|
| `position != -1` | Key was found |
| `table[position] != null` | Slot not empty |
| `!DELETED.equals(...)` | Slot not deleted |
| `value.equals(oldValue)` | Current value matches expected |

### Visual

```
replace("TA102", Car2, Car99)

[5] → ["TA101", Car1]
[6] → ["TA102", Car2]   ← position = 6, value = Car2 = oldValue? YES!
[7] → DELETED

table[6].value = Car99

[6] → ["TA102", Car99]

return true
```

---

## Summary: All HashMapOa Methods

| Method | Key Points |
|--------|------------|
| `remove()` | Mark as DELETED, not null |
| `containsValue()` | Skip null and DELETED |
| `replace()` | Same as remove checks + value match |

All methods use `findPosition(key, false)` to locate keys.
