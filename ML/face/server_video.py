import os
from flask import Flask, flash, request, redirect, render_template,jsonify
from werkzeug.utils import secure_filename

UPLOAD_FOLDER = './uploads'

app = Flask(__name__)
app.secret_key = "secret key"
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
app.config['MAX_CONTENT_LENGTH'] = 100 * 1024 * 1024



ALLOWED_EXTENSIONS = set(['mp4','3gp'])


def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


@app.route('/')
def upload_form():
    return render_template('./upload.html')



@app.route('/upload', methods=['POST'])
def upload_file():
    if request.method == 'POST':
        # check if the post request has the file part
        if 'file' not in request.files:
            return jsonify({"success": "false"})
        file = request.files['file']
        if file.filename == '':
            return jsonify({"success": "false"})
        if file and allowed_file(file.filename):
            filename = secure_filename(file.filename)
            file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))
            return jsonify({"success": "true"})
        else:
            return jsonify({"success": "false"})


if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5001, debug=True)
