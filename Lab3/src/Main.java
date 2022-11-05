import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

   public static void main(String[] args) throws IOException {
        boolean a;
        System.setOut(new java.io.PrintStream(System.out, true, StandardCharsets.UTF_8));
        String path = "src\\test1.txt";
        FileReader fr = new FileReader(path);
        BufferedReader reader = new BufferedReader(fr);
//        int count = File.ReadAllLines(path).length();
        List<String> str = new ArrayList<>();
        String s;
        List<Command> commands;
        String line = reader.readLine();
        while(line != null){
            str.add(line.replace(" ", ""));
            line = reader.readLine();
        }

        String[] strArr = new String[str.size()];
        str.toArray(strArr);
        commands = Program.Parse(strArr);

        while (true)
        {
            System.out.println("\nВведите строку: ");
            var scanner = new Scanner(System.in);
            s = scanner.nextLine();
            String state = "h0E";
            a = Program.Check_string(commands, s, state, 0, 100);
            if (a)
                System.out.println("Строку можно разобрать");
            else
                System.out.println("Строку нельзя разобрать");
        }
    }
}
