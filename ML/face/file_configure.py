UPLOAD_FOLDER = './uploads'
ALLOWED_EXTENSIONS = set(['mp4', '3gp'])
MAX_FILE_SIZE = 100 * 1024 * 1024


def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS
