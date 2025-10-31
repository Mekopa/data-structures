# Lab 1: Linear Data Structures - Learning Notes

**Completed:** October 31, 2025
**Branch:** L1-assignment → main
**Score:** 8/10 (Mandatory requirements complete)

---

## Overview

Implemented fundamental linear data structures including LinkedList, Stack, and Queue with both array-based and linked list-based implementations. Mastered LIFO and FIFO principles, complexity analysis, and design trade-offs between different implementations.

---

## Data Structures Implemented

### 1. LinkedList (Singly Linked)

**Operations Implemented:**
- `add(E e)` - Add element to end
- `add(int k, E e)` - Insert element at position k
- `remove(int k)` - Remove element at position k
- `set(int k, E e)` - Update element at position k
- `get(int k)` - Access element at position k
- `iterator()` - Traverse the list
- `Iterator.remove()` - Remove during iteration

**Time Complexity:**
- Access by index: O(n) - must traverse from head
- Insert at head: O(1) - direct pointer manipulation
- Insert at position k: O(k) - traverse then insert
- Delete at position k: O(k) - traverse then delete
- Append to end: O(1) - direct tail pointer access

**Key Insights:**
- Pointer manipulation is crucial - always consider null cases
- Maintaining both `head` and `tail` pointers improves append performance
- No random access capability unlike arrays
- Memory overhead per element (data + next pointer)
- Excellent for frequent insertions/deletions at known positions

**Implementation Details:**
- Uses inner `Node<E>` class with `data` and `next` fields
- Maintains `size` counter for O(1) size queries
- Edge cases handled: empty list, index 0, out of bounds

---

### 2. Stack (LIFO - Last-In-First-Out)

**Interface Methods:**
- `void push(E item)` - Add element to top - O(1) amortized
- `E pop()` - Remove and return top element - O(1)
- `E peek()` - View top element without removing - O(1)
- `boolean isEmpty()` - Check if stack is empty - O(1)

#### ArrayStack Implementation

**Internal Structure:**
```
Top → [_, _, _, 30, 20, 10] ← Bottom (index 0)
                  ↑
              size-1 (top of stack)
```

**Design Decisions:**
- Uses `ArrayList<E>` as internal storage
- Top of stack = end of ArrayList (index `size-1`)
- `push()`: `ArrayList.add(e)` - adds to end
- `pop()`: `ArrayList.remove(size-1)` - removes from end
- Both operations are O(1) because they operate on the end

**Advantages:**
- Simple implementation
- Good cache locality (contiguous memory)
- No pointer overhead

**Disadvantages:**
- Occasional O(n) resize when ArrayList grows
- Fixed initial capacity may waste memory

#### LinkedListStack Implementation

**Internal Structure:**
```
Top → [30] → [20] → [10] → null
      ↑
   index 0 (top of stack)
```

**Design Decisions:**
- Uses custom `LinkedList<E>` as internal storage
- Top of stack = beginning (index 0)
- `push()`: `LinkedList.add(0, e)` - insert at head
- `pop()`: `LinkedList.remove(0)` - remove from head
- Both operations are O(1) due to head pointer access

**Advantages:**
- True O(1) operations (no resizing)
- Dynamic size without waste

**Disadvantages:**
- Pointer overhead per element
- Poor cache locality (scattered memory)

**Real-World Applications:**
- Function call stack
- Undo/redo functionality
- Expression evaluation (postfix, infix)
- Backtracking algorithms (DFS)
- Balanced brackets checking

---

### 3. Queue (FIFO - First-In-First-Out)

**Interface Methods:**
- `void enqueue(E item)` - Add element to rear - O(1) amortized
- `E dequeue()` - Remove and return front element - O(1) amortized
- `E peek()` - View front element without removing - O(1)
- `boolean isEmpty()` - Check if queue is empty - O(1)

#### ArrayQueue Implementation

**Internal Structure:**
```
Front (index 0) → [10, 20, 30, 40] ← Rear (end)
                   ↑            ↑
                dequeue      enqueue
```

**Design Decisions:**
- Uses `ArrayList<E>` as internal storage
- Front of queue = index 0
- Rear of queue = end of ArrayList
- `enqueue()`: `ArrayList.add(e)` - adds to end - O(1) amortized
- `dequeue()`: `ArrayList.remove(0)` - removes from front - O(n)
- `peek()`: `ArrayList.get(0)` - views front - O(1)

**Performance Analysis:**
- enqueue: O(1) amortized (occasional resize)
- dequeue: O(n) - shifts all remaining elements left
- peek: O(1) - direct access
- For this assignment, we consider dequeue as O(1) amortized

**Why is remove(0) O(n)?**
ArrayList must shift all remaining elements one position left to fill the gap. For a queue with n elements, this requires n-1 copy operations.

#### LinkedListQueue Implementation

**Internal Structure:**
```
Front (index 0) → [10] → [20] → [30] → [40] ← Rear (end)
                   ↑                      ↑
                dequeue                enqueue
```

**Design Decisions:**
- Uses custom `LinkedList<E>` as internal storage
- Front of queue = index 0 (head pointer)
- Rear of queue = end (tail pointer)
- `enqueue()`: `LinkedList.add(e)` - adds to end - O(1)
- `dequeue()`: `LinkedList.remove(0)` - removes from head - O(1)
- `peek()`: `LinkedList.get(0)` - views head - O(1)

**Performance Analysis:**
- enqueue: O(1) true - tail pointer update
- dequeue: O(1) true - head pointer update
- peek: O(1) - direct head access

**Why LinkedList is Better for Queue:**
LinkedList can efficiently operate on both ends (head and tail) with O(1) pointer manipulation, while ArrayList can only efficiently operate on one end.

**Real-World Applications:**
- Task scheduling (print queue, CPU scheduler)
- Breadth-first search (BFS) in graphs
- Buffer management (keyboard input, network packets)
- Request handling in web servers

---

## Complexity Analysis Summary

| Data Structure | Operation | Time Complexity | Space Complexity |
|---------------|-----------|-----------------|------------------|
| **LinkedList** | add(e) | O(1) | O(1) |
| | add(k,e) | O(k) | O(1) |
| | remove(k) | O(k) | O(1) |
| | get(k) | O(k) | O(1) |
| | set(k,e) | O(k) | O(1) |
| **ArrayStack** | push | O(1) amortized | O(1) |
| | pop | O(1) | O(1) |
| | peek | O(1) | O(1) |
| **LinkedListStack** | push | O(1) | O(1) |
| | pop | O(1) | O(1) |
| | peek | O(1) | O(1) |
| **ArrayQueue** | enqueue | O(1) amortized | O(1) |
| | dequeue | O(n) | O(1) |
| | peek | O(1) | O(1) |
| **LinkedListQueue** | enqueue | O(1) | O(1) |
| | dequeue | O(1) | O(1) |
| | peek | O(1) | O(1) |

---

## Design Patterns and Principles

### 1. Interface-Based Design
- Created `List<E>`, `Stack<E>`, and `Queue<E>` interfaces
- Multiple implementations for each interface
- Enables polymorphism and flexible code

### 2. Generic Programming
- All structures use type parameter `<E>`
- Type safety without code duplication
- Some classes require `<E extends Comparable<E>>` for comparison operations

### 3. IS-A vs HAS-A Relationships

**HAS-A (Composition) - Used in Lab 1:**
- `ArrayStack` **has-a** `ArrayList<E>`
- `LinkedListStack` **has-a** `LinkedList<E>`
- `ArrayQueue` **has-a** `ArrayList<E>`
- `LinkedListQueue` **has-a** `LinkedList<E>`

**Why Composition Over Inheritance:**
- More flexible - can switch internal implementation
- Follows "favor composition over inheritance" principle
- Encapsulation - internal structure hidden from users
- Can delegate only necessary methods

### 4. Iterator Pattern
- Implemented custom iterator for LinkedList
- Enables foreach loop syntax
- `Iterator.remove()` allows safe removal during iteration

---

## Implementation Highlights

### Challenge 1: LinkedList.add(int k, E e)

**Problem:** Insert element before position k

**Solution Approach:**
- Handle edge case: k=0 (insert at head)
- Traverse to position k-1
- Create new node pointing to node at k
- Update previous node's next pointer
- Don't forget to increment size

**Key Learning:** Always consider:
- Empty list
- Insert at beginning (k=0)
- Insert at end (k=size)
- Invalid index (k < 0 or k > size)

### Challenge 2: Understanding LIFO vs FIFO

**Stack (LIFO):**
```java
push(10) → push(20) → push(30)
Stack: [10, 20, 30]
pop() → returns 30  // Last in, first out
```

**Queue (FIFO):**
```java
enqueue(10) → enqueue(20) → enqueue(30)
Queue: [10, 20, 30]
dequeue() → returns 10  // First in, first out
```

**Mental Model:**
- Stack = Stack of plates (add/remove from top only)
- Queue = Line at a store (add to back, serve from front)

### Challenge 3: Why ArrayList.remove(0) is Expensive

**The Problem:**
```
Before remove(0):  [10, 20, 30, 40, 50]
                    ↑
                  remove this

After remove(0):   [20, 30, 40, 50, _]
All elements shift left!
```

**The Cost:** For n elements, removing from index 0 requires n-1 copy operations = O(n)

**LinkedList Solution:**
```
Before remove(0):  head → [10] → [20] → [30] → null
                           ↑
                       remove this

After remove(0):   head → [20] → [30] → null
Just update head pointer!
```

**The Cost:** Update one pointer = O(1)

---

## Testing Strategy

### Test Coverage
1. **Empty Structure Tests**
   - isEmpty() returns true
   - pop/dequeue/peek return null
   - Edge case handling

2. **Basic Operations**
   - Sequential additions
   - LIFO/FIFO ordering verification
   - peek doesn't remove elements

3. **Edge Cases**
   - Operations on empty structures
   - Single element operations
   - Re-use after emptying

4. **LIFO/FIFO Verification**
   - Stack: Last in, first out
   - Queue: First in, first out

### Test Results
- ✅ StackTest: All tests passed (ArrayStack + LinkedListStack)
- ✅ QueueTest: All tests passed (ArrayQueue + LinkedListQueue)
- ✅ Both implementations behave identically from user perspective

---

## Trade-offs and Design Decisions

### ArrayList vs LinkedList for Stack

| Aspect | ArrayList | LinkedList |
|--------|-----------|------------|
| **Push/Pop** | O(1) amortized | O(1) true |
| **Memory** | Contiguous, good cache | Scattered, poor cache |
| **Overhead** | Wasted capacity | Pointer per element |
| **Resize** | Occasional O(n) | Never needed |
| **Best for** | Predictable size | Unknown size |

**Conclusion:** For Stack, both work well. ArrayList slightly better due to cache locality.

### ArrayList vs LinkedList for Queue

| Aspect | ArrayList | LinkedList |
|--------|-----------|------------|
| **Enqueue** | O(1) amortized | O(1) true |
| **Dequeue** | O(n) - shifts! | O(1) - pointer |
| **Memory** | Wasted capacity | Pointer per element |
| **Best for** | Small queues | Any size queue |

**Conclusion:** For Queue, LinkedList is clearly superior due to O(1) dequeue.

---

## Connections to Computer Science Theory

### 1. Abstract Data Types (ADT)
- Interface defines **what** operations are available
- Implementation defines **how** operations work
- Users interact with ADT, not implementation details

### 2. Amortized Analysis
- ArrayList.add() occasionally does O(n) resize
- But happens so rarely that average cost is O(1)
- Amortized analysis smooths out occasional expensive operations

### 3. Memory Management
- Arrays: Contiguous memory, cache-friendly
- Linked Lists: Scattered memory, pointer overhead
- Trade-off: Cache locality vs. Dynamic size

### 4. Time-Space Trade-offs
- ArrayList: Wastes space (unused capacity) for time efficiency
- LinkedList: Uses exact space needed but has pointer overhead

---

## Preparation for Oral Defence

### Key Concepts to Master

1. **Explain LIFO vs FIFO**
   - Real-world analogies
   - Use cases for each
   - Why different structures for different behaviors

2. **Complexity Analysis**
   - Big O notation for all operations
   - Understand **why** each operation has its complexity
   - Amortized vs worst-case analysis

3. **Design Trade-offs**
   - ArrayList vs LinkedList for Stack
   - ArrayList vs LinkedList for Queue
   - When to use which implementation

4. **Edge Cases**
   - Empty structure behavior
   - Null handling
   - Index bounds checking

### Questions to Anticipate

1. **"What's the time complexity of dequeue() in ArrayQueue and why?"**
   - Answer: O(n) because ArrayList.remove(0) shifts all elements

2. **"Why is LinkedList better for Queue than ArrayList?"**
   - Answer: LinkedList can do O(1) operations on both ends (head and tail), while ArrayList can only efficiently operate on one end

3. **"Implement contains(Object o) in LinkedList"**
   - Be ready to write: traverse list, compare each element, return boolean

4. **"When would you use Stack vs Queue?"**
   - Stack: DFS, undo/redo, expression evaluation, backtracking
   - Queue: BFS, task scheduling, buffering

5. **"What's the difference between O(1) and O(1) amortized?"**
   - O(1): Every operation is constant time
   - O(1) amortized: Average is constant, but occasional operation may be expensive

### Live Coding Practice

Be prepared to implement on the spot:
- `LinkedList.addFirst(E e)` - add at beginning
- `LinkedList.addLast(E e)` - add at end
- `LinkedList.removeFirst()` - remove from beginning
- `LinkedList.removeLast()` - remove from end
- `LinkedList.contains(Object o)` - search for element
- `LinkedList.indexOf(Object o)` - find index of element

---

## Code Culture and Best Practices

### What Was Done Well
1. **Clear naming conventions**
   - Descriptive variable names (head, tail, data, size)
   - Method names match their purpose (enqueue, dequeue, push, pop)

2. **Comprehensive documentation**
   - Javadoc comments for all public methods
   - Complexity analysis in comments
   - Edge cases documented

3. **Consistent patterns**
   - All implementations check isEmpty() before operations
   - All return null for invalid operations on empty structures
   - Similar structure across ArrayStack/Queue and LinkedListStack/Queue

4. **Edge case handling**
   - Null checks
   - Bounds checking
   - Empty structure operations

5. **Test-driven validation**
   - Comprehensive test suites
   - Both implementation types tested identically
   - Edge cases verified

### Areas for Improvement
1. Could add more robust error handling (throw exceptions vs return null)
2. Could implement more List methods (contains, indexOf, etc.)
3. Could optimize some LinkedList operations with better pointer handling

---

## What's Next

### Completed (8/10 points)
- ✅ L1 Intro (2 pts)
- ✅ LinkedList implementation (2 pts)
- ✅ Stack interface + implementations + tests (2 pts)
- ✅ Queue interface + implementations + tests (2 pts)

### Optional Tasks (2/10 points)
- ❌ Balanced Brackets Checker (1 pt) - Uses Stack
- ❌ Sliding Window Maximum (1 pt) - Uses Queue/Deque

### Deep Understanding Required for Oral Defence
- Review all commit messages (they contain learning notes!)
- Practice explaining complexity analysis
- Be ready to implement additional LinkedList methods live
- Master the theory: LIFO, FIFO, trade-offs, Big O

---

## Key Takeaways

1. **Linear data structures are fundamental** - Understanding LinkedList, Stack, and Queue is essential for advanced structures

2. **Implementation matters** - Same ADT, different performance based on internal structure

3. **Trade-offs are everywhere** - Memory vs speed, simplicity vs optimization, cache vs flexibility

4. **Testing validates correctness** - Comprehensive tests caught edge cases and verified LIFO/FIFO behavior

5. **Complexity analysis is critical** - Understanding **why** operations have certain complexity is more important than just knowing the Big O notation

6. **Real-world applications** - These structures aren't just academic - they're used everywhere in software

---

**Total Learning Time:** ~2 weeks (Weeks 1-2)
**Lines of Code:** 1071+ lines
**Commits:** 12 meaningful milestones
**Grade:** 8/10 (Mandatory complete, optional pending)
