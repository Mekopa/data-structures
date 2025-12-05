#!/usr/bin/env python3
"""
Generate benchmark graphs for Lab 3 Report
Variant #6: HashMap.remove() vs java.util.HashMap.remove()
"""

import matplotlib.pyplot as plt
import pandas as pd
import numpy as np

# Benchmark data
data = {
    'Input Size': [10000, 20000, 40000, 80000],
    'HashMap.remove()': [164.703, 612.357, 974.472, 1662.817],
    'HashMap Error': [159.832, 299.610, 1321.127, 1563.213],
    'java.util.HashMap.remove()': [99.630, 223.154, 483.432, 1058.088],
    'Java Error': [25.210, 126.543, 313.546, 889.833]
}

df = pd.DataFrame(data)

# Set up the plot style
plt.style.use('seaborn-v0_8-whitegrid')
fig, ax = plt.subplots(figsize=(10, 6))

# Plot lines with error bars
x = df['Input Size']
ax.errorbar(x, df['HashMap.remove()'], yerr=df['HashMap Error'],
            marker='o', markersize=8, linewidth=2, capsize=5,
            label='HashMap.remove()', color='#2ecc71')
ax.errorbar(x, df['java.util.HashMap.remove()'], yerr=df['Java Error'],
            marker='s', markersize=8, linewidth=2, capsize=5,
            label='java.util.HashMap.remove()', color='#e74c3c')

# Labels and title
ax.set_xlabel('Input Size (number of elements)', fontsize=12)
ax.set_ylabel('Execution Time (microseconds)', fontsize=12)
ax.set_title('Benchmark: HashMap.remove() vs java.util.HashMap.remove()\n(Variant #6)', fontsize=14, fontweight='bold')

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

bars1 = ax2.bar(x_pos - width/2, df['HashMap.remove()'], width,
                yerr=df['HashMap Error'], label='HashMap.remove()',
                color='#2ecc71', capsize=5)
bars2 = ax2.bar(x_pos + width/2, df['java.util.HashMap.remove()'], width,
                yerr=df['Java Error'], label='java.util.HashMap.remove()',
                color='#e74c3c', capsize=5)

ax2.set_xlabel('Input Size (number of elements)', fontsize=12)
ax2.set_ylabel('Execution Time (microseconds)', fontsize=12)
ax2.set_title('Benchmark: HashMap.remove() vs java.util.HashMap.remove()\n(Variant #6)', fontsize=14, fontweight='bold')
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
print("\njava.util.HashMap speedup over custom HashMap:")
for i, row in df.iterrows():
    custom = row['HashMap.remove()']
    java = row['java.util.HashMap.remove()']
    speedup = custom / java
    print(f"  {int(row['Input Size']):,} elements: {speedup:.2f}x faster")
