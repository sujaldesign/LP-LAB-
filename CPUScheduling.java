import java.util.*;

class Process {
    String pid;
    int at, bt, wt, tat, priority, ct;
}

public class CPUScheduling {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        Process[] p = new Process[n];
        for (int i = 0; i < n; i++) {
            p[i] = new Process();
            System.out.println("\nEnter details for Process P" + (i + 1));
            p[i].pid = "P" + (i + 1);
            System.out.print("Arrival Time: ");
            p[i].at = sc.nextInt();
            System.out.print("Burst Time: ");
            p[i].bt = sc.nextInt();
            System.out.print("Priority: ");
            p[i].priority = sc.nextInt();
        }

        // ---------- FCFS ----------
        fcfs(p, n);

        // ---------- Priority ----------
        priorityScheduling(p, n);
    }

    static void fcfs(Process[] proc, int n) {
        // Sort by Arrival Time
        Process[] p = Arrays.copyOf(proc, n);
        Arrays.sort(p, Comparator.comparingInt(a -> a.at));

        int time = 0;
        double totalWT = 0, totalTAT = 0;

        for (int i = 0; i < n; i++) {
            if (time < p[i].at)
                time = p[i].at;

            p[i].wt = time - p[i].at;
            p[i].tat = p[i].wt + p[i].bt;
            time += p[i].bt;

            totalWT += p[i].wt;
            totalTAT += p[i].tat;
        }

        System.out.println("\n--- FCFS Scheduling ---");
        System.out.println("Process\tAT\tBT\tWT\tTAT");
        for (Process pr : p) {
            System.out.println(pr.pid + "\t" + pr.at + "\t" + pr.bt + "\t" + pr.wt + "\t" + pr.tat);
        }
        System.out.printf("\nAverage Waiting Time: %.2f", totalWT / n);
        System.out.printf("\nAverage Turnaround Time: %.2f\n", totalTAT / n);
    }

    static void priorityScheduling(Process[] proc, int n) {
        // Copy and sort by Arrival Time
        Process[] p = Arrays.copyOf(proc, n);
        Arrays.sort(p, Comparator.comparingInt(a -> a.at));

        int completed = 0, time = 0;
        double totalWT = 0, totalTAT = 0;
        boolean[] done = new boolean[n];

        System.out.println("\n--- Priority Scheduling (Non-Preemptive) ---");

        while (completed < n) {
            int idx = -1, highestPriority = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if (!done[i] && p[i].at <= time) {
                    if (p[i].priority < highestPriority) {
                        highestPriority = p[i].priority;
                        idx = i;
                    }
                }
            }

            if (idx == -1) {
                time++;
                continue;
            }

            p[idx].wt = time - p[idx].at;
            time += p[idx].bt;
            p[idx].tat = p[idx].wt + p[idx].bt;
            p[idx].ct = time;

            done[idx] = true;
            completed++;
        }

        System.out.println("Process\tPri\tAT\tBT\tWT\tTAT");
        for (Process pr : p) {
            System.out.println(pr.pid + "\t" + pr.priority + "\t" + pr.at + "\t" + pr.bt + "\t" + pr.wt + "\t" + pr.tat);
            totalWT += pr.wt;
            totalTAT += pr.tat;
        }

        System.out.printf("\nAverage Waiting Time: %.2f", totalWT / n);
        System.out.printf("\nAverage Turnaround Time: %.2f\n", totalTAT / n);
    }
}
