from PIL import Image

# Load the image
image_path = "logo-1.png"
image = Image.open(image_path)

# Convert the image to RGBA mode (if not already in it)
image = image.convert("RGBA")

# Get image data
data = image.getdata()

# Define the color to be removed (red background)
red_background = (150, 50, 50)  # Approximate red from the image
tolerance = 60  # Allow some variation in red

# Process image to remove red background
new_data = []
for item in data:
    r, g, b, a = item
    print(r,g,b,a)
    if abs(r - red_background[0]) < tolerance and abs(g - red_background[1]) < tolerance and abs(b - red_background[2]) < tolerance:
        new_data.append((255, 255, 255, 0))  # Transparent background
    else:
        new_data.append((255,255,255,255))  # Keep original color

# Apply new data to image
image.putdata(new_data)

# Save the processed image
output_path = "logo_1_transparent.png"
image.save(output_path)

# Provide the user with the download link
output_path