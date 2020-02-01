import os
import cv2
import face_recognition



def detect_faces_in_video(file_name):
    known_images = os.listdir("images")
    print(known_images)

    video = cv2.VideoCapture("uploads/" + file_name)

    known_faces = []

    for images in known_images:
        img = face_recognition.load_image_file("images/" + images)
        img_encodings = face_recognition.face_encodings(img)[0]
        known_faces.append(img_encodings)

    frame_number = 0

    known_face_count = len(known_images)
    flag = [0] * known_face_count

    while True:
        ret, frame = video.read()
        frame_number += 1

        if not ret:
            break

        # Convert the image from BGR color (which OpenCV uses) to RGB color (which face_recognition uses)
        rgb_frame = frame[:, :, ::-1]

        face_locations = face_recognition.face_locations(rgb_frame)
        face_encodings = face_recognition.face_encodings(rgb_frame, face_locations)

        for face_encoding in face_encodings:
            # See if the face is a match for the known face(s)
            match = face_recognition.compare_faces(known_faces, face_encoding, tolerance=0.40)

            print(match)

            counter = 0

            for face in match:
                if face == True and flag[counter] == 0:
                    roll = known_images[counter]
                    roll = roll.split('.')[0]
                    # database update
                    attendance(roll)
                    flag[counter] = 1
                counter += 1
from ML.face.server_video import attendance
