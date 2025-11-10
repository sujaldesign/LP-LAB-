import java.util.*;
import java.util.stream.*;

public class PageReplacementOptimal {

    // Parse input string like "7,0,1,2,0,3,0,4,2,3,0,3,2"
    static List<Integer> parseReferenceString(String s) {
        return Arrays.stream(s.trim().split("[,\\s]+"))
                     .filter(x -> !x.isEmpty())
                     .map(Integer::parseInt)
                     .collect(Collectors.toList());
    }

    // Print a formatted row showing frames and status
    static void printRow(int step, int ref, List<Integer> frames, int frameCount, String status) {
        String[] cols = new String[frameCount];
        for (int i = 0; i < frameCount; i++) {
            if (i < frames.size()) cols[i] = String.valueOf(frames.get(i));
            else cols[i] = "-";
        }
        if (frameCount == 3)
            System.out.printf("%-4d %-8d %-8s %-8s %-8s  %s\n", step, ref, cols[0], cols[1], cols[2], status);
        else {
            System.out.printf("%-4d %-8d", step, ref);
            for (String col : cols) System.out.printf(" %-8s", col);
            System.out.printf("  %s\n", status);
        }
    }

    // FIFO Page Replacement
    static void simulateFIFO(List<Integer> refs, int frameCount) {
        System.out.println("\n========== FIFO (Frames = " + frameCount + ") ==========");
        List<Integer> frames = new ArrayList<>();
        Queue<Integer> q = new LinkedList<>();
        int faults = 0;
        System.out.printf("%-4s %-8s %-8s %-8s %-8s\n", "Step", "Ref", "F1", "F2", "F3");

        int step = 1;
        for (int r : refs) {
            String status;
            if (frames.contains(r)) {
                status = "Hit";
            } else {
                faults++;
                if (frames.size() < frameCount) {
                    frames.add(r);
                    q.add(r);
                } else {
                    int toRemove = q.poll();
                    frames.set(frames.indexOf(toRemove), r);
                    q.add(r);
                }
                status = "Fault";
            }
            printRow(step, r, frames, frameCount, status);
            step++;
        }
        System.out.println("Total Page Faults (FIFO): " + faults);
    }

    // Optimal Page Replacement Algorithm
    static void simulateOptimal(List<Integer> refs, int frameCount) {
        System.out.println("\n========== Optimal (Frames = " + frameCount + ") ==========");
        List<Integer> frames = new ArrayList<>();
        int faults = 0;
        System.out.printf("%-4s %-8s %-8s %-8s %-8s\n", "Step", "Ref", "F1", "F2", "F3");

        int step = 1;
        for (int i = 0; i < refs.size(); i++) {
            int r = refs.get(i);
            String status;
            if (frames.contains(r)) {
                status = "Hit";
            } else {
                faults++;
                if (frames.size() < frameCount) {
                    frames.add(r);
                } else {
                    // find page to replace using optimal lookahead
                    int farthestIndex = -1;
                    int pageToReplace = -1;
                    for (int p : frames) {
                        int nextUse = Integer.MAX_VALUE;
                        for (int j = i + 1; j < refs.size(); j++) {
                            if (refs.get(j) == p) {
                                nextUse = j;
                                break;
                            }
                        }
                        if (nextUse > farthestIndex) {
                            farthestIndex = nextUse;
                            pageToReplace = p;
                        }
                    }
                    int idx = frames.indexOf(pageToReplace);
                    frames.set(idx, r);
                }
                status = "Fault";
            }
            printRow(step, r, frames, frameCount, status);
            step++;
        }
        System.out.println("Total Page Faults (Optimal): " + faults);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Page Replacement Simulation (FIFO & Optimal)");

        System.out.print("Enter reference string (comma or space separated). Press Enter to use sample: ");
        String input = sc.nextLine().trim();
        List<Integer> refs;
        if (input.isEmpty()) {
            refs = Arrays.asList(7,0,1,2,0,3,0,4,2,3,0,3,2);
            System.out.println("Using sample: " + refs);
        } else {
            refs = parseReferenceString(input);
        }

        System.out.print("Enter number of frames (press Enter for 3): ");
        String fline = sc.nextLine().trim();
        int frames = 3;
        if (!fline.isEmpty()) {
            try {
                frames = Integer.parseInt(fline);
            } catch (Exception e) {
                System.out.println("Invalid input. Using 3 frames.");
                frames = 3;
            }
        }

        System.out.println("\nEach step shows: Step | Reference | Frame1 | Frame2 | Frame3 | Status");

        simulateFIFO(refs, frames);
        simulateOptimal(refs, frames);

        sc.close();
    }
}
