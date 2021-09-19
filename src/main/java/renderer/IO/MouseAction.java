package renderer.IO;

import java.awt.event.*;

public class MouseAction implements MouseListener, MouseMotionListener, MouseWheelListener {

    private int mouseX = -1;
    private int mouseY = -1;
    private int mouseB = -1;
    private int scroll = 0;

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public boolean isScrollingUp(){
        return this.scroll == 1;
    }

    public boolean isScrollingDown(){
        return this.scroll == -1;
    }

    public void resetMouseWheel(){
        this.scroll = 0;
    }

    public Click getMouseB() {
        switch(this.mouseB){
            case 1:
                return Click.MOUSE_LEFT;
            case 2:
                return Click.MOUSE_SCROLL;
            case 3:
                return Click.MOUSE_RIGHT;
            default:
                return Click.UNKNOWN;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        //1-left click
        //2-roller
        //3-right click
        this.mouseB = e.getButton();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.mouseB = -1;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.mouseX = e.getX();
        this.mouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.mouseX = e.getX();
        this.mouseY = e.getY();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        scroll = e.getWheelRotation();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
