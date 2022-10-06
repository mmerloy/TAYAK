package controller;

import model.Calculator;
import model.Check;
import model.Translator;
import view.View;

import java.util.Scanner;

/**
 * @author mmerloy
 * @version 0.0.2
 * @since 06.10.2022
 */
public class Main
{
    public static void scenario()
    {
        int case_number;
        View.print("Программа-аналог калькулятора с использованием обратной польской нотации");

        while(true)
        {
            View.printMenu();
            case_number = Check.intCheck();
            switch (case_number)
            {
                case 1:
                    try {
                        Translator translator = new Translator();
                        Scanner in = new Scanner(System.in);
                        var expressionsList = translator.translate(in.nextLine());
                        Calculator calculator = new Calculator();
                        String result = calculator.calculateExpression(expressionsList);
                    } catch (Exception ex){
                        View.printError(ex.getMessage());
                    }

                    break;
                case 2:
                    System. exit(0);
                    break;
                default:
                    View.print("Попробуйте ввести еще раз");
                    break;
            }
        }
    }

    /**
     * main - запуск главного сценария программы
     * @param args - аргументы
     */
    public static void main (String[] args)
    {
        scenario();
    }
}