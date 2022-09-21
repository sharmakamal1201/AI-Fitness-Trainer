import numpy as np

def calculate_angle(start, mid, end):
    start = np.array(start)
    mid = np.array(mid)
    end = np.array(end)
    
    radians = np.arctan2(end[1]-mid[1], end[0]-mid[0]) - np.arctan2(start[1]-mid[1], start[0]-mid[0])
    angle = np.abs(radians*180.0/np.pi)
    
    if angle> 180.0:
        angle = 360 - angle
    
    return angle

