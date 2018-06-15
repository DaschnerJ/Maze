import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.ArrayList;

public class MazeDisplay
{
    int size = 900;
    public void start(Stage primaryStage){

        primaryStage.setTitle("Maze Gen");
        Button btn = new Button();
        Line line = new Line(100, 10, 10, 110); //error
        //Line2D line = new Line2D(); error
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();

    }

    public void start2(Stage primaryStage) {
        primaryStage.setTitle("Drawing Operations Test");
        Group root = new Group();
        StackPane holder = new StackPane();
        Canvas canvas = new Canvas(300, 250);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawShapes(gc);
        holder.getChildren().add(canvas);
        root.getChildren().add(holder);
        holder.setStyle("-fx-background-color: black");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void start3(Stage primaryStage, Maze m) {
        primaryStage.setTitle("Drawing Maze");
        Group root = new Group();
        StackPane holder = new StackPane();
        Canvas canvas = new Canvas(size, size);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawMaze(m, gc);
        holder.getChildren().add(canvas);
        root.getChildren().add(holder);
        holder.setStyle("-fx-background-color: black");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void drawShapes(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);
        gc.strokeLine(40, 10, 10, 40);
        gc.fillOval(10, 60, 30, 30);
        gc.strokeOval(60, 60, 30, 30);
        gc.fillRoundRect(110, 60, 30, 30, 10, 10);
        gc.strokeRoundRect(160, 60, 30, 30, 10, 10);
        gc.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN);
        gc.fillArc(60, 110, 30, 30, 45, 240, ArcType.CHORD);
        gc.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND);
        gc.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN);
        gc.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD);
        gc.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND);
        gc.fillPolygon(new double[]{10, 40, 10, 40},
                new double[]{210, 210, 240, 240}, 4);
        gc.strokePolygon(new double[]{60, 90, 60, 90},
                new double[]{210, 210, 240, 240}, 4);
        gc.strokePolyline(new double[]{110, 140, 110, 140},
                new double[]{210, 210, 240, 240}, 4);
    }

    private void drawMaze(Maze m, GraphicsContext gc)
    {
        Thread draw = new Thread()
        {
            public void run()
            {
                m.generate2(gc, size);
                Main.debug("Setting stroke to white.");
                gc.setStroke(Color.RED);
                Main.debug("Setting width to 5.");
                gc.setLineWidth(8);
                int scale = (5*m.findMaxY())/(m.findMaxX());
                Main.debug("Setting scale to "+scale+".");
                ArrayList<Point> points = m.points;
                Main.debug("Drawing "+points.size()+" points.");
                int maxX = m.findMaxX();
                int maxY = m.findMaxY();
                points.forEach(p ->
                {
                        int pX = p.getX()*scale + (size/2) - (maxX*scale)/2;
                        int pY = p.getY()*scale + (size/2) - (maxY*scale)/2;
                        Main.debug("Drawing point ("+pX +","+ pY+").");
                        gc.strokeLine(pX, pY, pX, pY);
                });
                gc.setStroke(Color.WHITE);
                points.forEach(p ->
                {
                    ArrayList<Point> to = p.to;
                    to.forEach(t ->
                    {
                        int pX = p.getX()*scale + (size/2) - (maxX*scale)/2;
                        int pY = p.getY()*scale + (size/2) - (maxY*scale)/2;
                        int tX = t.getX()*scale + (size/2) - (maxX*scale)/2;
                        int tY = t.getY()*scale + (size/2) - (maxY*scale)/2;
                        Main.debug("Drawing from point ("+pX +","+ pY+") to point ("+ tX +","+ tY +").");
                        gc.strokeLine(pX, pY, tX, tY);
                        try {
                            sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                });

            }
        };

        draw.start();

    }

}
