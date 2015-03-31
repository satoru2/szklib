package com.api.util;

public class ArrayUtil {

    public enum Direction {
        /** Direction to Left */
        LEFT,
        /** Direction to Right */
        RIGHT,
    }

    private ArrayUtil() {

    }

    /**
     * Shift to one
     * 
     * @param Object [] ary, int direction
     */
    public static Object circulationShift(Object[] ary, Direction direction) {

        Object[] workAry = new Object[ary.length];
        Object overFlow = null;
        if (Direction.RIGHT == direction) {
            // Shift to RIGHT
            overFlow = ary[ary.length - 1];
            // copy to workAry
            for (int i = 0; i < ary.length; i++) {
                workAry[i] = ary[i];
            }
            // shift
            for (int i = 1; i < ary.length; i++) {
                // move
                ary[i] = workAry[i - 1];
            }
            // last of workAry
            ary[0] = workAry[ary.length - 1];
        } else if (Direction.LEFT == direction) {
            // Shift to LEFT
            overFlow = ary[0];
            // copy to workAry
            for (int i = 0; i < ary.length; i++) {
                workAry[i] = ary[i];
            }
            // shift
            for (int i = 0; i < ary.length - 1; i++) {
                ary[i] = workAry[i + 1];
            }
            ary[ary.length - 1] = workAry[0];
        }
        return overFlow;
    }

    /**
     * Shift to one
     * 
     * @param ary
     * @param direction
     */
    public static String circulationShift(String[] ary, Direction direction) {

        String[] workAry = new String[ary.length];
        String overFlow = null;
        if (Direction.RIGHT == direction) {
            // Shift to RIGHT
            overFlow = ary[ary.length - 1];
            // copy to workAry
            for (int i = 0; i < ary.length; i++) {
                workAry[i] = ary[i];
            }
            // shift
            for (int i = 1; i < ary.length; i++) {
                // move
                ary[i] = workAry[i - 1];
            }
            // last of workAry
            ary[0] = workAry[ary.length - 1];
        } else if (Direction.LEFT == direction) {
            // Shift to LEFT
            overFlow = ary[0];
            // copy to workAry
            for (int i = 0; i < ary.length; i++) {
                workAry[i] = ary[i];
            }
            // shift
            for (int i = 0; i < ary.length - 1; i++) {
                ary[i] = workAry[i + 1];
            }
            ary[ary.length - 1] = workAry[0];
        }
        return overFlow;
    }

    /**
     * Shift to one
     * 
     * @param int[] ary
     * @param int direction
     * @return int overFlow
     */
    public static int circulationShift(int[] ary, Direction direction) {

        int[] workAry = new int[ary.length];
        int overFlow = -1;

        if (Direction.RIGHT == direction) {
            // Shift to RIGHT
            // overFlow
            overFlow = ary[ary.length - 1];
            // copy to workAry
            for (int i = 0; i < ary.length; i++) {
                workAry[i] = ary[i];
            }
            // shift
            for (int i = 1; i < ary.length; i++) {
                // move
                ary[i] = workAry[i - 1];
            }
            // last of workAry
            ary[0] = workAry[ary.length - 1];
        } else if (Direction.LEFT == direction) {
            // Shift to LEFT
            overFlow = ary[0];
            // copy to workAry
            for (int i = 0; i < ary.length; i++) {
                workAry[i] = ary[i];
            }
            // shift
            for (int i = 0; i < ary.length - 1; i++) {
                ary[i] = workAry[i + 1];
            }
            ary[ary.length - 1] = workAry[0];
        }
        return overFlow;
    }

    /**
     * shift to one
     * 
     * @param ary
     * @param direction
     */
    public static Class<?> circulationShift(Class<?>[] ary, Direction direction) {

        Class<?>[] workAry = new Class<?>[ary.length];
        Class<?> overFlow = null;

        if (Direction.RIGHT == direction) {
            // Shift to RIGHT
            overFlow = ary[ary.length - 1];
            // copy to workAry
            for (int i = 0; i < ary.length; i++) {
                workAry[i] = ary[i];
            }
            // shift
            for (int i = 1; i < ary.length; i++) {
                // move
                ary[i] = workAry[i - 1];
            }
            // last of workAry
            ary[0] = workAry[ary.length - 1];
        } else if (Direction.LEFT == direction) {
            // Shift to LEFT
            overFlow = ary[0];
            // copy to workAry
            for (int i = 0; i < ary.length; i++) {
                workAry[i] = ary[i];
            }
            // shift
            for (int i = 0; i < ary.length - 1; i++) {
                ary[i] = workAry[i + 1];
            }
            ary[ary.length - 1] = workAry[0];
        }
        return overFlow;
    }

    /**
     * Shift To Designation Number
     * 
     * @param ary
     * @param direction
     * @param num
     */
    public static void circulationShift(Object[] ary, Direction direction, int num) {

        Object[] workAry = new Object[ary.length];

        // out of bounds arraySize
        if (num >= ary.length) {
            num %= ary.length;
            System.out.println(num);
        }

        if (Direction.RIGHT == direction) {
            // Shift to RIGHT
            // copy to workAry
            for (int i = 0; i < ary.length; i++) {
                workAry[i] = ary[i];
            }

            for (int i = num; i < ary.length; i++) {
                // move
                ary[i] = workAry[i - num];

            }
            int workIdx = 0;
            for (int i = ary.length - num; i < ary.length; i++) {
                ary[workIdx++] = workAry[i];
            }
        } else if (Direction.LEFT == direction) {
            // Shift to LEFT
            // copy to workAry
            for (int i = 0; i < ary.length; i++) {
                workAry[i] = ary[i];
            }
            // shift
            for (int i = 0; i < ary.length - num; i++) {
                ary[i] = workAry[i + num];
            }
            int workIdx = 0;
            for (int i = ary.length - num; i < ary.length; i++) {
                ary[i] = workAry[workIdx++];
            }

        }
    }

    /**
     * shift to number
     * 
     * @param ary
     * @param direction
     * @param num
     */
    public static void circulationShift(String[] ary, Direction direction, int num) {

        String[] workAry = new String[ary.length];

        // out of bounds arraySize
        if (num >= ary.length) {
            num %= ary.length;
            System.out.println(num);
        }

        if (Direction.RIGHT == direction) {
            // Shift to RIGHT
            // copy to workAry
            for (int i = 0; i < ary.length; i++) {
                workAry[i] = ary[i];
            }

            for (int i = num; i < ary.length; i++) {
                // move
                ary[i] = workAry[i - num];

            }
            int workIdx = 0;
            for (int i = ary.length - num; i < ary.length; i++) {
                ary[workIdx++] = workAry[i];
            }
        } else if (Direction.LEFT == direction) {
            // Shift to LEFT
            // copy to workAry
            for (int i = 0; i < ary.length; i++) {
                workAry[i] = ary[i];
            }
            // shift
            for (int i = 0; i < ary.length - num; i++) {
                ary[i] = workAry[i + num];
            }
            int workIdx = 0;
            for (int i = ary.length - num; i < ary.length; i++) {
                ary[i] = workAry[workIdx++];
            }

        }
    }

    /**
     * shift to number
     * 
     * @param ary
     * @param direction
     * @param num
     */
    public static void circulationShift(int[] ary, Direction direction, int num) {

        int[] workAry = new int[ary.length];

        // out of bounds arraySize
        if (num >= ary.length) {
            num %= ary.length;
            System.out.println(num);
        }

        if (Direction.RIGHT == direction) {
            // Shift to RIGHT
            // copy to workAry
            for (int i = 0; i < ary.length; i++) {
                workAry[i] = ary[i];
            }

            for (int i = num; i < ary.length; i++) {
                // move
                ary[i] = workAry[i - num];

            }
            int workIdx = 0;
            for (int i = ary.length - num; i < ary.length; i++) {
                ary[workIdx++] = workAry[i];
            }
        } else if (Direction.LEFT == direction) {
            // Shift to LEFT
            // copy to workAry
            for (int i = 0; i < ary.length; i++) {
                workAry[i] = ary[i];
            }
            // shift
            for (int i = 0; i < ary.length - num; i++) {
                ary[i] = workAry[i + num];
            }
            int workIdx = 0;
            for (int i = ary.length - num; i < ary.length; i++) {
                ary[i] = workAry[workIdx++];
            }

        }
    }

    /**
     * shift to number
     * 
     * @param ary
     * @param direction
     * @param num
     */
    public static void circulationShift(Class<?>[] ary, Direction direction, int num) {

        Class<?>[] workAry = new Class<?>[ary.length];

        // out of bounds arraySize
        if (num >= ary.length) {
            num %= ary.length;
            System.out.println(num);
        }

        if (Direction.RIGHT == direction) {
            // Shift to RIGHT
            // copy to workAry
            for (int i = 0; i < ary.length; i++) {
                workAry[i] = ary[i];
            }

            for (int i = num; i < ary.length; i++) {
                // move
                ary[i] = workAry[i - num];

            }
            int workIdx = 0;
            for (int i = ary.length - num; i < ary.length; i++) {
                ary[workIdx++] = workAry[i];
            }
        } else if (Direction.LEFT == direction) {
            // Shift to LEFT
            // copy to workAry
            for (int i = 0; i < ary.length; i++) {
                workAry[i] = ary[i];
            }
            // shift
            for (int i = 0; i < ary.length - num; i++) {
                ary[i] = workAry[i + num];
            }
            int workIdx = 0;
            for (int i = ary.length - num; i < ary.length; i++) {
                ary[i] = workAry[workIdx++];
            }
        }
    }

}