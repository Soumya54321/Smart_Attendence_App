import os
import cv2
import face_recognition

known_images = os.listdir("images")
print(known_images)

video = cv2.VideoCapture("video.mp4")
length = int(video.get(cv2.CAP_PROP_FRAME_COUNT))
# print(length)

fourcc = cv2.VideoWriter_fourcc(*'XVID')
output_movie = cv2.VideoWriter('output.avi', fourcc, 24.00, (1280, 720))

known_faces = []
for images in known_images:
    img = face_recognition.load_image_file("images/"+images)
    img_encodings = face_recognition.face_encodings(img)[0]
    known_faces.append(img_encodings)

face_locations = []
face_encodings = []
face_names = []
frame_number = 0

while True:
    # get each frame
    ret, frame = video.read()
    frame_number += 1

    if not ret:
        break

    # Convert the image from BGR color (which OpenCV uses) to RGB color (which face_recognition uses)
    rgb_frame = frame[:, :, ::-1]

    face_locations = face_recognition.face_locations(rgb_frame)
    face_encodings = face_recognition.face_encodings(rgb_frame, face_locations)

    face_names = []


    for face_encoding in face_encodings:
        # See if the face is a match for the known face(s)
        match = face_recognition.compare_faces(known_faces, face_encoding, tolerance=0.50)

        print(match)

        name = None
        if match[0]:
            name = "b"
        elif match[1]:
            name = "k"
        elif match[2]:
            name = "p"

        face_names.append(name)


        # Label the results
        # for (top, right, bottom, left), name in zip(face_locations, face_names):
        #     if not name:
        #         continue
        #
        #     # Draw a box around the face
        #     cv2.rectangle(frame, (left, top), (right, bottom), (0, 0, 255), 2)
        #
        #     # Draw a label with a name below the face
        #     cv2.rectangle(frame, (left, bottom - 25), (right, bottom), (0, 0, 255), cv2.FILLED)
        #     font = cv2.FONT_HERSHEY_DUPLEX
        #     cv2.putText(frame, name, (left + 6, bottom - 6), font, 0.5, (255, 255, 255), 1)
        #
        # # Write the resulting image to the output video file
        # print("Writing frame {} / {}".format(frame_number, length))
        # output_movie.write(frame)


video.release()
cv2.destroyAllWindows()

