import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

public class Renderer
{
    public static void main(String[] args)
    {
        final boolean WIREFRAME_MODE = false;

        JFrame frame = new JFrame();
        Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());

        // Slider to control horizontal rotation
        JSlider headingSlider = new JSlider(0, 360, 180);
        pane.add(headingSlider, BorderLayout.SOUTH);

        // Slider to control vertical rotation
        JSlider pitchSlider = new JSlider(SwingConstants.VERTICAL, -90, 90, 0);
        pane.add(pitchSlider, BorderLayout.EAST);

        // Panel to display render results
        JPanel renderPanel = new JPanel() {
            {
                final Timer timer = new Timer(100, new ActionListener() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        // TODO: Rotate the cube automatically
                    }
                });
                // TODO:
		// timer.start();
            }

            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // ------ Rendering code -------
                List<Triangle> tris = new ArrayList<>();
            /*  // Tetrahedron
                tris.add(new Triangle(new Vertex(100, 100, 100),
                                      new Vertex(-100, -100, 100),
                                      new Vertex(-100, 100, -100),
                                      Color.WHITE));
                tris.add(new Triangle(new Vertex(100, 100, 100),
                                      new Vertex(-100, -100, 100),
                                      new Vertex(100, -100, -100),
                                      Color.RED));
                tris.add(new Triangle(new Vertex(-100, 100, -100),
                                      new Vertex(100, -100, -100),
                                      new Vertex(100, 100, 100),
                                      Color.GREEN));
                tris.add(new Triangle(new Vertex(-100, 100, -100),
                                      new Vertex(100, -100, -100),
                                      new Vertex(-100, -100, 100),
                                      Color.BLUE));
            */

                // Cube
                tris.add(new Triangle(new Vertex(-100, -100, -100),
                                      new Vertex(100, -100, -100),
                                      new Vertex(100, -100, 100),
                                      Color.WHITE));
                tris.add(new Triangle(new Vertex(-100, -100, -100),
                                      new Vertex(100, -100, 100),
                                      new Vertex(-100, -100, 100),
                                      Color.WHITE));
                
                tris.add(new Triangle(new Vertex(-100, -100, -100),
                                      new Vertex(-100, -100, 100),
                                      new Vertex(-100, 100, 100),
                                      Color.BLUE));
                tris.add(new Triangle(new Vertex(-100, -100, -100),
                                      new Vertex(-100, 100, 100),
                                      new Vertex(-100, 100, -100),
                                      Color.BLUE));

                tris.add(new Triangle(new Vertex(-100, -100, -100),
                                      new Vertex(-100, 100, -100),
                                      new Vertex(100, 100, -100),
                                      Color.RED));
                tris.add(new Triangle(new Vertex(-100, -100, -100),
                                      new Vertex(100, -100, -100),
                                      new Vertex(100, 100, -100),
                                      Color.RED));

                tris.add(new Triangle(new Vertex(100, -100, -100),
                                      new Vertex(100, -100, 100),
                                      new Vertex(100, 100, 100),
                                      Color.PINK));
                tris.add(new Triangle(new Vertex(100, -100, -100),
                                      new Vertex(100, 100, -100),
                                      new Vertex(100, 100, 100),
                                      Color.PINK));
                                      
                tris.add(new Triangle(new Vertex(100, -100, 100),
                                      new Vertex(-100, -100, 100),
                                      new Vertex(-100, 100, 100),
                                      Color.GREEN));
                tris.add(new Triangle(new Vertex(100, -100, 100),
                                      new Vertex(100, 100, 100),
                                      new Vertex(-100, 100, 100),
                                      Color.GREEN));

                tris.add(new Triangle(new Vertex(-100, 100, -100),
                                      new Vertex(-100, 100, 100),
                                      new Vertex(100, 100, 100),
                                      Color.ORANGE));
                tris.add(new Triangle(new Vertex(-100, 100, -100),
                                      new Vertex(100, 100, -100),
                                      new Vertex(100, 100, 100),
                                      Color.ORANGE));

                /* Transformation matrices reference
                 * 
                 * XY Rotation Matrix:
                 * 
                 *  [    cos   -sin    0     ]
                 *  [    sin    cos    0     ]
                 *  [    0      0      1     ]
                 * 
                 * 
                 * YZ Rotation Matrix:
                 * 
                 *  [    1      0      0     ]
                 *  [    0      cos    sin   ]
                 *  [    0     -sin    cos   ]
                 * 
                 * 
                 * XZ Rotation Matrix:
                 * 
                 *  [    cos    0     -sin   ]
                 *  [    0      1      0     ]
                 *  [    sin    0      cos   ]
                 */

                // Create rotation matrix
                double heading = Math.toRadians(headingSlider.getValue());
                Matrix3 headingTransform = new Matrix3(new double[] {
                    Math.cos(heading), 0, Math.sin(heading),
                    0, 1, 0,
                    -Math.sin(heading), 0, Math.cos(heading)
                });

                double pitch = Math.toRadians(pitchSlider.getValue());
                Matrix3 pitchTransform = new Matrix3(new double[] {
                    1, 0, 0,
                    0, Math.cos(pitch), Math.sin(pitch),
                    0, -Math.sin(pitch), Math.cos(pitch)
                });
                Matrix3 transform = headingTransform.multiply(pitchTransform);
                
                if (WIREFRAME_MODE) {
                    g2.translate(getWidth() / 2, getHeight() / 2);
                    g2.setColor(Color.WHITE);

                    for (Triangle t : tris) {
                        Vertex v1 = transform.transform(t.v1);
                        Vertex v2 = transform.transform(t.v2);
                        Vertex v3 = transform.transform(t.v3);
    
                        Path2D path = new Path2D.Double();
                        path.moveTo(v1.x, v1.y);
                        path.lineTo(v2.x, v2.y);
                        path.lineTo(v3.x, v3.y);
                        path.closePath();
                        g2.draw(path);
                    }
                }
                else {
                    BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

                    // z-buffering makes sure that we only color a pixel if it is above
                    // the depth of the last seen element at that pixel, to avoid overlaps
                    double[] zBuffer = new double[img.getWidth() * img.getHeight()];

                    // Initialize the array with extremely far away depths
                    for (int q = 0; q < zBuffer.length; q++) {
                        zBuffer[q] = Double.NEGATIVE_INFINITY;
                    }

                    for (Triangle t : tris) {
                        Vertex v1 = transform.transform(t.v1);
                        Vertex v2 = transform.transform(t.v2);
                        Vertex v3 = transform.transform(t.v3);

                        // Manual transformations since not using Graphics2D
                        v1.x += getWidth() / 2;
                        v1.y += getHeight() / 2;
                        v2.x += getWidth() / 2;
                        v2.y += getHeight() / 2;
                        v3.x += getWidth() / 2;
                        v3.y += getHeight() / 2;

                        // Compute triangle normals (for shading)
                        Vertex ab = new Vertex(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
                        Vertex ac = new Vertex(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z);

                        Vertex norm = new Vertex(
                             ab.y * ac.z - ab.z * ac.y,
                             ab.z * ac.x - ab.x * ac.z,
                             ab.x * ac.y - ab.y * ac.x
                        );

                        double normalLength = Math.sqrt(norm.x * norm.x + norm.y * norm.y + norm.z * norm.z);

                        norm.x /= normalLength;
                        norm.y /= normalLength;
                        norm.z /= normalLength;

                        /* Explanation of flat shading
                         * We need the cosine between the triangle normal and the light direction.
                         * This assumes light comes from [0 0 1] -- directly behind the camera.
                         * 
                         * Angle between two vectors:
                         * 
                         *                   A (dot) B
                         *  cos(theta) = -----------------
                         *                 ||A|| X ||B||
                         * 
                         * Since the length of the light direction vector [0 0 1] is 1, we simplify:
                         * 
                         *  cos(theta) = A (dot) B = [ax ay az] (dot) [bx by bz]
                         *  cos(theta) = A (dot) B = [ax ay az] (dot) [0  0  1 ] = az
                         * 
                         * Note: we drop the sign because we don't care which side faces the camera.
                         */
                        double angleCos = Math.abs(norm.z);

                        // Compute rectangular bounds for triangle
                        int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
                        int maxX = (int) Math.min(img.getWidth() - 1, 
                                                  Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
                        int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
                        int maxY = (int) Math.min(img.getHeight() - 1,
                                                  Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));

                        // Calculate area of triangle
                        double triangleArea = (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);
                        
                        // Used implementation of barycentric coordinate rasterization from elsewhere:
                        for (int y = minY; y <= maxY; y++) {
                            for (int x = minX; x <= maxX; x++) {
                                double b1 = 
                                  ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triangleArea;
                                double b2 =
                                  ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triangleArea;
                                double b3 =
                                  ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triangleArea;
                                if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
                                    double depth = b1 * v1.z + b2 * v2.z + b3 * v3.z;
                                    int zIndex = y * img.getWidth() + x;
                                    
                                    if (zBuffer[zIndex] < depth) {
                                        img.setRGB(x, y, getShade(t.color, angleCos).getRGB());
                                        zBuffer[zIndex] = depth;
                                    }
                                }
                            }
                        }
                    }   // end: for (Triangle t : tris)

                    // Display the rasterized image
                    g2.drawImage(img, 0, 0, null);

                }   // end: else    [if not WIREFRAME_MODE]
            }
        };
        pane.add(renderPanel, BorderLayout.CENTER);

        headingSlider.addChangeListener(e -> renderPanel.repaint());
        pitchSlider.addChangeListener(e -> renderPanel.repaint());

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }

    public static Color getShade(Color color, double shade) 
    {
        /*
        // Simple shading-- falls off too quickly.

        int red = (int) (color.getRed() * shade);
        int green = (int) (color.getGreen() * shade);
        int blue = (int) (color.getBlue() * shade);
        return new Color(red, green, blue);
        */

        // Shading with approximated conversion from sRGB to linear RGB
        double redLinear = Math.pow(color.getRed(), 2.4) * shade;
        double greenLinear = Math.pow(color.getGreen(), 2.4) * shade;
        double blueLinear = Math.pow(color.getBlue(), 2.4) * shade;
    
        int red = (int) Math.pow(redLinear, 1/2.4);
        int green = (int) Math.pow(greenLinear, 1/2.4);
        int blue = (int) Math.pow(blueLinear, 1/2.4);
    
        return new Color(red, green, blue);
    }
}