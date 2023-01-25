package dev.miscsb.conegen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import dev.miscsb.conegen.controller.*;
import dev.miscsb.conegen.transformations.*;
import dev.miscsb.conegen.util.Camera;
import dev.miscsb.conegen.util.MatrixUtil;
import dev.miscsb.conegen.util.Point2D;
import dev.miscsb.conegen.util.Point3D;
import dev.miscsb.conegen.util.Quaternion;
import dev.miscsb.conegen.util.QuaternionUtil;

public class App extends JFrame {
    public static final int W_WIDTH = 900;
    public static final int W_HEIGHT = 600;
    
    public App() { initUI(); }
    
    private void initUI() {
        add(new Board());
        setResizable(false);
        pack();
        setTitle("conegen");
        setLocationRelativeTo(null);        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            App ex = new App();
            ex.setVisible(true);
            ex.setResizable(true);
            ex.setBounds(0, 0, W_WIDTH, W_HEIGHT);
        });
    }

    class Board extends JPanel
        implements ActionListener, KeyListener {

        private Timer timer;

        private CubeController cubeController;
        private Camera camera;

        private List<ShapeController> groups;

        private PointGroup axes;

        private static final double FACTOR = 100d;

        public Board() {
            initBoard();
            this.camera = new Camera(new Point3D(0, 0, 10), -5, QuaternionUtil.axisAngleToQuaternion(0.2, 0, 1, 0));

            this.cubeController = new CubeController(10);
            this.cubeController.setTransformation(new Translation(0, 0, 0));
            this.cubeController.applyAll();

            // Quaternion rq1 = QuaternionUtil.getRotationQuaternion(Math.PI / 128, MatrixUtil.toUnitVector(0, 0, 1));
            // Transformation r = new Rotation(rq1).about(cubeController.getCenter());
            // Transformation transl = new Translation(0, 0, 10);
            // cubeController.setTransformation(r);

            cubeController.setTransformation(new Translation(0, 0, 0));

            double d = 5;

            this.axes = new PointGroup(
                new Point3D[] {
                    new Point3D(-d, 0, 0),
                    new Point3D(d, 0, 0),
                    new Point3D(0, -d, 0),
                    new Point3D(0, d, 0),
                    new Point3D(0, 0, -d),
                    new Point3D(0, 0, d)
                },
                new int[][] {
                    {0, 1},
                    {2, 3},
                    {4, 5}
                },
                new Point3D(0, 0, 0)
            );
            this.axes.setColor(Color.GREEN);

            groups = new ArrayList<>();
            groups.add(this.cubeController);
            groups.add(this.axes);
        }

        private void initBoard() {
            setBackground(Color.BLACK);
            setPreferredSize(new Dimension(800, 800));
            addKeyListener(this);
            setFocusable(true);
            
            timer = new Timer(24, this);
            timer.start();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawGroups(g);
        }

        private void drawGroups(Graphics g) {
            Toolkit.getDefaultToolkit().sync();
            g.setColor(Color.WHITE);
            for (ShapeController shape : this.groups) {
                g.setColor(shape.getColor());
                List<Point2D> points = Arrays.stream(shape.getPoints()).map(camera::projectPoint).map(this::adjust).toList();
                Arrays.stream(shape.getEdges()).forEach(arr -> {
                    Point2D p1 = points.get(arr[0]), p2 = points.get(arr[1]);
                    g.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
                });
            }
        }

        private Point2D adjust(Point2D point) {
            return new Point2D(
                (int) (FACTOR * point.x) + W_WIDTH / 2, 
                (int) (FACTOR * point.y) + W_HEIGHT / 2);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            this.cubeController.applyAll();
            repaint();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            double step = e.isShiftDown() ? 5.0 : 1.0;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT: 
                    camera.pinhole.x += step; break;
                case KeyEvent.VK_RIGHT: 
                    camera.pinhole.x -= step; break;
                case KeyEvent.VK_UP: 
                    camera.pinhole.z += step; break;
                case KeyEvent.VK_DOWN: 
                    camera.pinhole.z -= step; break;
                case '-':
                    camera.pinhole.y -= step; break;
                case '=':
                    camera.pinhole.y += step; break;
            }
            System.out.println(camera.projectPoint(new Point3D(0, 0, -100)));
        }

        @Override
        public void keyReleased(KeyEvent arg0) {}

        @Override
        public void keyTyped(KeyEvent arg0) {}
    }
}

