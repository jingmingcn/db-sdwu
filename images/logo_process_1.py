import cv2
import numpy as np
import svgwrite
from skimage import measure

# Reload the uploaded transparent logo
image_path = "logo_1_transparent.png"
image = cv2.imread(image_path, cv2.IMREAD_UNCHANGED)

# Convert image to grayscale
gray = cv2.cvtColor(image, cv2.COLOR_BGRA2GRAY)

# Apply threshold to extract the foreground
_, binary = cv2.threshold(gray, 128, 255, cv2.THRESH_BINARY_INV)

# Find contours
contours = measure.find_contours(binary, 0.5)

# Create an SVG file
svg_path = "logo_vector.svg"
dwg = svgwrite.Drawing(svg_path, profile='tiny')

# Scale factor to maintain resolution
scale_factor = 1.0

# Convert contours to SVG paths
for contour in contours:
    points = [(p[1] * scale_factor, p[0] * scale_factor) for p in contour]
    path_data = "M " + " L ".join([f"{x},{y}" for x, y in points]) + " Z"
    dwg.add(dwg.path(d=path_data, fill="black", stroke="black", stroke_width=1))

# Save the SVG file
dwg.save()

# Provide the user with the download link
svg_path