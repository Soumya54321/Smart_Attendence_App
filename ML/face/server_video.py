# import os
# import glob
# import shutil
from flask import request, render_template, jsonify, Flask
from flask_pymongo import PyMongo
from werkzeug.utils import secure_filename

from ML.face.file_configure import allowed_file, UPLOAD_FOLDER, MAX_FILE_SIZE
from ML.face.database_configure import URI
from ML.face.detect_face import detect_faces_in_video



app = Flask(__name__)

app.secret_key = "secret key"
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
app.config['MAX_CONTENT_LENGTH'] = MAX_FILE_SIZE
app.config["MONGO_URI"] = URI
mongo = PyMongo(app)


@app.route('/')
def upload_form():
    return render_template('./upload.html')


@app.route('/', methods=['POST'])
def upload_file():
    if request.method == 'POST':
        # check if the post request has the file part
        data = request.form
        dept = data['dept']
        year = data['year']
        section = data['section']
        print(dept, year, section)
        if 'file' not in request.files:
            return jsonify({"success": "false"})
        file = request.files['file']
        if file.filename == '':
            return jsonify({"success": "false"})
        if file and allowed_file(file.filename):
            filename = secure_filename(file.filename)
            file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))

            # src_dir = calender_year + "/" + dept + "/" + year + "/" + sec
            # dst_dir = "./images"
            # for jpegfile in glob.iglob(os.path.join(src_dir, "*.jpeg")):
            #     shutil.copy(jpegfile, dst_dir)

            detect_faces_in_video(file.filename)
            return jsonify({"success": "true"})
        else:
            return jsonify({"success": "false"})


def attendance(roll):
    mongo.db.year_2020.insert({"roll": roll})


if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5001, debug=True)
