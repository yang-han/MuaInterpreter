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
//            for(String s : tokens){
//                System.out.print(s);
//                System.out.println(s.length());
//            }
            if ( tokens.length <= 0 || tokens[0].length() <= 0) {
                System.out.println("blank statement");
                continue;
            }
            Character flag = tokens[0].charAt(0);
            if (Character.isDigit(flag)) {
                System.out.print("Number: ");
                try {
                    System.out.println(get_value_of_num(tokens[0]));
                }catch (java.lang.NumberFormatException e){
                    System.out.println("Unexpected char got. Can't parse number.");
                }
            }
        }
    }

    private static float get_value_of_num(String s) {
        return Float.parseFloat(s);
    }

}
