
import os
import zipfile

def zipdir(path, ziph):
    # ziph is zipfile handle
    for root, dirs, files in os.walk(path):
        # Exclude git, idea, bin, build directories
        if any(x in root for x in ['.git', '.idea', 'bin', 'build', '__pycache__']):
            continue
            
        for file in files:
            if file.endswith('.java') or file.endswith('.md') or file.endswith('.pdf'):
                file_path = os.path.join(root, file)
                arcname = os.path.relpath(file_path, os.path.join(path, '..'))
                # We want the archive to start at the root of the project
                # currently root is c:\...\CRYPTOGRAPHY UI DESIGN
                # We want the internal path to be relative to that.
                
                rel_path = os.path.relpath(file_path, path)
                ziph.write(file_path, rel_path)

zip_filename = 'Cryptography_Suite_Source_Code.zip'
with zipfile.ZipFile(zip_filename, 'w', zipfile.ZIP_DEFLATED) as zipf:
    zipdir('.', zipf)

print(f"Created {zip_filename}")
