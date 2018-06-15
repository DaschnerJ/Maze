import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    static boolean debug = true;
    private static Maze maze;
    public static void main(String[] args)
    {
        debug("Creating new maze.");
        Maze m = new Maze(10, 26,1);
        debug("Setting seeded tiles.");
        //m.addEndPoint(m.findPoint(0, 0));
        //m.addEndPoint(m.findPoint(35, 35 ));
        debug("Generating...");
        debug("Launching stage.");
        maze = m;
        launch();

    }

    public static ArrayList<Point> points()
    {
        ArrayList<Point> points = new ArrayList<>();
        for(int j = 0; j < 10; j++) {
            for (int i = 0; i < 26; i++) {
                points.add(new Point(i, j, 0));
            }
        }
        for(int j = 0; j < 10; j++) {
            for (int i = 0; i < 26; i++) {
                points.add(new Point(j+26, i, 0));
            }
        }
        for(int j = 0; j < 10; j++) {
            for (int i = 0; i < 26; i++) {
                points.add(new Point(i, j+16, 0));
            }
        }
        return points;
    }

    public static void debug(String msg)
    {
        if(debug)
            System.out.println(msg);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        debug("Creating the display.");
        MazeDisplay md = new MazeDisplay();
        debug("Launching the display...");
        md.start3(primaryStage, maze);
    }
}
