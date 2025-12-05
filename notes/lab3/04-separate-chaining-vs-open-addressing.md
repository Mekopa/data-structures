# Separate Chaining vs Open Addressing

## Quick Comparison

| Aspect | Separate Chaining (HashMap) | Open Addressing (HashMapOa) |
|--------|----------------------------|----------------------------|
| Each slot holds | Linked list (many items) | ONE item only |
| Collision handling | Add to chain | Probe for next empty slot |
| Delete method | Remove from linked list | Mark as DELETED |
| Memory | Extra pointers for links | No extra pointers |
| Load factor | Can exceed 1.0 | Must stay < 1.0 |

---

## Separate Chaining (HashMap)

### Structure

```
table[0] → null
table[1] → [A] → [B] → [C] → null    ← linked list
table[2] → [D] → null
table[3] → null
```

### Collision? Add to Chain

```
"Apple" hashes to slot 1  → add to chain
"Banana" hashes to slot 1 → add to same chain

table[1] → [Apple] → [Banana] → null
```

### Delete? Remove from List

```
Before: table[1] → [Apple] → [Banana] → [Cherry] → null

Delete Banana:

After:  table[1] → [Apple] → [Cherry] → null

Simple! Just adjust pointers.
```

---

## Open Addressing (HashMapOa)

### Structure

```
table[0] → item
table[1] → EMPTY
table[2] → item
table[3] → DELETED
table[4] → item
```

### Collision? Probe for Next Slot

```
"Apple" hashes to slot 2  → slot 2 empty → store at 2
"Banana" hashes to slot 2 → slot 2 taken → try 3 → empty → store at 3
"Cherry" hashes to slot 2 → slot 2 taken → try 3 → taken → try 4 → store at 4
```

```
[2] Apple
[3] Banana
[4] Cherry
```

### Delete? Mark as DELETED (Not Empty!)

**Why can't we just make it empty?**

```
Initial state:
[2] Apple   ← hashed here
[3] Banana  ← hashed to 2, probed to 3
[4] Cherry  ← hashed to 2, probed to 4

Delete Banana (WRONG - make empty):
[2] Apple
[3] EMPTY   ← problem!
[4] Cherry

Search for Cherry:
  hash("Cherry") → 2
  [2] = Apple, not Cherry → probe
  [3] = EMPTY → "Cherry doesn't exist!"

WRONG! Cherry is at [4] but we stopped at empty slot!
```

**Solution: Use DELETED marker**

```
Delete Banana (RIGHT - mark deleted):
[2] Apple
[3] DELETED  ← "something was here, keep looking"
[4] Cherry

Search for Cherry:
  hash("Cherry") → 2
  [2] = Apple → probe
  [3] = DELETED → keep probing!
  [4] = Cherry → FOUND!
```

---

## The DELETED Marker

### What It Means

| Slot State | Meaning | When Searching | When Inserting |
|------------|---------|----------------|----------------|
| `null` (empty) | Never used | Stop searching | Can insert here |
| `DELETED` | Was used, now removed | Keep searching | Can insert here |
| `item` | Contains data | Check if match | Keep probing |

### In Code (HashMapOa.java)

```java
private final Entry<K, V> DELETED = new Entry<>();  // Special marker

// When checking slots:
if (table[position] == null) {
    // Empty - never used
}
if (DELETED.equals(table[position])) {
    // Deleted - keep searching, but can insert here
}
```

---

## Probing Methods

When collision occurs, how to find next slot?

| Method | Formula | Example (start at 5) |
|--------|---------|----------------------|
| Linear | +1, +2, +3... | 5 → 6 → 7 → 8 |
| Quadratic | +1, +4, +9, +16... | 5 → 6 → 9 → 14 |
| Double Hash | +h2, +2*h2, +3*h2... | depends on second hash |

### Linear Probing (Most Common)

```java
position = (index + i + 1) % table.length;

// If index=5, table.length=10:
// i=0: (5 + 0 + 1) % 10 = 6
// i=1: (5 + 1 + 1) % 10 = 7
// i=2: (5 + 2 + 1) % 10 = 8
```

---

## Summary

```
Separate Chaining:
- Slot = linked list
- Collision = add to list
- Delete = remove from list (simple!)

Open Addressing:
- Slot = one item
- Collision = probe next slot
- Delete = mark DELETED (tricky!)
- Must keep DELETED so searches don't break
```
