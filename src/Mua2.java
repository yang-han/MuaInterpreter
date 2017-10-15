import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class Mua2 {
    public static void main(String[] args) {
        Interpreter2 interpreter2 = new Interpreter2();
        interpreter2.run();
    }
}

abstract class Value {

    abstract public void print();
}

class _Word extends Value {
    private String value;

    public Value set(String v) {
        value = v;
        return this;
    }

    public String get() {
        return value;
    }

    @Override
    public void print() {
        System.out.println(value);
    }
}

class _Number extends Value {
    private float value;

    public Value set(float v) {
        value = v;
        return this;
    }

    public float get() {
        return this.value;
    }

    @Override
    public void print() {
        System.out.println(value);
    }
}

class _List extends Value {
    ArrayList<Value> value = new ArrayList<>();

    public void append(Value v){
        value.add(v);
    }
    @Override
    public void print() {
        System.out.println("<List:");
        for (Value v : value) {
            v.print();
        }
        System.out.println(">");
    }
}

class _Bool extends Value {
    private Boolean value;

    public Value set(Boolean v) {
        value = v;
        return this;
    }

    public Boolean get() {
        return value;
    }

    @Override
    public void print() {
        System.out.println(value);
    }

}

interface Calculate {
    _Number run();
}


class Interpreter2 {
    private Scanner LineScan = new Scanner(System.in);
    private Scanner scan;
    private HashMap<String, Value> dict = new HashMap<>();
    private HashMap<String, Calculate> method = new HashMap<>();

    public Interpreter2() {

        method.put("add", new Calculate() {
            @Override
            public _Number run() {
                TwoNumbers num = getTwoNumbers();
                _Number result = new _Number();
                result.set(num.getFirst().get() + num.getSecond().get());
                return result;
            }
        });

        method.put("sub", new Calculate() {
            @Override
            public _Number run() {
                TwoNumbers num = getTwoNumbers();
                _Number result = new _Number();
                result.set(num.getFirst().get() - num.getSecond().get());
                return result;
            }
        });

        method.put("mul", new Calculate() {
            @Override
            public _Number run() {
                TwoNumbers num = getTwoNumbers();
                _Number result = new _Number();
                result.set(num.getFirst().get() * num.getSecond().get());
                return result;
            }
        });

        method.put("div", new Calculate() {
            @Override
            public _Number run() {
                TwoNumbers num = getTwoNumbers();
                _Number result = new _Number();
                result.set(num.getFirst().get() / num.getSecond().get());
                return result;
            }
        });

        method.put("mod", new Calculate() {
            @Override
            public _Number run() {
                TwoNumbers num = getTwoNumbers();
                _Number result = new _Number();
                result.set(num.getFirst().get() % num.getSecond().get());
                return result;
            }
        });

    }


    class TwoNumbers {
        private _Number value1;
        private _Number value2;

        public TwoNumbers(_Number v1, _Number v2) {
            value1 = v1;
            value2 = v2;
        }

        public _Number getFirst() {
            return value1;
        }

        public _Number getSecond() {
            return value2;
        }
    }

    private TwoNumbers getTwoNumbers() {
        String str1 = scan.next();
        _Number v1 = new _Number();
        _Number v2 = new _Number();
        try {
            v1.set(Float.parseFloat(str1));
        } catch (Exception e) {
            v1 = method.get(str1).run();
        }
        String str2 = scan.next();
        try {
            v2.set(Float.parseFloat(str2));
        } catch (Exception e) {
            v2 = method.get(str2).run();
        }
        return new TwoNumbers(v1, v2);
    }

    private Value getValue(String op){

        if (op.charAt(0) == '"') {
            return new _Word().set(op.substring(1, op.length()));
        }

        if (op.charAt(0) == '['){
            _List result = new _List();
            op = op.substring(1, op.length());
            while(op.charAt(op.length()-1) != ']'){
                result.append(getValue(op));
                op = scan.next();

            }
            result.append(getValue(op.substring(0, op.length()-1)));
            return result;
        }

        if (op.equals("thing") || op.charAt(0) == ':') {
            String key;
            if(op.equals("thing")){
                key = scan.next();
                if (key.charAt(0) != '"'){
                    System.out.println("Illegal word format.");
                    return null;
                }
            }
            else {
                key = op;
            }
            Value v = dict.get(key.substring(1, key.length()));
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
        String PS1 = "Mua> ";
        while (true) {
            System.out.print(PS1);
            scan = new Scanner(LineScan.nextLine());
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
                Value v = getValue();
                if (v == null) {
                    System.out.println("Parse failed. Please check syntax.");
                } else {
                    v.print();
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