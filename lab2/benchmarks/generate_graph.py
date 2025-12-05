#!/usr/bin/env python3
"""
Generate benchmark graphs for Lab 2 Report
Variant #6: BstSet.remove() vs TreeSet.remove()
"""

import matplotlib.pyplot as plt
import pandas as pd
import numpy as np

# Benchmark data
data = {
    'Input Size': [10000, 20000, 40000, 80000],
    'BstSet.remove()': [1028.026, 2432.713, 5837.389, 14232.249],
    'BstSet Error': [95.886, 116.857, 74.424, 1118.097],
    'TreeSet.remove()': [1155.936, 2693.423, 6384.907, 15311.066],
    'TreeSet Error': [60.977, 12.647, 300.402, 652.148]
}

df = pd.DataFrame(data)

# Set up the plot style
plt.style.use('seaborn-v0_8-whitegrid')
fig, ax = plt.subplots(figsize=(10, 6))

# Plot lines with error bars
x = df['Input Size']
ax.errorbar(x, df['BstSet.remove()'], yerr=df['BstSet Error'],
            marker='o', markersize=8, linewidth=2, capsize=5,
            label='BstSet.remove()', color='#2ecc71')
ax.errorbar(x, df['TreeSet.remove()'], yerr=df['TreeSet Error'],
            marker='s', markersize=8, linewidth=2, capsize=5,
            label='TreeSet.remove()', color='#e74c3c')

# Labels and title
ax.set_xlabel('Input Size (number of elements)', fontsize=12)
ax.set_ylabel('Execution Time (microseconds)', fontsize=12)
ax.set_title('Benchmark: BstSet.remove() vs TreeSet.remove()\n(Variant #6)', fontsize=14, fontweight='bold')

# Format x-axis
ax.set_xticks(x)
ax.set_xticklabels(['10,000', '20,000', '40,000', '80,000'])

# Legend
ax.legend(loc='upper left', fontsize=11)

# Grid
ax.grid(True, alpha=0.3)

# Tight layout
plt.tight_layout()

# Save the figure
plt.savefig('benchmark_graph.png', dpi=150, bbox_inches='tight')
plt.savefig('benchmark_graph.pdf', bbox_inches='tight')
print("Saved: benchmark_graph.png and benchmark_graph.pdf")

# Also create a bar chart comparison
fig2, ax2 = plt.subplots(figsize=(10, 6))

x_pos = np.arange(len(df['Input Size']))
width = 0.35

bars1 = ax2.bar(x_pos - width/2, df['BstSet.remove()'], width,
                yerr=df['BstSet Error'], label='BstSet.remove()',
                color='#2ecc71', capsize=5)
bars2 = ax2.bar(x_pos + width/2, df['TreeSet.remove()'], width,
                yerr=df['TreeSet Error'], label='TreeSet.remove()',
                color='#e74c3c', capsize=5)

ax2.set_xlabel('Input Size (number of elements)', fontsize=12)
ax2.set_ylabel('Execution Time (microseconds)', fontsize=12)
ax2.set_title('Benchmark Comparison: BstSet vs TreeSet Remove Operation\n(Variant #6)', fontsize=14, fontweight='bold')
ax2.set_xticks(x_pos)
ax2.set_xticklabels(['10,000', '20,000', '40,000', '80,000'])
ax2.legend(loc='upper left', fontsize=11)
ax2.grid(True, alpha=0.3, axis='y')

plt.tight_layout()
plt.savefig('benchmark_bar_chart.png', dpi=150, bbox_inches='tight')
plt.savefig('benchmark_bar_chart.pdf', bbox_inches='tight')
print("Saved: benchmark_bar_chart.png and benchmark_bar_chart.pdf")

# Print summary table
print("\n" + "="*60)
print("BENCHMARK RESULTS SUMMARY")
print("="*60)
print(df.to_string(index=False))
print("="*60)

# Calculate speedup
df['Speedup'] = df['TreeSet.remove()'] / df['BstSet.remove()']
print("\nBstSet speedup over TreeSet:")
for i, row in df.iterrows():
    print(f"  {int(row['Input Size']):,} elements: {row['Speedup']:.2f}x faster")
