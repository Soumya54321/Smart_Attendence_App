import face_recognition
import os
from PIL import Image
from flask import Flask, jsonify, request, redirect

# You can change this to any folder on your system
ALLOWED_EXTENSIONS = {'png', 'jpg', 'jpeg', 'gif','mp4','avi'}

app = Flask(__name__)


def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


@app.route('/', methods=['GET', 'POST'])
def upload_image():
    # Check if a valid image file was uploaded
    if request.method == 'POST':
        if 'file' not in request.files:
            return redirect(request.url)

        file = request.files['file']

        if file.filename == '':
            return redirect(request.url)

        if file and allowed_file(file.filename):
            # The image file seems valid! Detect faces and return the result.
            return detect_faces_in_image(file)

    # If no valid image file was uploaded, show the file upload form:
    return '''
    <!doctype html>
    <title>Is this a picture of Obama?</title>
    <h1>Upload a picture and see if it's a picture of Obama!</h1>
    <form method="POST" enctype="multipart/form-data">
      <input type="file" name="file">
      <input type="submit" value="Upload">
    </form>
    '''


def detect_faces_in_image(file_stream):
    image_files = os.listdir('ML/face/images')
    print(image_files)

    unknown_image = face_recognition.load_image_file(file_stream)
    unknown_image_encodings = face_recognition.face_encodings(unknown_image)

    # find the face then get the encodings
    face_locations = face_recognition.face_locations(unknown_image)
    number_of_faces = 0
    for face_location in face_locations:
        top, right, bottom, left = face_location
        # print("A face is located at pixel location Top: {}, Left: {}, Bottom: {}, Right: {}".format(top, left, bottom, right))
        face_image = unknown_image[top:bottom, left:right]
        pil_image = Image.fromarray(face_image)
        pil_image.save("ML/face/faces_detected/" + str(number_of_faces) + ".png")
        number_of_faces += 1

    known_faces = []

    for image_file in image_files:
        image = face_recognition.load_image_file("ML/face/images/" + image_file)
        image_encodings = face_recognition.face_encodings(image)[0]
        known_faces.append(image_encodings)

    return_result={}

    for i in range(number_of_faces):
        unknown_image2 = face_recognition.load_image_file("ML/face/faces_detected/" + str(i) + ".png")
        try:
            unknown_image2_encodings = face_recognition.face_encodings(unknown_image2, num_jitters=10)[0]
        except IndexError:
            print(i,
                  "I wasn't able to locate any faces in at least one of the images. Check the image files. Aborting...")
            continue
        results = face_recognition.compare_faces(known_faces, unknown_image2_encodings, tolerance=0.475)
        print(i, results)


        return_result[i]= ' '.join([str(elem) for elem in results])
    print(return_result)
    return jsonify(return_result)

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5001, debug=True)