package net.minecraft.graffiti.kawo;

import net.minecraft.util.MathHelper;

public class Vec2DImpl implements Vec2D {
        public enum PointType {
                FROM, TO, ;
        }


        protected final Point from;
        protected final Point to;


        public Vec2DImpl(double fX, double fY, double tX, double tY) {
                this(new Point(fX, fY), new Point(tX, tY));
        }


        public Vec2DImpl(Point from, Point to) {
                this.from = from;
                this.to = to;
        }


        public Point get(PointType key) {
                switch (key) {
                case FROM:
                        return from;
                case TO:
                        return to;
                }
                throw new IllegalArgumentException(key.toString());
        }


        @Override
        public Point getFrom() {
                return get(PointType.FROM);
        }


        @Override
        public double getLength() {
                return MathHelper.sqrt_double(getLengthSquare());
        }


        @Override
        public double getLengthSquare() {
                Point p = new Point(from, to);
                return p.x * p.x + p.y * p.y;
        }


        @Override
        public Point getTo() {
                return get(PointType.TO);
        }


        @Override
        public Point getUnitVectorPoint() {
                double length = getLength();
                Point p = new Point(from, to);


                return p.multiplyPoint(1.0D / length);
        }


        @Override
        public String toString() {
                return "V[from" + from + ", to" + to + "]";
        }
}
