package components;

import javax.swing.*;
import java.awt.*;

public class PerformanceGraph extends JPanel {
    private long encTime = 0;
    private long decTime = 0;

    public PerformanceGraph() {
        setOpaque(true);
        setBackground(Theme.CARD_BG); // Match Card BG
        setPreferredSize(new Dimension(300, 200));
    }

    public void setEncTime(long t) {
        this.encTime = t;
        repaint();
    }

    public void setDecTime(long t) {
        this.decTime = t;
        repaint();
    }

    public void reset() {
        this.encTime = 0;
        this.decTime = 0;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Paint background first
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int barWidth = 60;
        int spacing = 80;
        int topMargin = 50;
        int maxBarHeight = h - topMargin - 40;
        int startX = (w - (2 * barWidth + spacing)) / 2;

        // Draw Grid
        g2.setColor(new Color(60, 60, 60));
        g2.drawLine(20, h - 30, w - 20, h - 30); // X-Axis

        // Summary Text
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));

        g2.setColor(Theme.GRAPH_ENC_COLOR);
        g2.drawString("Encryption", startX, 25);
        g2.setFont(new Font("Consolas", Font.PLAIN, 12));
        g2.drawString(formatTime(encTime), startX, 45);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g2.setColor(Theme.GRAPH_DEC_COLOR);
        g2.drawString("Decryption", startX + barWidth + spacing, 25);
        g2.setFont(new Font("Consolas", Font.PLAIN, 12));
        g2.drawString(formatTime(decTime), startX + barWidth + spacing, 45);

        long maxVal = Math.max(encTime, decTime);
        if (maxVal == 0)
            maxVal = 1;

        // Enc Bar
        int encHeight = (int) ((double) encTime / maxVal * maxBarHeight);
        if (encHeight < 5 && encTime > 0)
            encHeight = 5;
        g2.setColor(Theme.GRAPH_ENC_COLOR);
        g2.fillRoundRect(startX, h - 30 - encHeight, barWidth, encHeight, 10, 10);

        // Dec Bar
        int decHeight = (int) ((double) decTime / maxVal * maxBarHeight);
        if (decHeight < 5 && decTime > 0)
            decHeight = 5;
        g2.setColor(Theme.GRAPH_DEC_COLOR);
        g2.fillRoundRect(startX + barWidth + spacing, h - 30 - decHeight, barWidth, decHeight, 10, 10);
    }

    private String formatTime(long nanos) {
        if (nanos == 0)
            return "0 ms";
        if (nanos < 10000)
            return nanos + " ns";
        return String.format("%.3f ms", nanos / 1_000_000.0);
    }
}
