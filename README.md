[![Build Status](https://travis-ci.org/OneAfricaMedia/notifications-microservice.svg?branch=master)](https://travis-ci.org/OneAfricaMedia/notifications-microservice)
[![Release](https://img.shields.io/github/release/OneAfricaMedia/notifications-microservice.svg)](https://github.com/OneAfricaMedia/notifications-microservice/releases)

# Notifications microservice

This microservice can be used in combination with our [Android Notifications library](https://github.com/OneAfricaMedia/android-notifications).

It is a simple interface to Firebase and can be a starting point for everyone who wants to implement push notifications with full flexibility of changing the actual implementation.

## Preparation

Please reigster your app with [Firebase](https://firebase.google.com/) to get an API key and your google-services.json, which your app needs to receive push notifications.

## Installation (Dev)

The easiest way to start using this microservice is to download one of the [releases](https://github.com/OneAfricaMedia/notifications-microservice/releases), unzip and start it, like so:

```sh
tar xzf microservice-notifications-*.tgz
cd microservice-notifications*
export PLAY_CRYPTO_SECRET="PLEASE_SET_YOUR_OWN_SECRET"
export FCM_APIURL="https://fcm.googleapis.com/fcm/send"
export FCM_APIKEY="YOUR_FIREBASE_API_KEY_HERE"
export HTTPAUTH_USERNAME="USERNAME"
export HTTPAUTH_PASSWORD="PASSWORD"
export WEBINTERFACE_ENABLED="false"
bin/microservice-notifications -Dplay.evolutions.db.default.autoApply=true
```

The service will then be available and running on port 9000 of your server.

Data will be stored in memory, so please use this only for testing. Once you want to set up a production instance consider using a persistent storage.

## Installation (Production)

The microservice will listen on port 9000 when started as stated above. Please refer to the [Play Framework documentation](https://www.playframework.com/documentation/2.5.x/HTTPServer) on how to make it available on ports 80 or 443.

To change the backend, an easy way is to create your own configuration file and use that. Please refer to the [Play Framework documentation](https://www.playframework.com/documentation/2.5.x/ProductionConfiguration).

## curl examples

Here are some examples of how to query the microservice with curl, to get you started.

Have a look at our [Android Notifications library](https://github.com/OneAfricaMedia/android-notifications) to see how to implement this in your Android application.

**List all devices**

curl -i -X GET -s http://localhost:9000/devices

**Add a device**

curl -i -H 'Content-Type: application/json' -X POST --data '{"id":1,"user_id":0,"operating_system":"os","operating_system_version":"os_version","manufacturer":"manufacturer","model":"model","instance_id":"instance_id"}' -s http://localhost:9000/devices

**Remove a device**

curl -i -X DELETE -s http://localhost:9000/devices/1

**Update a device**

curl -i -H 'Content-Type: application/json' -X PUT --data '{"id":2,"user_id":0,"operating_system":"os","operating_system_version":"os_version","manufacturer":"manufacturer","model":"model","instance_id":"instance_id"}' -s http://localhost:9000/devices/2

**List all devices for a specific user**

curl -i -X GET -s http://localhost:9000/users/0/devices

**Send a push notification to a specific device id**

curl -i -H 'Content-Type: application/json' -X POST --data '{"message":"YOUR MESSAGE","to":"DEVICE_TOKEN"}' -s http://localhost:9000/fcmmessages

**Send a push notification to a specific user**

curl -i -H 'Content-Type: application/json' -X POST --data '{"message":"YOUR MESSAGE","to":"DEVICE_TOKEN"}' -s http://localhost:9000/users/0/sendmessage

## Object formats

**Device**

```java
{
    "id": 1,
    "instance_id": "f1og4VeKHk4:APA91bFjYxNK3j-VJzZYSKslr5fY4LInqQu5DNWcsjq1SozH_RZUeZhtJWJrs9N4KHkwFB5v1GfRu_yhX7KiBDHy0AH7suscd9xlJTlwfTB-h6XDrjFzP0olGtdcpmSKPCFRY02XVd_C",
    "manufacturer": "LGE",
    "model": "Nexus 5X",
    "operating_system": "Android",
    "operating_system_version": "6.0.1",
    "user_id": 0
}
```

