package ProjectX;

import javax.swing.*;
import java.awt.*;

/**
 * Created by - on 04.09.2016 12:14.
 */
public class ContentPane extends JPanel {

    public ContentPane() {

        setOpaque(false);

    }


    @Override
    protected void paintComponent(Graphics g) {

        // Allow super to paint
        super.paintComponent(g);

        // Apply our own painting effect
        Graphics2D g2d = (Graphics2D) g.create();
        // 50% transparent Alpha
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.0f));

        g2d.setColor(getBackground());
        g2d.fill(getBounds());
        g2d.dispose();

    }

}
