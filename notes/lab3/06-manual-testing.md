# Manual Testing

## Overview

Tests were added to `ManualTest.java` to verify all 6 implemented methods work correctly.

**Run with:**
```bash
cd lab3/l3-assignment-hash-tables/l3-assignment-hash-tables
mvn compile -q && java -cp target/classes demo.ManualTest
```

---

## Test Cases

### containsValue()

| Test | Input | Expected | Purpose |
|------|-------|----------|---------|
| Existing value | car1 | true | Value in map |
| Existing value | car2 | true | Another value in map |
| Non-existing value | fake car | false | Value not in map |

### replace()

| Test | Input | Expected | Purpose |
|------|-------|----------|---------|
| Correct oldValue | key exists, oldValue matches | true | Normal replacement |
| Wrong oldValue | key exists, oldValue doesn't match | false | Safety check works |
| Non-existing key | fake key | false | Key validation works |

### remove()

| Test | Input | Expected | Purpose |
|------|-------|----------|---------|
| Existing key | TA102 | returns Car | Normal removal |
| After removal | contains(TA102) | false | Key is gone |
| Size after removal | size() | decremented | Size updated |
| Remove again | TA102 | null | Already removed |
| Non-existing key | FAKE_KEY | null | Key not found |

---

## Special Test: DELETED Marker (Open Addressing)

This test verifies the DELETED marker works correctly:

```
Before remove:
[5] TA156
[6] TA102  ← will be removed
[7] TA178  ← must still be findable!

After remove:
[5] TA156
[6] DELETED  ← marker, not empty
[7] TA178    ← search continues past DELETED

Test: contains(TA178) → true ✓
```

**Why this matters:**
- If we used `null` instead of `DELETED`, search would stop at slot 6
- TA178 would be "lost" even though it's still in the table
- The DELETED marker tells search: "keep looking!"

---

## Test Results Summary

### HashMap (Separate Chaining)

```
containsValue(car1 - exists): true ✓
containsValue(car2 - exists): true ✓
containsValue(fake car - not exists): false ✓

replace(TA156, car1, newCar) - should be true: true ✓
replace(TA102, car1, newCar) - wrong oldValue: false ✓
replace(FAKE_KEY, ...) - key not exists: false ✓

remove(TA102) returned: Renault_Megane ✓
contains(TA102) after remove: false ✓
remove(TA102) again - should be null: null ✓
remove(FAKE_KEY) - should be null: null ✓
```

### HashMapOa (Open Addressing)

```
containsValue(car1 - exists): true ✓
containsValue(car2 - exists): true ✓
containsValue(fake car - not exists): false ✓

replace(TA156, car1, newCar) - should be true: true ✓
replace(TA102, car1, newCar) - wrong oldValue: false ✓
replace(FAKE_KEY, ...) - key not exists: false ✓

remove(TA102) returned: Renault_Megane ✓
contains(TA102) after remove: false ✓
remove(TA102) again - should be null: null ✓
remove(FAKE_KEY) - should be null: null ✓

DELETED marker test:
contains(TA178) after removing TA102: true ✓
get(TA178): Toyota_Corolla ✓
```

---

## All Tests Passed!

Both implementations (HashMap and HashMapOa) work correctly for:
- `remove(K key)`
- `containsValue(Object value)`
- `replace(K key, V oldValue, V newValue)`
