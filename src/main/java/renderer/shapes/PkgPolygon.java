package renderer.shapes;

import renderer.point.PkgPoint;
import renderer.point.PointConverter;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PkgPolygon {
    private PkgPoint[] points;
    private Color color;

    public PkgPolygon(Color color, PkgPoint... points){
        this.points = new PkgPoint[points.length];
        this.color = color;
        for(int i = 0; i < points.length; i++){
            PkgPoint p = points[i];
            this.points[i] = new PkgPoint(p.x, p.y, p.z);
        }
    }

    public PkgPolygon(PkgPoint... points){
        this.points = new PkgPoint[points.length];
        this.color = Color.GREEN;
        for(int i = 0; i < points.length; i++){
            PkgPoint p = points[i];
            this.points[i] = new PkgPoint(p.x, p.y, p.z);
        }
    }

    public void render(Graphics g){
        Polygon poly = new Polygon();
        for(int i = 0; i< points.length; i++){
            Point p = PointConverter.convertPoint(points[i]);
            poly.addPoint(p.x, p.y);
        }
        g.setColor(this.color);
        g.fillPolygon(poly);
    }

    public void setColor(Color color){
        this.color = color;
    }

    public void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees){
        for(PkgPoint p: points){
            PointConverter.rotateAxisX(p, CW, xDegrees);
            PointConverter.rotateAxisY(p, CW, yDegrees);
            PointConverter.rotateAxisZ(p, CW, zDegrees);
        }
    }

    public double getAverageX(){
        double sum = 0;
        for(PkgPoint p: this.points){
            sum += p.x;
        }
        return sum/this.points.length;
    }

    //Read up on static methods
    public static PkgPolygon[] sortPolygons(PkgPolygon[] polygons){
        List<PkgPolygon> polygonList = new ArrayList<>();
        for(PkgPolygon poly: polygons){
            polygonList.add(poly);
        }
        Collections.sort(polygonList, new Comparator<PkgPolygon>() {
            @Override
            public int compare(PkgPolygon o1, PkgPolygon o2) {
                return o2.getAverageX() - o1.getAverageX() < 0 ? 1:-1;
            }
        });
        for(int i = 0; i < polygons.length; i++){
            polygons[i] = polygonList.get(i);
        }
        return polygons;
    }
}
