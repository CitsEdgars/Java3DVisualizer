package renderer.shapes;

import renderer.point.PkgPoint;
import renderer.point.PointConverter;

import java.awt.*;

public class Tetrahedron {
    private PkgPolygon[] polygon;
    private Color color;

    public Tetrahedron(Color color, PkgPolygon... polygons){
        this.color = color;
        this.polygon = polygons;
        this.setPolygonColor();
    }

    public Tetrahedron(PkgPolygon... polygons){
        this.polygon = polygons;
    }

    public void render(Graphics g){
        for(PkgPolygon poly: this.polygon){
            poly.render(g);
        }
    }

    private void sortPolygons(){
        PkgPolygon.sortPolygons(this.polygon);
    }

    public void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees){
        for(PkgPolygon p: this.polygon){
            p.rotate(CW, xDegrees, yDegrees, zDegrees);
        }
        this.sortPolygons();
    }

    private void setPolygonColor(){
        for(PkgPolygon poly: this.polygon){
            poly.setColor(this.color);
        }
    }



}
