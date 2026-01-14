#!/usr/bin/env python3
"""
Generate benchmark graphs for Lab 2 Report
Variant #6: BstSet.remove() vs TreeSet.remove()
Includes both Random and Sorted data comparisons
"""

import matplotlib.pyplot as plt
import numpy as np

# ============================================
# BENCHMARK DATA
# ============================================

input_sizes = [10000, 20000, 40000, 80000]
labels = ['10k', '20k', '40k', '80k']

# Random data results
bst_random = [1028.026, 2432.713, 5837.389, 14232.249]
bst_random_err = [95.886, 116.857, 74.424, 1118.097]
tree_random = [1155.936, 2693.423, 6384.907, 15311.066]
tree_random_err = [60.977, 12.647, 300.402, 652.148]

# Sorted data results (BstSet crashes at 40k+)
bst_sorted = [33.558, 78.995, None, None]  # None = CRASH
bst_sorted_err = [2.109, 79.093, None, None]
tree_sorted = [287.178, 576.595, 1375.981, 6376.627]
tree_sorted_err = [70.372, 35.398, 539.607, 505.395]

# Set up style
plt.style.use('seaborn-v0_8-whitegrid')

# ============================================
# GRAPH 1: Random Data Comparison (Line Graph)
# ============================================
fig1, ax1 = plt.subplots(figsize=(10, 6))

x = np.arange(len(input_sizes))
ax1.errorbar(x, bst_random, yerr=bst_random_err,
             marker='o', markersize=8, linewidth=2, capsize=5,
             label='BstSet.remove()', color='#2ecc71')
ax1.errorbar(x, tree_random, yerr=tree_random_err,
             marker='s', markersize=8, linewidth=2, capsize=5,
             label='TreeSet.remove()', color='#e74c3c')

ax1.set_xlabel('Input Size', fontsize=12)
ax1.set_ylabel('Execution Time (μs)', fontsize=12)
ax1.set_title('Random Data: BstSet vs TreeSet Remove Operation\n(BstSet ~10% faster)',
              fontsize=14, fontweight='bold')
ax1.set_xticks(x)
ax1.set_xticklabels(labels)
ax1.legend(loc='upper left', fontsize=11)
ax1.grid(True, alpha=0.3)

plt.tight_layout()
plt.savefig('benchmark_graph.png', dpi=150, bbox_inches='tight')
print("Saved: benchmark_graph.png (Random Data)")

# ============================================
# GRAPH 2: Bar Chart Comparison (Random Data)
# ============================================
fig2, ax2 = plt.subplots(figsize=(10, 6))

width = 0.35
x_pos = np.arange(len(input_sizes))

bars1 = ax2.bar(x_pos - width/2, bst_random, width,
                yerr=bst_random_err, label='BstSet.remove()',
                color='#2ecc71', capsize=5)
bars2 = ax2.bar(x_pos + width/2, tree_random, width,
                yerr=tree_random_err, label='TreeSet.remove()',
                color='#e74c3c', capsize=5)

ax2.set_xlabel('Input Size', fontsize=12)
ax2.set_ylabel('Execution Time (μs)', fontsize=12)
ax2.set_title('Random Data: BstSet vs TreeSet (Bar Chart)\n(Variant #6)',
              fontsize=14, fontweight='bold')
ax2.set_xticks(x_pos)
ax2.set_xticklabels(labels)
ax2.legend(loc='upper left', fontsize=11)
ax2.grid(True, alpha=0.3, axis='y')

plt.tight_layout()
plt.savefig('benchmark_bar_chart.png', dpi=150, bbox_inches='tight')
print("Saved: benchmark_bar_chart.png (Random Data Bar Chart)")

# ============================================
# GRAPH 3: Sorted Data Comparison (Shows CRASH)
# ============================================
fig3, ax3 = plt.subplots(figsize=(10, 6))

# TreeSet - full line
ax3.errorbar(x, tree_sorted, yerr=tree_sorted_err,
             marker='s', markersize=8, linewidth=2, capsize=5,
             label='TreeSet.remove()', color='#e74c3c')

# BstSet - only first two points, then crash marker
bst_valid = [bst_sorted[0], bst_sorted[1]]
bst_valid_err = [bst_sorted_err[0], bst_sorted_err[1]]
ax3.errorbar([0, 1], bst_valid, yerr=bst_valid_err,
             marker='o', markersize=8, linewidth=2, capsize=5,
             label='BstSet.remove()', color='#2ecc71')

# Add CRASH markers
for i in [2, 3]:
    ax3.scatter(i, tree_sorted[i] * 0.5, s=200, c='#2ecc71', marker='X', zorder=5)
    ax3.annotate('CRASH\nStackOverflow', (i, tree_sorted[i] * 0.5),
                 textcoords="offset points", xytext=(0, -30),
                 ha='center', fontsize=9, color='#c0392b', fontweight='bold')

ax3.set_xlabel('Input Size', fontsize=12)
ax3.set_ylabel('Execution Time (μs)', fontsize=12)
ax3.set_title('Sorted Data: BstSet CRASHES at 40k+ Elements\n(TreeSet remains stable)',
              fontsize=14, fontweight='bold')
ax3.set_xticks(x)
ax3.set_xticklabels(labels)
ax3.legend(loc='upper left', fontsize=11)
ax3.grid(True, alpha=0.3)

plt.tight_layout()
plt.savefig('benchmark_sorted.png', dpi=150, bbox_inches='tight')
print("Saved: benchmark_sorted.png (Sorted Data - Shows Crash)")

# ============================================
# GRAPH 4: Combined Comparison (2x2 subplot)
# ============================================
fig4, axes = plt.subplots(2, 2, figsize=(14, 10))

# Top-left: Random data line
ax = axes[0, 0]
ax.errorbar(x, bst_random, yerr=bst_random_err,
            marker='o', markersize=6, linewidth=2, capsize=4,
            label='BstSet', color='#2ecc71')
ax.errorbar(x, tree_random, yerr=tree_random_err,
            marker='s', markersize=6, linewidth=2, capsize=4,
            label='TreeSet', color='#e74c3c')
ax.set_title('Random Data Performance', fontsize=12, fontweight='bold')
ax.set_xlabel('Input Size')
ax.set_ylabel('Time (μs)')
ax.set_xticks(x)
ax.set_xticklabels(labels)
ax.legend(fontsize=9)
ax.grid(True, alpha=0.3)

# Top-right: Random data bar
ax = axes[0, 1]
ax.bar(x_pos - width/2, bst_random, width, label='BstSet', color='#2ecc71')
ax.bar(x_pos + width/2, tree_random, width, label='TreeSet', color='#e74c3c')
ax.set_title('Random Data: BstSet ~10% Faster', fontsize=12, fontweight='bold')
ax.set_xlabel('Input Size')
ax.set_ylabel('Time (μs)')
ax.set_xticks(x_pos)
ax.set_xticklabels(labels)
ax.legend(fontsize=9)
ax.grid(True, alpha=0.3, axis='y')

# Bottom-left: Sorted data with crash
ax = axes[1, 0]
ax.errorbar(x, tree_sorted, yerr=tree_sorted_err,
            marker='s', markersize=6, linewidth=2, capsize=4,
            label='TreeSet', color='#e74c3c')
ax.errorbar([0, 1], bst_valid, yerr=bst_valid_err,
            marker='o', markersize=6, linewidth=2, capsize=4,
            label='BstSet', color='#2ecc71')
for i in [2, 3]:
    ax.scatter(i, tree_sorted[i] * 0.4, s=150, c='#2ecc71', marker='X', zorder=5)
    ax.annotate('CRASH', (i, tree_sorted[i] * 0.4),
                textcoords="offset points", xytext=(0, -20),
                ha='center', fontsize=8, color='#c0392b', fontweight='bold')
ax.set_title('Sorted Data: BstSet Crashes!', fontsize=12, fontweight='bold')
ax.set_xlabel('Input Size')
ax.set_ylabel('Time (μs)')
ax.set_xticks(x)
ax.set_xticklabels(labels)
ax.legend(fontsize=9)
ax.grid(True, alpha=0.3)

# Bottom-right: Summary table as text
ax = axes[1, 1]
ax.axis('off')
summary_text = """
BENCHMARK SUMMARY
═══════════════════════════════════════

RANDOM DATA:
  • BstSet wins by ~7-11%
  • Both scale as O(n log n)
  • No crashes

SORTED DATA:
  • BstSet CRASHES at 40k+ elements
  • StackOverflowError in addRecursive()
  • TreeSet remains stable

CONCLUSION:
  BstSet: Faster but UNSAFE
  TreeSet: Slightly slower but RELIABLE

  For production: Use TreeSet
"""
ax.text(0.1, 0.9, summary_text, transform=ax.transAxes,
        fontsize=11, verticalalignment='top', fontfamily='monospace',
        bbox=dict(boxstyle='round', facecolor='wheat', alpha=0.5))

plt.suptitle('Variant #6: BstSet.remove() vs TreeSet.remove() - Complete Analysis',
             fontsize=14, fontweight='bold', y=1.02)
plt.tight_layout()
plt.savefig('benchmark_combined.png', dpi=150, bbox_inches='tight')
print("Saved: benchmark_combined.png (Combined Analysis)")

# ============================================
# PRINT SUMMARY
# ============================================
print("\n" + "="*60)
print("BENCHMARK RESULTS SUMMARY")
print("="*60)

print("\nRANDOM DATA:")
print("-" * 40)
for i, size in enumerate(input_sizes):
    speedup = tree_random[i] / bst_random[i]
    print(f"  {size:,} elements: BstSet {speedup:.2f}x faster")

print("\nSORTED DATA:")
print("-" * 40)
for i, size in enumerate(input_sizes):
    if bst_sorted[i] is not None:
        print(f"  {size:,} elements: BstSet {bst_sorted[i]:.1f} μs, TreeSet {tree_sorted[i]:.1f} μs")
    else:
        print(f"  {size:,} elements: BstSet CRASH (StackOverflow), TreeSet {tree_sorted[i]:.1f} μs")

print("="*60)
