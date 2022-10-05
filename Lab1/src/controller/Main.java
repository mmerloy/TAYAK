package controller;

import model.Calculator;
import model.Check;
import view.View;

import java.io.IOException;

/**
 * @author mmerloy
 * @version 0.0.1
 * @since 05.10.2022
 */
public class Main
{
    public static void scenario()
    {
        int case_number;
        View.print("Программа-аналог калькулятора с использованием обратной польской нотации");

        while (true)
        {
            View.printMenu();
            case_number = Check.intCheck();
            switch (case_number)
            {
                case 1:
                    Calculator calculator = new Calculator();
                    //
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
     * main
     * @param args - аргументы
     */
    public static void main (String[] args)
    {
        scenario();
    }
}