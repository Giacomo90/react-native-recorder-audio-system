# react-native-recorder-audio-system

React-native library, made to record system sounds
 of all android devices from version 10 and later.


## Installation

```sh
npm install react-native-recorder-audio-system
```
compatible with react native 0.7 or higher.
Warning: The methods of version 0.3.X are not compatible with this version.
## Usage android > 10

Tested with physical  with a android 10,13 and 15, but it is compatible with all versions.


```ts
import ScreenAudioRecorder, { Options } from 'react-native-recorder-audio-system';


// permissions required for operation and to avoid any crashes
  async function requestPermission () {
    const recordPermission = await PermissionsAndroid.check(
      PermissionsAndroid.PERMISSIONS.RECORD_AUDIO
    );

    if (!recordPermission) {
      let granted;
      try {
        granted = await PermissionsAndroid.request(
          PermissionsAndroid.PERMISSIONS.RECORD_AUDIO
        );

        if (granted === PermissionsAndroid.RESULTS.GRANTED) {
          console.log('You can use the library record audio');
        } else {
          console.log(' permission denied');
        }
      } catch (err) {
        console.log(err);
      }
    }
  };



useEffect(()=>{
const options: Options = {
 sampleRate:32000,
 channelAudio:2,
  bitAudio:16,
   useMic:false
};

ScreenAudioRecorder.init(options);

ScreenAudioRecorder.on('data', data => {
  console.log(data) // or other code
});
requestPermission () 
},[])


function Record(){
  ScreenAudioRecorder.start()
  .then(rx=>{console.log(rx)}) // answers true if the required permissions have been accepted or false if the user has refused
  .catch(err=>{comsole.log(err)}) // answer error
}


function stopRecord(){
AudioRecord.stop();
.then(rx=>{console.log(rx)}) // It only responds with it resf the recording has been stopped, otherwise it responds with error.
.catch(err=>{condole.log(err))
}

return(...)
```


## Options 

| Name | Description | Default |
|------|-----------------------------------|-----------|
|sampleRate| 16000,22000,32000,44000,48000 | 22000|
|channelAudio| Channels, 1 = MONO, 2 = STEREO. | 1 |
|bitAudio| 8 or 16 bit | 8 |
|useMic| false or true | false |








I was forgetting, at least these requirements on the build

```
    ext {
        buildToolsVersion = "35.0.0"
        minSdkVersion = 29
        compileSdkVersion = 35
        targetSdkVersion = 34
        ndkVersion = "26.1.10909125"
        kotlinVersion = "1.9.24"
    }

```


## License

MIT

## Credits/References

code copied from 
https://github.com/Nilsantos/react-native-screen-audio-recorder

## Info

Base library used for the STEREOPHONE application, many features were added later offline.

You can use my library "react-native-streaming-playback" to listen to the recorded PCM from here