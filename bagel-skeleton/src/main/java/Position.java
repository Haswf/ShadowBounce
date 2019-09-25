import bagel.util.Point;

public class Position {
    private Point centre; // position of the centre of the object on the screen
    private Point topLeft; // top left point of the object on the screen
    private double height;
    private double width;

    public Position(){
        this.centre = new Point(0,0);
    }

    public Position(Point centre){
        this.centre = centre;
    }

    public Position(Point centre, Point topLeft){
        this.centre = centre;
        this.topLeft = topLeft;
        this.height = 2 * (centre.y - topLeft.y);
        this.width = 2 * (centre.x - topLeft.x);
    }

    public Position(Point centre, Double width, Double height){
        this.centre = centre;
        this.topLeft = new Point(centre.x - width/2, centre.y - height/2);
        this.height = height;
        this.width = width;
    }

    public Position(Position other){
        this.centre = new Point(other.centre.x, other.centre.y);
        this.topLeft = new Point(other.topLeft.x, other.topLeft.y);
        this.height = other.height;
        this.width = other.width;
    }

    Point getCentre(){
        return new Point(this.centre.x, this.centre.y);
    }

    Position setCentre(Point newCentre){
        this.centre = newCentre;
        this.topLeft = new Point(newCentre.x - width/2, newCentre.y - height/2);
        return this;
    }

    Point getTopLeft(){
        return new Point(this.topLeft.x, this.topLeft.y);
    }

    void setTopLeft(Point newTopLeft){
        this.topLeft = newTopLeft;
        this.centre = new Point(newTopLeft.x + width/2, newTopLeft.y + height/2);
    }

    public boolean equals(Object other) {
        // check if references are the same
        if (this == other)
            return true;
        // check if the object exists
        if (other == null)
            return false;
        // type check before casting
        if (this.getClass() != other.getClass())
            return false;
        Position p = (Position) other;
        // TODO: equals is not working due to Bagel.Point bug
        return this.getCentre().equals(p.getCentre())
                && this.getTopLeft().equals(p.getTopLeft());
    }

    public String toString(){
        return String.format("Point center at (%f, %f)", this.getCentre().x, this.getCentre().y);
    }

    /* Return distance from this GameObject to another. */
    public double distance(GameObject other){
        return other.getPosition().getCentre().asVector().sub(this.getCentre().asVector()).length();
    }

    /* Return distance from this GameObject to a point. */
    public double distance(Point other){
        return other.asVector().sub(this.getCentre().asVector()).length();
    }

    /* Return distance from this GameObject to another Position. */
    public double distance(Position other){
        return other.getCentre().asVector().sub(this.getCentre().asVector()).length();
    }

}
