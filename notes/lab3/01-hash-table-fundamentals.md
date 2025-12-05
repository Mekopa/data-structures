# Hash Table Fundamentals

## The Problem: Slow Searching

**Without hashing (list/array):**
- Find "John" in 1 million contacts
- Worst case: check all 1,000,000 items
- Time complexity: **O(n)**

**With hashing:**
- Calculate where "John" should be
- Go directly there: 1 lookup
- Time complexity: **O(1)** average

---

## Core Concept: Hash Function

A **hash function** converts any key into an array index:

```
Key  →  [Hash Function]  →  Array Index
```

**Example:**
```
Array size: 10

"Apple"  → hashCode() → 63476538 → 63476538 % 10 = 8  → slot [8]
"Banana" → hashCode() → 19824798 → 19824798 % 10 = 8  → slot [8]  COLLISION!
```

**In Java:** Every object has `.hashCode()` method that returns an integer.

---

## Terminology

| Term | Meaning |
|------|---------|
| **Table** | The array that stores data |
| **Slot** | One position in the array (e.g., `table[5]`) |
| **Hash Function** | Formula that converts key → index |
| **Collision** | Two keys hash to the same index |
| **Load Factor** | How full the table is (e.g., 0.75 = 75%) |
| **Rehash** | Create bigger table when too full |

---

## Collisions: The Inevitable Problem

**Pigeonhole principle:** If you have more possible keys than array slots, collisions MUST happen.

**Two solutions:**

### 1. Separate Chaining

Each slot holds a **linked list** of all items that hash to that index.

```
table[0] → null
table[1] → null
table[2] → [Carol] → [Bob] → [Anna] → null
table[3] → null
table[4] → [Dave] → null
```

- Collision? Add to the linked list
- Can hold unlimited items per slot
- Uses extra memory for pointers

### 2. Open Addressing

Each slot holds **ONE item only**. Collision? Find another empty slot.

```
table[0] → item
table[1] → EMPTY
table[2] → item
table[3] → DELETED
table[4] → item
```

- Collision? Probe for next empty slot
- Probing methods: Linear, Quadratic, Double Hashing
- Table can never exceed 100% full

---

## Comparison Table

| Aspect | Separate Chaining | Open Addressing |
|--------|-------------------|-----------------|
| Slot holds | Linked list (many) | One item |
| Collision | Add to list | Find next slot |
| Delete | Remove from list | Mark as DELETED |
| Memory | Extra pointers | No extra pointers |
| Load factor | Can exceed 1.0 | Must stay < 1.0 |

---

## The DELETED Marker (Open Addressing Only)

**Why needed?**

```
Initial state:
[5] John
[6] Jane  ← hashed to 5, probed to 6
[7] Jim   ← hashed to 5, probed to 7

Delete Jane (wrong way - make empty):
[5] John
[6] EMPTY
[7] Jim

Search for Jim:
  Hash("Jim") → 5
  [5] = John, not Jim → probe
  [6] = EMPTY → "Jim doesn't exist!" WRONG!
```

**Solution:** Mark as DELETED, not EMPTY:

```
[5] John
[6] DELETED  ← "keep looking, something was here"
[7] Jim

Search for Jim:
  [5] = John → probe
  [6] = DELETED → keep probing!
  [7] = Jim → FOUND!
```

---

## Hash Table = Array + Hash Function + Collision Handling

```
┌─────────────────────────────────────────┐
│            HASH TABLE                   │
│                                         │
│  ┌──────────────┐                       │
│  │ Hash Function│  key → index          │
│  └──────────────┘                       │
│          ↓                              │
│  ┌──────────────────────────────────┐   │
│  │ Array (the "table")              │   │
│  │ [0] [1] [2] [3] [4] [5] [6] [7]  │   │
│  └──────────────────────────────────┘   │
│          ↓                              │
│  ┌──────────────────┐                   │
│  │ Collision Handler│                   │
│  │ (chain or probe) │                   │
│  └──────────────────┘                   │
└─────────────────────────────────────────┘
```

---

## Why Use Hash Tables?

| Data Structure | Search Time | Use When |
|----------------|-------------|----------|
| Array (unsorted) | O(n) | Small data, rare searches |
| Array (sorted) | O(log n) | Data doesn't change often |
| Linked List | O(n) | Frequent insert/delete at ends |
| **Hash Table** | **O(1)** avg | **Fast lookup by key** |

**Trade-off:** Uses more memory, but saves lots of time.

---

## In This Lab

| Class | Method | Your Task |
|-------|--------|-----------|
| `HashMap` | Separate Chaining | Implement remove, containsValue, replace |
| `HashMapOa` | Open Addressing | Implement remove (with DELETED), containsValue, replace |

---

## Key Takeaways

1. **Hash table = fast dictionary** - O(1) average lookup
2. **Hash function** converts any key to array index
3. **Collisions are unavoidable** - must handle them
4. **Two collision strategies:**
   - Separate Chaining: linked list at each slot
   - Open Addressing: probe for next empty slot
5. **DELETED marker** needed only for Open Addressing deletes
6. **Table** is just a fancy word for "the array"
