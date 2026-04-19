import os

def replace_in_files(dir_path, old_str, new_str):
    for root, dirs, files in os.walk(dir_path):
        for file in files:
            if file.endswith('.java') or file.endswith('.fxml') or file.endswith('pom.xml'):
                file_path = os.path.join(root, file)
                try:
                    with open(file_path, 'r', encoding='utf-8') as f:
                        content = f.read()
                    
                    if old_str in content:
                        new_content = content.replace(old_str, new_str)
                        with open(file_path, 'w', encoding='utf-8') as f:
                            f.write(new_content)
                        print(f"Updated {file_path}")
                except Exception as e:
                    print(f"Failed to process {file_path}: {e}")

if __name__ == '__main__':
    replace_in_files('.', 'com.sahilasopa', 'com.dhruvijain')
