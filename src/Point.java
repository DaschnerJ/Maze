import java.util.ArrayList;
import java.util.Random;

public class Point {

    int x,y;
    ArrayList<Point> to;
    ArrayList<Point> adjacent;

    int seed;

    public Point(int x, int y, int seed)
    {
        this.x = x;
        this.y = y;
        this.seed = seed;
        to = new ArrayList<>();
        adjacent = new ArrayList<>();
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public ArrayList<Point> getAdjacent(ArrayList<Point> points)
    {
        if(!adjacent.isEmpty())
            return adjacent;
        ArrayList<Point> adjacent = new ArrayList<>();

        points.forEach(p ->
        {
            if(p.getX() != x && p.getY() == y)
            {
                if(p.getX() < x+2 && p.getX() > x-2 && p.getY() < x+2 && p.getX() > x-2)
                {
                    adjacent.add(p);
                }
            }
        });

        return adjacent;
    }

    public void updateAdjacent(ArrayList<Point> points)
    {
        ArrayList<Point> adjacent = new ArrayList<>();

        points.forEach(p ->
        {
            if(p.getX() != x && p.getY() == y)
            {
                if(p.getX() < x+2 && p.getX() > x-2 && p.getY() < x+2 && p.getX() > x-2)
                {
                    adjacent.add(p);
                }
            }
        });

        this.adjacent = adjacent;
    }

    public boolean isClosed()
    {
        if(getPathable().isEmpty())
        {
            return true;
        }
        return false;
    }

    public void setTo(Point p)
    {
        to.add(p);
    }

    public void addTo(Point p)
    {
        to.add(p);
        p.setTo(this);
    }

    public ArrayList<Point> getTo()
    {
        return to;
    }

    public ArrayList<Point> getPathable()
    {
        ArrayList<Point> pathable = new ArrayList<>();
        for(Point p : adjacent)
        {
            if(p.getTo().size() < 2)
            {
                pathable.add(p);
            }
        }
        return pathable;
    }

    public Point pickTo()
    {
        Random r = new Random(seed);
        if(!isClosed()) {
            ArrayList<Point> paths = getPathable();
            Point p = paths.get(r.nextInt(paths.size()));
            addTo(p);
            return p;
        }
        return null;
    }


}
