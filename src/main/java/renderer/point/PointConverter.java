package renderer.point;

import renderer.Display;

import java.awt.*;

import static java.lang.Math.pow;

public class PointConverter {

    private static double scale = 1;
    private static final double zoomFactor = 1.07;

    public static Point convertPoint(PkgPoint point3D){
        double x3d = point3D.y * scale;
        double y3d = point3D.z * scale;
        double depth = point3D.x * scale;
        double[] newVal = scale(x3d, y3d, depth);

        int x2d = (int)(Display.WIDTH/2 + newVal[0]);
        int y2d = (int)(Display.HEIGHT/2 - newVal[1]);

        return new Point(x2d, y2d);
    }

    private static double[] scale(double x3d, double y3d, double depth){
        double dist = Math.sqrt(Math.pow(x3d, 2) + pow(y3d, 2));
        double angle = Math.atan2(y3d, x3d);
        double depth_camera = 15 - depth;
        double localScale = Math.abs(1400 / (depth_camera + 1400));
        dist = dist * localScale;
        double[] newVal = new double[2];
        newVal[0] = dist * Math.cos(angle);
        newVal[1] = dist * Math.sin(angle);
        return newVal;
    }

    public static void rotateAxisX(PkgPoint p, boolean CW, double degrees){
        double radius = Math.sqrt(pow(p.y, 2) + pow(p.z, 2));
        double angle = Math.atan2(p.y, p.z);
        angle += 2 * Math.PI / 360 * degrees * (CW ? 1 : -1);
        p.y = radius * Math.sin(angle);
        p.z = radius * Math.cos(angle);

    }

    public static void rotateAxisY(PkgPoint p, boolean CW, double degrees){
        double radius = Math.sqrt(pow(p.x, 2) + pow(p.z, 2));
        double angle = Math.atan2(p.x, p.z);
        angle += 2 * Math.PI / 360 * degrees * (CW ? -1 : 1);
        p.x = radius * Math.sin(angle);
        p.z = radius * Math.cos(angle);

    }

    public static void rotateAxisZ(PkgPoint p, boolean CW, double degrees){
        double radius = Math.sqrt(pow(p.y, 2) + pow(p.x, 2));
        double angle = Math.atan2(p.y, p.x);
        angle += 2 * Math.PI / 360 * degrees * (CW ? -1 : 1);
        p.y = radius * Math.sin(angle);
        p.x = radius * Math.cos(angle);

    }

    public static void zoomIn(){
        scale = scale * zoomFactor;
    }

    public static void zoomOut(){
        scale = scale / zoomFactor;
    }

}
