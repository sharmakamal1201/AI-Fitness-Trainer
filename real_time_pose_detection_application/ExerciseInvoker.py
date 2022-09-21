from BicepCurl import *
from PushUp import *
import pyttsx3
import threading
from queue import Queue

kill_active_threads = False
ins_on_screen = "."
textqueue = Queue()
with textqueue.mutex:
    textqueue.queue.clear()

def play_audio():
    engine = pyttsx3.init()
    engine.setProperty('rate', 130)
    engine.setProperty('voice', 'HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Speech\Voices\Tokens\TTS_MS_EN-US_ZIRA_11.0')  # changes the voice
    global kill_active_threads
    while True:
        if kill_active_threads == True:
            break
        text = textqueue.get()
        print(text)
        if(text == ins_on_screen):
            #engine.say(text)
            engine.runAndWait()
        if textqueue.empty():
            break

def Speaker(text):
    textqueue.put(text)
    t = threading.Thread(target=play_audio,daemon=True)
    t.start()

def Exercise(exerciseName, landmarks, image, new_process, setCount):
    if(exerciseName == 'biceps curl'):
       return bicepsUtils(landmarks, image, new_process, setCount)
    if(exerciseName == 'pushup'):
        return pushUpsUtils(landmarks, image, new_process, setCount)


def Run(exerciseName):
    global kill_active_threads
    kill_active_threads = True
    Speaker("")
    kill_active_threads = False
    textqueue = Queue()
    with textqueue.mutex:
        textqueue.queue.clear()
    instruction1 = "You have selected " + exerciseName + ". "
    instruction2 = "We will train together and make sure to do it in a right way. "
    instruction3 = "Follow the instructions. Press y to start."
    new_process = "contraction"
    setCount = 0
    initialize = True
    prev_instruction = ""
    global ins_on_screen
    ins_on_screen = instruction1 + instruction2 + instruction3
    Speaker(ins_on_screen)
    # capture live video
    frameNo = 0
    prevFrameNo = 0
    cap = cv2.VideoCapture(0) # 0 for laptop camera
    with mp_pose.Pose(min_detection_confidence=0.5, min_tracking_confidence=0.5) as pose:
        while cap.isOpened():
            frameNo += 1
            frameNo = frameNo % 100000
            if prevFrameNo > frameNo:
                prevFrameNo = frameNo
            ret, frame = cap.read() # frame is image

            # recoloring to RGB
            image = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB) # BGR to RGB
            image.flags.writeable = False # saving memory

            # make detection
            results = pose.process(image)

            # recoloring back to BGR
            image.flags.writeable = True
            image = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)
            
            # image shape
            h, w, c = image.shape

            # extract landmarks
            try:
                if initialize:
                    show_text_on_image(image, instruction1, [0.01, 0.10], color=(255, 255, 255), fontSize=1)
                    show_text_on_image(image, instruction2, [0.01, 0.15], color=(255, 255, 255), fontSize=1)
                    show_text_on_image(image, instruction3, [0.01, 0.20], color=(255, 255, 255), fontSize=1)
                else:
                    landmarks = results.pose_landmarks.landmark
                    new_process, isPreviousCompleted, new_instruction, setCount = Exercise(exerciseName, landmarks, image, new_process, setCount)
                    ins_on_screen = new_instruction
                    show_text_on_image(image, ins_on_screen, [0.01, 0.75], color=(255, 255, 255), fontSize=1)
                    if (prev_instruction != new_instruction and new_instruction != "" and (frameNo-prevFrameNo)>100):
                        prevFrameNo = frameNo
                        prev_instruction = new_instruction
                        Speaker(new_instruction)

            except:
                pass

            # rendering detection
            mp_drawing.draw_landmarks(image, results.pose_landmarks, mp_pose.POSE_CONNECTIONS,
                                     mp_drawing.DrawingSpec(color=(245,117,66), thickness=2, circle_radius=2),
                                     mp_drawing.DrawingSpec(color=(245,66,230), thickness=2, circle_radius=2))
            
            show_text_on_image(image, "set completed - " + str(setCount), [0.75, 0.95], color=(0, 255, 0), fontSize=2)

            # Resize
            img = get_resized_for_display_img(image)
            
            # show frame
            cv2.imshow('mediapipe frame', img)
            
            if(cv2.waitKey(10) & 0xFF == ord('y')): # press y to start
                initialize = False;

            if(ret == False or (cv2.waitKey(10) & 0xFF == ord('q'))): # press q to quit
                break;
           
        cap.release()
        cv2.destroyAllWindows()
        cv2.waitKey(1)