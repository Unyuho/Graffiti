package net.minecraft.graffiti.kawo;

import java.util.Comparator;


public class Point {
        public enum Axis {
                X, Y, ;
        }


        /**
         * x、y、それぞれについて、<br>
         * より大きい値を採用した新しいPointを返す
         *
         * @param a
         * @param b
         * @return
         */
        public static Point maxPoint(Point a, Point b) {
                return comparedPoint(a, b, new Comparator<Double>() {
                        @Override
                        public int compare(Double o1, Double o2) {
                                return o1 - o2 > 0 ? -1 : 1;// o1のほうが大きければ-1(o1側を採用)
                        }
                });
        }


        /**
         * x、y、それぞれについて、<br>
         * より小さい値を採用した新しいPointを返す
         *
         * @param a
         * @param b
         * @return
         */
        public static Point minPoint(Point a, Point b) {
                return comparedPoint(a, b, new Comparator<Double>() {
                        @Override
                        public int compare(Double o1, Double o2) {
                                return o1 - o2 < 0 ? -1 : 1;// o1のほうが小さければ-1(o1側を採用)
                        }
                });
        }


        /**
         * x、y、それぞれについて<br>
         * comparator.compareが負の場合にa、それ以外ではbの値を採用した<br>
         * 新しいPointを返す
         *
         * @param a
         * @param b
         * @param comparator
         * @return
         */
        private static Point comparedPoint(Point a, Point b, Comparator<Double> comparator) {
                double x = 0, y = 0;
                for (Axis axis : Axis.values()) {
                        Double aValue = a.get(axis);
                        Double bValue = b.get(axis);
                        double maxValue = comparator.compare(aValue, bValue) < 0 ? aValue : bValue;
                        switch (axis) {
                        case X:
                                x = maxValue;
                                break;
                        case Y:
                                y = maxValue;
                                break;
                        }
                }
                return new Point(x, y);
        }


        public final double x;


        public final double y;


        public Point(double x, double y) {
                this.x = x;
                this.y = y;
        }


        public Point(Point from, Point to) {
                this(to.x - from.x, to.y - from.y);
        }


        public Point addedPoint(Point point) {
                return new Point(x + point.x, y + point.y);
        }


        public Double get(Axis key) {
                switch (key) {
                case X:
                        return x;
                case Y:
                        return y;
                }
                throw new IllegalArgumentException(key.toString());
        }


        @Override
        public String toString() {
                return "P[" + x + "," + y + "](" + super.toString() + ")";
        }


        Point multiplyPoint(double factor) {
                return new Point(x * factor, y * factor);
        }


}
