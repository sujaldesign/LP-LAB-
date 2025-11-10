import java.util.*;
import java.util.regex.*;

public class PassTwoAssembler {
    public static void main(String[] args) {

        //input
        String[] IC = {
            "(AD,01)(C,100)",
            "(IS,04)(1)(S,1)",
            "(IS,01)(2)(S,2)",
            "(IS,02)(1)(S,3)",
            "(AD,02)"
        };

        // Sample Symbol Table
        Map<Integer, Integer> SYMTAB = new HashMap<>();
        SYMTAB.put(1, 100);
        SYMTAB.put(2, 101);
        SYMTAB.put(3, 102);

        System.out.println("--- PASS II OUTPUT ---");
        System.out.println("Address\tObject Code");

        int LC = 0;

        for (String line : IC) {

            // START (AD,01)
            if (line.startsWith("(AD,01)")) {
                Matcher m = Pattern.compile("\\(C,(\\d+)\\)").matcher(line);
                if (m.find()) LC = Integer.parseInt(m.group(1));
                continue;
            }

            // END (AD,02)
            if (line.startsWith("(AD,02)"))
                break;

            // Imperative Statements (IS)
            if (line.startsWith("(IS")) {
                Matcher m = Pattern.compile("\\(IS,(\\d+)\\)\\((\\d)\\)\\(S,(\\d+)\\)").matcher(line);

                if (m.find()) {
                    int opcode = Integer.parseInt(m.group(1));
                    int reg = Integer.parseInt(m.group(2));
                    int symIndex = Integer.parseInt(m.group(3));
                    int addr = SYMTAB.get(symIndex);

                    System.out.println(LC + "\t" + String.format("%02d %02d %03d", opcode, reg, addr));
                    LC++;
                }
            }
        }
    }
}
