# TrueLayer code challenge

See task [here](https://docs.google.com/document/d/1L0xflPoRq2fbPH0vK9--FGUVyny5LjTCwh-Cki41Xps/edit#)

## Requirements : 

* Git
* Java11
* Gradle
* Docker
* Docker-Compose

## Getting Started

* Clone locally 
```bash
git clone git@github.com:lucaCambi77/trueLayer.git
```

* Build the project 
```bash
./gradlew clean build
```

## Run as Spring boot application

```bash
./gradlew bootRun
```

## Run with Docker

```bash
docker build -t truelayer/tranlsation .
```
```bash
docker-compose up -d
```

## Endpoints : 

```
GET http://localhost:5000/pokemon/{name}?version=xxx

name -> name of the pokemon to search
version -> optional parameter for a specific pokemon version, latest version otherwise
```


