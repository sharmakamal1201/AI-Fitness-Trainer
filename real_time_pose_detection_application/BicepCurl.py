import mediapipe as mp
import numpy as np
from utils import *
from image_edits import *

mp_drawing = mp.solutions.drawing_utils
mp_pose = mp.solutions.pose

# bicepts curl
# rules:
# 1. shoulder-elbow line closer to shoulder-hip line
# 2. extension must be close to straight line between shoulder and wrist
# 3. contraction must be less than 20 degree between shoulder-elbow-wrist

def bicepsUtils(landmarks, image, process, setCount):
    # get x,y cordinates of keypoints
    right_shoulder = [landmarks[mp_pose.PoseLandmark.RIGHT_SHOULDER.value].x, 
                      landmarks[mp_pose.PoseLandmark.RIGHT_SHOULDER.value].y]
    right_elbow = [landmarks[mp_pose.PoseLandmark.RIGHT_ELBOW.value].x, 
                   landmarks[mp_pose.PoseLandmark.RIGHT_ELBOW.value].y]
    right_wrist = [landmarks[mp_pose.PoseLandmark.RIGHT_WRIST.value].x, 
                   landmarks[mp_pose.PoseLandmark.RIGHT_WRIST.value].y]
    right_hip = [landmarks[mp_pose.PoseLandmark.RIGHT_HIP.value].x, 
                 landmarks[mp_pose.PoseLandmark.RIGHT_HIP.value].y]
    left_shoulder = [landmarks[mp_pose.PoseLandmark.LEFT_SHOULDER.value].x, 
                     landmarks[mp_pose.PoseLandmark.LEFT_SHOULDER.value].y]
    left_elbow = [landmarks[mp_pose.PoseLandmark.LEFT_ELBOW.value].x, 
                  landmarks[mp_pose.PoseLandmark.LEFT_ELBOW.value].y]
    left_wrist = [landmarks[mp_pose.PoseLandmark.LEFT_WRIST.value].x, 
                  landmarks[mp_pose.PoseLandmark.LEFT_WRIST.value].y]
    left_hip = [landmarks[mp_pose.PoseLandmark.LEFT_HIP.value].x, 
                landmarks[mp_pose.PoseLandmark.LEFT_HIP.value].y]
    
    # calculate angles
    left_hip_shoulder_elbow_angle = int(calculate_angle(left_hip, left_shoulder, left_elbow))
    right_hip_shoulder_elbow_angle = int(calculate_angle(right_hip, right_shoulder, right_elbow))
    left_shoulder_elbow_wrist_angle = int(calculate_angle(left_shoulder, left_elbow, left_wrist))
    right_shoulder_elbow_wrist_angle = int(calculate_angle(right_shoulder, right_elbow, right_wrist))
    
    # show angles on image
    show_text_on_image(image, left_hip_shoulder_elbow_angle, left_shoulder, (255, 255, 255), 2)
    show_text_on_image(image, right_hip_shoulder_elbow_angle, right_shoulder, (255, 255, 255), 2)
    show_text_on_image(image, left_shoulder_elbow_wrist_angle, left_elbow, (255, 255, 255), 2)
    show_text_on_image(image, right_shoulder_elbow_wrist_angle, right_elbow, (255, 255, 255), 2)
                
    instruction = ""
    completion_percentage = process + " - 0 %"
    isCompleted = False
        
    if process == "extension":
        completed = (100*np.minimum(left_shoulder_elbow_wrist_angle, right_shoulder_elbow_wrist_angle))//180
        completion_percentage = process + " - " + str(completed) + " %"
        
        # rule no. 1
        if left_hip_shoulder_elbow_angle > 20:
            instruction = "Move your left elbow closer to hip"
        elif right_hip_shoulder_elbow_angle > 20:
            instruction = "Move your right elbow closer to hip"
        
        # rule no. 2
        elif left_shoulder_elbow_wrist_angle < 170 and right_shoulder_elbow_wrist_angle < 170:
            instruction = "Extend the bicep muscle, try to make straight line between shoulder and wrist"
        elif left_shoulder_elbow_wrist_angle < 170 and right_shoulder_elbow_wrist_angle > 170:
            instruction = "Extend the bicep muscle, try to make straight line between left shoulder and wrist"
        elif left_shoulder_elbow_wrist_angle > 170 and right_shoulder_elbow_wrist_angle < 170:
            instruction = "Extend the bicep muscle, try to make straight line between right shoulder and wrist"
        else:
            instruction = ""
            completion_percentage = "extension - 100 %"
            isCompleted = True
            process = "contraction"

    
    if process == "contraction":
        completed = (100*(180-np.maximum(left_shoulder_elbow_wrist_angle, right_shoulder_elbow_wrist_angle)))//180
        completion_percentage = process + " - " + str(completed) + " %"
        
        # rule no. 1
        if left_hip_shoulder_elbow_angle > 20:
            instruction = "Move your left elbow closer to hip"
        elif right_hip_shoulder_elbow_angle > 20:
            instruction = "Move your right elbow closer to hip"
        
        # rule no. 3
        elif left_shoulder_elbow_wrist_angle > 30 and right_shoulder_elbow_wrist_angle > 30:
            instruction = "contract the bicep muscle, lift your wrists closer to shoulders"
        elif left_shoulder_elbow_wrist_angle > 30 and right_shoulder_elbow_wrist_angle < 30:
            instruction = "contract the bicep muscle, lift your left wrist closer to shoulder"
        elif left_shoulder_elbow_wrist_angle < 30 and right_shoulder_elbow_wrist_angle > 30:
            instruction = "contract the bicep muscle, lift your right wrist closer to shoulder"
        else:
            instruction = ""
            completion_percentage = "contraction - 100 %"
            isCompleted = True
            process = "extension"
        
    if process == "contraction" and isCompleted == True:
        setCount += 1

    # show message on image
    show_text_on_image(image, completion_percentage, [0.01, 0.05], color=(0, 255, 0), fontSize=1)
    return process, isCompleted, instruction, setCount