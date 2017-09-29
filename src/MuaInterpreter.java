import java.util.Scanner;

public class MuaInterpreter {
    private String[] types;

    {
        types = new String[]{"number", "word", "list", "bool"};
    }

    public static void main(String arg[]) {
        Scanner scan = new Scanner(System.in);
        while (true) {
            String PS1 = "MUA> ";
            System.out.print(PS1);
            String program = scan.nextLine();
            if (program.equals("exit")) {
                System.out.println("Bye~");
                break;
            }
            String[] tokens = program.split(" ");
            if (tokens.length <= 0 || tokens[0].length() <= 0) {
                System.out.println("Blank statement.");
                continue;
            }

            for (String token : tokens) {
                if (token.length() <= 0) continue;
                Character mark = token.charAt(0);
                if (token.length() >= 2 && token.substring(0, 2).equals("//")) break;
                if (Character.isDigit(mark) || mark == '-') {
                    System.out.print("Number: ");
                    try {
                        System.out.println(get_value_of_num(token));
                    } catch (java.lang.NumberFormatException e) {
                        System.out.println("Unexpected char got. Can't parse number.");
                    }
                }
            }
        }
    }

    private static float get_value_of_num(String s) {
        return Float.parseFloat(s);
    }

    private static Object getValue(String[]  tokens){
        Integer a = 0;
        return a;
    }

}
