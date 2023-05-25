FROM amazon/aws-lambda-python:3.8
WORKDIR /var/task
COPY requirements.txt  .
RUN pip3 install --no-cache-dir -r requirements.txt --target "${LAMBDA_TASK_ROOT}"
COPY app.py ${LAMBDA_TASK_ROOT}
ENV TZ="Asia/Seoul"
CMD ["app.handler"]
