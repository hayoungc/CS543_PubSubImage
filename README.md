# 2016 Fall CS543 Team Programming Assignment: Hashtag Image Sharing Service
(HISS)

A service for sharing images with hashtags, whose client is implemented as an
Android app and server is based on MQTT broker.

Team A: Byungsoo Ko, Hayoung Choi, Jaewon Kim, Mingyu Jin, Sanggyu Nam

## Android App Client

The Android app for this service connects to a specified MQTT broker, and
provides features such as publishing images with hashtags, subscribing to
hashtags, and showing the image post feed. The usage and implementation are
described in the report. Here we describe how the source code is structured and
how you should build the client.

Basically, the source code is structured as an Android Studio project. This
project targets **Android SDK version 22** and uses **Android Build Tools
24.0.1**. Also, since this project contains a Gradle wrapper, you don't have to
take care of Gradle's version. Package dependencies are described in
`app/build.gradle` and resolved during the build process automatically. Make
sure you are connected to the Internet on your first build so that the build
script can download the dependencies.

You can build this client by

1) opening this project in **Android Studio** or **IntelliJ IDEA** and build
the target `app`, or

2) running the Gradle wrapper. To build the client using the Gradle wrapper,
    run this command under `app/`:

```
../gradlew build
```

Then, apk files (for debug and release, unsigned) will be created under
`app/build/outputs/apk/`.

If you don't have physical Android device to test this app, we recommend you to
use [GenyMotion](https://www.genymotion.com/) to build a virtual Android device
and run this app.

## MQTT broker

We use [HBMQTT](https://github.com/beerfactory/hbmqtt/) to run a MQTT broker.
To run HBMQTT broker, install `hbmqtt` with `pip` and run `hbmqtt` on your
shell. It is highly recommended to create a virtual environment for HBMQTT
installation, using tools like `pyenv` and `virtualenv`.
[Read the document](http://hbmqtt.readthedocs.io/en/latest/quickstart.html) of
HBMQTT for installing HBMQTT and running its broker.
