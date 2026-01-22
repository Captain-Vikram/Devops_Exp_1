import sys
# Force Python to use UTF-8 for printing
sys.stdout.reconfigure(encoding='utf-8')
import os
from playwright.sync_api import sync_playwright

# 1. Get the URL from Jenkins (Argument 1)
url = sys.argv[1] if len(sys.argv) > 1 else "https://motion.dev"

# Create an output folder for neatness
output_dir = "captured_data"
if not os.path.exists(output_dir):
    os.makedirs(output_dir)

print(f"---------------------------------------------")
print(f"🔗 TARGET URL: {url}")
print(f"---------------------------------------------")

with sync_playwright() as p:
    # Launch browser (Headless means it runs invisibly in background)
    print("[SYSTEM] Launching Browser...")
    browser = p.chromium.launch(headless=True)
    page = browser.new_page()

    # Go to the website
    print("[SYSTEM] Loading page (this might take a moment)...")
    page.goto(url, timeout=60000) # 60 seconds timeout
    
    # 2. Save HTML Source
    print("[SYSTEM] Downloading HTML source...")
    html_content = page.content()
    with open(f"{output_dir}/page_source.html", "w", encoding="utf-8") as f:
        f.write(html_content)
    
    # 3. Take Full Page Screenshot
    print("[SYSTEM] Taking full-page screenshot...")
    page.screenshot(path=f"{output_dir}/full_page.png", full_page=True)

    browser.close()

print(f"---------------------------------------------")
print(f"✅ SUCCESS! Files saved in '{output_dir}' folder:")
print(f"   1. page_source.html")
print(f"   2. full_page.png")
print(f"---------------------------------------------")