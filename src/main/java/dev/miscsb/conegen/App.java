package dev.miscsb.conegen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import dev.miscsb.conegen.controller.*;
import dev.miscsb.conegen.transformations.Translation;
import dev.miscsb.conegen.util.Camera;
import dev.miscsb.conegen.util.Point3D;
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

        private Map<PointGroup, Color> groups;
        private Camera camera;

        public Board() {
            initBoard();
            this.camera = new Camera(new Point3D(0, 0, -15), QuaternionUtil.yawPitchRollToQuaternion(yaw, pitch, roll), 200);

            double d = 5;
            var axes = Map.ofEntries(
                Map.entry(PointGroups.line(Point3D.ORIGIN, new Point3D(d, 0, 0)), Color.RED),
                Map.entry(PointGroups.line(Point3D.ORIGIN, new Point3D(0, d, 0)), Color.GREEN),
                Map.entry(PointGroups.line(Point3D.ORIGIN, new Point3D(0, 0, d)), Color.BLUE)
            );

            double bottomRadius = 2;
            double topRadius = 0.2;

            double baseLength = 3;
            double baseHeight = 0.2;
            double coneHeight = 8;

            double[] normal = new double[] { 0, 1, 0 };
            int numEdges = 20;

            PointGroup cone = PointGroups.compose(
                List.of(
                    PointGroups.circle(bottomRadius, normal, numEdges).transformed(new Translation(0, baseHeight, 0)),
                    PointGroups.circle(topRadius, normal, numEdges).transformed(new Translation(0, coneHeight, 0)),
                    PointGroups.rectangularPrism(baseLength, baseHeight, baseLength)
                ), List.of());

            groups = new HashMap<>();
            groups.put(cone, Color.YELLOW);
            groups.putAll(axes);
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
            for (var entry : this.groups.entrySet()) {
                PointGroup shape = entry.getKey();
                g.setColor(entry.getValue());
                for (double[] line : camera.projectLines(shape.getVertices(), shape.getEdges())) {
                    g.drawLine(
                        (int) (line[0]) + W_WIDTH / 2,
                        (int) (line[1]) + W_HEIGHT / 2,
                        (int) (line[2]) + W_WIDTH / 2,
                        (int) (line[3]) + W_HEIGHT / 2);
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
                    camera.focalLength -= step*0.1; break;
                case '=': case '+':
                    camera.focalLength += step*0.1; break;

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

