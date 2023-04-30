# Spring Boot AWS S3 Bucket + File Upload Example

This is a simple Spring Boot application that demonstrates how to upload, download and delete files from AWS S3.

## Requirements

1. Java 17
2. Maven 3
3. AWS Account

## Steps to Setup

**1. Clone the application**

```bash
git clone https://github.com/sachinlakshitha/spring-boot-aws-s3-file-upload.git
```

**2. Configure AWS S3 credentials**

Open `src/main/resources/application.properties` file and configure your AWS S3 credentials.

```properties
# AWS S3 Bucket Name
aws.s3.bucket=

# AWS S3 Region
aws.s3.region=

# AWS S3 Access Key
aws.s3.access.key=

# AWS S3 Secret Key
aws.s3.secret.key=

# AWS S3 Base URL
aws.s3.base.url=
```

**3. Build and run the app using maven**

```bash
mvn package
java -jar target/spring-boot-s3-file-upload-1.0.0-SNAPSHOT.jar
```

Alternatively, you can run the app without packaging it using -

```bash
mvn spring-boot:run
```

The app will start running at <http://localhost:8080>.

## Example of Requests

### Upload File

    POST /api/file/upload

    curl -F "file=@/path/to/file" http://localhost:8080/api/file/upload

### Download File

    GET /api/file/download/{fileName}

    curl -X GET http://localhost:8080/api/file/download/{fileName}

### Delete File

    DELETE /api/file/{fileName}

    curl -X DELETE http://localhost:8080/api/file/{fileName}

### Delete Folder

    DELETE /api/folder/{folderName}

    curl -X DELETE http://localhost:8080/api/folder/{folderName}
