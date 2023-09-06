![Mitobi](img/mitobi.jpg)
## About
Mitobi is a tool for backing up internal Android app data without the need for root access or a PC. It works by modifying the APK and then injecting Mitobi into the APK.
## Installing
> We are currently working on our injector called [Pita](https://github.com/HaruByte/Pita). \
> Mitobi is just a preview of [Pita](https://github.com/HaruByte/Pita).

TODO
## Building
1. Ensure that Java 17 and Android SDK are installed on your device.
2. To build the test app:
```
./gradlew assembleDebug
```
3. To build the DEX file:
```
./gradlew :lib:generateDexDebug
```