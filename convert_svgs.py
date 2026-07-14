import xml.etree.ElementTree as ET
import re
import os
import sys

def svg_to_android_vector(svg_content, output_size=24):
    # Parse SVG
    svg_content = svg_content.replace('xmlns=', 'xmlns:svg=')
    svg_content = svg_content.replace('<svg', '<svg xmlns:android="http://schemas.android.com/apk/res/android"')
    
    # Extract viewBox or width/height
    viewBox_match = re.search(r'viewBox="([^"]*)"', svg_content)
    if viewBox_match:
        viewBox = viewBox_match.group(1).split()
        if len(viewBox) == 4:
            vw, vh = float(viewBox[2]), float(viewBox[3])
        else:
            vw, vh = output_size, output_size
    else:
        width_match = re.search(r'width="(\d+)"', svg_content)
        height_match = re.search(r'height="(\d+)"', svg_content)
        vw = float(width_match.group(1)) if width_match else output_size
        vh = float(height_match.group(1)) if height_match else output_size
    
    # Extract paths
    paths = re.findall(r'<path[^>]*d="([^"]*)"[^>]*/>', svg_content)
    fills = re.findall(r'<path[^>]*fill="([^"]*)"[^>]*/>', svg_content)
    
    # Build Android Vector Drawable
    result = f'''<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="{int(output_size)}dp"
    android:height="{int(output_size)}dp"
    android:viewportWidth="{int(vw)}"
    android:viewportHeight="{int(vh)}">
'''
    
    for i, path_data in enumerate(paths):
        fill = fills[i] if i < len(fills) else '#000000'
        fill = fill.replace('"', '')
        
        # Convert hex colors
        if fill.startswith('#'):
            android_fill = fill.upper()
        elif fill == 'none':
            continue
        else:
            android_fill = '#000000'
        
        result += f'    <path\n'
        result += f'        android:fillColor="{android_fill}"\n'
        result += f'        android:pathData="{path_data}"/>\n'
    
    result += '</vector>'
    return result

def convert_svg_file(input_path, output_path, size=24):
    with open(input_path, 'r', encoding='utf-8') as f:
        svg_content = f.read()
    
    android_vector = svg_to_android_vector(svg_content, size)
    
    with open(output_path, 'w', encoding='utf-8') as f:
        f.write(android_vector)
    
    print(f"Converted: {input_path} -> {output_path}")

# Convert all SVGs
input_dir = r"C:\Users\alber\Documents\Projects\Linked\assets\icons"
output_dir = r"C:\Users\alber\Documents\Projects\Linked\app\src\main\res\drawable"

svg_files = [
    "linked-logo.svg",
    "figma-icon.svg", 
    "claude-icon.svg",
    "instagram-icon.svg",
    "youtube-icon.svg",
    "dribbble-icon.svg",
    "github-icon.svg",
    "pinterest-icon.svg",
    "vercel-icon.svg",
    "linkedin-icon.svg",
    "notion-icon.svg",
    "canva-icon.svg",
    "amazon-icon.svg"
]

for svg_file in svg_files:
    input_path = os.path.join(input_dir, svg_file)
    output_name = svg_file.replace('-', '_').replace('.svg', '.xml')
    output_path = os.path.join(output_dir, output_name)
    
    if os.path.exists(input_path):
        try:
            convert_svg_file(input_path, output_path, size=24)
        except Exception as e:
            print(f"Error converting {svg_file}: {e}")
    else:
        print(f"File not found: {input_path}")

print("\nConversion complete!")
