package Interpreter;


import java.util.*;

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

    public _Word first() {
        String _res;
        if (value.substring(0, 1).equals("\"")) {
            _res = value.substring(0, 2);
        } else {
            _res = value.substring(0, 1);
        }
        return new _Word().set(_res);
    }

    public _Word last() {
        String _res;
        if (value.substring(0, 1).equals("\"")) {
            _res = "\"" + value.substring(value.length() - 1, value.length());
        } else {
            _res = value.substring(value.length() - 1, value.length());
        }
        return new _Word().set(_res);
    }


    public _Word butfirst() {
        String _res;
        if (value.substring(0, 1).equals("\"")) {
            _res = "\"" + value.substring(2, value.length());
        } else {
            _res = value.substring(1, value.length());
        }
        return new _Word().set(_res);
    }

    public _Word butlast() {
        String _res;
        _res = value.substring(0, value.length() - 1);
        return new _Word().set(_res);
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

    public Value first() {
        if (value.size() == 0) {
            System.out.println("empty list~");
        }
        Value _v = value.get(0);
        if (_v instanceof _Word) {
            String _s = _v.getString();
            _Word result = new _Word();
            result.set(_s.split(" ")[0]);
            return result;
        } else {
            return _v;
        }
    }

    public Value last() {
        if (value.size() == 0) {
            System.out.println("empty list~");
        }
        Value _v = value.get(value.size() - 1);
        if (_v instanceof _Word) {
            String _s = _v.getString();
            _Word result = new _Word();
            String[] _words = _s.split(" ");
            result.set(_words[_words.length - 1]);
            return result;
        } else {
            return _v;
        }
    }

    public Value butfirst() {
        if (value.size() == 0) {
            System.out.println("empty list~");
        }
        Value _v = value.get(0);
        _List result = new _List();
        if (_v instanceof _Word) {
            String _s = _v.getString();
            _Word _result = new _Word();
            String[] _words = _s.split(" ");
            StringBuilder _stringBuilder = new StringBuilder();
            for (int i = 1; i < _words.length; ++i) {
                _stringBuilder.append(_words[i]);
            }
            _result.set(_stringBuilder.toString());
            result.append(_result);
        }
//        else {
//            for (int i=1;i<value.size();++i) {
//                result.append(value.get(i));
//            }
//        }
        for (int i = 1; i < value.size(); ++i) {
            result.append(value.get(i));
        }
        return result;
    }

    public Value butlast() {
        if (value.size() == 0) {
            System.out.println("empty list~");
        }
        Value _v = value.get(value.size() - 1);
        _List result = new _List();
        for (int i = 0; i < value.size() - 1; ++i) {
            result.append(value.get(i));
        }
        if (_v instanceof _Word) {
            String _s = _v.getString();
            _Word _result = new _Word();
            String[] _words = _s.split(" ");
            StringBuilder _stringBuilder = new StringBuilder();
            for (int i = 0; i < _words.length - 1; ++i) {
                _stringBuilder.append(_words[i]);
            }
            _result.set(_stringBuilder.toString());
            result.append(_result);
        }
        return result;
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
    Random random = new Random(0);
    //    private ScriptEngineManager manager = new ScriptEngineManager();
//    private ScriptEngine engine = manager.getEngineByName("javascript");
    private ArrayList operation = new ArrayList<String>() {{
        add("make");
        add("print");
        add("erase");
        add("run");
        add("repeat");
    }};
    HashSet<String> cal_op = new HashSet<>();

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

        cal_op.add("+");
        cal_op.add("-");
        cal_op.add("*");
        cal_op.add("/");
        cal_op.add("%");

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
//                System.out.println("debug v1  "+v1.toString());
                Value v2 = getValue();
//                System.out.println("debug  "+v1+" "+v2);
                if (v1 instanceof _Word && v2 instanceof _Word) {
                    int b = v1.getString().compareTo(v2.getString());
//                    System.out.println(b<0);
                    return new _Bool().set(b < 0);

                } else if (v1 instanceof _Number && v2 instanceof _Number) {
                    int b = v1.getFloat().compareTo(v2.getFloat());
//                    System.out.println(b<0);
                    return new _Bool().set(b < 0);
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

    private HashMap<String, Value> prepareDict(HashMap<String, Value> last_dict) {
        HashMap<String, Value> _dict = new HashMap<>();
        _Number pi = new _Number();
        pi.set((float) 3.14159);
        for (String key : last_dict.keySet()) {
            _dict.put(key, last_dict.get(key));
        }
        _dict.put("\"pi", pi);
        return _dict;
    }

    private Value getValue(String op) {

//        System.out.println("- - " +op);
        if (op.charAt(0) == '"') {
            return new _Word().set(op);
        } else if (op.equals("[")) {
            _List result = new _List();
            while (true) {
                String str = scan.next();
                Value v;
                if (str.equals("[")) {
                    v = getValue(str);
                } else if (str.equals("]")) {
                    return result;
                } else {
                    v = new _Word().set(str);
                }
                result.append(v);
            }
        } else if (op.equals("read")) {
            System.out.print(PS2);
            Scanner temp_scan = scan;
            scan = new Scanner(System.in);
            Value result = getValue();
            scan = temp_scan;
            return result;
        } else if (op.equals("readlinst")) {
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
        } else if (op.equals("thing") || op.charAt(0) == ':') {
            String key;
            if (op.equals("thing")) {
                key = getValue().getString();
            } else {
                key = "\"" + op.substring(1, op.length());
            }

            Value v = dict.get(key);
            if (v == null) {
                System.out.println("No value bound with this word: " + key);
            } else {
                return v;
            }
        } else if (op.equalsIgnoreCase("true") || op.equalsIgnoreCase("false")) {
            return new _Bool().set(Boolean.parseBoolean(op));
        } else if (op.equals("isname")) {
            String key = scan.next();
            if (key.charAt(0) != '"') {
                System.out.println("Illegal word.");
            } else {
                return new _Bool().set(dict.containsKey(key.substring(1, key.length())));
            }
        } else if (dict.containsKey("\"" + op)) {
            return callFunc("\"" + op);
        } else if (method.containsKey(op)) {
            return method.get(op).run();
        } else if (cal_op.contains(op)) {
            return new _Word().set(op);
        } else if (op.equals("random")) {
            int times = getValue().getFloat().intValue();
            _Number result = new _Number();
            result.set(random.nextInt(times));

        } else if (op.equals("sqrt")) {
            float num = getValue().getFloat();
            _Number result = new _Number();
            result.set((float) Math.sqrt(num));
            return result;
        } else if (op.equals("isnumber")) {
            Value _v = getValue();
            _Bool result = new _Bool();
            result.set(_v instanceof _Number);
            return result;
        } else if (op.equals("isword")) {
            Value _v = getValue();
            _Bool result = new _Bool();
            result.set(_v instanceof _Word);
            return result;
        } else if (op.equals("islist")) {
            Value _v = getValue();
            _Bool result = new _Bool();
            result.set(_v instanceof _List);
            return result;
        } else if (op.equals("isbool")) {
            Value _v = getValue();
            _Bool result = new _Bool();
            result.set(_v instanceof _Bool);
            return result;
        } else if (op.equals("isempty")) {
            Value _v = getValue();
            _Bool result = new _Bool();
            if (_v instanceof _List) {
                result.set(((_List) _v).value.size() == 0);
            } else if (_v instanceof _Word) {
                result.set(_v.getString().length() == 0 || _v.getString().equals("\""));
            }
            return result;
        } else if (op.equals("int")) {
            Value _v = getValue();
            _Number result = new _Number();
            result.set((float) Math.floor(_v.getFloat()));
            return result;
        } else if (op.equals("first")) {
            Value _v = getValue();
            if (_v instanceof _List) {
                return ((_List) _v).first();
            } else if (_v instanceof _Word) {
                return ((_Word) _v).first();
            }
        } else if (op.equals("last")) {
            Value _v = getValue();
            if (_v instanceof _List) {
                return ((_List) _v).last();
            } else if (_v instanceof _Word) {
                return ((_Word) _v).last();
            }
        } else if (op.equals("butfirst")) {
            Value _v = getValue();
            if (_v instanceof _List) {
                return ((_List) _v).butfirst();
            } else if (_v instanceof _Word) {
                return ((_Word) _v).butfirst();
            }
        } else if (op.equals("butlast")) {
            Value _v = getValue();
            if (_v instanceof _List) {
                return ((_List) _v).butlast();
            } else if (_v instanceof _Word) {
                return ((_Word) _v).butlast();
            }
        } else if (op.equals("sentence")) {
            Value _v1 = getValue();
            Value _v2 = getValue();
            _List _result = new _List();
            if (_v1 instanceof _List) {
                for (Value _v : ((_List) _v1).value) {
                    _result.append(_v);
                }
            } else {
                _result.append(_v1);
            }
            if (_v2 instanceof _List) {
                for (Value _v : ((_List) _v2).value) {
                    _result.append(_v);
                }
            } else {
                _result.append(_v2);
            }
            return _result;
        } else if (op.equals("list")) {
            _List _result = new _List();
            _result.append(getValue());
            _result.append(getValue());
            return _result;
        } else if (op.equals("join")) {
            _List _list = (_List) getValue();
            Value _v = getValue();
            _list.append(_v);
            return _list;
        } else if (op.equals("word")) {
            _Word _word = (_Word) getValue();
            Value _v = getValue();
            _Word _result = new _Word();
            if (_v instanceof _Word) {
                _result.set(_word.toString() + _v.toString().substring(1));
            } else {
                _result.set(_word.toString() + _v.toString());
            }
            return _result;

        } else if (op.equals("(")) {
            int count = 1;
            StringBuilder expr = new StringBuilder();
            String now;
            while (scan.hasNext()) {
                now = scan.next();
                if (now.equals("(")) {
                    count++;
                } else if (now.equals(")")) {
                    count--;
                }
                if (count == 0) {
                    break;
                }
                expr.append(now);
                expr.append(" ");
            }

            String expression = expr.toString().replace("(", " ( ").replace(")", " ) ");
            expression = expression.replace("+", " + ").replace("-", " - ");
            expression = expression.replace("*", " * ").replace("/", " / ");
            expression = expression.replace("%", " % ");
            expression = expression.replaceAll("\\+(\\s+)-\\s+", "+ -");
            expression = expression.replaceAll("-(\\s+)-\\s+", "- -");
            expression = expression.replaceAll("\\*(\\s+)-\\s+", "* -");
            expression = expression.replaceAll("/(\\s+)-\\s+", "/ -");
            expression = expression.replaceAll("%(\\s+)-\\s+", "% -");
            expression = expression.replaceAll("\\((\\s+)-\\s+", "( -");
            expression = expression.replaceAll("\b(\\s+)-\\s+", "-");
            Scanner scan_backup = scan;
            scan = new Scanner(expression);
//            System.out.println("expression "+expression);
            StringBuilder real_expr_builder = new StringBuilder();
            while (scan.hasNext()) {
                String n = getValue().toString();
//                System.out.println(n);
                real_expr_builder.append(n);
            }
            String real_expr = real_expr_builder.toString();
//            System.out.println(real_expr);
            scan = scan_backup;
            try {
                return new _Number().set(calculator.cal(real_expr));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                return new _Number().set(Float.parseFloat(op));
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


    private Value callFunc(String op) {
        Value return_val_backup = return_val;
        return_val = null;
        String func_list = dict.get(op).toString();
        func_list = func_list.substring(1, func_list.length() - 1);
//        System.out.println(func_list);

        Scanner scan_backup = scan;
        scan = new Scanner(func_list);
        Value arg_list = getValue();
        Value operation_list = getValue();
//        System.out.println("arg_list "+arg_list);
//        System.out.println("operation_list "+operation_list);
        scan = scan_backup;
//        System.out.println("scan_next: "+scan.next());

        HashMap<String, Value> dict_backup = dict;
        dict = prepareDict(dict_backup);

        String arg_string = arg_list.toString();
        Scanner arg_scan = new Scanner(arg_string.substring(1, arg_string.length() - 1));
        while (arg_scan.hasNext()) {
            String arg = "\"" + arg_scan.next();
            Value val = getValue();
//            System.out.println("arg:");
//            System.out.println(arg);
//            System.out.println(val);
            dict.put(arg, val);
        }
        scan_backup = scan;
        runStatement(operation_list.toString());
        scan = scan_backup;
//        System.out.println("--------------=");
        dict = dict_backup;
        Value result = return_val;
//        System.out.print("return: ");
//        System.out.println(result);
//        System.out.println(return_val);
        return_val = return_val_backup;
//        if(result != return_val){
//            System.out.println("return    "+result.toString());
//        }
//        System.out.println("scan_next2: "+scan.next());

        return result;
    }


    public void run() {
//        String Prompt = ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n" +
//                "> Welcome to MuaInterpreter By yh !\n" +
//                "> If you need the Interative mode,\n" +
//                "> plz use commandline args '-i' or '--interactive'\n" +
//                ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + "\n";
//        System.out.println(Prompt);

        dict = prepareDict(new HashMap<>());
        while (LineScan.hasNext()) {
            System.out.print(PS1);
            String statement = LineScan.nextLine();
//            System.out.println("input: " + statement);
            if (!runStatement(statement)) break;
        }
    }

    private boolean runStatement(String statement) {
        if (statement.isEmpty()) {
            return true;
        }
        Scanner scan_backup = scan;
        statement = statement.replace("[", " [ ").replace("]", " ] ");
        statement = statement.replace("(", " ( ").replace(")", " ) ");
        scan = new Scanner(statement);
//        System.out.println(statement.replace("[", " [ ").replace("]", " ] "));
//        System.out.println(dict);
//        System.out.println("Statement:  "+statement);
        while (scan.hasNext()) {

            String op = scan.next();
//            System.out.println("op "+op);

            if (op.equals("exit")) {
                System.out.println("Bye~");
                return false;
            } else if (op.equals("stop")) {
//                System.out.println("Bye~");
                scan.nextLine();
                scan = scan_backup;
                return true;
            } else if (op.length() >= 2 && op.substring(0, 2).equals("//")) {
                scan.nextLine();
                scan = scan_backup;
                return true;
            } else if (op.equals("print")) {
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

            } else if (op.equals("make")) {
                String key = scan.next();
                if (key.charAt(0) != '"') {
                    System.out.println("Illegal word.");
                } else {
                    dict.put(key, getValue());
                }
            } else if (op.equals("erase")) {

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
            } else if (op.equals("run")) {
                scan = scan_backup;
                return runStatement(getValue().toString());
            } else if (op.equals("output")) {
                return_val = getValue();
//                System.out.println("getvalue: ");
//                System.out.println(return_val);
            } else if (op.equals("repeat")) {
                int times = getValue().getFloat().intValue();
                Value stmt = getValue();
                for (int i = 0; i < times; i++) {
                    runStatement(stmt.toString());
                }
            } else if (op.equals("wait")) {
                int t = getValue().getFloat().intValue();
                try {
                    Thread.sleep(t);
                } catch (java.lang.InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("wait " + String.valueOf(t) + "ms done");

            } else if (dict.containsKey("\"" + op)) {
                callFunc("\"" + op);
            } else if (op.equals("poall")) {
                System.out.println(dict.toString());
            } else if (op.equals("erall")) {
                dict = new HashMap<>();
            } else if (op.equals("if")) {
                boolean _check = getValue().getBoolean();
                _List _list1 = (_List) getValue();
                _List _list2 = (_List) getValue();
//                System.out.println("if if if"+_list1.toString() +"else else else"+_list2.toString());
                if (_check) {
                    runStatement(_list1.toString());
                } else {
                    runStatement(_list2.toString());
                }
            }
        }
        scan = scan_backup;
        return true;
    }

}

