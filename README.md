# Locate Me Android

LocateMeAndroid is a location sharing application using MQTT, Android Location API and OpenStreetMap.

```bash
mosquitto_sub -t /locateme/locations -h iot.eclipse.org -d -q 2
```

Result,

```json
{"latitude":21.757385956576073,"longitude":91.36528150774365,"deviceId":"f01931d61d1d5ff0"}
```
