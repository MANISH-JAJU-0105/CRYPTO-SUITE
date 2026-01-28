package ciphers;

import components.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class BaseCipherPanel extends JPanel {
    protected JTextArea inputArea;
    protected JTextArea outputArea;
    protected PerformanceGraph graph;
    protected JTextArea statsArea;
    protected SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");

    protected JRadioButton textModeBtn;
    protected JRadioButton fileModeBtn;
    protected JPanel inputCardPanel;
    protected CardLayout inputCardLayout;
    protected JTextField filePathField;
    protected File selectedFile;
    protected String cipherTitle;

    public BaseCipherPanel(String title) {
        super();
        this.cipherTitle = title;
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(Theme.MAIN_BG);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // Header
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(Theme.FONT_TITLE);
        titleLabel.setForeground(Theme.TEXT_PRIMARY);
        add(titleLabel, BorderLayout.NORTH);

        // Main Grid
        java.awt.GridLayout gl = new java.awt.GridLayout(1, 2, 30, 0);
        JPanel mainGrid = new JPanel(gl);
        mainGrid.setOpaque(true);
        mainGrid.setBackground(Theme.MAIN_BG);
        mainGrid.setBorder(new EmptyBorder(25, 0, 0, 0));

        // --- Left Column ---
        JPanel leftCol = new JPanel();
        leftCol.setLayout(new BoxLayout(leftCol, BoxLayout.Y_AXIS));
        leftCol.setOpaque(true);
        leftCol.setBackground(Theme.MAIN_BG);

        // Input Card
        CardPanel inputPanel = new CardPanel();
        inputPanel.setLayout(new BorderLayout(15, 15));
        inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        modePanel.setOpaque(false);
        textModeBtn = new JRadioButton("Text");
        fileModeBtn = new JRadioButton("File");
        styleRadioButton(textModeBtn);
        styleRadioButton(fileModeBtn);
        textModeBtn.setSelected(true);
        ButtonGroup bg = new ButtonGroup();
        bg.add(textModeBtn);
        bg.add(fileModeBtn);
        JLabel modeLbl = new JLabel("INPUT SOURCE");
        modeLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        modeLbl.setForeground(Theme.ACCENT_COLOR);
        modePanel.add(modeLbl);
        modePanel.add(textModeBtn);
        modePanel.add(fileModeBtn);
        inputPanel.add(modePanel, BorderLayout.NORTH);

        inputCardLayout = new CardLayout();
        inputCardPanel = new JPanel(inputCardLayout);
        inputCardPanel.setOpaque(false);

        inputArea = createTextArea(true);
        inputCardPanel.add(new DarkScrollPane(inputArea), "TEXT");

        JPanel filePanel = new JPanel(new BorderLayout(10, 0));
        filePanel.setOpaque(false);
        filePathField = createTextField("No file selected");
        filePathField.setEditable(false);
        JButton browseBtn = new NeonButton("Browse", new Color(60, 60, 80));
        browseBtn.addActionListener(e -> chooseFile());
        filePanel.add(filePathField, BorderLayout.CENTER);
        filePanel.add(browseBtn, BorderLayout.EAST);

        JPanel fileWrapper = new JPanel(new GridBagLayout());
        fileWrapper.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        fileWrapper.add(filePanel, gbc);
        inputCardPanel.add(fileWrapper, "FILE");

        inputPanel.add(inputCardPanel, BorderLayout.CENTER);

        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        controlsPanel.setOpaque(false);
        addControls(controlsPanel);
        JButton resetBtn = new NeonButton("Reset", new Color(220, 60, 60)); // Red Reset Button
        resetBtn.addActionListener(e -> reset());
        controlsPanel.add(resetBtn);
        inputPanel.add(controlsPanel, BorderLayout.SOUTH);

        leftCol.add(inputPanel);
        leftCol.add(Box.createVerticalStrut(25));

        // Output Card
        CardPanel outputPanel = new CardPanel();
        outputPanel.setLayout(new BorderLayout(15, 15));
        outputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel outLbl = new JLabel("OUTPUT RESULT");
        outLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        outLbl.setForeground(Theme.ACCENT_COLOR);
        outputPanel.add(outLbl, BorderLayout.NORTH);

        outputArea = createTextArea(false);
        outputArea.setForeground(new Color(100, 255, 150));
        outputPanel.add(new DarkScrollPane(outputArea), BorderLayout.CENTER);

        leftCol.add(outputPanel);

        // --- Right Column ---
        JPanel rightCol = new JPanel();
        rightCol.setLayout(new BoxLayout(rightCol, BoxLayout.Y_AXIS));
        rightCol.setOpaque(true);
        rightCol.setBackground(Theme.MAIN_BG);

        // Stats Card
        CardPanel statsPanel = new CardPanel();
        statsPanel.setLayout(new BorderLayout(15, 15));
        statsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        JLabel statsLbl = new JLabel("PERFORMANCE METRICS");
        statsLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statsLbl.setForeground(Theme.ACCENT_COLOR);
        statsPanel.add(statsLbl, BorderLayout.NORTH);

        statsArea = new JTextArea();
        statsArea.setFont(Theme.FONT_MONO);
        statsArea.setBackground(Theme.INPUT_BG);
        statsArea.setForeground(Theme.TEXT_PRIMARY);
        statsArea.setEditable(false);
        statsArea.setOpaque(true); // Explicitly Opaque
        statsArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        statsPanel.add(statsArea, BorderLayout.CENTER);

        rightCol.add(statsPanel);
        rightCol.add(Box.createVerticalStrut(25));

        // Graph Card
        CardPanel graphPanel = new CardPanel();
        graphPanel.setLayout(new BorderLayout(15, 15));
        graphPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel graphLbl = new JLabel("TIME ANALYSIS");
        graphLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        graphLbl.setForeground(Theme.ACCENT_COLOR);
        graphPanel.add(graphLbl, BorderLayout.NORTH);

        graph = new PerformanceGraph();
        graphPanel.add(graph, BorderLayout.CENTER);

        rightCol.add(graphPanel);

        mainGrid.add(leftCol);
        mainGrid.add(rightCol);

        add(mainGrid, BorderLayout.CENTER);

        ActionListener modeListener = e -> {
            if (textModeBtn.isSelected())
                inputCardLayout.show(inputCardPanel, "TEXT");
            else
                inputCardLayout.show(inputCardPanel, "FILE");
        };
        textModeBtn.addActionListener(modeListener);
        fileModeBtn.addActionListener(modeListener);
    }

    private void styleRadioButton(JRadioButton rb) {
        rb.setOpaque(false);
        rb.setForeground(Theme.TEXT_PRIMARY);
        rb.setFont(Theme.FONT_NORMAL);
        rb.setFocusPainted(false);
    }

    private void chooseFile() {
        JFileChooser fc = new JFileChooser();
        int res = fc.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            selectedFile = fc.getSelectedFile();
            filePathField.setText(selectedFile.getName());
        }
    }

    private void reset() {
        inputArea.setText("");
        outputArea.setText("");
        statsArea.setText("");
        filePathField.setText("No file selected");
        selectedFile = null;
        graph.reset();
        resetFields();
    }

    protected void resetFields() {
    } // To be overridden

    protected String getInputText() throws Exception {
        if (textModeBtn.isSelected()) {
            return inputArea.getText();
        } else {
            if (selectedFile == null || !selectedFile.exists()) {
                throw new Exception("No file selected");
            }
            return new String(Files.readAllBytes(selectedFile.toPath()));
        }
    }

    protected void measureAndRun(Runnable task, boolean isEncrypt) {
        long startNano = System.nanoTime();
        long startTime = System.currentTimeMillis();

        try {
            task.run();
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
            return;
        }

        long endNano = System.nanoTime();
        long endTime = System.currentTimeMillis();
        long duration = endNano - startNano;

        StringBuilder stats = new StringBuilder();
        stats.append(" Operation  : ").append(isEncrypt ? "Encryption" : "Decryption").append("\n");
        stats.append(" Start Time : ").append(timeFormat.format(new Date(startTime))).append("\n");
        stats.append(" End Time   : ").append(timeFormat.format(new Date(endTime))).append("\n");
        stats.append(" Total Time : ").append(String.format("%.3f", duration / 1000000.0)).append(" ms");

        if (fileModeBtn.isSelected() && selectedFile != null) {
            try {
                String content = outputArea.getText();
                String name = selectedFile.getName();
                int dotIndex = name.lastIndexOf('.');
                String baseName = (dotIndex == -1) ? name : name.substring(0, dotIndex);

                String suffix = isEncrypt ? "_ENC" : "_DEC";
                // Get cipher name from title, remove spaces and special chars for filename
                String cipherTag = cipherTitle.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();

                String newName = baseName + "_" + cipherTag + suffix + ".txt";

                // Save to ENCRYPTION folder
                File encryptionDir = new File(System.getProperty("user.home"),
                        "Documents/CRYPTOGRAPHY UI DESGIN/ENCRYPTION");
                if (!encryptionDir.exists()) {
                    encryptionDir.mkdirs();
                }
                File outputFile = new File(encryptionDir, newName);

                Files.write(outputFile.toPath(), content.getBytes());
                stats.append("\n Saved to   : ").append(outputFile.getAbsolutePath());
            } catch (Exception e) {
                stats.append("\n Save Error : ").append(e.getMessage());
            }
        }

        statsArea.setText(stats.toString());

        if (isEncrypt)
            graph.setEncTime(duration);
        else
            graph.setDecTime(duration);
    }

    protected abstract void addControls(JPanel panel);

    protected JTextArea createTextArea(boolean editable) {
        JTextArea area = new JTextArea();
        area.setFont(Theme.FONT_MONO);
        area.setBackground(Theme.INPUT_BG);
        area.setForeground(Theme.TEXT_PRIMARY);
        area.setCaretColor(Theme.ACCENT_COLOR);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(editable);
        area.setOpaque(true); // Explicitly Opaque
        area.setBorder(new EmptyBorder(10, 10, 10, 10));
        return area;
    }

    protected void runSimulation(boolean encrypt) {
        try {
            String text = getInputText();
            String key = cipherTitle.replaceAll("[^a-zA-Z]", "");
            if (key.isEmpty())
                key = "DEFAULT";

            StringBuilder res = new StringBuilder();
            int keyIndex = 0;

            for (char c : text.toCharArray()) {
                if (c >= 32 && c <= 126) { // Printable ASCII
                    int kVal = key.charAt(keyIndex % key.length());
                    int shift = kVal % 95;

                    int cVal = c - 32;
                    int newVal;

                    if (encrypt) {
                        newVal = (cVal + shift) % 95;
                    } else {
                        newVal = (cVal - shift);
                        if (newVal < 0)
                            newVal += 95;
                    }

                    res.append((char) (32 + newVal));
                } else {
                    res.append(c);
                }
                keyIndex++;
            }
            outputArea.setText(res.toString());

        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }

    protected JTextField createTextField(String text) {
        JTextField field = new JTextField(text, 10);
        field.setFont(Theme.FONT_MONO);
        field.setBackground(Theme.INPUT_BG);
        field.setForeground(Theme.TEXT_PRIMARY);
        field.setCaretColor(Theme.ACCENT_COLOR);
        field.setOpaque(true); // Explicitly Opaque
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(60, 60, 60)),
                new EmptyBorder(5, 10, 5, 10)));
        return field;
    }
}
