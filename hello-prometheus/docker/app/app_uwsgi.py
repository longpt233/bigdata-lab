from flask import Flask
from werkzeug.middleware.dispatcher import DispatcherMiddleware
from prometheus_client import make_wsgi_app

# Create my app
app = Flask(__name__)

# Add prometheus wsgi middleware to route /metrics requests
app.wsgi_app = DispatcherMiddleware(app.wsgi_app, {
    '/metrics': make_wsgi_app()
})

@app.route('/ping')
def ping():
    return 'pong'


# pip install uwsgi
# uwsgi --http 127.0.0.1:8000 --wsgi-file app_uwsgi.py --callable app

# WSGI (Web Server Gateway Interface): đứng giữa app và server(nginx, apache)
# tận dụng mutilprocessing (đa lõi)
# có gunicorn, uWSGI

# ASGI(Asynchronous Server Gateway Interface)
# async
# có uvicorn hypercorn với daphne (mỗi thằng django dùng)

# fastapi  thì uvicorn + gunicorn
# gunicorn app.main:app --bind 0.0.0.0:8000 -k uvicorn.workers.UvicornWorker -w 8
# chạy gunicorn để tận dụng đa lõi
# bật uvicorn trong mỗi worker để hỗ trợ async (async def ở cái controller)
