package com.example.englishwordslearning.utils;

import java.util.Random;

/**
 * Утилитный класс для работы с генерацией случайных чисел
 */
public class RandomUtil {

    private static Random random = new Random();

    /**
     * Генерирует случайное число до 100 и вычисляет, является ли оно успешным (больше границы)
     * @param minBorder число, все значения выше которого считаются успешными
     */
    public static boolean success(int minBorder) {
        return random.nextInt(100) + 1 > minBorder;
    }

    /**
     * Генерирует случайный индекс элемента списка
     * @param listSize размер списка
     */
    public static int randomListIndex(int listSize) {
        return random.nextInt(listSize);
    }
}
