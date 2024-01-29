from flask import Flask
from prometheus_client import Counter, start_http_server

app = Flask(__name__)
c = Counter('ping_requests_total', 'Description of counter')


@app.route('/ping')
def ping():
    c.inc()     # Increment by 1
    return 'pong'


if __name__ == '__main__':
    start_http_server(8000)
    app.run(host='0.0.0.0', port=8081, debug=False)  # để debug = true sẽ lỗi. the code reloads after the flask server is up


