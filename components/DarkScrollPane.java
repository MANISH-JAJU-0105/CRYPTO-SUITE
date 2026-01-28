package components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class DarkScrollPane extends JScrollPane {
    public DarkScrollPane(Component view) {
        super(view);
        setBorder(null);
        getViewport().setOpaque(true);
        getViewport().setBackground(Theme.INPUT_BG);
        setOpaque(true);
        setBackground(Theme.INPUT_BG);
        getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(80, 80, 80);
                this.trackColor = Theme.INPUT_BG;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
        });
    }

    private JButton createZeroButton() {
        JButton jbutton = new JButton();
        jbutton.setPreferredSize(new Dimension(0, 0));
        return jbutton;
    }
}
