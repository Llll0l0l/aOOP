from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/api/students', methods=['POST'])
def handle_student_data():
    data = request.get_json()
    student_id = data['student_id']
    destinations = data['destinations']
    # Do something with the student data (e.g. save to a database)
    # ...
    # Return a response to the client
    return jsonify({'message': 'Student data received.'}), 201

if __name__ == '__main__':
    app.run(debug=True)
