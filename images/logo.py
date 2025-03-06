from PIL import Image
from PIL import ImageFilter

# 打开用户上传的图片
image_path = "logo-1.png"
image = Image.open(image_path)

# 转换为RGBA模式，以便处理透明度
image = image.convert("RGBA")
pixels = image.load()

# 定义要替换的颜色（红色背景）和替换后的颜色（透明）
red_background = (160, 40, 49, 255)  # 近似红色
tolerance = 50  # 颜色容差

# 处理图片
width, height = image.size
for x in range(width):
    for y in range(height):
        r, g, b, a = pixels[x, y]
        
        # 如果颜色接近红色背景，就变成透明
        if abs(r - red_background[0]) < tolerance and abs(g - red_background[1]) < tolerance and abs(b - red_background[2]) < tolerance:
            pixels[x, y] = (0, 0, 0, 0)  # 透明
        
        # 如果是白色前景，变成红色
        # elif r > 200 and g > 200 and b > 200:  # 近似白色
            # pixels[x, y] = (200, 0, 0, a)  # 变为红色

image =  image.filter(ImageFilter.SMOOTH_MORE)

# 保存处理后的图片
output_path = "logo-1_transparent.png"
image.save(output_path, format="PNG")

# 返回处理后的图片路径
output_path