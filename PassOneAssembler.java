import java.util.*;

class PassOneAssembler {
    static class Symbol {
        String name;
        int address;

        Symbol(String name, int address) {
            this.name = name;
            this.address = address;
        }
    }

    public static void main(String[] args) {

        Map<String, String> MOT = new HashMap<>();
        MOT.put("STOP", "(IS,00)");
        MOT.put("ADD", "(IS,01)");
        MOT.put("SUB", "(IS,02)");
        MOT.put("MULT", "(IS,03)");
        MOT.put("MOVER", "(IS,04)");
        MOT.put("MOVEM", "(IS,05)");
        MOT.put("COMP", "(IS,06)");
        MOT.put("BC", "(IS,07)");
        MOT.put("DIV", "(IS,08)");
        MOT.put("READ", "(IS,09)");
        MOT.put("PRINT", "(IS,10)");


        Map<String, String> AD = new HashMap<>();
        AD.put("START", "(AD,01)");
        AD.put("END", "(AD,02)");
        AD.put("ORIGIN", "(AD,03)");
        AD.put("EQU", "(AD,04)");
        AD.put("LTORG", "(AD,05)");


        Map<String, String> DL = new HashMap<>();
        DL.put("DC", "(DL,01)");
        DL.put("DS", "(DL,02)");


        Map<String, String> REG = new HashMap<>();
        REG.put("AREG", "(RG,01)");
        REG.put("BREG", "(RG,02)");
        REG.put("CREG", "(RG,03)");
        REG.put("DREG", "(RG,04)");

        Scanner sc = new Scanner(System.in);
        List<Symbol> symbolTable = new ArrayList<>();
        List<String> intermediateCode = new ArrayList<>();

        System.out.println("Enter Assembly Code (Type 'END' to finish):");
        List<String> code = new ArrayList<>();

        while (true) {
            String line = sc.nextLine().trim();
            code.add(line);
            if (line.equals("END")) break;
        }

        int LC = 0; 
        for (String line : code) {
            String[] parts = line.split("[ ,]+");

            // START
            if (parts[0].equals("START")) {
                LC = Integer.parseInt(parts[1]);
                intermediateCode.add(AD.get("START") + " (C," + LC + ")");
                continue;
            }

            // END
            if (parts[0].equals("END")) {
                intermediateCode.add(AD.get("END"));
                break;
            }


            if (DL.containsKey(parts[1])) {
                String sym = parts[0];
                if (parts[1].equals("DC")) {
                    symbolTable.add(new Symbol(sym, LC));
                    intermediateCode.add(DL.get("DC") + " (C," + parts[2] + ")");
                    LC++;
                } else if (parts[1].equals("DS")) {
                    symbolTable.add(new Symbol(sym, LC));
                    intermediateCode.add(DL.get("DS") + " (C," + parts[2] + ")");
                    LC += Integer.parseInt(parts[2]);
                }
                continue;
            }


            if (MOT.containsKey(parts[0])) {
                StringBuilder ic = new StringBuilder(MOT.get(parts[0]) + " ");

                // Register
                if (REG.containsKey(parts[1])) {
                    ic.append(REG.get(parts[1]) + " ");
                }

                // Operand
                String operand = parts[2];
                if (operand.startsWith("='")) {
                    ic.append("(C," + operand.substring(2, operand.length() - 1) + ")");
                } else {
                    // Check if symbol exists
                    int index = -1;
                    for (int i = 0; i < symbolTable.size(); i++) {
                        if (symbolTable.get(i).name.equals(operand)) {
                            index = i + 1;
                            break;
                        }
                    }
                    if (index == -1) {
                        symbolTable.add(new Symbol(operand, -1)); 
                        index = symbolTable.size();
                    }
                    ic.append("(S," + index + ")");
                }

                intermediateCode.add(ic.toString());
                LC++;
            }
        }


        int addr = 200;
        for (Symbol s : symbolTable) {
            if (s.address == -1)
                s.address = addr++;
        }

        System.out.println("\n--- SYMBOL TABLE ---");
        System.out.println("Symbol\tAddress");
        for (Symbol s : symbolTable) {
            System.out.println(s.name + "\t" + s.address);
        }

        System.out.println("\n--- INTERMEDIATE CODE ---");
        for (String s : intermediateCode) {
            System.out.println(s);
        }
    }
}

//START 200
//MOVER AREG, ='5'
//ADD BREG, ONE
//MOVEM AREG, TEMP
//ONE DC 1
//TEMP DS 1
//END
