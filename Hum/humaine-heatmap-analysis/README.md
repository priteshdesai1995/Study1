# Humaine Heatmap Analysis

## Features
This Service is design to Generate HeatMap Image for Each HumaineAI Account.


## Tech Stack
- NodeJs 
- PostgresSQL

## Setup Steps

**1.Clone the repository :**

```sh
git clone https://bitbucket.org/humaine-tech-development/humaine-heatmap-analysis.git
```

**2. Install Dependencies**
```bash
npm install
```
**3. Updating database connections and other environment variables in .env file**
```dosini
HOST=localhost
DB_USER=postgres
DB_PASSWORD=root
DB=postgres
SCHEMA=humainedev

DIALECT=postgres
POOL_MAX=10
POOL_MIN=1
POOL_ACQUIRE=30000
POOL_IDLE=10000
HEAT_MAP_FILE_LOCATION=./plugin/heatmap.js
CUSTOM_JS_LOCATION=./plugin/custom.js
TOOL_TIP_JS_LOCATION=./plugin/tooltip.js
CUSTOM_CSS_LOCATION=./plugin/styles/style.css
BODY_LIMIT=50mb
PORT=3001

BUCKET_NAME=hum-dev-heatmap-job
REGION=AWS_REGION
CRON_EXPRESSION=*/30 * * * *

AWS_S3_ACCESS_KEY=AWS_S3_ACCESS_KEY
AWS_S3_SECRET_KEY=AWS_S3_SECRET_KEY
AWS_S3_BUCKET_REGION=AWS_S3_REGION
AWS_S3_BUCKET_NAME=AWS_S3_BUCKET_NAME
AWS_S3_SIGNED_URL_EXPIRATION_TIME=30
```

**4. Start the Application**
```bash
npm run start
```
