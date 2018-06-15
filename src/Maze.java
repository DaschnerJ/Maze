import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Thread.sleep;

public class Maze {

    ArrayList<Point> points;

    ArrayList<Point> ends;

    int seed;

    Random r;

    public Maze(ArrayList<Point> list, int seed)
    {
        this.seed = seed;
        this.points = new ArrayList<>();
        this.ends = new ArrayList<>();
        list.forEach(x ->
        {
            points.add(x);
        });
        points.forEach(x ->
        {
            x.updateAdjacent(points);
        });
//        points.forEach(x ->
//        {
//            Main.debug("Point (" + x.getX() + ", " + x.getY() + ") ["+x.getTo().size()+"].");
//        });
         r = new Random(seed);
    }

    public Maze(int dimX, int dimY, int seed)
    {
        Main.debug("Setting seed...");
        this.seed = seed;
        Main.debug("Creating end points array...");
        this.ends = new ArrayList<>();
        Main.debug("Creating points array...");
        this.points = new ArrayList<>();
        for(int i = 0; i < dimX; i++)
        {
            for(int j = 0; j < dimY; j++)
            {
                Main.debug("Making new point ("+i+","+j+")...");
                Point p = new Point(i, j, seed);
                points.add(p);
            }
        }
        Main.debug("Updating information about adjacent points...");
        points.forEach(x ->
        {
            x.updateAdjacent(points);
        });
        r = new Random(seed);
    }

    public void addPoints(ArrayList<Point> points)
    {
        Main.debug("Adding "+points.size()+" points...");
        points.addAll(points);
    }

    public void addEndPoint(Point p)
    {
        Main.debug("Adding a new end point.");
        ends.add(p);
    }

    ArrayList<Point> temp = new ArrayList<>();

    public void generate2(GraphicsContext gc, int size)
    {
        Main.debug("Setting stroke to white.");
        gc.setStroke(Color.RED);
        Main.debug("Setting width to 5.");
        gc.setLineWidth(8);
        int scale = (5*this.findMaxY())/(this.findMaxX());
        Main.debug("Setting scale to "+scale+".");
        ArrayList<Point> points = this.points;
        Main.debug("Drawing "+points.size()+" points.");
        int maxX = this.findMaxX();
        int maxY = this.findMaxY();
        points.forEach(p ->
        {
            int pX = p.getX()*scale + (size/2) - (maxX*scale)/2;
            int pY = p.getY()*scale + (size/2) - (maxY*scale)/2;
            Main.debug("Drawing point ("+pX +","+ pY+").");
            gc.strokeLine(pX, pY, pX, pY);
        });

        if(temp.isEmpty())
        {
            temp.add(points.get(r.nextInt(points.size())));
        }
        Point last = temp.get(0);
        while(temp.size() < points.size())
        {
            Point current = last.pickToNoRepeat(r);
            if(current != null) {
                int pX = last.getX()*scale + (size/2) - (maxX*scale)/2;
                int pY = last.getY()*scale + (size/2) - (maxY*scale)/2;
                int cX = current.getX()*scale + (size/2) - (maxX*scale)/2;
                int cY = current.getY()*scale + (size/2) - (maxY*scale)/2;
                Main.debug("Drawing point ("+pX +","+ pY+").");
                gc.setStroke(Color.DARKSLATEBLUE);
                gc.strokeLine(pX, pY, pX, pY);
                gc.setStroke(Color.AQUA);
                gc.strokeLine(cX, cY, cX, cY);
                last = current;
                if (!temp.contains(last)) {
                    temp.add(last);
                }
            }
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void generate()
    {
        Main.debug("Checking if there is end points.");
        if(ends.isEmpty())
        {
            Main.debug("Adding a starting end point since none exists.");
            addEndPoint(points.get(r.nextInt(points.size())));
        }
        while(!ends.isEmpty())
        {
            Main.debug("Picking from " + ends.size() + " points.");
            int pick = r.nextInt(ends.size());
            Main.debug("Picked point " + pick + ".");
            Point p = ends.get(pick);
            if(p == null)
            {
                System.out.println("Point is null!");
            }
            Main.debug("MinX: " + findMinX() + " MinY: " + findMinY() + " MaxX: " + findMaxX() + " MaxY: " + findMaxY());
            if(p.getTo().size() > 1)
            {
                Main.debug("This point has more than two connections. Rolling chance for it to connect again.");
                if(r.nextInt(100) <= 5)
                {
                    Main.debug("Skipping.");
                    continue;
                }
            }
            Main.debug("Picking new path for point ("+p.getX()+","+p.getY()+").");
            Point ret = p.pickTo();
            if(ret != null) {
                Main.debug("Picked new point (" + ret.getX() + "," + ret.getY() + ").");
                Main.debug("Updating adjacent points...");
                p.updateAdjacent(points);
                ret.updateAdjacent(points);
                Main.debug("Checking if new points are still end points.");
                checkEnd(p);
                checkEnd(ret);
            }
            else
            {
                Main.debug("Failed to find a point.");
                Main.debug("Updating adjacent points...");
                p.updateAdjacent(points);
                Main.debug("Checking if the new point are still and end point.");
                checkEnd(p);
            }
        }
    }

    public void checkEnd(Point p)
    {
        if(p.isClosed())
        {
            if(ends.contains(p))
                ends.remove(p);
        }
        else
        {
            if(!ends.contains(p))
                ends.add(p);
        }
    }

    public int findMaxX()
    {
        final int[] x = {points.get(0).getX()};
        points.forEach(p -> {
            if(p.getX() > x[0])
                x[0] = p.getX();
        });
        return x[0];
    }

    public int findMaxY()
    {
        final int[] x = {points.get(0).getY()};
        points.forEach(p -> {
            if(p.getY() > x[0])
                x[0] = p.getY();
        });
        return x[0];
    }

    public int findMinX()
    {
        final int[] x = {points.get(0).getX()};
        points.forEach(p -> {
            if(p.getX() < x[0])
                x[0] = p.getX();
        });
        return x[0];
    }

    public void removeIslands()
    {
        points.forEach(p ->
        {
            int x = p.getX();
            int y = p.getY();
            Point a = findPoint(x+1, y);
            Point b = findPoint(x + 1, y + 1);
            Point c = findPoint(x, y+1);
            if(a != null && b != null && c != null)
            {
                Main.debug("There is 4 points.");
                if(p.getTo().contains(a) && a.getTo().contains(b) && b.getTo().contains(c) && c.getTo().contains(p))
                {
                    Main.debug("Each point is connected and forms an island!");
                    unlinkIsland(p, a, b, c, 5,0);
                }
            }
        });
    }

    public void unlinkIsland(Point p, Point a, Point b, Point c, int attempts, int count)
    {
        if(count < attempts) {
            int rand = r.nextInt(4);
            switch (rand) {
                case 0: {
                    if (p.getTo().size() > 1 && a.getTo().size() > 1) {
                        p.to.remove(a);
                        a.to.remove(p);
                    } else {
                        unlinkIsland(p, a, b, c, 3, count++);
                    }
                    break;
                }
                case 1: {
                    if (b.getTo().size() > 1 && a.getTo().size() > 1) {
                        a.to.remove(b);
                        b.to.remove(a);
                    } else {
                        unlinkIsland(p, a, b, c, 3, count++);
                    }
                    break;
                }
                case 2: {
                    if (b.getTo().size() > 1 && c.getTo().size() > 1) {
                        b.to.remove(c);
                        c.to.remove(b);
                    } else {
                        unlinkIsland(p, a, b, c, 3, count++);
                    }
                    break;
                }
                case 3: {
                    if (c.getTo().size() > 1 && p.getTo().size() > 1) {
                        c.to.remove(p);
                        p.to.remove(c);
                    } else {
                        unlinkIsland(p, a, b, c, 3, count++);
                    }
                    break;
                }
                default: {
                    if (c.getTo().size() > 1 && p.getTo().size() > 1) {
                        c.to.remove(p);
                        p.to.remove(c);
                    } else {
                        unlinkIsland(p, a, b, c, 3, count++);
                    }
                    break;
                }
            }
        }
    }

    public Point findPoint(int x, int y)
    {
        final Point[] p = {null};
        points.forEach(t ->
        {
            if(t.getX() == x && t.getY() == y)
                p[0] = t;
        });
        return p[0];
    }

    public int findMinY()
    {
        final int[] x = {points.get(0).getY()};
        points.forEach(p -> {
            if(p.getY() < x[0])
                x[0] = p.getY();
        });
        return x[0];
    }


}
