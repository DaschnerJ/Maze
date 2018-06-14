import java.util.ArrayList;
import java.util.Random;

public class Maze {

    ArrayList<Point> points;

    ArrayList<Point> ends;

    int seed;

    Random r;

    public Maze(ArrayList<Point> points, int seed)
    {
        this.seed = seed;
        this.points = new ArrayList<>();
        this.ends = new ArrayList<>();
        points.addAll(points);
        points.forEach(x ->
        {
            x.updateAdjacent(points);
        });
         r = new Random(seed);
    }

    public Maze(int dimX, int dimY, int seed)
    {
        this.seed = seed;
        this.ends = new ArrayList<>();
        for(int i = 0; i < dimX; i++)
        {
            for(int j = 0; j < dimY; i++)
            {
                Point p = new Point(i, j, seed);
                points.add(p);
            }
        }
        points.forEach(x ->
        {
            x.updateAdjacent(points);
        });
        r = new Random(seed);
    }

    public void addPoints(ArrayList<Point> points)
    {
        points.addAll(points);
    }

    public void addEndPoint(Point p)
    {
        ends.add(p);
    }

    public void generate()
    {
        while(!ends.isEmpty())
        {
            Point p = ends.get(r.nextInt(ends.size()));
            Point ret = p.pickTo();

            p.updateAdjacent(points);
            ret.updateAdjacent(points);
            
            checkEnd(p);
            checkEnd(ret);
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


}
