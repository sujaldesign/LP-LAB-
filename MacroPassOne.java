import java.util.*;

public class MacroPassOne {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<String> code = new ArrayList<>();
        System.out.println("Enter the program (type END to finish):");

        // Read input
        while (true) {
            String line = sc.nextLine().trim();
            code.add(line);
            if (line.equals("END")) break;
        }

        List<String> MDT = new ArrayList<>();
        Map<String, Integer> MNT = new LinkedHashMap<>();
        List<String> intermediate = new ArrayList<>();s

        boolean inMacroDef = false;
        String currentMacro = "";
        int mdtIndex = 1;

        for (int i = 0; i < code.size(); i++) {
            String line = code.get(i);

            if (line.equalsIgnoreCase("MACRO")) {
                inMacroDef = true;
                continue;
            }

            if (inMacroDef) {
                // Macro header line
                if (currentMacro.isEmpty()) {
                    String[] parts = line.split("\\s+");
                    currentMacro = parts[0];
                    MNT.put(currentMacro, mdtIndex);
                    continue;
                }

                // Macro body lines
                if (line.equalsIgnoreCase("MEND")) {
                    MDT.add("MEND");
                    inMacroDef = false;
                    currentMacro = "";
                    mdtIndex = MDT.size() + 1;
                } else {
                    MDT.add(line);
                }
            } else {
                // Normal program line → goes to Intermediate Code
                intermediate.add(line);
            }
        }

        // Print Results
        System.out.println("\n--- MACRO NAME TABLE (MNT) ---");
        System.out.println("Macro Name\tMDT Index");
        for (Map.Entry<String, Integer> e : MNT.entrySet()) {
            System.out.println(e.getKey() + "\t\t" + e.getValue());
        }

        System.out.println("\n--- MACRO DEFINITION TABLE (MDT) ---");
        System.out.println("Index\tDefinition");
        for (int i = 0; i < MDT.size(); i++) {
            System.out.println((i + 1) + "\t" + MDT.get(i));
        }

        System.out.println("\n--- INTERMEDIATE CODE ---");
        for (String line : intermediate) {
            System.out.println(line);
        }
    }
}

//MACRO
//INCR &A
//ADD &A, =1
//MEND
//START
//INCR DATA
//END