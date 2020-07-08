FROM python:3

ADD load-data.py /
ADD reuters_word_index.json /

RUN pip install redis

ENV REDIS_HOST localhost

CMD [ "python", "./load-data.py" ]
