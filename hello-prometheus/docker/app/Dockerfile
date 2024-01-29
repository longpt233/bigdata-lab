FROM python:3.11-alpine

ADD ./requirements.txt /tmp/requirements.txt
RUN pip install -r /tmp/requirements.txt

# ADD . /tmp/latest
# RUN pip install -e /tmp/latest --upgrade

ADD ./app.py /var/flask/
WORKDIR /var/flask


CMD python3 app.py