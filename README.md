# 🔐 Cryptography Suite - Advanced Encryption Application

A comprehensive Java Swing application featuring **200+ cryptographic algorithms** with a modern, cyberpunk-themed user interface.

![Version](https://img.shields.io/badge/version-1.0.0-blue)
![Java](https://img.shields.io/badge/java-8%2B-orange)
![License](https://img.shields.io/badge/license-MIT-green)

## ✨ Features

### 🎨 Modern UI Design
- **Cyberpunk-themed interface** with particle effects background
- **Animated sidebar buttons** with smooth transitions
- **Neon-styled components** for a premium look
- **Dark theme** optimized for long coding sessions
- **Search functionality** to quickly find algorithms

### 🔒 Cryptography Algorithms
The suite includes implementations of numerous cipher algorithms:
- Classical Ciphers (Caesar, Vigenère, Playfair, etc.)
- Modern Encryption Standards
- Hash Functions
- Custom Algorithms
- And many more...

### 📊 Additional Features
- **Performance Graph** visualization
- **Splash Screen** with loading animation
- **Professional Scrollbars** with custom styling
- **Responsive Design** that adapts to window size

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Java Runtime Environment (JRE)

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/YOUR_USERNAME/cryptography-suite.git
cd cryptography-suite
```

2. **Compile the application**
```bash
javac CryptoApp.java
```

3. **Run the application**
```bash
java CryptoApp
```

## 📁 Project Structure

```
CRYPTOGRAPHY-SUITE/
├── CryptoApp.java              # Main application file
├── components/                  # UI Components
│   ├── AnimatedSidebarButton.java
│   ├── CyberBackground.java
│   ├── NeonButton.java
│   ├── DarkScrollPane.java
│   ├── SplashScreen.java
│   ├── PerformanceGraph.java
│   ├── Theme.java
│   ├── CardPanel.java
│   └── ContentPanel.java
├── ciphers/                     # Cipher implementations (200+)
│   └── [Various cipher classes]
├── generate_code_pdf.py        # Professional PDF documentation generator
├── zip_source.py               # Source code archiver
└── README.md                   # This file
```

## 🛠️ Utilities

### PDF Documentation Generator
Generate professional PDF documentation of your source code:

```bash
python generate_code_pdf.py
```

**Features:**
- Professional cover page with project information
- Automatic table of contents generation
- Syntax-highlighted code blocks with line numbers
- File statistics (lines, size, type)
- Custom headers and footers
- Support for Java, Python, Markdown, and Text files

## 🎯 Usage

1. **Launch the application** - The splash screen will appear during initialization
2. **Browse ciphers** - Use the sidebar to navigate through available algorithms
3. **Search functionality** - Type in the search bar to filter ciphers
4. **Select an algorithm** - Click on any cipher to view its implementation
5. **Encrypt/Decrypt** - Use the cipher's interface to process your data

## 🎨 UI Components

### CyberBackground
- Dynamic particle system
- Animated background effects
- Cyberpunk aesthetic

### AnimatedSidebarButton
- Smooth hover animations
- Click feedback
- Color transitions

### NeonButton
- Glowing effect
- Modern styling
- Interactive feedback

## 📦 Building for Distribution

To create a distributable JAR file:

```bash
jar cvfm CryptoSuite.jar manifest.txt *.class components/*.class ciphers/*.class
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👨‍💻 Author

**Developer Team**

## 🙏 Acknowledgments

- Inspired by modern cryptography and cybersecurity practices
- UI design influenced by cyberpunk aesthetics
- Built with Java Swing for cross-platform compatibility

## 📸 Screenshots

*Screenshots coming soon...*

## 🗺️ Roadmap

- [ ] Add more cipher algorithms
- [ ] Implement file encryption/decryption
- [ ] Add algorithm performance benchmarks
- [ ] Create mobile version
- [ ] Add educational mode with explanations
- [ ] Implement algorithm visualization

## 📞 Support

For support, email your-email@example.com or open an issue in the repository.

---

**⭐ Star this repository if you find it helpful!**

*Built with ❤️ and lots of ☕*
