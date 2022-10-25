import graph.Graph;
import graph.TuringMachine;
import graph.Utils;
import graph.command.errors.IllegalCommandFormatException;
import graph.parts.Entity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static void main(String[] args) {
        File file = new File("var1.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader("var1.txt"));
            Graph gp = new Graph();
            AtomicBoolean errors = new AtomicBoolean(false);
            br.lines().forEach(s -> {
                try {
                    gp.addNewEdgeFromCommand(Utils.parseFromString(s));
                } catch (IllegalCommandFormatException e) {
                    System.out.println("Your file is incorrect!");
                    errors.set(true);
                    //e.printStackTrace();
                }
            });
            if (!errors.get()) {
                System.out.println("Изначальный граф");
                System.out.println(gp.toString());


                Entity issueEntity;
                while ((issueEntity = Utils.getIssueEntity(gp)) != null) {
                    Utils.normalizeForOneEntity(gp, issueEntity);
                }
                while (Utils.haveUselessEntities(gp)) {
                    Utils.removeUselessEntity(gp);
                }

                TuringMachine tm = new TuringMachine(gp);
                System.out.println("Детерминированный граф");
                System.out.println(gp.toString());
                System.out.println(tm.isCorrectString("ab+cd    *eee=357"));
                //System.out.println(tm.isCorrectString("a"));
//                System.out.println(tm.isCorrectString("a"));
//                System.out.println(tm.isCorrectString("ahg"));
            }
          //Utils.normalizeForOneEntity(gp,new Entity("q0"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}