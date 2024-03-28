FROM centos
FROM openjdk:11
RUN  mkdir /usr/src/transactional-middleware-jobs
COPY target /usr/src/transactional-middleware-jobs/
WORKDIR /usr/src/transactional-middleware-jobs
CMD ["java","-jar","data-transactional-middleware.jar"]