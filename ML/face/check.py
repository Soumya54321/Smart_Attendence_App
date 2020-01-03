import os
import face_recognition
from PIL import Image

image_files = os.listdir('images')
print(image_files)

unknown_image = face_recognition.load_image_file("unknown6.jpeg")
unknown_image_encodings = face_recognition.face_encodings(unknown_image)

# find the face then get the encodings
face_locations = face_recognition.face_locations(unknown_image)
number_of_faces = 0
for face_location in face_locations:
    top, right, bottom, left = face_location
    # print("A face is located at pixel location Top: {}, Left: {}, Bottom: {}, Right: {}".format(top, left, bottom, right))
    face_image = unknown_image[top:bottom, left:right]
    pil_image = Image.fromarray(face_image)
    pil_image.save("faces_detected/"+str(number_of_faces)+".png")
    number_of_faces += 1

known_faces = []

for image_file in image_files:
    image = face_recognition.load_image_file("images/"+image_file)
    image_encodings = face_recognition.face_encodings(image)[0]
    known_faces.append(image_encodings)

for i in range(number_of_faces):
    unknown_image2 = face_recognition.load_image_file("faces_detected/"+str(i)+".png")
    try:
        unknown_image2_encodings = face_recognition.face_encodings(unknown_image2, num_jitters=10)[0]
    except IndexError:
        print(i, "I wasn't able to locate any faces in at least one of the images. Check the image files. Aborting...")
        continue
    results = face_recognition.compare_faces(known_faces, unknown_image2_encodings, tolerance=0.475)
    print(i, results)

# 0.0362 0.0520
