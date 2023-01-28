package dev.miscsb.conegen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import dev.miscsb.conegen.controller.*;
import dev.miscsb.conegen.transformations.Rotation;
import dev.miscsb.conegen.transformations.Translation;
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

        private Map<PointGroup, Color> groups;
        private PointGroup cone;
        private PointGroup gridlines;
        private Camera camera;

        private double yaw = 0, pitch = 0, roll = Math.PI;

        public Board() {
            initBoard();
            this.camera = new Camera(new Point3D(0, 0, -15), QuaternionUtil.yawPitchRollToQuaternion(yaw, pitch, roll), 200);

            double d = 20;
            // var axes = Map.ofEntries(
            //     Map.entry(PointGroups.line(Point3D.ORIGIN, new Point3D(d, 0, 0)), Color.RED),
            //     Map.entry(PointGroups.line(Point3D.ORIGIN, new Point3D(0, d, 0)), Color.GREEN),
            //     Map.entry(PointGroups.line(Point3D.ORIGIN, new Point3D(0, 0, d)), Color.BLUE)
            // );

            PointGroup gridlines = PointGroups.compose(IntStream.rangeClosed(-5, 5)
                .mapToDouble(i -> i * d)
                .boxed()
                .<PointGroup> mapMulti((r, consumer) -> {
                    consumer.accept(PointGroups.line(new Point3D(100, 0, r), new Point3D(-100, 0, r)));
                    consumer.accept(PointGroups.line(new Point3D(r, 0, 100), new Point3D(r, 0, -100)));
                }).toList(),
                List.of()
            );
            this.gridlines = gridlines;

            final double bottomRadius = 2, topRadius = 0.2;
            final double baseLength = 3, baseHeight = 0.2, coneHeight = 8;
            double[] normal = new double[] { 0, 1, 0 };
            int numEdges = 20;
            PointGroup cone = PointGroups.compose(
                List.of(
                    PointGroups.circle(bottomRadius, normal, numEdges).transformed(new Translation(0, baseHeight, 0)),
                    PointGroups.circle(topRadius, normal, numEdges).transformed(new Translation(0, coneHeight, 0)),
                    PointGroups.rectangularPrism(baseLength, baseHeight, baseLength)
                ),
                IntStream.range(0, numEdges).mapToObj(i -> new int[] { 0, i, 1, i }).toList()
            );
            this.cone = cone;

            double a = baseLength;
            double b = coneHeight;
            double angle = Math.atan2(a, b);
            cone.transform(new Rotation(QuaternionUtil.yawPitchRollToQuaternion(0, Math.PI / 2 + angle, 0)));
            cone.transform(new Translation(0, a * b / Math.sqrt(a*a + b*b), 0));

            groups = new HashMap<>();
            groups.put(gridlines, Color.WHITE);
            groups.put(cone, Color.YELLOW);
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

        @Override
        public void keyPressed(KeyEvent e) {
            double step = e.isShiftDown() ? 5.0 : 1.0;

            double[] coneTranslate = new double[] { 0, 0, 0 };

            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:  camera.pinhole.x += step; break;
                case KeyEvent.VK_RIGHT: camera.pinhole.x -= step; break;
                case KeyEvent.VK_UP:    camera.pinhole.y += step; break;
                case KeyEvent.VK_DOWN:  camera.pinhole.y -= step; break;
                
                case 'a': case 'A': coneTranslate[0] += step; break;
                case 'd': case 'D': coneTranslate[0] -= step; break;
                case 'w': case 'W': coneTranslate[2] += step; break;
                case 's': case 'S': coneTranslate[2] -= step; break;

                case 'j': case 'J': cone.transformAboutCenter(new Rotation(QuaternionUtil.yawPitchRollToQuaternion(-step*0.1, 0, 0))); break;
                case 'l': case 'L': cone.transformAboutCenter(new Rotation(QuaternionUtil.yawPitchRollToQuaternion(+step*0.1, 0, 0))); break;
                
                case '-': case '_': camera.focalLength -= step*0.1; break;
                case '=': case '+': camera.focalLength += step*0.1; break;
                case ' ': {
                    if (this.groups.keySet().contains(gridlines)) {
                        this.groups.keySet().remove(gridlines);
                    } else {
                        this.groups.put(gridlines, Color.WHITE);
                    }
                } break;
            }

            cone.transform(new Translation(coneTranslate[0], coneTranslate[1], coneTranslate[2]));

        }

        private void moveCameraWithKeyboard(KeyEvent e) {
            double step = e.isShiftDown() ? 5.0 : 1.0;

            double[] cameraMove = new double[] { 0, 0, 0 };

            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT: 
                    cameraMove[0] -= step; break;
                case KeyEvent.VK_RIGHT: 
                    cameraMove[0] += step; break;
                case KeyEvent.VK_UP: 
                    cameraMove[2] += step; break;
                case KeyEvent.VK_DOWN: 
                    cameraMove[2] -= step; break;
                case 'i': case 'I':
                    cameraMove[1] -= step; break;
                case 'k': case 'K':
                    cameraMove[1] += step; break;
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

            cameraMove = QuaternionUtil.applyRotationQuaternion(cameraMove, Quaternion.inverse(camera.orientation));
            camera.pinhole.x += cameraMove[0];
            camera.pinhole.y += cameraMove[1];
            camera.pinhole.z += cameraMove[2];
        }

        @Override
        public void keyReleased(KeyEvent arg0) {}

        @Override
        public void keyTyped(KeyEvent arg0) {}
    }
}

