import json
import matplotlib.pyplot as plt
import numpy as np
import os
from pathlib import Path

def load_metrics(filename):
    # Look for metrics file in metrics directory relative to script location
    script_dir = Path(__file__).parent
    metrics_file = script_dir.parent / 'metrics' / filename
    
    if not metrics_file.exists():
        raise FileNotFoundError(f"Metrics file not found at {metrics_file}")
        
    with open(metrics_file, 'r') as f:
        return json.load(f)

def plot_metrics(metrics):
    workers = list(metrics['networkTimeByWorker'].keys())
    network_times = [metrics['networkTimeByWorker'][w] for w in workers]
    compute_times = [metrics['computeTimeByWorker'][w] for w in workers]
    task_counts = [metrics['taskCountByWorker'][w] for w in workers]

    x = np.arange(len(workers))
    width = 0.35

    fig, (ax1, ax2) = plt.subplots(2, 1, figsize=(10, 10))

    # Time distribution
    ax1.bar(x - width/2, network_times, width, label='Network Time')
    ax1.bar(x + width/2, compute_times, width, label='Compute Time')
    ax1.set_ylabel('Time (ms)')
    ax1.set_title('Time Distribution by Worker')
    ax1.set_xticks(x)
    ax1.set_xticklabels(workers)
    ax1.legend()

    # Tasks per worker
    ax2.bar(x, task_counts)
    ax2.set_ylabel('Number of Tasks')
    ax2.set_title('Tasks Processed by Worker')
    ax2.set_xticks(x)
    ax2.set_xticklabels(workers)

    plt.tight_layout()
    plt.savefig('performance_metrics.png')
    plt.close()

if __name__ == '__main__':
    try:
        metrics = load_metrics('performance_metrics.json')
        plot_metrics(metrics)
        print("Performance graphs generated successfully!")
    except Exception as e:
        print(f"Error generating performance graphs: {e}")