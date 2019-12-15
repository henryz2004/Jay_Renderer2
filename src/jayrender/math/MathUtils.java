package jayrender.math;

import java.util.List;

/**
 *
 * Collection of a bunch of static methods amassed over time
 */
public class MathUtils {

    public static final double e  = 2.7183;
    public static final double pi = 3.1416;

    private MathUtils() {}

    // Static methods (ordered according to return type)
    public static void          squareInplace(double[] array) {
        for (int i=0; i < array.length; i++) {
            array[i] = array[i] * array[i];
        }
    }
    public static void          subtractByInplace(double[] array, double number) {
        for (int i=0; i < array.length; i++) {
            array[i] -= number;
        }
    }

    public static double        clamp(int min, int max, double number) {
        if (number > max) return max;
        if (number < min) return min;
        return number;
    }
    public static double        sum(double[] array) {
        double sum = 0;
        for (double item : array) {
            sum += item;
        }
        return sum;
    }
    public static double        round(double number, int decimalPlaces) {
        double multiplier = java.lang.Math.pow(10, decimalPlaces);
        return java.lang.Math.round(number*multiplier)/multiplier;
    }
    public static double        findAverage(List<Double> list) {
        double average = 0;
        for (double item : list) {
            average += item/list.size();
        }
        return average;
    }
    public static double        findAverageOfDimension(List<double[]> list, int dimensionIndex) {
        // Find the average of all the numbers of dimensionIndex
        // For example if the input is [1,2], [3,4], [5,6]
        // and the dimension index is 1, the value returned would be the average of 2, 4, and 6

        double average = 0;
        for (double[] item : list) {
            average += item[dimensionIndex] / list.size();
        }
        return average;
    }

    public static double[]      multiplyCorresponding(double[] array, double[] otherArray) {
        double[] resultList = new double[array.length];
        for (int i=0; i<array.length; i++) {
            resultList[i] = array[i] * otherArray[i];
        }
        return resultList;
    }
    public static double[]      square(double[] array) {
        double[] resultList = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            resultList[i] = array[i] * array[i];
        }
        return resultList;
    }
    public static double[]      subtractBy(List<Double> list, double number) {
        return subtractBy((double[]) (Object) list.toArray(), number);  // Safe because we KNOW that the object is actually a double[]
    }
    public static double[]      subtractBy(double[] array, double number) {
        double[] returnList = new double[array.length];
        for (int i=0; i < array.length; i++) {
            returnList[i] = array[i] - number;
        }
        return returnList;
    }
}
