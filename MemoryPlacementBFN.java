import java.util.*;

public class MemoryPlacementBFN {

    // ---------- FIRST FIT ----------
    static void firstFit(int blockSize[], int processSize[]) {
        int m = blockSize.length;
        int n = processSize.length;
        int allocation[] = new int[n];
        Arrays.fill(allocation, -1);

        int[] remaining = Arrays.copyOf(blockSize, m);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (remaining[j] >= processSize[i]) {
                    allocation[i] = j;
                    remaining[j] -= processSize[i];
                    break;
                }
            }
        }

        System.out.println("\n===== FIRST FIT =====");
        displayResults(processSize, blockSize, allocation, remaining);
    }

    // ---------- BEST FIT ----------
    static void bestFit(int blockSize[], int processSize[]) {
        int m = blockSize.length;
        int n = processSize.length;
        int allocation[] = new int[n];
        Arrays.fill(allocation, -1);

        int[] remaining = Arrays.copyOf(blockSize, m);

        for (int i = 0; i < n; i++) {
            int bestIdx = -1;
            for (int j = 0; j < m; j++) {
                if (remaining[j] >= processSize[i]) {
                    if (bestIdx == -1 || remaining[j] < remaining[bestIdx]) {
                        bestIdx = j;
                    }
                }
            }
            if (bestIdx != -1) {
                allocation[i] = bestIdx;
                remaining[bestIdx] -= processSize[i];
            }
        }

        System.out.println("\n===== BEST FIT =====");
        displayResults(processSize, blockSize, allocation, remaining);
    }

    // ---------- NEXT FIT ----------
    static void nextFit(int blockSize[], int processSize[]) {
        int m = blockSize.length;
        int n = processSize.length;
        int allocation[] = new int[n];
        Arrays.fill(allocation, -1);

        int[] remaining = Arrays.copyOf(blockSize, m);
        int lastPos = 0;

        for (int i = 0; i < n; i++) {
            int count = 0;
            boolean allocated = false;
            while (count < m) {
                int j = (lastPos + count) % m;
                if (remaining[j] >= processSize[i]) {
                    allocation[i] = j;
                    remaining[j] -= processSize[i];
                    lastPos = j;
                    allocated = true;
                    break;
                }
                count++;
            }
            if (!allocated)
                allocation[i] = -1;
        }

        System.out.println("\n===== NEXT FIT =====");
        displayResults(processSize, blockSize, allocation, remaining);
    }

    // ---------- DISPLAY TABLE ----------
    static void displayResults(int[] processSize, int[] blockSize, int[] allocation, int[] remaining) {
        System.out.printf("%-10s %-20s %-15s %-20s %-20s\n",
                "Process", "Process Size (KB)", "Block No", "Block Size (KB)", "Unused Space (KB)");
        for (int i = 0; i < processSize.length; i++) {
            if (allocation[i] != -1) {
                int blk = allocation[i];
                System.out.printf("%-10s %-20d %-15d %-20d %-20d\n",
                        "P" + (i + 1), processSize[i], blk + 1, blockSize[blk], remaining[blk]);
            } else {
                System.out.printf("%-10s %-20d %-15s %-20s %-20s\n",
                        "P" + (i + 1), processSize[i], "Not Alloc", "-", "-");
            }
        }
    }

    // ---------- MAIN ----------
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of memory blocks: ");
        int m = sc.nextInt();
        int[] blocks = new int[m];
        System.out.println("Enter sizes of memory blocks (KB):");
        for (int i = 0; i < m; i++) {
            blocks[i] = sc.nextInt();
        }

        System.out.print("\nEnter number of processes: ");
        int n = sc.nextInt();
        int[] processes = new int[n];
        System.out.println("Enter sizes of processes (KB):");
        for (int i = 0; i < n; i++) {
            processes[i] = sc.nextInt();
        }

        bestFit(blocks, processes);
        firstFit(blocks, processes);
        nextFit(blocks, processes);

        sc.close();
    }
}

