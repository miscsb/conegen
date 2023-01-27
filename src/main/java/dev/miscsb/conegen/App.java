package dev.miscsb.conegen;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import dev.miscsb.conegen.controller.*;
import dev.miscsb.conegen.util.Camera;
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

        private List<PointGroupController> groups;
        private Camera camera;

        private int factor = 128;

        public Board() {
            initBoard();
            this.camera = new Camera(new Point3D(0, 0, -25), Quaternion.IDENTITY, 1);

            CubeController cubeController = new CubeController(10, Color.WHITE);

            double d = 5;
            List<PointGroupController> axes = List.of(
                new LineController(Point3D.ORIGIN, new Point3D(d, 0, 0), Color.RED),
                new LineController(Point3D.ORIGIN, new Point3D(0, d, 0), Color.GREEN),
                new LineController(Point3D.ORIGIN, new Point3D(0, 0, d), Color.BLUE)
            );

            CircleController circleController = new CircleController(10, new double[] {1, 1, 1}, 32, Color.BLUE);

            groups = new ArrayList<>();
            groups.add(cubeController);
            groups.add(circleController);
            groups.addAll(axes);
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
            drawCameraPosition(g);
            drawGroups(g);
        }

        private void drawGroups(Graphics g) {
            Toolkit.getDefaultToolkit().sync();
            g.setColor(Color.WHITE);
            for (PointGroupController shape : this.groups) {
                g.setColor(shape.getColor());
                for (double[] line : camera.projectLines(shape.getPoints(), shape.getEdges())) {
                    g.drawLine(
                        (int) (factor * line[0]) + W_WIDTH / 2,
                        (int) (factor * line[1]) + W_HEIGHT / 2,
                        (int) (factor * line[2]) + W_WIDTH / 2,
                        (int) (factor * line[3]) + W_HEIGHT / 2);
                }
            }
        }

        private void drawCameraPosition(Graphics g) {
            Toolkit.getDefaultToolkit().sync();
            g.setColor(Color.WHITE);
            g.drawString(camera.toString(), 10, 20);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            groups.forEach(PointGroupController::applyAll);
            repaint();
        }

        private double yaw = 0, pitch = 0, roll = Math.PI;
        @Override
        public void keyPressed(KeyEvent e) {
            double step = e.isShiftDown() ? 5.0 : 1.0;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT: 
                    camera.pinhole.x -= step; break;
                case KeyEvent.VK_RIGHT: 
                    camera.pinhole.x += step; break;
                case KeyEvent.VK_UP: 
                    camera.pinhole.z += step; break;
                case KeyEvent.VK_DOWN: 
                    camera.pinhole.z -= step; break;
                case 'i': case 'I':
                    camera.pinhole.y += step; break;
                case 'k': case 'K':
                    camera.pinhole.y -= step; break;
                case '-': case '_':
                    camera.focalLength += step*0.1; break;
                case '=': case '+':
                    camera.focalLength -= step*0.1; break;

                case 'a': case 'A': yaw += step*0.1; break;
                case 'd': case 'D': yaw -= step*0.1; break;
                case 'w': case 'W': pitch += step*0.1; break;
                case 's': case 'S': pitch -= step*0.1; break;
            }
            camera.orientation = QuaternionUtil.yawPitchRollToQuaternion(yaw, pitch, roll);
        }

        @Override
        public void keyReleased(KeyEvent arg0) {}

        @Override
        public void keyTyped(KeyEvent arg0) {}
    }
}

