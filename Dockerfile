FROM ubuntu:latest
LABEL authors="valen"

EXPOSE 8080
ENTRYPOINT ["top", "-b"]