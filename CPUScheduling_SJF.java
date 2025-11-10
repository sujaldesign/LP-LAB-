import java.util.*;

class Process {
    String id;
    int arrivalTime, burstTime, remainingTime, completionTime, waitingTime, turnaroundTime;
    Process(String id, int at, int bt) {
        this.id = id;
        this.arrivalTime = at;
        this.burstTime = bt;
        this.remainingTime = bt;
    }
}

public class CPUScheduling_SJF {

    // ===== SJF Preemptive =====
    static void sjfPreemptive(List<Process> processes) {
        int n = processes.size(), completed = 0, currentTime = 0;
        double totalWT = 0, totalTAT = 0;
        Process shortest = null;

        while (completed < n) {
            shortest = null;
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && p.remainingTime > 0) {
                    if (shortest == null || p.remainingTime < shortest.remainingTime) {
                        shortest = p;
                    }
                }
            }

            if (shortest == null) {
                currentTime++;
                continue;
            }

            shortest.remainingTime--;
            currentTime++;

            if (shortest.remainingTime == 0) {
                completed++;
                shortest.completionTime = currentTime;
                shortest.turnaroundTime = shortest.completionTime - shortest.arrivalTime;
                shortest.waitingTime = shortest.turnaroundTime - shortest.burstTime;
                totalWT += shortest.waitingTime;
                totalTAT += shortest.turnaroundTime;
            }
        }

        System.out.println("\n--- SJF (Preemptive) Scheduling ---");
        displayResults(processes, totalWT, totalTAT);
    }

    static void displayResults(List<Process> processes, double totalWT, double totalTAT) {
        System.out.printf("%-8s %-12s %-10s %-10s %-10s %-10s\n", 
            "Process", "Arrival", "Burst", "Waiting", "Turnaround", "Completion");
        for (Process p : processes) {
            System.out.printf("%-8s %-12d %-10d %-10d %-10d %-10d\n", 
                p.id, p.arrivalTime, p.burstTime, p.waitingTime, p.turnaroundTime, p.completionTime);
        }
        System.out.printf("\nAverage Waiting Time: %.2f", totalWT / processes.size());
        System.out.printf("\nAverage Turnaround Time: %.2f\n", totalTAT / processes.size());
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        List<Process> processes = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            System.out.println("\nEnter details for Process " + (i + 1) + ":");
            System.out.print("Arrival Time: ");
            int at = sc.nextInt();
            System.out.print("Burst Time: ");
            int bt = sc.nextInt();
            processes.add(new Process("P" + (i + 1), at, bt));
        }

        sjfPreemptive(processes);
        sc.close();
    }
}
