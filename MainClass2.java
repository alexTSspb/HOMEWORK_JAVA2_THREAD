import java.util.Arrays;

public class MainClass2
{
    private static final int SIZE = 10_000_000;


    public static void main(String[] args) throws InterruptedException {
        long time;
        float[] array = new float[SIZE];
        time = UserThread.firstMethod(array);
        System.out.println("Проход по массиву занимает: " + time + " .ms");
        System.out.println("Проход по массиву деля на 2 части и склеивая занимает: " + UserThread.secondMethod(array) + " .ms");
    }

}

class UserThread
{
    //2 метода
    static long firstMethod(float[] array)
    {
        long time = System.currentTimeMillis();
        Arrays.fill(array,1);
        for (int i = 0; i < array.length; i++)
            array[i] = (float)(array[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        time = System.currentTimeMillis() - time;
        return time;
    }

    static long secondMethod(float[] array) throws InterruptedException {
        //Засечем время
        long time = System.currentTimeMillis();
        //нполняем массив единцами
        Arrays.fill(array, 1f);
        int sizeHalf;
        int sizeHalf_2;
        if(array.length%2 == 0) {
            sizeHalf = array.length / 2;
            sizeHalf_2 = sizeHalf;

        }else
        {
            sizeHalf = array.length/2;
            sizeHalf_2 = array.length/2 + 1;
        }
        //создаем два массива
        float[] arrayFirst = new float[sizeHalf];
        float[] arraySecond = new float[sizeHalf_2];
        //Разделим массив на два равных
        System.arraycopy(array, 0, arrayFirst, 0, sizeHalf);
        System.arraycopy(array, sizeHalf, arraySecond,0, sizeHalf_2);
        //Создаем потоки
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < arrayFirst.length; i++)
                {
                    arrayFirst[i] = (float)(arrayFirst[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));

                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int j = 0; j < arraySecond.length; j++)
                {
                    arraySecond[j] = (float)(arraySecond[j] * Math.sin(0.2f + j / 5) * Math.cos(0.2f + j / 5) * Math.cos(0.4f + j / 2));
                }
            }
        });

        //Запускаем потоки
        thread1.start();
        thread2.start();
        //Останавливаем потоки
        thread1.join();
        thread2.join();
        //Склеиваем потоки
        System.arraycopy(arrayFirst,0, array, 0, arrayFirst.length);
        System.arraycopy(arraySecond,0, array, arrayFirst.length, arraySecond.length);
        //
        time = System.currentTimeMillis() - time;
        return time;

    }
}