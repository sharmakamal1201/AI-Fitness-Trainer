import mediapipe as mp
import numpy as np
from utils import *
from image_edits import *

mp_drawing = mp.solutions.drawing_utils
mp_pose = mp.solutions.pose


#push ups 
# rules
# 1. shoulder - hips - angle test
# 2. knee should not be bent


def pushUpsUtils(landmarks, image, process, setCount):
    right_shoulder = [landmarks[mp_pose.PoseLandmark.RIGHT_SHOULDER.value].x, 
                      landmarks[mp_pose.PoseLandmark.RIGHT_SHOULDER.value].y]
    right_hip = [landmarks[mp_pose.PoseLandmark.RIGHT_HIP.value].x, 
                 landmarks[mp_pose.PoseLandmark.RIGHT_HIP.value].y]
    right_heel = [landmarks[mp_pose.PoseLandmark.RIGHT_HEEL.value].x, 
                      landmarks[mp_pose.PoseLandmark.RIGHT_HEEL.value].y]
    right_elbow = [landmarks[mp_pose.PoseLandmark.RIGHT_ELBOW.value].x, 
                   landmarks[mp_pose.PoseLandmark.RIGHT_ELBOW.value].y]
    right_wrist = [landmarks[mp_pose.PoseLandmark.RIGHT_WRIST.value].x, 
                  landmarks[mp_pose.PoseLandmark.RIGHT_WRIST.value].y]
    left_shoulder = [landmarks[mp_pose.PoseLandmark.LEFT_SHOULDER.value].x, 
                     landmarks[mp_pose.PoseLandmark.LEFT_SHOULDER.value].y]
    left_hip = [landmarks[mp_pose.PoseLandmark.LEFT_HIP.value].x, 
                landmarks[mp_pose.PoseLandmark.LEFT_HIP.value].y]
    left_heel = [landmarks[mp_pose.PoseLandmark.LEFT_HEEL.value].x, 
                      landmarks[mp_pose.PoseLandmark.LEFT_HEEL.value].y]
    left_elbow = [landmarks[mp_pose.PoseLandmark.LEFT_ELBOW.value].x, 
                  landmarks[mp_pose.PoseLandmark.LEFT_ELBOW.value].y]
    left_wrist = [landmarks[mp_pose.PoseLandmark.LEFT_WRIST.value].x, 
                  landmarks[mp_pose.PoseLandmark.LEFT_WRIST.value].y]
                

    # calculate angles between left hip and left shoulder
    left_heel_hip_shoulder_angle = int(calculate_angle(left_heel, left_hip, left_shoulder))
    right_heel_hip_shoulder_angle = int(calculate_angle(right_heel, right_hip, right_shoulder))
    left_wrist_elbow_shoulder_angle = int(calculate_angle(left_wrist, left_elbow, left_shoulder))
    right_wrist_elbow_shoulder_angle = int(calculate_angle(right_wrist, right_elbow, right_shoulder))



    #show angles on images
    show_text_on_image(image, left_heel_hip_shoulder_angle, left_hip, (255, 255, 255), 2)
    show_text_on_image(image, right_heel_hip_shoulder_angle, right_hip, (255, 255, 255), 2)
    show_text_on_image(image, left_wrist_elbow_shoulder_angle, left_elbow, (255, 255, 255), 2)
    show_text_on_image(image, right_wrist_elbow_shoulder_angle, right_elbow, (255, 255, 255), 2)

    instruction = ""
    completion_percentage = process + " - 0 %"
    isCompleted = False

    if process == "extension":
        completed = (100*np.maximum(np.minimum(left_wrist_elbow_shoulder_angle, right_wrist_elbow_shoulder_angle)-90,0))//90
        completion_percentage = process + " - " + str(completed) + " %"
        
        # rule no. 1
        if left_heel_hip_shoulder_angle < 170 or right_heel_hip_shoulder_angle < 170:
            instruction = "beep beep beep"
        else:
            instruction = ""
            completion_percentage = "extension - 100 %"
            isCompleted = True
            process = "contraction"

    if process == "contraction":
        completed = (100*(180-np.maximum(left_wrist_elbow_shoulder_angle, right_wrist_elbow_shoulder_angle)))//180
        if completed>50:
            completed = 100
        completion_percentage = process + " - " + str(completed) + " %"
        
        # rule no. 1
        if left_heel_hip_shoulder_angle < 170 or right_heel_hip_shoulder_angle < 170:
            instruction = "beep beep beep"
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




