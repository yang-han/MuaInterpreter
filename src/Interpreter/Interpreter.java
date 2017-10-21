package Interpreter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

class MuaTypeException extends RuntimeException {

}

abstract class Value extends Object {

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
    ArrayList<Value> value = new ArrayList<>();

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
        return String.valueOf(value);
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
    private HashMap<String, Value> dict = new HashMap<>();
    private HashMap<String, Calculate> method = new HashMap<>();
    private ArrayList operation = new ArrayList<String>(){{add("make"); add("print"); add("erase"); add("make");}};

    private String PS1 = "Mua> ";
    private String PS2 = "> ";

    public Interpreter(Boolean Interactive) {
        if(!Interactive){
            PS1 = "";
            PS2 = "";
        }
        else {
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

        if (op.charAt(0) == '"') {
            return new _Word().set(op.substring(1, op.length()));
        }

        if (op.equals("[")) {
            _List result = new _List();
            int cnt = 1;
            while (true){
                String str = scan.next();
                Value v;
                if(str.equals("[")){
                    v = getValue(str);
                    cnt ++;
                }
                else if (str.equals("]")){
                    return result;
                }
                else{
                    try {
                        v = new _Number().set(Float.parseFloat(str));
                    }
                    catch (java.lang.NumberFormatException e){
                        if (str.charAt(0) == '"'){
                            v = new _Word().set(str.substring(1, str.length()));
                        }
                        else if (method.containsKey(str) || operation.contains(str)){
                            v = new _Word().set(str);
                        }
                        else{
                            throw new MuaTypeException();
                        }
                    }
                }
                result.append(v);
            }
        }

        if (op.equals("read")){
            System.out.print(PS2);
            Scanner temp_scan = scan;
            scan = new Scanner(System.in);
            Value result = getValue();
            scan = temp_scan;
            return result;
        }

        if (op.equals("readlinst")){
            System.out.print(PS2);
            Scanner temp_scan = scan;

            String line = LineScan.nextLine();
            scan = new Scanner(line.replace("[", " [ ").replace("]", " ] "));
            _List list = new _List();
            while(scan.hasNext()){
                list.append(getValue());
            }
            scan = temp_scan;
            return list;
        }

        if (op.equals("thing") || op.charAt(0) == ':') {
            String key;
            if (op.equals("thing")) {
                key = getValue().getString();
            }
            else {
                key = op.substring(1, op.length());
            }
            Value v = dict.get(key);
            if (v == null) {
                System.out.println("No value bound with this word.");
            } else {
                return v;
            }
        }

        if (op.equalsIgnoreCase("true") || op.equalsIgnoreCase("false")) {
            return new _Bool().set(Boolean.parseBoolean(op));
        }

        if (op.equals("isname")) {
            String key = scan.next();
            if (key.charAt(0) != '"') {
                System.out.println("Illegal word.");
            } else {
                return new _Bool().set(dict.containsKey(key.substring(1, key.length())));
            }
        }

        try {
            return new _Number().set(Float.parseFloat(op));
        } catch (java.lang.NumberFormatException e) {
            if (method.containsKey(op)) {
                return method.get(op).run();
            }
        }

        return null;
    }

    private Value getValue() {
        String op = scan.next();
        return getValue(op);
    }

    public void run() {
        String Prompt = ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n" +
                        "> Welcome to MuaInterpreter By yh !\n" +
                        "> If you need the Interative mode,\n" +
                        "> plz use commandline args '-i' or '--interactive'\n" +
                        ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + "\n";
        System.out.println(Prompt);
        while (true) {
            System.out.print(PS1);
            if(LineScan.hasNext()) {
                scan = new Scanner(LineScan.nextLine().replace("[", " [ ").replace("]", " ] "));
            }
            else {
                break;
            }
            if(!scan.hasNext()){
                continue;
            }
            String op = scan.next();

            if (op.equals("exit")) {
                System.out.println("Bye~");
                break;
            }
            if (op.length() >= 2 && op.substring(0, 2).equals("//")) {
                scan.nextLine();
                continue;
            }
            if (op.equals("print")) {
                Value v = null;
                try {
                    v = getValue();
                } catch (java.util.NoSuchElementException e) {
                    System.out.println("Incomplete input. Please check.");
                } catch (MuaTypeException e){
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
                    dict.put(key.substring(1, key.length()), getValue());
                }
            }
            if (op.equals("erase")) {

                String key = scan.next();
                if (key.charAt(0) != '"') {
                    System.out.println("Illegal word.");
                } else if (!dict.containsKey(key.substring(1, key.length()))) {
                    System.out.println("No value bound with this word.");
                } else {
                    Iterator<String> it = dict.keySet().iterator();
                    while (it.hasNext()) {
                        String k = it.next();
                        if (k.equals(key.substring(1, key.length()))) {
                            it.remove();
                            dict.remove(k);
                        }
                    }
                }
            }

        }
    }
}