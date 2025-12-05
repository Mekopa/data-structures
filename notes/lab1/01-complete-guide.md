# Lab 1: Linear Data Structures - Complete Guide

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

---

### 2. Stack (LIFO - Last-In-First-Out)

**Interface Methods:**
- `void push(E item)` - Add element to top - O(1) amortized
- `E pop()` - Remove and return top element - O(1)
- `E peek()` - View top element without removing - O(1)
- `boolean isEmpty()` - Check if stack is empty - O(1)

#### ArrayStack vs LinkedListStack

| Aspect | ArrayStack | LinkedListStack |
|--------|------------|-----------------|
| **Push/Pop** | O(1) amortized | O(1) true |
| **Memory** | Contiguous, good cache | Scattered, poor cache |
| **Overhead** | Wasted capacity | Pointer per element |
| **Best for** | Predictable size | Unknown size |

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

#### ArrayQueue vs LinkedListQueue

| Aspect | ArrayQueue | LinkedListQueue |
|--------|------------|-----------------|
| **Enqueue** | O(1) amortized | O(1) true |
| **Dequeue** | O(n) - shifts! | O(1) - pointer |
| **Best for** | Small queues | Any size queue |

**Why LinkedList is Better for Queue:**
LinkedList can efficiently operate on both ends (head and tail) with O(1) pointer manipulation, while ArrayList can only efficiently operate on one end.

**Real-World Applications:**
- Task scheduling (print queue, CPU scheduler)
- Breadth-first search (BFS) in graphs
- Buffer management (keyboard input, network packets)
- Request handling in web servers

---

## Complexity Summary

| Data Structure | Operation | Time | Space |
|----------------|-----------|------|-------|
| **LinkedList** | add(e) | O(1) | O(1) |
| | add(k,e) | O(k) | O(1) |
| | remove(k) | O(k) | O(1) |
| | get(k) | O(k) | O(1) |
| **ArrayStack** | push/pop/peek | O(1) | O(1) |
| **LinkedListStack** | push/pop/peek | O(1) | O(1) |
| **ArrayQueue** | enqueue | O(1) | O(1) |
| | dequeue | O(n) | O(1) |
| **LinkedListQueue** | enqueue/dequeue | O(1) | O(1) |

---

## Key Design Patterns

### 1. Interface-Based Design
- Created `List<E>`, `Stack<E>`, and `Queue<E>` interfaces
- Multiple implementations for each interface
- Enables polymorphism and flexible code

### 2. Composition (HAS-A)
- `ArrayStack` **has-a** `ArrayList<E>`
- `LinkedListStack` **has-a** `LinkedList<E>`
- Preferred over inheritance for flexibility

### 3. Generic Programming
- All structures use type parameter `<E>`
- Type safety without code duplication

---

## Defence Quick Reference

### Common Questions

**Q: "What's the time complexity of dequeue() in ArrayQueue and why?"**
A: O(n) because ArrayList.remove(0) shifts all elements

**Q: "Why is LinkedList better for Queue than ArrayList?"**
A: LinkedList can do O(1) operations on both ends (head and tail)

**Q: "What's the difference between O(1) and O(1) amortized?"**
A: O(1): Every operation is constant time. O(1) amortized: Average is constant, but occasional operation may be expensive (like ArrayList resize)

**Q: "When would you use Stack vs Queue?"**
A: Stack for DFS, undo/redo, backtracking. Queue for BFS, task scheduling, buffering.

### Mental Models
- **Stack** = Stack of plates (add/remove from top only)
- **Queue** = Line at a store (add to back, serve from front)

---

**Grade:** 8/10 (Mandatory complete)
