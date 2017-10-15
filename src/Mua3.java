import java.util.*;

public class Mua3 {
    private static Scanner scan = new Scanner(System.in);
    public static void main(String[] args){
        while(true){
            String t = scan.next();
            if(t.equals("exit"))break;
            System.out.println(t.length());
            System.out.println(t);

        }
    }
}


class Interpreter{
    private Scanner scan = new Scanner(System.in);
    private Map<String, Object> dict = new HashMap<String, Object>();
    private List<String> operation = new ArrayList<>();
    public Interpreter(){
        operation.add("make");
    }
    public void run(){
        while(true){
            System.out.print("Mua3> ");
            String token = scan.next();
            if(Character.isDigit(token.charAt(0)) || token.charAt(0) == '-') {}
        }
    }

    private Object getValue(){
        String token = scan.next();
        if(token.equals("exit"))return token;
        Character marker = token.charAt(0);
        if(Character.isDigit(marker) || marker.equals('-')) {return Float.parseFloat(token);}
        if(marker.equals(':')){return dict.get(token.substring(1));}
        if(marker.equals('"')){}
        return 0;
    }

    private void make(){
        String key = scan.next();
        Object value = getValue();
        dict.put(key, value);
    }
}
