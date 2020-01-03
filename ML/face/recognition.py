from PIL import Image
import os
import matplotlib.pyplot as plt
import matplotlib.image as mpimg
import face_recognition

image_files = os.listdir('images')
print(image_files)

unknown_image = face_recognition.load_image_file("unknown3.jpeg")
face_locations = face_recognition.face_locations(unknown_image)
print(len(face_locations))
unknown_image_encodings = face_recognition.face_encodings(unknown_image)
print(len(unknown_image_encodings))
i = 0
for face_location in face_locations:

    # Print the location of each face in this image
    top, right, bottom, left = face_location
    print("A face is located at pixel location Top: {}, Left: {}, Bottom: {}, Right: {}".format(top, left, bottom, right))

    # You can access the actual face itself like this:
    face_image = unknown_image[top:bottom, left:right]
    pil_image = Image.fromarray(face_image)
    pil_image.save(str(i)+".png")
    i += 1

for i in range(0,len(unknown_image_encodings)):
    for image_file in image_files:
        image = face_recognition.load_image_file("images/"+image_file)
        image_encodings = face_recognition.face_encodings(image)[0]
        result = face_recognition.compare_faces([image_encodings], unknown_image_encodings[i])
        if result==[True]:
            #print(result)
            print(image_file, result)
