FROM zenato/puppeteer

USER root

# Clearing .env file since env vars will be
# handled via entry.sh
# Adding jq since its needed for env vars handling
RUN > .env && \
    apt-get update && \
    apt-get install -y jq

# Copying all source code and built modules
COPY . .

# Setting entry.sh as executable
RUN chmod +x ./cicd/entry.sh

# Setting entry.sh as entrypoint of the container
ENTRYPOINT ["./cicd/entry.sh"]