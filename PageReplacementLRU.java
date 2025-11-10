import java.util.*;
import java.util.stream.*;

public class PageReplacementLRU {

    // Parse input like "7,0,1,2" or "7 0 1 2"
    static List<Integer> parseReferenceString(String s) {
        return Arrays.stream(s.trim().split("[,\\s]+"))
                     .filter(x -> !x.isEmpty())
                     .map(Integer::parseInt)
                     .collect(Collectors.toList());
    }

    // FIFO simulation
    static void simulateFIFO(List<Integer> refs, int frames) {
        System.out.println("\n========== FIFO (Frames = " + frames + ") ==========");
        List<Integer> frameList = new ArrayList<>();
        Queue<Integer> q = new LinkedList<>();
        int faults = 0;
        System.out.printf("%-4s %-8s %-8s %-8s %-8s\n", "Step", "Ref", "F1", "F2", "F3");
        int step = 1;
        for (int r : refs) {
            String status;
            if (frameList.contains(r)) {
                status = "Hit";
            } else {
                faults++;
                if (frameList.size() < frames) {
                    frameList.add(r);
                    q.add(r);
                } else {
                    int toRemove = q.poll();
                    int idx = frameList.indexOf(toRemove);
                    frameList.set(idx, r);
                    q.add(r);
                }
                status = "Fault";
            }
            // print row: show up to frames (if frames != 3 prints more/less columns)
            printFramesRow(step, r, frameList, frames, status);
            step++;
        }
        System.out.println("Total Page Faults (FIFO): " + faults);
    }

    // LRU simulation (using last-used timestamps)
    static void simulateLRU(List<Integer> refs, int frames) {
        System.out.println("\n========== LRU (Frames = " + frames + ") ==========");
        List<Integer> frameList = new ArrayList<>();
        Map<Integer, Integer> lastUsed = new HashMap<>(); // page -> time
        int time = 0;
        int faults = 0;
        System.out.printf("%-4s %-8s %-8s %-8s %-8s\n", "Step", "Ref", "F1", "F2", "F3");
        int step = 1;
        for (int r : refs) {
            time++;
            String status;
            if (frameList.contains(r)) {
                status = "Hit";
                lastUsed.put(r, time);
            } else {
                faults++;
                if (frameList.size() < frames) {
                    frameList.add(r);
                    lastUsed.put(r, time);
                } else {
                    // find LRU page among current frames
                    int lruPage = frameList.get(0);
                    int lruTime = lastUsed.getOrDefault(lruPage, 0);
                    for (int p : frameList) {
                        int t = lastUsed.getOrDefault(p, 0);
                        if (t < lruTime) {
                            lruTime = t;
                            lruPage = p;
                        }
                    }
                    int idx = frameList.indexOf(lruPage);
                    frameList.set(idx, r);
                    lastUsed.remove(lruPage);
                    lastUsed.put(r, time);
                }
                status = "Fault";
            }
            printFramesRow(step, r, frameList, frames, status);
            step++;
        }
        System.out.println("Total Page Faults (LRU): " + faults);
    }

    // Helper to print a single row of frames (adjusts to frames count)
    static void printFramesRow(int step, int ref, List<Integer> framesList, int totalFrames, String status) {
        // Build frame columns up to totalFrames
        String[] cols = new String[totalFrames];
        for (int i = 0; i < totalFrames; i++) {
            if (i < framesList.size()) cols[i] = String.valueOf(framesList.get(i));
            else cols[i] = "-";
        }
        // If totalFrames > 3, print limited default columns (but program formats generically)
        // For consistent output with problem (3 frames), print 3 columns F1-F3
        if (totalFrames == 3) {
            System.out.printf("%-4d %-8d %-8s %-8s %-8s  %s\n",
                    step, ref, cols[0], cols[1], cols[2], status);
        } else {
            // Generic printing: print Step Ref then frames
            System.out.printf("%-4d %-8d", step, ref);
            for (int i = 0; i < totalFrames; i++) {
                System.out.printf(" %-8s", cols[i]);
            }
            System.out.printf("  %s\n", status);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Page Replacement Simulation (FIFO & LRU)");
        System.out.print("Enter reference string (comma or space separated). Press Enter to use sample: ");
        String line = sc.nextLine().trim();
        List<Integer> refs;
        if (line.isEmpty()) {
            refs = Arrays.asList(7,0,1,2,0,3,0,4,2,3,0,3,2);
            System.out.println("Using sample: " + refs);
        } else {
            try {
                refs = parseReferenceString(line);
            } catch (Exception e) {
                System.out.println("Invalid input. Exiting.");
                sc.close();
                return;
            }
        }

        System.out.print("Enter number of frames (press Enter for 3): ");
        String fline = sc.nextLine().trim();
        int frames = 3;
        if (!fline.isEmpty()) {
            try {
                frames = Integer.parseInt(fline);
                if (frames <= 0) throw new NumberFormatException();
            } catch (Exception e) {
                System.out.println("Invalid frames. Using default = 3.");
                frames = 3;
            }
        } else {
            System.out.println("Using default frames = 3");
        }

        // Force headers for 3 frames output format required by problem
        System.out.println("\nNote: Each row shows Step, Reference, Frame1, Frame2, Frame3, and Hit/Fault");
        // FIFO
        simulateFIFO(refs, frames);
        // LRU
        simulateLRU(refs, frames);

        sc.close();
    }
}

//7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2