# Hash Function Deep Dive

## The Key Line Explained

```java
int index = HashManager.hash(key.hashCode(), table.length, ht);
```

### Word by Word Breakdown

| Part | Meaning |
|------|---------|
| `int` | Store a whole number |
| `index` | Name for the result |
| `=` | Assign right side to left side |
| `HashManager` | Toolbox class with hash functions |
| `.` | "Go inside and use..." |
| `hash(...)` | Function that shrinks big numbers to small |
| `key` | Input from business logic (e.g., "TA102") |
| `.hashCode()` | Get a number from the key |
| `table.length` | Size of the array (e.g., 8) |
| `ht` | Hash Type - which formula to use |

---

## The Flow

```
"TA102"  →  hashCode()  →  2121010  →  hash(2121010, 8, DIVISION)  →  2
   ↑            ↑             ↑              ↑                         ↑
  key     get number     big number    shrink to fit              final slot
```

---

## hashCode() - Key Points

- **Every Java object** has `hashCode()` method
- Returns an **integer** (can be huge, positive or negative)
- **Deterministic**: same key → same number ALWAYS
- **Calculated from content**, not random

```java
"Apple".hashCode()   →  63476538   // from A-p-p-l-e
"Apple".hashCode()   →  63476538   // same every time!
"Banana".hashCode()  →  1982479823 // different content = different number
```

---

## Why Not Random?

```
put("TA102", Car1):
    "TA102" → 2121010 → slot 2 → stored

get("TA102"):
    "TA102" → 2121010 → slot 2 → found!
```

If random: you'd never find your data again!

---

## Hash Types (ht)

| Type | Formula | Purpose |
|------|---------|---------|
| `DIVISION` | `hashcode % tableLength` | Simple, fast |
| `MULTIPLICATION` | Complex formula with constant | Better spread |
| `JCF7` | Java 7 bit shifting | Java's old way |
| `JCF` | Java 8+ bit shifting | Java's current way |

### DIVISION Method (Most Common)

```java
Math.abs(hashcode) % tableLength

// Example:
Math.abs(2121010) % 8 = 2
```

- `Math.abs()` - make positive (hashcode can be negative)
- `% 8` - remainder after dividing (always 0-7 for table size 8)

---

## Why Different Methods Exist?

Goal: **spread keys evenly** across slots

```
BAD spread (many collisions):     GOOD spread (few collisions):
[0] → 10 items                    [0] → 3 items
[1] → empty                       [1] → 2 items
[2] → empty                       [2] → 4 items
[3] → 15 items                    [3] → 3 items

Slow lookups!                     Fast lookups!
```

Different formulas spread differently depending on data patterns.

---

## Key Types

Keys can be **any type**:

```java
HashMap<String, Car>    // String keys
HashMap<Integer, Car>   // Integer keys
HashMap<Car, String>    // Custom object keys
```

For custom objects, **must override hashCode()**:

```java
@Override
public int hashCode() {
    return Objects.hash(make, model, year, mileage, price);
}
```

---

## Summary

1. **key** = input from your code
2. **hashCode()** = converts key to big number (deterministic)
3. **hash(...)** = shrinks big number to fit table (0 to length-1)
4. **index** = the slot where key belongs
