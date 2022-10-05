package model;

import view.View;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Check {
    /**
     * intCheck()- проверка вводимого значения на соответствие типу int
     */
    public static int intCheck()
    {
        int number = 0;
        boolean CorrectValues;
        Scanner in = new Scanner(System.in);

        do
        {
            try
            {
                CorrectValues = true;
                number = in.nextInt();
            }
            catch (InputMismatchException e)
            {
                CorrectValues = false;
                View.printError("\n Это должно быть целое число. Попробуйте ещё раз\n");
                in.nextLine();
            }
        }
        while (!CorrectValues);

        return number;
    }

    /**
     * doubleCheck()- проверка вводимого значения на соответствие типу double
     */

    public static double doubleCheck()
    {
        double number = 0;
        boolean CorrectValues;
        Scanner in = new Scanner(System.in);

        do
        {
            try
            {
                CorrectValues = true;
                number = in.nextDouble();
            }
            catch (InputMismatchException e)
            {
                CorrectValues = false;
                View.printError("\nЭто должно быть вещественное число. Попробуйте ещё раз\n");
                in.nextLine();
            }
        }
        while (!CorrectValues);

        return number;
    }
}
