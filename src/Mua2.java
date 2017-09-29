import java.util.*;

public class Mua2 {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String PS1 = "MUA2> ";

        while (true) {
            System.out.print(PS1);
            String statement = scan.nextLine();
            statement = statement.replace("[", " [ ").replace("]", " ] ");
            String[] tokens =  statement.split(" ");

            if(tokens[0].equals("exit")){
                System.out.println("Bye~");
                break;
            }

            List<String> tokens_itr = new ArrayList<>();
            Collections.addAll(tokens_itr, tokens);
            Iterator<String> itr = tokens_itr.iterator();
            while(itr.hasNext()){
                System.out.println(itr.next());
            }


        }
    }
}
