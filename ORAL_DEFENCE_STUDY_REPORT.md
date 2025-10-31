# 📚 Lab 1 Oral Defence Study Report

**Student:** Mekopa
**Lab:** L1 - Linear Data Structures
**Preparation Date:** October 31, 2025
**Defence Points:** 8/10 (Critical component)
**Status:** ✅ All mandatory requirements complete

---

## 🎯 Executive Summary

You have successfully completed all mandatory tasks for Lab 1:
- ✅ **LinkedList** implementation with 7 core methods
- ✅ **Stack** interface + ArrayStack + LinkedListStack + Tests
- ✅ **Queue** interface + ArrayQueue + LinkedListQueue + Tests
- ✅ Comprehensive test coverage demonstrating LIFO/FIFO behavior

**Your Score Breakdown:**
- Exercise (2/10): ✅ Complete
- Oral Defence (8/10): 🎯 **THIS IS WHAT YOU'RE PREPARING FOR**

---

## 🔥 Top 10 Questions You MUST Be Able to Answer

### 1. **Explain LIFO vs FIFO with real-world examples**

**Model Answer:**
- **LIFO (Last In, First Out)** - Stack of plates
  - You add plates to the top
  - You take plates from the top
  - The last plate added is the first one removed
  - **Real-world:** Function call stack, undo/redo, browser back button

- **FIFO (First In, First Out)** - Line at a store
  - People join at the back (rear)
  - People are served from the front
  - The first person to arrive is the first to be served
  - **Real-world:** Print queue, CPU task scheduler, BFS algorithm

**Visual:**
```
Stack (LIFO):
push(1) → push(2) → push(3) → [1,2,3]
pop() → 3 (last in, first out)

Queue (FIFO):
enqueue(1) → enqueue(2) → enqueue(3) → [1,2,3]
dequeue() → 1 (first in, first out)
```

---

### 2. **What's the time complexity of dequeue() in ArrayQueue and why?**

**Answer:**
- **O(n)** where n is the number of elements in the queue

**Why?**
```
Before: [10, 20, 30, 40, 50]  (size = 5)
         ↑ remove(0)

After:  [20, 30, 40, 50]
```

When you call `ArrayList.remove(0)`:
1. Element at index 0 is removed
2. **ALL remaining elements shift left by one position**
3. For n elements, this requires (n-1) copy operations
4. Therefore: **O(n) time complexity**

**Follow-up:** "Why is it O(1) amortized in this assignment?"
- In practice, queue operations alternate between enqueue and dequeue
- The amortized cost averages out over many operations
- For this lab, we consider it O(1) amortized for simplicity

---

### 3. **Why is LinkedListQueue better than ArrayQueue?**

**Answer:**
```
ArrayQueue:
- enqueue: O(1) amortized (add to end)
- dequeue: O(n) (remove from front, shifts all elements)

LinkedListQueue:
- enqueue: O(1) true (update tail pointer)
- dequeue: O(1) true (update head pointer)
```

**Key Insight:** LinkedList can efficiently operate on **BOTH ends** (head and tail) with pointer manipulation. ArrayList can only efficiently operate on **ONE end** (the end).

**When to use each:**
- ArrayQueue: Small queues where shifting cost is negligible
- LinkedListQueue: Large queues or performance-critical applications

---

### 4. **Explain your LinkedList.add(int k, E e) implementation**

**Answer:**
```java
// Three cases to handle:
1. k == 0: Insert at head
   first = new Node<>(e, first);

2. k == size: Insert at end (append)
   Traverse to last node, create new node

3. k in middle: Insert before position k
   Traverse to node at k-1
   Create new node pointing to node at k
   Update previous.next to new node
```

**Time Complexity:** O(k) - must traverse k nodes to reach position

**Edge Cases:**
- Empty list (k must be 0)
- Invalid k (k < 0 or k > size) → return false
- Null element → return false

---

### 5. **What's the difference between O(1) and O(1) amortized?**

**Answer:**

**O(1) - True Constant Time:**
- Every single operation takes constant time
- No variation in worst case
- Example: LinkedList.add(0, e) - always just pointer update

**O(1) Amortized:**
- **Most** operations are O(1)
- **Occasionally** an operation is expensive (e.g., O(n))
- But expensive operations happen so rarely that **average** is O(1)

**Example: ArrayList.add(e)**
```
Most of the time: O(1) - just add to end
Occasionally: O(n) - when array is full, resize (copy all elements)

If capacity doubles each resize:
- 1000 adds = 999 O(1) operations + 1 O(n) resize
- Average = O(1) amortized
```

---

### 6. **Walk through Stack implementation design decisions**

**ArrayStack:**
```java
private ArrayList<E> data;

Top of stack = end of ArrayList (index size-1)

push(e): data.add(e)         // O(1) amortized
pop():   data.remove(size-1) // O(1)
peek():  data.get(size-1)    // O(1)
```

**Why end of ArrayList?**
- ArrayList is efficient at adding/removing from the END
- Removing from the front would be O(n) (shift all elements)

**LinkedListStack:**
```java
private LinkedList<E> data;

Top of stack = beginning (index 0)

push(e): data.add(0, e)  // O(1) - head pointer update
pop():   data.remove(0)  // O(1) - head pointer update
peek():  data.get(0)     // O(1) - direct access to head
```

**Why beginning of LinkedList?**
- LinkedList is efficient at operations on BOTH ends
- Head pointer allows O(1) access to first element
- Could also use end, but beginning is simpler

---

### 7. **Explain Iterator.remove() implementation**

**Answer:**
```java
@Override
public void remove() {
    // Guard: Can only remove after calling next()
    if (lastReturnedNode == null) {
        throw IllegalStateException();
    }

    // Case 1: Removing first node
    if (lastReturnedNode == first) {
        first = lastReturnedNode.next;
        if (first == null) last = null;
    }
    // Case 2: Removing middle/end node
    else {
        // Find node BEFORE the one to remove
        Node<E> previous = first;
        while (previous.next != lastReturnedNode) {
            previous = previous.next;
        }
        // Bypass the removed node
        previous.next = lastReturnedNode.next;
        if (previous.next == null) last = previous;
    }

    size--;
    lastReturnedNode = null; // Prevent double removal
}
```

**Key Points:**
- Can't remove before calling `next()`
- Can't remove twice in a row
- Must update `first`, `last`, and `size`
- Must traverse to find previous node (O(n))

---

### 8. **Compare ArrayList vs LinkedList for Stack - which is better?**

**Answer:**

| Aspect | ArrayList | LinkedList |
|--------|-----------|------------|
| **Operations** | O(1) amortized | O(1) true |
| **Memory Layout** | Contiguous (good cache) | Scattered (poor cache) |
| **Memory Overhead** | Wasted capacity | Pointer per node |
| **Resize Cost** | Occasional O(n) | Never |
| **Random Access** | O(1) | O(n) |

**Verdict:** **ArrayList is slightly better for Stack**

**Why?**
1. Stack only needs access to one end (top)
2. Cache locality matters - contiguous memory is faster
3. Amortized O(1) is acceptable for stack operations
4. Less memory overhead (no pointers)

**When LinkedList wins:**
- If you need guaranteed O(1) (no resizing ever)
- If you're combining Stack with other operations requiring LinkedList

---

### 9. **How would you implement contains(Object o) in LinkedList?**

**Live Coding Answer:**
```java
public boolean contains(Object o) {
    if (o == null) {
        return false;
    }

    // Traverse the list
    for (Node<E> current = first; current != null; current = current.next) {
        if (current.element.equals(o)) {
            return true;
        }
    }

    return false;
}
```

**Time Complexity:** O(n) - worst case must check every node

**Follow-up: "What about indexOf(Object o)?"**
```java
public int indexOf(Object o) {
    if (o == null) return -1;

    int index = 0;
    for (Node<E> current = first; current != null; current = current.next) {
        if (current.element.equals(o)) {
            return index;
        }
        index++;
    }

    return -1; // Not found
}
```

---

### 10. **What are the key edge cases in LinkedList operations?**

**Answer:**

1. **Empty List Operations**
   - get(0) on empty list → return null
   - remove(0) on empty list → return null
   - size should be 0, first/last should be null

2. **Single Element**
   - Removing the only element should set both first AND last to null
   - Adding to empty list should set both first AND last

3. **Boundary Indices**
   - add(0, e): Insert at head (special case)
   - add(size, e): Insert at end (append)
   - remove(0): Remove head (update first pointer)
   - remove(size-1): Remove tail (update last pointer)

4. **Invalid Indices**
   - k < 0 or k > size for add() → return false
   - k < 0 or k >= size for get/remove → return null

5. **Null Elements**
   - add(null) → return false (don't accept null elements)

---

## 🧠 Quick Reference: Time Complexity Cheat Sheet

### LinkedList<E>
```
Operation       | Time Complexity | Why
----------------|-----------------|----------------------------
add(e)          | O(1)            | Direct tail pointer access
add(k, e)       | O(k)            | Traverse k nodes
remove(k)       | O(k)            | Traverse k nodes
get(k)          | O(k)            | Traverse k nodes
set(k, e)       | O(k)            | Traverse k nodes
size()          | O(1)            | Cached size variable
isEmpty()       | O(1)            | Check if first == null
iterator()      | O(1)            | Create iterator object
Iterator.next() | O(1)            | Move to next node
Iterator.remove()| O(n)           | Must find previous node
```

### Stack<E> (Both Implementations)
```
Operation  | ArrayStack        | LinkedListStack
-----------|-------------------|------------------
push(e)    | O(1) amortized    | O(1) true
pop()      | O(1)              | O(1)
peek()     | O(1)              | O(1)
isEmpty()  | O(1)              | O(1)
```

### Queue<E>
```
Operation  | ArrayQueue        | LinkedListQueue
-----------|-------------------|------------------
enqueue(e) | O(1) amortized    | O(1)
dequeue()  | O(n) ⚠️          | O(1)
peek()     | O(1)              | O(1)
isEmpty()  | O(1)              | O(1)
```

**⚠️ Critical:** Know why ArrayQueue.dequeue() is O(n)!

---

## 💻 Live Coding Challenges - Practice These!

### Challenge 1: Implement addFirst() in LinkedList
```java
public boolean addFirst(E e) {
    // Your implementation here
    // Hint: Same as add(0, e) but can be optimized
}
```

<details>
<summary>Solution</summary>

```java
public boolean addFirst(E e) {
    if (e == null) return false;

    first = new Node<>(e, first);
    if (last == null) {  // Was empty
        last = first;
    }
    size++;
    return true;
}
```
Time: O(1)
</details>

---

### Challenge 2: Implement removeLast() in LinkedList
```java
public E removeLast() {
    // Your implementation here
    // Hint: Need to find second-to-last node
}
```

<details>
<summary>Solution</summary>

```java
public E removeLast() {
    if (isEmpty()) return null;

    E removed = last.element;

    if (first == last) {  // Only one element
        first = null;
        last = null;
    } else {
        // Find second-to-last node
        Node<E> current = first;
        while (current.next != last) {
            current = current.next;
        }
        current.next = null;
        last = current;
    }

    size--;
    return removed;
}
```
Time: O(n) - must traverse to find previous node

**This is why doubly-linked lists exist!**
</details>

---

### Challenge 3: Reverse a Stack
```java
// Given a Stack<Integer> with [1, 2, 3, 4, 5] (5 on top)
// Reverse it to [5, 4, 3, 2, 1] (1 on top)
public static <E> void reverseStack(Stack<E> stack) {
    // Your implementation here
}
```

<details>
<summary>Solution</summary>

```java
public static <E> void reverseStack(Stack<E> stack) {
    Queue<E> queue = new LinkedListQueue<>();

    // Move all elements from stack to queue
    while (!stack.isEmpty()) {
        queue.enqueue(stack.pop());
    }

    // Move all elements back from queue to stack
    while (!queue.isEmpty()) {
        stack.push(queue.dequeue());
    }
}
```

**Why this works:**
- Stack: LIFO → pop gives [5,4,3,2,1]
- Queue: FIFO → enqueue stores [5,4,3,2,1]
- Queue: FIFO → dequeue gives [5,4,3,2,1]
- Stack: LIFO → push stores reversed [1,2,3,4,5] (1 on top)

Time: O(n), Space: O(n)
</details>

---

### Challenge 4: Check Balanced Brackets
```java
// Check if brackets are balanced: "({[]})" → true, "({[}])" → false
public static boolean isBalanced(String expr) {
    // Your implementation here using Stack
}
```

<details>
<summary>Solution</summary>

```java
public static boolean isBalanced(String expr) {
    Stack<Character> stack = new ArrayStack<>();

    for (char ch : expr.toCharArray()) {
        if (ch == '(' || ch == '{' || ch == '[') {
            stack.push(ch);
        }
        else if (ch == ')' || ch == '}' || ch == ']') {
            if (stack.isEmpty()) return false;

            char top = stack.pop();
            if ((ch == ')' && top != '(') ||
                (ch == '}' && top != '{') ||
                (ch == ']' && top != '[')) {
                return false;
            }
        }
    }

    return stack.isEmpty();
}
```

**Application:** Compiler syntax checking, HTML tag validation
</details>

---

## 🎤 Mock Defence Dialogue - Practice This Out Loud!

**Lecturer:** "Explain your Stack implementation."

**You:** "I implemented two Stack versions:

1. **ArrayStack** uses ArrayList internally, with the top of the stack at the end (index size-1). This gives O(1) amortized push/pop because ArrayList is efficient at adding/removing from the end.

2. **LinkedListStack** uses my custom LinkedList, with the top at index 0 (the head). This gives true O(1) operations through head pointer manipulation.

Both implement the same Stack<E> interface, demonstrating polymorphism."

---

**Lecturer:** "Why is ArrayQueue.dequeue() slow?"

**You:** "It's O(n) because it calls ArrayList.remove(0), which removes from the front. This forces all remaining elements to shift left by one position. For a queue with n elements, that's n-1 copy operations.

In contrast, LinkedListQueue.dequeue() is O(1) because it just updates the head pointer. That's why LinkedList is superior for Queue implementations."

---

**Lecturer:** "Implement contains() for LinkedList right now."

**You:** [Write code confidently]
```java
public boolean contains(Object o) {
    if (o == null) return false;

    for (Node<E> current = first; current != null; current = current.next) {
        if (current.element.equals(o)) {
            return true;
        }
    }

    return false;
}
```

"This is O(n) because in the worst case, the element is at the end or not present, requiring us to check every node."

---

**Lecturer:** "What if we want O(1) contains()?"

**You:** "We'd need a different data structure. LinkedList requires sequential search. For O(1) contains(), we'd use:

- **HashSet** (Lab 3) - average O(1) with hash table
- **Binary Search Tree** (Lab 2) - O(log n) if balanced

The trade-off is that these structures have higher memory overhead and different insertion/deletion characteristics."

---

## 📋 Pre-Defence Checklist

### Code Knowledge
- [ ] Can explain every method in LinkedList
- [ ] Can explain every method in Stack implementations
- [ ] Can explain every method in Queue implementations
- [ ] Know all time complexities and WHY
- [ ] Can draw visual diagrams of data structures

### Theory Mastery
- [ ] Understand LIFO vs FIFO deeply
- [ ] Can explain O(1) vs O(1) amortized
- [ ] Know when to use ArrayList vs LinkedList
- [ ] Understand cache locality and memory layout
- [ ] Can explain IS-A vs HAS-A (composition)

### Live Coding Readiness
- [ ] Can implement addFirst/removeLast
- [ ] Can implement contains/indexOf
- [ ] Can implement simple algorithms using Stack/Queue
- [ ] Can modify code quickly without errors

### Communication Skills
- [ ] Can explain concepts with real-world analogies
- [ ] Can draw diagrams to support explanations
- [ ] Can answer "why" questions, not just "what"
- [ ] Can admit when unsure and reason through it

---

## 🔍 Common Mistakes to Avoid

### 1. ❌ "ArrayList is always better because it's faster"
✅ **Correct:** "It depends on the use case. ArrayList has better cache locality, but LinkedList is better when you need frequent insertions/deletions at both ends or guaranteed O(1) operations."

### 2. ❌ "Dequeue is O(1) because I tested it and it was fast"
✅ **Correct:** "ArrayQueue.dequeue() is O(n) due to element shifting, even if it feels fast for small queues. LinkedListQueue.dequeue() is true O(1)."

### 3. ❌ "I used ArrayList in LinkedListStack"
✅ **Correct:** "ArrayStack uses ArrayList. LinkedListStack uses my custom LinkedList implementation."

### 4. ❌ "Iterator.remove() is O(1)"
✅ **Correct:** "Iterator.remove() is O(n) because we must traverse from the head to find the node before the one being removed."

### 5. ❌ Getting confused between stack top and queue front
✅ **Correct:** Stack operates on ONE end (top). Queue operates on TWO ends (front for removal, rear for addition).

---

## 🎯 What the Lecturer is Evaluating

### 1. **Deep Understanding (40%)**
- Not just memorization, but true comprehension
- Ability to explain WHY, not just WHAT
- Understanding trade-offs and design decisions

### 2. **Code Ownership (30%)**
- Can you navigate your code confidently?
- Do you understand every line you wrote?
- Can you defend your implementation choices?

### 3. **Problem-Solving Ability (20%)**
- Can you implement new methods on the spot?
- Can you debug issues quickly?
- Can you apply your knowledge to new problems?

### 4. **Communication Skills (10%)**
- Clear explanations
- Proper technical terminology
- Ability to use diagrams/examples

---

## 🚀 Final Preparation Strategy

### 3 Days Before Defence:
1. **Review all code** - Read every line, understand every decision
2. **Practice time complexity** - Quiz yourself on every operation
3. **Review learning notes** - Reinforce concepts

### 2 Days Before Defence:
1. **Practice live coding** - Implement addFirst, contains, indexOf
2. **Mock interview** - Answer the top 10 questions out loud
3. **Draw diagrams** - Practice visualizing LinkedList, Stack, Queue

### 1 Day Before Defence:
1. **Quick review** - Skim this study report
2. **Mental preparation** - Visualize confident answers
3. **Rest well** - Sharp mind is crucial

### Day of Defence:
1. **Quick warm-up** - Review time complexity cheat sheet
2. **Stay calm** - You know this material
3. **Think before speaking** - It's okay to pause and organize thoughts
4. **Be honest** - If unsure, say so and reason through it

---

## 💪 You've Got This!

**What You've Accomplished:**
- ✅ Implemented complex data structures from scratch
- ✅ Written comprehensive test suites
- ✅ Demonstrated understanding through clean code
- ✅ Created 12+ meaningful commits showing learning progression

**Your Strengths:**
- Strong implementation skills
- Good testing practices
- Clear documentation
- Logical code organization

**Remember:**
- You understand these concepts - you implemented them!
- The lecturer wants you to succeed
- It's okay to take time to think through answers
- Clarity > Speed

---

## 📞 Quick Contact References

**Key Files to Review:**
- `/lab1/L1-assignment-linear-data-strucutres/src/util/LinkedList.java:67` - add(k,e)
- `/lab1/L1-assignment-linear-data-strucutres/src/util/LinkedList.java:175` - remove(k)
- `/lab1/L1-assignment-linear-data-strucutres/src/util/ArrayStack.java:10` - ArrayStack
- `/lab1/L1-assignment-linear-data-strucutres/src/util/LinkedListStack.java:8` - LinkedListStack
- `/lab1/L1-assignment-linear-data-strucutres/src/util/ArrayQueue.java:19` - ArrayQueue
- `/lab1/L1-assignment-linear-data-strucutres/src/util/LinkedListQueue.java` - LinkedListQueue

**Detailed Learning Notes:**
- `/notes/lab1-learning-notes.md` - 514 lines of comprehensive documentation

---

**Good luck with your oral defence! You're well prepared! 🎓**

