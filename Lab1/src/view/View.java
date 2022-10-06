package view;


public class View
{
    public static final String Error = "Error: ";

    /**
     * Метод print - выводит на устройство вывода значение свойства или строку
     * @param message передаем ключ для отображения значения свойства или строку для печати
     */
    public static void print(String message)
    {
        System.out.println( message +"\n");
    }


    /**
     * printError - выводит на устройство вывода сообщение об ошибке
     * @param message передаем ключ для отображения значения свойства или строку для печати
     */
    public static void printError(String message)
    {
        print(Error + message);
    }

    /**
     * printMenu()- вывод меню с возможностью выбора
     */
    public static void printMenu()
    {
        System.out.println("1 - Вычисление\n");
        System.out.println("2 - Выход из программы \n");
        System.out.println("-------------------------------\n");
    }

}
