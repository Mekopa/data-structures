# Components of the L1 Assessment

## L1 Intro — 2 points
1. Familiarize yourself with the programming code in the `l1-intro` project.
2. Implement and test the unimplemented methods of the `l1-intro` project.

---

## L1 Mandatory Part (6 points)
1. Familiarize yourself with the programming code in the `l1-assignment-linear-data-structures` project.
2. Implement and test the unimplemented methods of the `l1-assignment-linear-data-structures` project. (2 points)

3. Create a `Stack<E>` interface that has the following abstract methods:
   - `E pop()` — Deletes and returns the top element (O(1))
   - `void push(E item)` — Inserts a new element at the top (O(1) amortized)
   - `E peak()` — Returns the top element (O(1))
   - `boolean isEmpty()` — Checks if the stack is empty (O(1))

4. Create `ArrayStack<E>` and `LinkedListStack<E>` classes implementing `Stack<E>`.
   - `ArrayStack<E>` implements the stack using an array (`ArrayList<E>`)
   - `LinkedListStack<E>` implements it using a linked list (`LinkedList<E>` or `Generic_Lists`)
   - Test both. (2 points)

5. Create a `Queue<E>` interface with the following methods:
   - `void enqueue(E item)` — Adds a new element to the end (O(1) amortized)
   - `E dequeue()` — Deletes and returns the first element (O(1) amortized)
   - `E peak()` — Returns the first element (O(1))
   - `boolean isEmpty()` — Checks if the queue is empty (O(1))

6. Create `ArrayQueue<E>` and `LinkedListQueue<E>` implementing `Queue<E>`.
   - `ArrayQueue<E>` uses an array (`ArrayList<E>`)
   - `LinkedListQueue<E>` uses a linked list (`LinkedList<E>` or `GenericList`)
   - Test both. (2 points)

---

## L1 Optional Part (2 points)

### Task 1 — Balanced Brackets (1 point)
We have a string of length N (0 < N ≤ 1000) consisting of `{`, `[`, `(`, `]`, `)`, `}`.  
Check if the brackets are **balanced**.

#### Conditions
- Open brackets must be closed by the same type of bracket.
- Open brackets must be closed in the correct order.
- Every close bracket has a corresponding open bracket.

#### Examples
```
"{(})" → Unbalanced
"[" → Unbalanced
"{()}{[]}" → Balanced
```

The solution must return a boolean denoting whether brackets are balanced or not. The computational
complexity of the algorithm must be O(n).


| Input        | Output   |
|--------------|----------|
| `}`          | False    |
| `{()}{[]}`   | True     |
| `[{}}`       | False    |
| `{()[{}]}{}` | True     |
| `{(})`       | False    |
| `([(]{)})`   | False    |

Computational complexity: **O(n)**

---

### Task 2 — Sliding Window Maximum (1 point)
We have a sequence `a1, a2, …, an` and window size `m` (1 ≤ m ≤ n).  
Find the maximum element in each sliding window.

Example (`m=4`):

Input: [2, 7, 3, 1, 5, 2, 6, 2]
Output: [7, 7, 5, 6, 6]

Your task is to implement an algorithm, which calculates maximum element of each subsequence bounded by
sliding window. The naive solution of this problem is O(nm). You need to implement an algorithm that
ensures a computational complexity of O(n).

For:

k = 1 → [2, 7, 3, 1, 5, 2, 6, 2]
k = 8 → [7]


Complexity: **O(n)**

---

## L1 Assessment — Practical and Theoretical Part

During the assessment, lecturers may ask students to complete or explain the following.

### Possible Practical Exercises (Mandatory Part)
Implement one or more of:
- `void add(int index, E element)`
- `boolean addAll(LinkedList<? extends E> c)`
- `boolean addAll(int index, LinkedList<? extends E> c)`
- `boolean contains(Object o)`
- `boolean containsAll(LinkedList<?> c)`
- `boolean equals(Object o)`
- `int indexOf(Object o)`
- `int lastIndexOf(Object o)`
- `E remove(int index)`
- `boolean remove(Object o)`
- `boolean removeAll(LinkedList<?> c)`
- `boolean retainAll(LinkedList<?> c)`
- `E set(int index, E element)`
- `List<E> subList(int fromIndex, int toIndex)`
- `void addFirst(E e)`
- `void addLast(E e)`
- `E removeFirst()`
- `boolean removeFirstOccurrence(Object o)`
- `E removeLast()`
- `boolean removeLastOccurrence(Object o)`
- `void removeRange(int fromIndex, int toIndex)`

#### Reference
- http://docs.oracle.com/javase/8/docs/api/java/util/List.html

---

### Possible Practical Exercises (Optional Part)
1. Determine the maximum depth of the bracket pairs (Task 1).  
2. Count the number of different pairs of parentheses (Task 1).  
3. Find the minimum element of each subsequence bounded by the sliding window (Task 2).

---

### Theoretical Questions
1. Computational complexity of the implemented operations.  
2. Advantages and disadvantages of array-based vs linked list-based stack implementation.  
3. Advantages and disadvantages of array-based vs linked list-based queue implementation.  
4. Explanation of various code nuances.




