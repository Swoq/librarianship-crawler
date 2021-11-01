#!/bin/sh
docker build -t swoqe/librarianship-crawler -f ./crawler/Dockerfile .
docker build -t swoqe/librarianship-processor -f ./processor/Dockerfile .
docker build -t swoqe/librarianship-consumer -f ./consumer/Dockerfile .
