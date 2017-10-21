import Interpreter.Interpreter;

public class MuaInterpreter {
    public static void main(String[] args) {
        Boolean Interactive = false;
        for (String s: args) {
            if (s.equalsIgnoreCase("--interactive") || s.equalsIgnoreCase("-i"))Interactive = true;

        }
        Interpreter interpreter = new Interpreter(Interactive);
        interpreter.run();
    }
}
