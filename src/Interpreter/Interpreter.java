package Interpreter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;

class MuaTypeException extends RuntimeException {

}

abstract class Value {

    @Override
    abstract public String toString();

    abstract public String getString() throws MuaTypeException;

    abstract public Float getFloat() throws MuaTypeException;

    abstract public Boolean getBoolean() throws MuaTypeException;
}

class _Word extends Value {
    private String value;

    public _Word set(String v) {
        value = v;
        return this;
    }

    public String getString() {
        return value;
    }

    public Float getFloat() throws MuaTypeException {
        throw new MuaTypeException();
    }

    public Boolean getBoolean() throws MuaTypeException {
        throw new MuaTypeException();
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}

class _Number extends Value {
    private float value;

    public _Number set(float v) {
        value = v;
        return this;
    }

    public String getString() throws MuaTypeException {
        throw new MuaTypeException();
    }

    public Float getFloat() {
        return value;
    }

    public Boolean getBoolean() throws MuaTypeException {
        throw new MuaTypeException();
    }


    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

class _List extends Value {
    public ArrayList<Value> value = new ArrayList<>();

    public void append(Value v) {
        value.add(v);
    }

    public String getString() throws MuaTypeException {
        throw new MuaTypeException();
    }

    public Float getFloat() throws MuaTypeException {
        throw new MuaTypeException();
    }

    public Boolean getBoolean() throws MuaTypeException {
        throw new MuaTypeException();

    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[ ");
        for (Value v : value) {
            result.append(v.toString()).append(" ");
        }
        result.append(" ]");
        return result.toString();
    }

}

class _Bool extends Value {
    private Boolean value;

    public _Bool set(Boolean v) {
        value = v;
        return this;
    }

    public String getString() throws MuaTypeException {
        throw new MuaTypeException();
    }

    public Float getFloat() throws MuaTypeException {
        throw new MuaTypeException();
    }

    public Boolean getBoolean() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}

interface Calculate {
    Value run();
}


public class Interpreter {
    private Scanner LineScan = new Scanner(System.in);
    private Scanner scan;
    private Calculator calculator = new Calculator();
    private Value return_val = null;
    private HashMap<String, Value> dict = new HashMap<>();
    private HashMap<String, Calculate> method = new HashMap<>();
    private ScriptEngineManager manager = new ScriptEngineManager();
    private ScriptEngine engine = manager.getEngineByName("javascript");
    private ArrayList operation = new ArrayList<String>() {{
        add("make");
        add("print");
        add("erase");
        add("run");
        add("repeat");
    }};

    private String PS1 = "Mua> ";
    private String PS2 = "> ";

    public Interpreter(Boolean Interactive) {
        if (!Interactive) {
            PS1 = "";
            PS2 = "";
        } else {
            PS1 = "Mua> ";
            PS2 = "> ";
        }

        method.put("add", new Calculate() {
            @Override
            public _Number run() {
                _Number result = new _Number();
                result.set(getValue().getFloat() + getValue().getFloat());
                return result;
            }
        });

        method.put("sub", new Calculate() {
            @Override
            public _Number run() {
                _Number result = new _Number();
                result.set(getValue().getFloat() - getValue().getFloat());
                return result;
            }
        });

        method.put("mul", new Calculate() {
            @Override
            public _Number run() {
                _Number result = new _Number();
                result.set(getValue().getFloat() * getValue().getFloat());
                return result;
            }
        });

        method.put("div", new Calculate() {
            @Override
            public _Number run() {
                _Number result = new _Number();
                result.set(getValue().getFloat() / getValue().getFloat());
                return result;
            }
        });

        method.put("mod", new Calculate() {
            @Override
            public _Number run() {
                _Number result = new _Number();
                result.set(getValue().getFloat() % getValue().getFloat());
                return result;
            }
        });

        method.put("eq", new Calculate() {
            @Override
            public _Bool run() {
                Value v1 = getValue();
                Value v2 = getValue();
                if (v1 instanceof _Word && v2 instanceof _Word) {
                    return new _Bool().set(v1.getString().equals(v2.getString()));
                } else if (v1 instanceof _Number && v2 instanceof _Number) {
                    return new _Bool().set(v1.getFloat().equals(v2.getFloat()));
                }

                return new _Bool().set(false);
            }
        });

        method.put("lt", new Calculate() {
            @Override
            public _Bool run() {
                Value v1 = getValue();
                Value v2 = getValue();
                if (v1 instanceof _Word && v2 instanceof _Word) {
                    int b = v1.getString().compareTo(v2.getString());
                    if (b < 0) {
                        return new _Bool().set(true);
                    } else {
                        return new _Bool().set(false);
                    }
                } else if (v1 instanceof _Number && v2 instanceof _Number) {
                    int b = v1.getFloat().compareTo(v2.getFloat());
                    if (b < 0) {
                        return new _Bool().set(true);
                    } else {
                        return new _Bool().set(false);
                    }
                }

                return null;
            }
        });

        method.put("gt", new Calculate() {
            @Override
            public _Bool run() {
                return new _Bool().set(!method.get("lt").run().getBoolean());
            }
        });

        method.put("and", new Calculate() {
            @Override
            public _Bool run() {
                return new _Bool().set(getValue().getBoolean() && getValue().getBoolean());
            }
        });

        method.put("or", new Calculate() {
            @Override
            public _Bool run() {
                return new _Bool().set(getValue().getBoolean() || getValue().getBoolean());
            }
        });

        method.put("not", new Calculate() {
            @Override
            public _Bool run() {
                return new _Bool().set(!getValue().getBoolean());
            }
        });

    }

    private Value getValue(String op) {

//        System.out.println("- - " +op);
        if (op.charAt(0) == '"') {
            return new _Word().set(op);
        }

        else if (op.equals("[")) {
            _List result = new _List();
            while (true) {
                String str = scan.next();
                Value v;
                if (str.equals("[")) {
                    v = getValue(str);
                } else if (str.equals("]")) {
                    return result;
                } else{
                    v = new _Word().set(str);
                }
                result.append(v);
            }
        }

        else if (op.equals("read")) {
            System.out.print(PS2);
            Scanner temp_scan = scan;
            scan = new Scanner(System.in);
            Value result = getValue();
            scan = temp_scan;
            return result;
        }

        else if (op.equals("readlinst")) {
            System.out.print(PS2);
            Scanner temp_scan = scan;

            String line = LineScan.nextLine();
            scan = new Scanner(line.replace("[", " [ ").replace("]", " ] "));
            _List list = new _List();
            while (scan.hasNext()) {
                list.append(getValue());
            }
            scan = temp_scan;
            return list;
        }

        else if (op.equals("thing") || op.charAt(0) == ':') {
            String key;
            if (op.equals("thing")) {
                key = getValue().getString();
            } else {
                key = "\"" + op.substring(1, op.length());
            }

//            System.out.println("now: ");
//            System.out.println(dict);
            Value v = dict.get(key);
            if (v == null) {
                System.out.println("No value bound with this word: "+key);
            } else {
                return v;
            }
        }

        else if (op.equalsIgnoreCase("true") || op.equalsIgnoreCase("false")) {
            return new _Bool().set(Boolean.parseBoolean(op));
        }

        else if (op.equals("isname")) {
            String key = scan.next();
            if (key.charAt(0) != '"') {
                System.out.println("Illegal word.");
            } else {
                return new _Bool().set(dict.containsKey(key.substring(1, key.length())));
            }
        }
//        else if(op.equals("output")){
//            return_val = getValue();
//            System.out.println("getvalue: ");
//            System.out.println(return_val);
//        }
        else if(dict.containsKey("\""+op)){
//            System.out.println("here~~~~");
            return callFunc("\""+op);
        }
        else if (method.containsKey(op)) {
            return method.get(op).run();
        }
        else {
            try {
                return new _Number().set(calculator.cal(op));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private Value getValue() {
        String op = scan.next();
        return getValue(op);
    }


    private Value callFunc(String op){
        Value return_val_backup = return_val;
        return_val = null;
        String func_list = dict.get(op).toString();
        func_list = func_list.substring(1, func_list.length()-1);
//        System.out.println(func_list);

        Scanner scan_backup = scan;
        scan = new Scanner(func_list);
        Value arg_list = getValue();
        Value operation_list = getValue();
        scan = scan_backup;

        HashMap<String, Value> dict_backup = dict;
        dict = new HashMap<>();

        String arg_string = arg_list.toString();
        Scanner arg_scan = new Scanner(arg_string.substring(1, arg_string.length()-1));
        while(arg_scan.hasNext()){
            String arg = arg_scan.next();
            Value val = getValue();
//            System.out.println("arg:");
//            System.out.println(arg);
//            System.out.println(val);
            dict.put(arg, val);
        }
        runStatement(operation_list.toString());
//        System.out.println("--------------=");
        dict = dict_backup;
        Value result = return_val;
//        System.out.print("return: ");
//        System.out.println(result);
//        System.out.println(return_val);
        return_val = return_val_backup;

        return result;
    }


    public void run() {
//        String Prompt = ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n" +
//                "> Welcome to MuaInterpreter By yh !\n" +
//                "> If you need the Interative mode,\n" +
//                "> plz use commandline args '-i' or '--interactive'\n" +
//                ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + "\n";
//        System.out.println(Prompt);

        while (LineScan.hasNext()) {
            System.out.print(PS1);
            String statement = LineScan.nextLine();
//            System.out.println("input: " + statement);
            if(!runStatement(statement)) break ;
        }
    }

    private boolean runStatement(String statement){
        if(statement.isEmpty()){return true;}
        scan = new Scanner(statement.replace("[", " [ ").replace("]", " ] "));
//        System.out.println(statement.replace("[", " [ ").replace("]", " ] "));
//        System.out.println(dict);
        while(scan.hasNext()){

            String op = scan.next();

            if (op.equals("exit")) {
                System.out.println("Bye~");
                return false;
            }
            if (op.equals("stop")) {
//                System.out.println("Bye~");
                scan.nextLine();
                return true;
            }
            if (op.length() >= 2 && op.substring(0, 2).equals("//")) {
                scan.nextLine();
                return true;
            }
            if (op.equals("print")) {
                Value v = null;
                try {
                    v = getValue();
                } catch (java.util.NoSuchElementException e) {
                    System.out.println("Incomplete input. Please check.");
                } catch (MuaTypeException e) {
                    System.out.println("TypeError. Please check.");
                }
                if (v == null) {
                    System.out.println("Parse failed. Please check syntax.");
                } else {
                    System.out.println(v);
                }

            }
            if (op.equals("make")) {
                String key = scan.next();
                if (key.charAt(0) != '"') {
                    System.out.println("Illegal word.");
                } else {
                    dict.put(key, getValue());
                }
            }
            if (op.equals("erase")) {

                String key = scan.next();
                if (key.charAt(0) != '"') {
                    System.out.println("Illegal word.");
                } else if (!dict.containsKey(key)) {
                    System.out.println("No value bound with this word.");
                } else {
                    Iterator<String> it = dict.keySet().iterator();
                    while (it.hasNext()) {
                        String k = it.next();
                        if (k.equals(key)) {
                            it.remove();
                            dict.remove(k);
                        }
                    }
                }
            }
            if (op.equals("run")){
                return runStatement(getValue().toString());
            }
            else if(op.equals("output")){
                return_val = getValue();
//                System.out.println("getvalue: ");
//                System.out.println(return_val);
            }
            if (op.equals("repeat")){
                int times = getValue().getFloat().intValue();
                Value stmt = getValue();
                for (int i = 0; i < times; i++) {
                    runStatement(stmt.toString());
                }
            }
            if(dict.containsKey("\""+op)){
                callFunc("\""+op);
            }
        }
        return true;
    }

}

