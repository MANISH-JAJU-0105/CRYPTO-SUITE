#!/usr/bin/env python3
"""
Advanced Code Documentation PDF Generator
Generates professional PDF documentation from source code
Author: Enhanced by Antigravity AI
Version: 2.0.0
"""

import os
import sys
import datetime
from pathlib import Path
from typing import List, Tuple

# Check and install fpdf2 if needed
try:
    from fpdf import FPDF
except ImportError:
    print("Installing required package: fpdf2...")
    import subprocess
    subprocess.check_call([sys.executable, "-m", "pip", "install", "fpdf2"])
    from fpdf import FPDF


# ============================================================================
# CONFIGURATION
# ============================================================================
class Config:
    """Configuration settings for PDF generation"""
    # Project information
    PROJECT_NAME = "Cryptography Suite"
    PROJECT_DESCRIPTION = "Advanced Cryptography Application - Source Code Documentation"
    AUTHOR = "Developer Team"
    VERSION = "1.0.0"
    
    # File scanning
    ROOT_DIR = "."
    EXTENSIONS = ['.java', '.py', '.txt', '.md']
    EXCLUDE_DIRS = ['.git', '__pycache__', 'node_modules', '.idea', 'build', 'dist']
    EXCLUDE_FILES = ['desktop.ini', '.gitignore', '.DS_Store']
    
    # PDF output
    OUTPUT_FILENAME = "SOURCE_CODE_PROFESSIONAL.pdf"
    
    # Styling
    FONT_TITLE = "Helvetica"
    FONT_HEADER = "Helvetica"
    FONT_CODE = "Courier"
    FONT_SIZE_TITLE = 24
    FONT_SIZE_HEADER = 14
    FONT_SIZE_SUBHEADER = 12
    FONT_SIZE_CODE = 8
    FONT_SIZE_TOC = 10
    
    # Colors (RGB)
    COLOR_PRIMARY = (41, 128, 185)      # Blue
    COLOR_SECONDARY = (52, 73, 94)      # Dark Blue-Gray
    COLOR_ACCENT = (231, 76, 60)        # Red
    COLOR_CODE_BG = (245, 245, 245)     # Light Gray
    COLOR_HEADER = (44, 62, 80)         # Dark Gray
    COLOR_TEXT = (0, 0, 0)              # Black
    
    # Layout
    PAGE_MARGIN = 15
    CODE_LINE_HEIGHT = 4


# ============================================================================
# PDF CLASS WITH ENHANCED FEATURES
# ============================================================================
class ProfessionalCodePDF(FPDF):
    """Enhanced PDF class with header, footer, and styling"""
    
    def __init__(self, config):
        super().__init__()
        self.config = config
        self.toc_entries = []
        
    def header(self):
        """Custom header for each page"""
        if self.page_no() == 1:
            return  # No header on cover page
            
        # Header background
        self.set_fill_color(*self.config.COLOR_HEADER)
        self.rect(0, 0, 210, 20, 'F')
        
        # Project name
        self.set_font(self.config.FONT_HEADER, 'B', 10)
        self.set_text_color(255, 255, 255)
        self.set_xy(10, 8)
        self.cell(0, 10, self.config.PROJECT_NAME, 0, 0, 'L')
        
        # Page number
        self.set_xy(10, 8)
        page_str = f'Page {self.page_no()}'
        self.cell(0, 10, page_str, 0, 0, 'R')
        
        self.ln(15)
        
    def footer(self):
        """Custom footer for each page"""
        if self.page_no() == 1:
            return  # No footer on cover page
            
        self.set_y(-15)
        self.set_font(self.config.FONT_HEADER, 'I', 8)
        self.set_text_color(128, 128, 128)
        timestamp = datetime.datetime.now().strftime("%Y-%m-%d %H:%M")
        self.cell(0, 10, f'Generated: {timestamp}', 0, 0, 'C')
        
    def add_cover_page(self):
        """Create professional cover page"""
        self.add_page()
        
        # Background gradient effect (simulated with rectangles)
        self.set_fill_color(*self.config.COLOR_PRIMARY)
        self.rect(0, 0, 210, 100, 'F')
        self.set_fill_color(*self.config.COLOR_SECONDARY)
        self.rect(0, 100, 210, 197, 'F')
        
        # Title
        self.set_y(80)
        self.set_font(self.config.FONT_TITLE, 'B', self.config.FONT_SIZE_TITLE)
        self.set_text_color(255, 255, 255)
        self.multi_cell(0, 15, self.config.PROJECT_NAME, 0, 'C')
        
        # Subtitle
        self.ln(5)
        self.set_font(self.config.FONT_HEADER, '', 16)
        self.multi_cell(0, 10, "Source Code Documentation", 0, 'C')
        
        # Description
        self.ln(20)
        self.set_font(self.config.FONT_HEADER, '', 12)
        self.multi_cell(0, 8, self.config.PROJECT_DESCRIPTION, 0, 'C')
        
        # Info box
        self.ln(30)
        self.set_fill_color(255, 255, 255)
        self.set_draw_color(*self.config.COLOR_PRIMARY)
        self.set_line_width(0.5)
        y_pos = self.get_y()
        self.rect(40, y_pos, 130, 40, 'D')
        
        self.set_text_color(*self.config.COLOR_SECONDARY)
        self.set_font(self.config.FONT_HEADER, 'B', 11)
        self.set_xy(40, y_pos + 8)
        self.cell(130, 6, f"Version: {self.config.VERSION}", 0, 1, 'C')
        self.cell(130, 6, f"Author: {self.config.AUTHOR}", 0, 1, 'C')
        date_str = datetime.datetime.now().strftime('%B %d, %Y')
        self.cell(130, 6, f"Date: {date_str}", 0, 1, 'C')
        
    def add_toc(self):
        """Add table of contents"""
        if not self.toc_entries:
            return
            
        self.add_page()
        self.set_text_color(*self.config.COLOR_TEXT)
        self.set_font(self.config.FONT_HEADER, 'B', 18)
        self.cell(0, 15, "Table of Contents", 0, 1, 'L')
        self.ln(5)
        
        self.set_font(self.config.FONT_HEADER, '', self.config.FONT_SIZE_TOC)
        
        for title, page_num in self.toc_entries:
            # File name (truncate if too long)
            display_title = title if len(title) <= 70 else title[:67] + "..."
            self.cell(150, 6, f"  {display_title}", 0, 0, 'L')
            self.cell(0, 6, str(page_num), 0, 1, 'R')
            
    def add_file_chapter(self, file_path, content, file_num, total_files):
        """Add a new file as a chapter"""
        # Record TOC entry
        self.add_page()
        current_page = self.page_no()
        self.toc_entries.append((file_path, current_page))
        
        # File header with colored background
        self.set_fill_color(*self.config.COLOR_PRIMARY)
        self.set_text_color(255, 255, 255)
        self.set_font(self.config.FONT_HEADER, 'B', self.config.FONT_SIZE_HEADER)
        self.cell(0, 12, f"File {file_num}/{total_files}", 0, 1, 'L', True)
        
        # File path
        self.set_fill_color(*self.config.COLOR_SECONDARY)
        self.set_font(self.config.FONT_HEADER, '', self.config.FONT_SIZE_SUBHEADER)
        self.multi_cell(0, 8, file_path, 0, 'L', True)
        
        self.ln(4)
        
        # File stats
        self.set_text_color(*self.config.COLOR_TEXT)
        self.set_font(self.config.FONT_HEADER, 'I', 9)
        lines = content.count('\n') + 1
        size = len(content)
        ext = Path(file_path).suffix
        stats_text = f"Lines: {lines} | Size: {size:,} bytes | Type: {ext}"
        self.cell(0, 5, stats_text, 0, 1, 'L')
        
        self.ln(3)
        
        # Code content
        self.add_code_block(content)
        
    def add_code_block(self, code):
        """Add formatted code block with line numbers"""
        self.set_fill_color(*self.config.COLOR_CODE_BG)
        self.set_text_color(*self.config.COLOR_TEXT)
        self.set_font(self.config.FONT_CODE, '', self.config.FONT_SIZE_CODE)
        
        # Handle encoding issues - very robust approach
        safe_code = self.sanitize_text(code)
        
        # Add line numbers and format
        lines = safe_code.split('\n')
        for i, line in enumerate(lines, 1):
            # Prevent page overflow
            if self.get_y() > 270:
                self.add_page()
                
            # Add with line number
            line_text = f"{i:4d} | {line}"
            
            # Additional sanitization for each line
            line_text = self.sanitize_text(line_text)
            
            try:
                self.multi_cell(0, self.config.CODE_LINE_HEIGHT, line_text, 0, 'L', False)
            except Exception:
                # Ultimate fallback
                try:
                    fallback = f"{i:4d} | [encoding error in line]"
                    self.multi_cell(0, self.config.CODE_LINE_HEIGHT, fallback, 0, 'L', False)
                except:
                    pass  # Skip problematic lines completely
    
    def sanitize_text(self, text):
        """Sanitize text for PDF encoding"""
        if not text:
            return ""
            
        # Replace common Unicode characters
        replacements = {
            '\u2019': "'",  # Right single quotation mark
            '\u201c': '"',  # Left double quotation mark
            '\u201d': '"',  # Right double quotation mark
            '\u2013': '-',  # En dash
            '\u2014': '--', # Em dash
            '\u2026': '...', # Ellipsis
            '\u00a0': ' ',  # Non-breaking space
        }
        
        for old, new in replacements.items():
            text = text.replace(old, new)
        
        # Encode to latin-1 with replacement
        try:
            text = text.encode('latin-1', 'replace').decode('latin-1')
        except Exception:
            # If that fails, force ASCII
            text = text.encode('ascii', 'replace').decode('ascii')
            
        return text


# ============================================================================
# MAIN GENERATOR
# ============================================================================
def scan_files(config):
    """Scan directory for source code files"""
    files_found = []
    
    root_path = Path(config.ROOT_DIR).resolve()
    print(f"\n🔍 Scanning directory: {root_path}")
    print(f"📋 Looking for extensions: {', '.join(config.EXTENSIONS)}")
    
    for root, dirs, files in os.walk(root_path):
        # Exclude directories
        dirs[:] = [d for d in dirs if d not in config.EXCLUDE_DIRS]
        
        for file in files:
            # Check extension and exclusions
            if (any(file.endswith(ext) for ext in config.EXTENSIONS) and 
                file not in config.EXCLUDE_FILES):
                
                file_path = Path(root) / file
                rel_path = file_path.relative_to(root_path)
                
                # Skip excluded patterns
                if any(excl in str(rel_path) for excl in config.EXCLUDE_DIRS):
                    continue
                
                try:
                    with open(file_path, 'r', encoding='utf-8') as f:
                        content = f.read()
                    files_found.append((str(rel_path), content))
                    print(f"  ✓ {rel_path}")
                except Exception as e:
                    print(f"  ✗ {rel_path}: {e}")
                    
    return files_found


def generate_pdf(config):
    """Main PDF generation function"""
    print("\n" + "="*70)
    print("  📄 PROFESSIONAL CODE DOCUMENTATION GENERATOR")
    print("="*70)
    
    # Scan files
    files = scan_files(config)
    
    if not files:
        print("\n❌ No files found matching criteria!")
        return False
    
    print(f"\n✅ Found {len(files)} file(s)")
    
    # Create PDF
    print(f"\n📝 Generating PDF...")
    pdf = ProfessionalCodePDF(config)
    pdf.set_auto_page_break(auto=True, margin=config.PAGE_MARGIN)
    
    # Add cover page
    print("  → Adding cover page...")
    pdf.add_cover_page()
    
    # Add files
    print("  → Processing source files...")
    for i, (file_path, content) in enumerate(files, 1):
        print(f"  → [{i}/{len(files)}] {file_path}")
        pdf.add_file_chapter(file_path, content, i, len(files))
    
    # Add TOC (insert after cover)
    print("  → Generating table of contents...")
    pdf.add_toc()
    
    # Save PDF
    output_path = Path(config.OUTPUT_FILENAME)
    try:
        pdf.output(str(output_path))
        file_size = output_path.stat().st_size
        print(f"\n✅ SUCCESS! PDF generated:")
        print(f"   📁 File: {output_path.absolute()}")
        print(f"   📊 Size: {file_size:,} bytes ({file_size/1024/1024:.2f} MB)")
        print(f"   📄 Pages: {pdf.page_no()}")
        print(f"   📋 Files: {len(files)}")
        return True
    except Exception as e:
        print(f"\n❌ ERROR: Failed to save PDF: {e}")
        import traceback
        traceback.print_exc()
        return False


# ============================================================================
# ENTRY POINT
# ============================================================================
if __name__ == "__main__":
    config = Config()
    success = generate_pdf(config)
    
    print("\n" + "="*70)
    if success:
        print("  🎉 DOCUMENTATION GENERATION COMPLETE!")
    else:
        print("  ⚠️  GENERATION FAILED - Please check errors above")
    print("="*70 + "\n")
    
    sys.exit(0 if success else 1)
