import java.util.*;

class Command
{
    public int state = 0;
    public String word = "λ";
    public String text = "";
    public int new_state = 0;
    public String new_text = "";
}

public class Program
{
    private static boolean sContains(String s, char c){
        return s.indexOf(c) != 0;
    }

    static List<Command> Parse(String[] strs)
    {
        List<Command> commands = new ArrayList<Command>();
        StringBuilder P= new StringBuilder();
        var Z = new StringBuilder();
        String a = "QWERTYUIOPASDFGHJKLZXCVBNM";
        String notTerminal ="";
        int count=0;
        for (String str : strs) {
            for (int j = 0; j < str.length(); j++) {
                char ijChar = str.charAt(j);
                if ((ijChar != '>') && (ijChar != '|')) {
                    if (Z.toString().indexOf(ijChar) == -1) {
                        Z.append(ijChar);
                    }
                    if (a.indexOf(ijChar) == -1)
                        P.append(ijChar);
                } else {
                    if (ijChar == '>')
                        count++;
                }
            }
        }
        if (count > Arrays.stream(strs).count())
        {
            Z.append(">");
            P.append(">");
        }
        String zString = Z.toString();
        for (int i=0; i<Z.length(); i++)
        {
            if (sContains(a, zString.charAt(i)) && !sContains(notTerminal, zString.charAt(i)))
            {
                notTerminal += zString.charAt(i);
            }

        }

        for (int i=0; i<Arrays.stream(strs).count(); i++)
        {
            char c = strs[i].charAt(0);
            strs[i] = strs[i].substring(strs[i].indexOf('>') + 1);
            while (strs[i].isEmpty())
            {
                Command com = new Command();
                com.state = 0;
                com.word = "λ";
                com.text = c+"";
                com.new_state = 0;
                String t = strs[i].substring(0, strs[i].indexOf('|'));
                com.new_text = "";// new String(com.new_text.Reverse().ToArray());
                for (int j = t.length()-1; j >= 0; j--)
                {
                    char tj = t.charAt(j);
                    if (tj == ')')
                        com.new_text += '(';
                    else if (tj == '(')
                        com.new_text += ')';
                    else com.new_text += tj;
                }
                strs[i] = strs[i].substring(strs[i].indexOf('|') + 1);
                commands.add(com);

            }

        }
        String pString = P.toString();
        for (int i = 0; i < P.length(); i++)
        {
            Command com = new Command();
            com.state = 0;
            com.word = Character.toString(pString.charAt(i));
            com.text = com.word;
            com.new_state = 0;
            com.new_text = "λ";
            commands.add(com);
        }

        Command com2 = new Command();
        com2.text = "h0";
        com2.new_text = "λ";
        commands.add(com2);

        Z.append("h0");
        System.out.println("P: " + P);
        System.out.println("Z: " + Z);
        //Console.WriteLine("NotTerminal: " + notTerminal);
        System.out.println("Полученные команды:");
        for (Command c : commands)
            System.out.println("(s" + c.state + "," + c.word + "," + c.text + ")=(s" + c.new_state + "," + c.new_text + ")");

        return commands;
    }

    static boolean Check_string(List<Command> commands, String str, String state, int step, int max)
    {
        String str1;
        String state1;

        if ((Objects.equals(str, "λ")) && (Objects.equals(state, "h0")))
            return true;

        boolean a = false;
        if (str.isEmpty())
            return false;

        if (step == max)
            return false;

        String lastStateChar = Character.toString(state.charAt(state.length() - 1));
        for (int j = 0; j<commands.size(); j++)
        {
            if ((lastStateChar.equals(commands.get(j).word)) && (Character.toString(str.charAt(0)).equals(commands.get(j).word)))
            {
                str1 = str.substring(1); //str.Remove(0, 1);
                if (str1.equals(""))
                    str1 = "λ";
                state1 = state.substring(0, state.length() - 1);//.Remove(state.length() - 1, 1);

                a = Check_string(commands, str1, state1, step + 1, max);
                if (a)
                {
                    System.out.println("(s0," + str1 + "," + state1 + ")");
                    return true;
                }

            }

        }


        for (int j=0; j<commands.size(); j++)
        {
            //state[state.length() - 1].ToString()
            if ((lastStateChar.equals(commands.get(j).text)) && (commands.get(j).word.equals("λ")))
            {
                state1 = state.substring(0, state.length() - 1) + commands.get(j).new_text;
                str1 = str;

                a = Check_string(commands, str1, state1, step + 1, max);
                if (a)
                {
                    System.out.println("(s0," + str1 + "," + state1 + ")");
                    return true;
                }

            }

        }

        return false;
    }

}

