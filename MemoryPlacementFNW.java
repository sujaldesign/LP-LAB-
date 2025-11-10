import java.util.*;

public class MemoryPlacementFNW {
    
    static class Block {
        int size, id;
        boolean allocated = false;
        Block(int id, int size) {
            this.id = id;
            this.size = size;
        }
    }

    static void displayResult(String title, int[] process, int[] allocation, int[] blockSizeBefore, int[] blockSizeAfter) {
        System.out.println("\n===============================");
        System.out.println(title + " Allocation Result");
        System.out.println("===============================");
        System.out.printf("%-10s %-15s %-10s %-15s %-15s\n", "Process", "Process Size(KB)", "Block No", "Block Size(KB)", "Unused Space(KB)");
        for (int i = 0; i < process.length; i++) {
            if (allocation[i] != -1) {
                int unused = blockSizeAfter[allocation[i]] - process[i];
                System.out.printf("P%-9d %-15d %-10d %-15d %-15d\n", i + 1, process[i], allocation[i] + 1, blockSizeBefore[allocation[i]], unused);
            } else {
                System.out.printf("P%-9d %-15d %-10s %-15s %-15s\n", i + 1, process[i], "Not Alloc", "-", "-");
            }
        }
    }

    static void firstFit(int[] block, int[] process) {
        int[] allocation = new int[process.length];
        int[] blockCopy = block.clone();
        int[] blockSizeBefore = block.clone();
        Arrays.fill(allocation, -1);

        for (int i = 0; i < process.length; i++) {
            for (int j = 0; j < blockCopy.length; j++) {
                if (blockCopy[j] >= process[i]) {
                    allocation[i] = j;
                    blockCopy[j] -= process[i];
                    break;
                }
            }
        }
        displayResult("First Fit", process, allocation, blockSizeBefore, blockCopy);
    }

    static void nextFit(int[] block, int[] process) {
        int[] allocation = new int[process.length];
        int[] blockCopy = block.clone();
        int[] blockSizeBefore = block.clone();
        Arrays.fill(allocation, -1);

        int j = 0; // start position for next fit
        for (int i = 0; i < process.length; i++) {
            int count = 0;
            while (count < blockCopy.length) {
                if (blockCopy[j] >= process[i]) {
                    allocation[i] = j;
                    blockCopy[j] -= process[i];
                    break;
                }
                j = (j + 1) % blockCopy.length;
                count++;
            }
        }
        displayResult("Next Fit", process, allocation, blockSizeBefore, blockCopy);
    }

    static void worstFit(int[] block, int[] process) {
        int[] allocation = new int[process.length];
        int[] blockCopy = block.clone();
        int[] blockSizeBefore = block.clone();
        Arrays.fill(allocation, -1);

        for (int i = 0; i < process.length; i++) {
            int worstIdx = -1;
            for (int j = 0; j < blockCopy.length; j++) {
                if (blockCopy[j] >= process[i]) {
                    if (worstIdx == -1 || blockCopy[j] > blockCopy[worstIdx]) {
                        worstIdx = j;
                    }
                }
            }
            if (worstIdx != -1) {
                allocation[i] = worstIdx;
                blockCopy[worstIdx] -= process[i];
            }
        }
        displayResult("Worst Fit", process, allocation, blockSizeBefore, blockCopy);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter number of memory blocks: ");
        int m = sc.nextInt();
        int[] blocks = new int[m];
        System.out.println("Enter block sizes (KB):");
        for (int i = 0; i < m; i++) {
            System.out.print("Block " + (i + 1) + ": ");
            blocks[i] = sc.nextInt();
        }

        System.out.print("\nEnter number of processes: ");
        int n = sc.nextInt();
        int[] process = new int[n];
        System.out.println("Enter process sizes (KB):");
        for (int i = 0; i < n; i++) {
            System.out.print("Process " + (i + 1) + ": ");
            process[i] = sc.nextInt();
        }

        firstFit(blocks, process);
        nextFit(blocks, process);
        worstFit(blocks, process);
        
        sc.close();
    }
}
