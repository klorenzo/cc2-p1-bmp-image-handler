import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;

public class BMPViewer extends JFrame {

    private JButton btnPreviousImage;
    private JButton btnNextImage;
    private JDesktopPane desktopPane;
    private JLabel lblCurrentImage;
    private JLabel lblHead;

    private ArrayList<String> imageFiles;
    private int counter = -1;

	public BMPViewer(ArrayList<String> imageFiles) {

        this.setTitle("Universidad Galileo | BMP Viewer ( CC2 Project 1 )");
        this.getContentPane().setBackground(new Color(200, 200, 200));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(800, 600));
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        this.imageFiles = imageFiles;

        desktopPane = new JDesktopPane();
        desktopPane.setPreferredSize(new Dimension(640, 480));

        lblCurrentImage = new JLabel();

        if(!imageFiles.isEmpty()) {
            this.loadImage(imageFiles.get(++counter));
        }

        btnPreviousImage = new JButton();
        btnPreviousImage.setText("Previous");
        btnPreviousImage.setMaximumSize(new Dimension(75, 25));
        btnPreviousImage.setMinimumSize(new Dimension(75, 25));
        btnPreviousImage.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                btnPreviousImageMouseClicked(evt);
            }
        });
        btnPreviousImage.setEnabled(false);

        btnNextImage = new JButton();
        btnNextImage.setText("Next");
        btnNextImage.setMaximumSize(new Dimension(75, 25));
        btnNextImage.setMinimumSize(new Dimension(75, 25));
        btnNextImage.setPreferredSize(new Dimension(75, 25));
        btnNextImage.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                btnNextImageMouseClicked(evt);
            }
        });

        GroupLayout desktopPaneLayout = new GroupLayout(desktopPane);
        desktopPane.setLayout(desktopPaneLayout);
        desktopPaneLayout.setHorizontalGroup(
                desktopPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(lblCurrentImage, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        desktopPaneLayout.setVerticalGroup(
            desktopPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(lblCurrentImage, GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
        );
        desktopPane.setLayer(lblCurrentImage, JLayeredPane.DEFAULT_LAYER);

        lblHead = new JLabel(); 
        lblHead.setBackground(new Color(0, 0, 0));
        lblHead.setFont(new Font("Arial", 0, 24));
        lblHead.setForeground(new Color(255, 255, 255));
        lblHead.setHorizontalAlignment(SwingConstants.CENTER);
        lblHead.setText("BMP Viewer");
        lblHead.setToolTipText("");
        lblHead.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        lblHead.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        lblHead.setMaximumSize(new Dimension(75, 20));
        lblHead.setMinimumSize(new Dimension(75, 20));
        lblHead.setPreferredSize(new Dimension(75, 20));

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(desktopPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnPreviousImage, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnNextImage, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblHead, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(80, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblHead, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(desktopPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPreviousImage, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNextImage, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();

    }

    public void loadImage(String imageFileName) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(imageFileName));
            if( (bufferedImage.getWidth() > 480) && (bufferedImage.getHeight() > 640) ) {
                lblCurrentImage.setIcon(new ImageIcon(bufferedImage.getScaledInstance(640, 480, Image.SCALE_SMOOTH)));
            } else {
                lblCurrentImage.setIcon(new ImageIcon(bufferedImage));
            }
        } catch(IOException ioe) {
                ioe.printStackTrace();
        }
    }

    private void btnNextImageMouseClicked(MouseEvent evt) {
        if( (counter + 1) < this.imageFiles.size() ) {
            this.loadImage(this.imageFiles.get(++counter));
        }
        if(counter == (this.imageFiles.size() - 1) ) {
            btnNextImage.setEnabled(false);
        } else {
            btnNextImage.setEnabled(true);
        }
        if(counter == 0 ) {
            btnPreviousImage.setEnabled(false);
        } else {
            btnPreviousImage.setEnabled(true);
        }
    }

    private void btnPreviousImageMouseClicked(MouseEvent evt) {
        if( (counter - 1) >= 0 ) {
            this.loadImage(this.imageFiles.get(--counter));
        }
        if(counter == (this.imageFiles.size() - 1) ) {
            btnNextImage.setEnabled(false);
        } else {
            btnNextImage.setEnabled(true);
        }
        if(counter == 0 ) {
            btnPreviousImage.setEnabled(false);
        } else {
            btnPreviousImage.setEnabled(true);
        }
    }

}