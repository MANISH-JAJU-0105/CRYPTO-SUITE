import ciphers.*;
import components.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CryptoApp extends JFrame {

    private ContentPanel contentPanel;
    private JPanel sidebarPanel;
    private JPanel cipherListPanel;
    private JTextField searchField;
    private List<AnimatedSidebarButton> sidebarButtons = new ArrayList<>();

    // Lazy Loading Maps
    private Map<String, Class<? extends JPanel>> cipherMap = new HashMap<>();
    private Map<String, JPanel> loadedPanels = new HashMap<>();

    public CryptoApp() {
        setTitle("Cryptography Suite - Premium Edition");
        setSize(1300, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Container
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(Theme.MAIN_BG);
        setContentPane(mainContainer);

        // --- Sidebar ---
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(Theme.SIDEBAR_BG);
        sidebarPanel.setBorder(new EmptyBorder(30, 20, 30, 20));
        sidebarPanel.setOpaque(true);
        sidebarPanel.setPreferredSize(new Dimension(300, getHeight()));

        JLabel appTitle = new JLabel("CRYPTO SUITE");
        appTitle.setForeground(Theme.TEXT_PRIMARY);
        appTitle.setFont(Theme.FONT_TITLE);
        appTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebarPanel.add(appTitle);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel versionLabel = new JLabel("v2.2 Ultimate");
        versionLabel.setForeground(Theme.ACCENT_COLOR);
        versionLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        versionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebarPanel.add(versionLabel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Search Bar
        searchField = new JTextField();
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        searchField.setFont(Theme.FONT_NORMAL);
        searchField.setBackground(Theme.INPUT_BG);
        searchField.setForeground(Theme.TEXT_PRIMARY);
        searchField.setCaretColor(Theme.ACCENT_COLOR);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(60, 60, 60), 1, true),
                new EmptyBorder(5, 15, 5, 15)));

        searchField.setText("Search Algorithms...");
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (searchField.getText().equals("Search Algorithms...")) {
                    searchField.setText("");
                }
                searchField.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(Theme.ACCENT_COLOR, 1, true),
                        new EmptyBorder(5, 15, 5, 15)));
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search Algorithms...");
                }
                searchField.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(60, 60, 60), 1, true),
                        new EmptyBorder(5, 15, 5, 15)));
            }
        });

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterCiphers();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterCiphers();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterCiphers();
            }
        });

        sidebarPanel.add(searchField);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel subTitle = new JLabel("AVAILABLE MODULES");
        subTitle.setForeground(Theme.TEXT_SECONDARY);
        subTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        subTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebarPanel.add(subTitle);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // --- Cipher List Panel ---
        cipherListPanel = new JPanel();
        cipherListPanel.setLayout(new BoxLayout(cipherListPanel, BoxLayout.Y_AXIS));
        cipherListPanel.setBackground(Theme.SIDEBAR_BG);
        cipherListPanel.setOpaque(true);
        // No direct add here, handled by scroll pane

        // Wrap sidebar list in ScrollPane
        JScrollPane listScroll = new JScrollPane(cipherListPanel);
        listScroll.setBorder(null);
        listScroll.getViewport().setBackground(Theme.SIDEBAR_BG);
        listScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        listScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        listScroll.getVerticalScrollBar().setUnitIncrement(16);
        listScroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Custom Scrollbar
        listScroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(60, 60, 60);
                this.trackColor = Theme.SIDEBAR_BG;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton j = new JButton();
                j.setPreferredSize(new Dimension(0, 0));
                return j;
            }
        });

        sidebarPanel.add(listScroll);

        // --- Content Panel ---
        contentPanel = new ContentPanel();

        // Welcome Panel with Cyber Background
        JPanel welcomePanel = createWelcomePanel();

        // --- Initialize Ciphers (Lazy Loading) ---
        initCiphers();

        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Show welcome initially
        contentPanel.showPanel(welcomePanel);
    }

    private JPanel createWelcomePanel() {
        // Use OverlayLayout to stack text on top of animation
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new OverlayLayout(wrapper));
        wrapper.setBackground(Theme.MAIN_BG);

        // 1. The Content Layer (Text)
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false); // Transparent so background shows through
        content.setAlignmentX(0.5f);
        content.setAlignmentY(0.5f);

        content.add(Box.createVerticalGlue());

        JLabel title = new JLabel("Welcome to Crypto Suite");
        title.setFont(new Font("Segoe UI", Font.BOLD, 40));
        title.setForeground(Theme.ACCENT_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(title);

        content.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel sub = new JLabel("Select a cipher from the sidebar to begin.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        sub.setForeground(Theme.TEXT_SECONDARY);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(sub);

        content.add(Box.createVerticalGlue());

        // 2. The Background Layer (Animation)
        CyberBackground bg = new CyberBackground();
        bg.setAlignmentX(0.5f);
        bg.setAlignmentY(0.5f);

        // Add to wrapper (Order matters for OverlayLayout: First added is on TOP)
        wrapper.add(content);
        wrapper.add(bg);

        return wrapper;
    }

    private void initCiphers() {
        addCipher("001 : A1Z26 CIPHER", A1Z26CipherPanel.class);
        addCipher("002 : ADFGVX CIPHER", ADFGVXCipherPanel.class);
        addCipher("003 : ADFGX CIPHER", ADFGXCipherPanel.class);
        addCipher("004 : AES-CBC", AESCBCPanel.class);
        addCipher("005 : AES-CTR", AESCTRPanel.class);
        addCipher("006 : AES-GCM", AESGCMPanel.class);
        addCipher("007 : AES", AESPanel.class);
        addCipher("008 : ARIA", ARIAPanel.class);
        addCipher("009 : ASCII SHIFT CIPHER", ASCIIShiftCipherPanel.class);
        addCipher("010 : AFFINE CIPHER", AffineCipherPanel.class);
        addCipher("011 : ALBERTI CIPHER", AlbertiCipherPanel.class);
        addCipher("012 : ALHAMBRA CIPHER", AlhambraCipherPanel.class);
        addCipher("013 : ALLIEDSHIFT CIPHER", AlliedShiftCipherPanel.class);
        addCipher("014 : ALPHANUMERICSUBSTITUTION", AlphanumericSubstitutionPanel.class);
        addCipher("015 : AMERICAN CIPHER DISK", AmericanCipherDiskPanel.class);
        addCipher("016 : ATBASH CIPHER", AtbashCipherPanel.class);
        addCipher("017 : AUTOKEY CIPHER", AutokeyCipherPanel.class);
        addCipher("018 : AUTOMATIC SUBSTITUTION", AutomaticSubstitutionPanel.class);
        addCipher("019 : BEECH CIPHER", BEECHCipherPanel.class);
        addCipher("020 : BACONIAN CIPHER", BaconianCipherPanel.class);
        addCipher("021 : BAZERIES CIPHER", BazeriesCipherPanel.class);
        addCipher("022 : BEALE CIPHER", BealeCipherPanel.class);
        addCipher("023 : BEAUFORT CIPHER", BeaufortCipherPanel.class);
        addCipher("024 : BEAUFORTVARIANT CIPHER", BeaufortVariantCipherPanel.class);
        addCipher("025 : BELLASO CIPHER", BellasoCipherPanel.class);
        addCipher("026 : BIFID CIPHER", BifidCipherPanel.class);
        addCipher("027 : BINARY CIPHER", BinaryCipherPanel.class);
        addCipher("028 : BLOWFISH", BlowfishPanel.class);
        addCipher("029 : BOOK CIPHER", BookCipherPanel.class);
        addCipher("030 : BRUCE CODE", BruceCodePanel.class);
        addCipher("031 : BURROWS-WHEELER", BurrowsWheelerPanel.class);
        addCipher("032 : BURROWS-WHEELER CIPHER", BurrowsWheelerCipherPanel.class);
        addCipher("033 : CADENUS CIPHER", CadenusCipherPanel.class);
        addCipher("034 : CAESAR CIPHER", CaesarCipherPanel.class);
        addCipher("035 : CALYPSO CIPHER", CalypsoCipherPanel.class);
        addCipher("036 : CAMELLIA", CamelliaPanel.class);
        addCipher("037 : CARDAN GRILLE", CardanGrillePanel.class);
        addCipher("038 : CAST-128", Cast128Panel.class);
        addCipher("039 : CAST-256", Cast256Panel.class);
        addCipher("040 : CHACHA20", ChaCha20Panel.class);
        addCipher("041 : CHACHA CIPHER", ChaChaCipherPanel.class);
        addCipher("042 : CHAO CIPHER", ChaoCipherPanel.class);
        addCipher("043 : CHINESE REMAINDER CIPHER", ChineseRemainderCipherPanel.class);
        addCipher("044 : CLEFIA", ClefiaPanel.class);
        addCipher("045 : COLUMNAR TRANSPOSITION", ColumnarTranspositionPanel.class);
        addCipher("046 : COMBINED CIPHER DISK", CombinedCipherDiskPanel.class);
        addCipher("047 : CONJUGATED CIPHER", ConjugatedCipherPanel.class);
        addCipher("048 : COPTIC CIPHER", CopticCipherPanel.class);
        addCipher("049 : CRAMER SHOUP", CramerShoupPanel.class);
        addCipher("050 : CRYPTOGRAM SUBSTITUTION", CryptogramSubstitutionPanel.class);
        addCipher("051 : DANCING MEN CIPHER", DancingMenCipherPanel.class);
        addCipher("052 : DES", DESPanel.class);
        addCipher("053 : DOUBLE COLUMNAR TRANSPOSITION", DoubleColumnarTranspositionPanel.class);
        addCipher("054 : DOUBLE DES", DoubleDESPanel.class);
        addCipher("055 : DOUBLE PLAYFAIR", DoublePlayfairPanel.class);
        addCipher("056 : DRACONIANSHIFT", DraconianShiftPanel.class);
        addCipher("057 : E0 BLUETOOTH", E0BluetoothPanel.class);
        addCipher("058 : ECB MODE", ECBModePanel.class);
        addCipher("059 : ECC", ECCPanel.class);
        addCipher("060 : EDON-80", Edon80Panel.class);
        addCipher("061 : ELGAMAL", ElGamalPanel.class);
        addCipher("062 : ELLIPTIC CURVE CIPHER", EllipticCurveCipherECCPanel.class);
        addCipher("063 : ENIGMA MACHINE", EnigmaMachinePanel.class);
        addCipher("064 : EXPONENTIAL CIPHER", ExponentialCipherPanel.class);
        addCipher("065 : EXTENDED VIGENERE", ExtendedVigenerePanel.class);
        addCipher("066 : FIALKA CIPHER", FialkaCipherPanel.class);
        addCipher("067 : FOUR SQUARE CIPHER", FourSquareCipherPanel.class);
        addCipher("068 : FRACTIONATED MORSE", FractionatedMorsePanel.class);
        addCipher("069 : FREEMASON CIPHER", FreemasonCipherPanel.class);
        addCipher("070 : FREQUENCY CIPHER", FrequencyCipherPanel.class);
        addCipher("071 : FRINGE CIPHER", FringeCipherPanel.class);
        addCipher("072 : GADERYPOLUKI", GaderypolukiPanel.class);
        addCipher("073 : GALOIS COUNTER MODE", GaloisCounterModePanel.class);
        addCipher("074 : GEE CIPHER", GEECipherPanel.class);
        addCipher("075 : GIFT", GIFTPanel.class);
        addCipher("076 : GOST", GOSTPanel.class);
        addCipher("077 : GRAIN", GrainPanel.class);
        addCipher("078 : GRILLE CIPHER", GrilleCipherPanel.class);
        addCipher("079 : GROMARK CIPHER", GromarkCipherPanel.class);
        addCipher("080 : GRONSFELD CIPHER", GronsfeldCipherPanel.class);
        addCipher("081 : HAGELIN M209", HagelinM209Panel.class);
        addCipher("082 : HASH BASED CIPHER", HashBasedCipherPanel.class);
        addCipher("083 : HASHING", HashingPanel.class);
        addCipher("084 : HEBERN ROTOR MACHINE", HebernRotorMachinePanel.class);
        addCipher("085 : HIGHT", HightPanel.class);
        addCipher("086 : HILL CIPHER", HillCipherPanel.class);
        addCipher("087 : HMAC BASED SYSTEMS", HMACBasedSystemsPanel.class);
        addCipher("088 : HOMOPHONIC SUBSTITUTION", HomophonicSubstitutionPanel.class);
        addCipher("089 : HORTON CIPHER", HortonCipherPanel.class);
        addCipher("090 : HUFFMAN CIPHER", HuffmanCipherPanel.class);
        addCipher("091 : IDEAL CIPHER", IDEALCipherPanel.class);
        addCipher("092 : IDEA", IDEAPanel.class);
        addCipher("093 : INDIRECT SUBSTITUTION", IndirectSubstitutionPanel.class);
        addCipher("094 : INVERSIVE CIPHER", InversiveCipherPanel.class);
        addCipher("095 : ISO STREAM CIPHERS", ISOStreamCiphersPanel.class);
        addCipher("096 : J CIPHER", JCipherPanel.class);
        addCipher("097 : JEFFERSON CYLINDER", JeffersonCylinderPanel.class);
        addCipher("098 : JIGSAW CIPHER", JigsawCipherPanel.class);
        addCipher("099 : JN-25", JN25Panel.class);
        addCipher("100 : KAHN CIPHER", KahnCipherPanel.class);
        addCipher("101 : KASISKI SYSTEM", KasiskiSystemPanel.class);
        addCipher("102 : KASUMI", KasumiPanel.class);
        addCipher("103 : KEYED CAESAR", KeyedCaesarPanel.class);
        addCipher("104 : KEYWORD CIPHER", KeywordCipherPanel.class);
        addCipher("105 : KLEIN", KleinPanel.class);
        addCipher("106 : KLINGON CIPHER", KlingonCipherPanel.class);
        addCipher("107 : KNAPSACK CIPHER", KnapsackCipherPanel.class);
        addCipher("108 : KYBER", KyberPanel.class);
        addCipher("109 : LATIN SQUARE CIPHER", LatinSquareCipherPanel.class);
        addCipher("110 : LAVARAND CIPHER", LavarandCipherPanel.class);
        addCipher("111 : LBLOCK", LBlockPanel.class);
        addCipher("112 : LED", LEDPanel.class);
        addCipher("113 : LFSR CIPHERS", LFSRCiphersPanel.class);
        addCipher("114 : LINEAR SUBSTITUTION", LinearSubstitutionPanel.class);
        addCipher("115 : LOGICAL XOR CIPHER", LogicalXORCipherPanel.class);
        addCipher("116 : LORENZ SZ40", LorenzSZ40Panel.class);
        addCipher("117 : M-138 CIPHER", M138CipherPanel.class);
        addCipher("118 : M-209", M209Panel.class);
        addCipher("119 : M-94 CIPHER", M94CipherPanel.class);
        addCipher("120 : MAC BACON CIPHER", MACBaconCipherPanel.class);
        addCipher("121 : MALBOLGE ENCRYPTION", MalbolgeEncryptionPanel.class);
        addCipher("122 : MARS", MarsPanel.class);
        addCipher("123 : MATRIX CIPHER", MatrixCipherPanel.class);
        addCipher("124 : MCELIECE", McEliecePanel.class);
        addCipher("125 : MERKLE HELLMAN", MerkleHellmanPanel.class);
        addCipher("126 : MISTY1", Misty1Panel.class);
        addCipher("127 : MODULAR CIPHER", ModularCipherPanel.class);
        addCipher("128 : MONOALPHABETIC SUBSTITUTION", MonoalphabeticSubstitutionPanel.class);
        addCipher("129 : MORSE CODE", MorseCodePanel.class);
        addCipher("130 : MYSZKOWSKI TRANSPOSITION", MyszkowskiTranspositionPanel.class);
        addCipher("131 : NATO CODE", NATOCodePanel.class);
        addCipher("132 : NIHILIST CIPHER", NihilistCipherPanel.class);
        addCipher("133 : NTRU", NTRUPanel.class);
        addCipher("134 : DOUBLE TRANSPOSITION", DoubleTranspositionPanel.class);
        addCipher("135 : HMAC", HMACPanel.class);
        addCipher("136 : HEBERN ROTOR", HebernRotorPanel.class);
        addCipher("137 : ISO STREAM", ISOStreamPanel.class);
        addCipher("138 : NOEKEON", NoekeonPanel.class);
        addCipher("139 : NULL CIPHER", NullCipherPanel.class);
        addCipher("140 : NUMBER STATION CIPHERS", NumberStationCiphersPanel.class);
        addCipher("141 : OTAR CIPHER", OTARCipherPanel.class);
        addCipher("142 : OBFUSCATED TRANSPOSITION", ObfuscatedTranspositionPanel.class);
        addCipher("143 : OCTAL CIPHER", OctalCipherPanel.class);
        addCipher("144 : ONE TIME PAD", OneTimePadPanel.class);
        addCipher("145 : OUT OF BAND CIPHER", OutofBandCipherPanel.class);
        addCipher("146 : PBKDF2 BASED SYSTEMS", PBKDF2BasedSystemsPanel.class);
        addCipher("147 : PSK CIPHER", PSKCipherPanel.class);
        addCipher("148 : PADDING ORACLE CIPHER", PaddingOracleCipherPanel.class);
        addCipher("149 : PANAMA", PanamaPanel.class);
        addCipher("150 : PASIGRAPHY CIPHER", PasigraphyCipherPanel.class);
        addCipher("151 : PERMUTATION CIPHER", PermutationCipherPanel.class);
        addCipher("152 : PIGPEN CIPHER", PigpenCipherPanel.class);
        addCipher("153 : PLAYFAIR CIPHER", PlayfairCipherPanel.class);
        addCipher("154 : POLYBIUS SQUARE", PolybiusSquarePanel.class);
        addCipher("155 : PORTA CIPHER", PortaCipherPanel.class);
        addCipher("156 : PRESENT", PresentPanel.class);
        addCipher("157 : PRIME NUMBER CIPHER", PrimeNumberCipherPanel.class);
        addCipher("158 : PRINCE", PrincePanel.class);
        addCipher("159 : PUFFERFISH CIPHER", PufferfishCipherPanel.class);
        addCipher("160 : QUADRATIC CIPHER", QuadraticCipherPanel.class);
        addCipher("161 : QUAGMIRE II-IV", QuagmireIIVPanel.class);
        addCipher("162 : QUANTUM CIPHERS", QuantumCiphersPanel.class);
        addCipher("163 : RABBIT", RabbitPanel.class);
        addCipher("164 : RAIL FENCE CIPHER", RailFenceCipherPanel.class);
        addCipher("165 : RANDOM SUBSTITUTION", RandomSubstitutionPanel.class);
        addCipher("166 : RC2", RC2Panel.class);
        addCipher("167 : RC4", RC4Panel.class);
        addCipher("168 : RC5", RC5Panel.class);
        addCipher("169 : RC6", RC6Panel.class);
        addCipher("170 : RECTANGLE", RectanglePanel.class);
        addCipher("171 : REDEFENSE CIPHER", RedefenseCipherPanel.class);
        addCipher("172 : RIVEST CIPHERS", RivestCiphersPanel.class);
        addCipher("173 : ROT13", ROT13Panel.class);
        addCipher("174 : ROT1/ROT47", ROT1ROT47Panel.class);
        addCipher("175 : RSA", RSAPanel.class);
        addCipher("176 : SHA BASED CIPHERS", SHABasedCiphersPanel.class);
        addCipher("177 : SIGABA", SIGABAPanel.class);
        addCipher("178 : SPN NETWORK", SPNNetworkPanel.class);
        addCipher("179 : SALSA20", Salsa20Panel.class);
        addCipher("180 : SCYTALE CIPHER", ScytaleCipherPanel.class);
        addCipher("181 : SEED", SeedPanel.class);
        addCipher("182 : SERPENT", SerpentPanel.class);
        addCipher("183 : SHIFT CIPHER", ShiftCipherPanel.class);
        addCipher("184 : SIMON", SimonPanel.class);
        addCipher("185 : SIMPLE SUBSTITUTION", SimpleSubstitutionPanel.class);
        addCipher("186 : SKIPJACK", SkipjackPanel.class);
        addCipher("187 : SNOW 3G", Snow3GPanel.class);
        addCipher("188 : SOLITAIRE CIPHER", SolitaireCipherPanel.class);
        addCipher("189 : SON OF PERMUTATION", SonofPermutationPanel.class);
        addCipher("190 : SPARTAN SCYTALE", SpartanScytalePanel.class);
        addCipher("191 : SPECK", SpeckPanel.class);
        addCipher("192 : STRADDLING CHECKERBOARD", StraddlingCheckerboardPanel.class);
        addCipher("193 : STREAM CIPHER", StreamCipherPanel.class);
        addCipher("194 : SWAGMAN CIPHER", SwagmanCipherPanel.class);
        addCipher("195 : TEA", TEAPanel.class);
        addCipher("196 : TABULA RECTA", TabulaRectaPanel.class);
        addCipher("197 : TAP CODE", TapCodePanel.class);
        addCipher("198 : TEMPLAR CIPHER", TemplarCipherPanel.class);
        addCipher("199 : THREE SQUARE CIPHER", ThreeSquareCipherPanel.class);
        addCipher("200 : TRIFID CIPHER", TrifidCipherPanel.class);
        addCipher("201 : TRITHEME CIPHER", TrithemeCipherPanel.class);
        addCipher("202 : TWOFISH", TwofishPanel.class);
        addCipher("203 : TYPEX CIPHER", TypexCipherPanel.class);
        addCipher("204 : ULTRA CIPHER", UltraCipherPanel.class);
        addCipher("205 : VERNAM CIPHER", VernamCipherPanel.class);
        addCipher("206 : VIGENERE CIPHER", VigenereCipherPanel.class);
        addCipher("207 : XOR CIPHER", XORCipherPanel.class);
    }

    private void addCipher(String name, Class<? extends JPanel> panelClass) {
        cipherMap.put(name, panelClass);

        AnimatedSidebarButton btn = new AnimatedSidebarButton(name);

        btn.addActionListener(e -> {
            resetSidebarButtons();
            btn.setSelected(true);
            loadCipher(name);
        });

        sidebarButtons.add(btn);
        cipherListPanel.add(btn);
        cipherListPanel.add(Box.createRigidArea(new Dimension(0, 2)));
    }

    private void loadCipher(String name) {
        if (!loadedPanels.containsKey(name)) {
            try {
                // Show loading cursor
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                Class<? extends JPanel> clazz = cipherMap.get(name);
                if (clazz != null) {
                    JPanel panel = clazz.getDeclaredConstructor().newInstance();
                    loadedPanels.put(name, panel);
                    contentPanel.showPanel(panel);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading cipher: " + e.getMessage());
            } finally {
                setCursor(Cursor.getDefaultCursor());
            }
        } else {
            contentPanel.showPanel(loadedPanels.get(name));
        }
    }

    private void resetSidebarButtons() {
        for (AnimatedSidebarButton b : sidebarButtons) {
            b.setSelected(false);
        }
    }

    private void filterCiphers() {
        String query = searchField.getText().toLowerCase();
        if (query.equals("search algorithms..."))
            query = "";

        cipherListPanel.removeAll();

        for (AnimatedSidebarButton btn : sidebarButtons) {
            if (btn.getText().toLowerCase().contains(query)) {
                cipherListPanel.add(btn);
                cipherListPanel.add(Box.createRigidArea(new Dimension(0, 2)));
            }
        }
        cipherListPanel.revalidate();
        cipherListPanel.repaint();
    }

    public static void main(String[] args) {
        // Enable hardware acceleration
        System.setProperty("sun.java2d.opengl", "true");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        components.SplashScreen splash = new components.SplashScreen();
        splash.showSplash();

        SwingUtilities.invokeLater(() -> new CryptoApp().setVisible(true));
    }
}