import os
import face_recognition

images = os.listdir("images")

unknown_image = face_recognition.load_image_file("unknown.jpeg")
unknown_image_encodings = face_recognition.face_encodings(unknown_image)[0]

known_faces = []

for image_file in images:
    image = face_recognition.load_image_file("images/"+image_file)
    image_encodings = face_recognition.face_encodings(image)[0]
    known_faces.append(image_encodings)

results = face_recognition.compare_faces(known_faces, unknown_image_encodings)

print(images)
print(results)

