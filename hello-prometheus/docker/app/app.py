from flask import Flask, request
from prometheus_flask_exporter import PrometheusMetrics

app = Flask(__name__)
metrics = PrometheusMetrics(app)
metrics.info('app_info', 'Application info', version='1.0.3')

by_path_counter = metrics.counter(
    'by_path_counter', 'Request count by request paths',
    labels={'path': lambda: request.path}
)


@app.route('/ping')
@by_path_counter
def simple_get():
    return "pong"


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8081, debug=False)   # //127.0.0.1:8081/metrics