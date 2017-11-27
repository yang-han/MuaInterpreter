package Interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;


class Calculator {
    static private HashMap<String, Priority> priorityHashMap;

    static {
        priorityHashMap = new HashMap<>();
        priorityHashMap.put("=", new Priority(0));
        priorityHashMap.put("+", new Priority(1));
        priorityHashMap.put("-", new Priority(1));
        priorityHashMap.put("*", new Priority(2));
        priorityHashMap.put("/", new Priority(2));
        priorityHashMap.put("%", new Priority(2));

    }

    public float cal(String input) {
//		System.out.println(input);
        String tokens = "0+" + input;
        tokens = tokens.replace("+", " + ").replace("-", " - ");
        tokens = tokens.replace("*", " * ").replace("/", " / ");
        tokens = tokens.replace("(", " ( ").replace(")", " ) ");
        tokens = tokens.replace("%", " % ");
        tokens = tokens.replaceAll("\\+(\\s+)-\\s+", "+ -");
        tokens = tokens.replaceAll("-(\\s+)-\\s+", "- -");
        tokens = tokens.replaceAll("\\*(\\s+)-\\s+", "* -");
        tokens = tokens.replaceAll("/(\\s+)-\\s+", "/ -");
        tokens = tokens.replaceAll("%(\\s+)-\\s+", "% -");
        tokens = tokens.replaceAll("\\((\\s+)-\\s+", "( -");
        tokens = tokens.replaceAll("\b(\\s+)-\\s+", "-");
//		System.out.println(tokens);
        Scanner real_scan = new Scanner(tokens);


        ArrayList<String> expression = new ArrayList<>();

        while (real_scan.hasNext()) {
            expression.add(real_scan.next());
        }

        return eval(expression);

    }

    private float eval(ArrayList<String> expression) {

        if (!expression.contains("(")) {
            expression.add("=");
//            System.out.println(expression);
            Stack<String> operators = new Stack<>();
            Stack<String> operands = new Stack<>();
            assert expression.size() % 2 == 0;
            for (int i = 0; i < expression.size(); ++i) {
                String op = expression.get(i);
//                System.out.println(op);
                if (i % 2 == 0) {
                    operands.push(op);
                } else {
                    while (!operators.empty() && priorityHashMap.get(op).priority <= priorityHashMap.get(operators.peek()).priority) {
//                        System.out.println(operands);
//                        System.out.println(operators);
                        String operator = operators.pop();
                        float operand2 = Float.parseFloat(operands.pop());
                        float operand1 = Float.parseFloat(operands.pop());
                        float v = calculate(operand1, operand2, operator);
//                        System.out.println(v);
                        operands.push(String.valueOf(v));
                    }
                    operators.push(op);
                }
            }
            assert operands.size() == 1;
            return Float.parseFloat(operands.pop());
        }

        int bracket_count = 0;
        boolean bracket_flag = false;
        ArrayList<String> bracket = new ArrayList<>();

        ArrayList<String> simplified_expression = new ArrayList<>();


        for (int i = 0; i < expression.size(); ++i) {
            String op = expression.get(i);

            if (op.equals(")")) {
                bracket_count -= 1;
            }
            if (bracket_flag && bracket_count == 0) {
                simplified_expression.add(String.valueOf(eval(bracket)));
                bracket = new ArrayList<>();
                bracket_flag = false;
                continue;
            }
            if (bracket_flag) {
                bracket.add(op);
                continue;
            }
            if (op.equals("(")) {
                bracket_count += 1;
                bracket_flag = true;
                continue;
            }
            simplified_expression.add(op);
        }

//        System.out.println(simplified_expression);
        return eval(simplified_expression);
    }


    private float calculate(float operand1, float operand2, String operator) {
        float val = 0;
        if (operator.equals("+")) {
            val = operand1 + operand2;

        } else if (operator.equals("-")) {
            val = operand1 - operand2;

        } else if (operator.equals("*")) {
            val = operand1 * operand2;

        } else if (operator.equals("/")) {
            val = operand1 / operand2;

        } else if (operator.equals("%")) {
            val = operand1 % operand2;

        }
        return val;
    }

}

class Priority {
    public int priority;

    public Priority(int p) {
        priority = p;
    }
}