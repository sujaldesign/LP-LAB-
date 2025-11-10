import java.util.*;

public class MacroPassTwo {
    public static void main(String[] args) {
        // MNT (Macro Name Table)
        Map<String, Integer> MNT = new HashMap<>();
        MNT.put("INCR", 1);

        // MDT (Macro Definition Table)
        Map<Integer, String> MDT = new HashMap<>();
        MDT.put(1, "ADD &A, =1");
        MDT.put(2, "MEND");

        // Intermediate Code
        String[] intermediateCode = {
            "START",
            "INCR DATA",
            "END"
        };

        System.out.println("--- PASS II OUTPUT (Expanded Code) ---");

        for (String line : intermediateCode) {
            String[] parts = line.split("\\s+");

            // Check if the line is a macro call
            if (MNT.containsKey(parts[0])) {
                int mdtIndex = MNT.get(parts[0]);
                String arg = parts.length > 1 ? parts[1] : "";

                // Expand from MDT until MEND
                while (true) {
                    String mdtLine = MDT.get(mdtIndex);
                    if (mdtLine.equalsIgnoreCase("MEND")) break;

                    // Replace parameters (&A) with actual arguments
                    String expandedLine = mdtLine.replace("&A", arg);
                    System.out.println(expandedLine);
                    mdtIndex++;
                }
            } else {
                // Non-macro line printed as-is
                System.out.println(line);
            }
        }
    }
}
