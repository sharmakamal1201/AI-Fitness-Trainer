from win32api import GetSystemMetrics
import numpy as np
import cv2

def show_text_on_image(image, text, location, color, fontSize):
    cv2.putText(image, str(text),tuple(np.multiply(location, [640, 480]).astype(int)),
                cv2.FONT_HERSHEY_SIMPLEX, 0.5, color, fontSize, cv2.LINE_AA)

def get_resized_for_display_img(img):
    screen_w, screen_h = GetSystemMetrics(0), GetSystemMetrics(1)
    h,w,channel_nbr = img.shape
    # img get w of screen and adapt h
    h = h * (screen_w / w)
    w = screen_w
    if h > screen_h: #if img h still too big
        # img get h of screen and adapt w
        w = w * (screen_h / h)
        h = screen_h
    w, h = w*0.9, h*0.9 # because you don't want it to be that big, right ?
    w, h = int(w), int(h) # you need int for the cv2.resize
    return cv2.resize(img, (w, h))