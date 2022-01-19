package dev.kosmx.dctAnim;

import java.util.Arrays;

public class DCT {

    //public static double[] array = {1, 2, 3, 4, 2, 3, 6, 3, 4, 2, -2, -3, 4, -5, -5, -1, -2, 2};
    public static double[] array = {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
    //public static double[] array = {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public static void main(String[] args) {

        double[] transformed = MDCT();
        /*
        System.out.println(Arrays.toString(transformed));
        System.out.println(Arrays.toString(IMDCT(transformed)));

         */


        transformed = DCT2(array);
        System.out.println(Arrays.toString(transformed));
        System.out.println(Arrays.toString(arrayMultiply(DCT3(array), 2f/array.length)));


    }

    public static double[] arrayMultiply(double[] array, double num) {
        for (int i = 0; i < array.length; i++) {
            array[i] *= num;
        }
        return array;
    }
    //"The DCT"
    public static double[] DCT2(double[] source) {
        double[] transformed = new double[source.length];

        for (int k = 0; k < transformed.length; k++){
            double sum = 0f;
            for (int n = 0; n < source.length; n++) {
                sum += source[n]*Math.cos(Math.PI/source.length *(n+.5)*k);
            }
            transformed[k] = sum;
        }
        return transformed;
    }

    public static double[] DCT3(double[] source) {
        double[] transformed = new double[source.length];

        for (int k = 0; k < transformed.length; k++){
            double sum = 0.5*source[0];
            for (int n = 1; n < source.length; n++) {
                sum += source[n]*Math.cos(Math.PI/source.length*(k+.5)*n);
            }
            transformed[k] = sum;
        }
        return transformed;
    }

    public static double[] IMDCT(double[] transformed) {
        double[] original = new double[transformed.length*2];

        for (int n = 0; n < original.length; n++) {
            double sum = 0;
            for (int k = 0; k < transformed.length; k++) {
                sum += transformed[k]*Math.cos(Math.PI/original.length*(n + 0.5 + (double)original.length/2)*(k + .5));
            }
            original[n] = sum;
        }
        return original;
    }

    public static double[] MDCT() {
        double[] transformed = new double[array.length/2];
        int N = transformed.length;

        for (int k = 0; k < transformed.length; k++) {
            double sum = 0f;
            for (int n = 0; n < 2*N; n++) {
                sum += array[n]*Math.cos(Math.PI/N*(n + (double)1/2 + (double)N/2)*(k + (double)1/2));
            }
            transformed[k] = sum;
        }
        return transformed;
    }
}
