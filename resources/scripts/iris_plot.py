import pandas as pd
import matplotlib.pyplot as plt
from itertools import combinations
from sklearn.metrics import silhouette_score
import argparse

# Parse precomputed WCSS from command line
parser = argparse.ArgumentParser()
parser.add_argument('input_csv', help='Path to clustered CSV FILE')
parser.add_argument('--wcss', type=float, required=True, help='Precomputed WCSS value')
args = parser.parse_args()
wcss = args.wcss

# 1) Reading info from CSV
df = pd.read_csv(args.input_csv, sep=',', decimal='.')

features = ['sepal_length','sepal_width','petal_length','petal_width']
df[features] = df[features].apply(pd.to_numeric, errors='raise')
labels = df['cluster'].values

# Compute cluster centroids X (for plotting)
centroids = df.groupby('cluster')[features].mean()

#Additional:
# 2) List of pairs for silhouette‑score
# all 6 ways to peak 2 features from 4
pairs = list(combinations(features, 2))
scores = [] # also just for silhouette_score
for x,y in pairs:
    X = df[[x,y]].values
    scores.append(silhouette_score(X, labels)) # reads how well the clusters are separated in this 2D projection
# peak with maximum resultЬMetrics
best_idx = max(range(len(pairs)), key=lambda i: scores[i])

# 4) Grid
fig, axes = plt.subplots(3, 2, figsize=(10, 12), squeeze=False)
axes = axes.flatten()

#for pair N=1 peak correspond graph and paint all dots
for idx, (x, y) in enumerate(pairs):
    ax = axes[idx] # peak correspond graph
    #cid - cluster number | sub only rows which belongs to cid -> paint dots
    for cid, sub in df.groupby('cluster'):
        ax.scatter(sub[x], sub[y], alpha=0.6, s=40,
                   label=f'Cluster {cid}' if idx == 0 else None)
    # Centroids X
    ax.scatter(centroids[x], centroids[y],
               marker='X', s=100, edgecolor='k',
               label='Centroid' if idx == 0 else None)


    #index's of graphs
    ax.set_xlabel(x.replace('_',' ').title())
    ax.set_ylabel(y.replace('_',' ').title())
    ax.set_title(f'({idx+1}) {x} vs {y}', loc='left', fontsize=10)
    ax.grid(True)

    # check if it best pair => mark as red
    if idx == best_idx:
        #border
        for spine in ax.spines.values():
            spine.set_edgecolor('red')
            spine.set_linewidth(2)

        ax.title.set_color('red')
        ax.title.set_fontweight('bold')

        ax.text(0.95, 0.05,
                f"silhouette = {scores[idx]:.2f}",
                transform=ax.transAxes,
                ha='right', va='bottom',
                color='red', fontsize=9)

# 5) Main title
fig.suptitle(f'Iris Clusters — WCSS = {wcss:.2f}', fontsize=16)
handles, labels = axes[0].get_legend_handles_labels()
fig.legend(handles, labels, loc='lower center',
           ncol=len(handles), bbox_to_anchor=(0.5, 0.02))
plt.tight_layout(rect=[0, 0.05, 1, 0.95])
plt.show()