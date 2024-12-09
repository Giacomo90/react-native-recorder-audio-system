# react-native-recorder-audio-system

React-native library, made to record system sounds
 of all android devices from version 10 and later.
## Installation

```sh
npm install react-native-recorder-audio-system
```

## Usage android < 14

```ts
import ScreenAudioRecorder, { Options } from 'react-native-recorder-audio-system ';



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
          console.log('You can use the mic');
        } else {
          console.log('Mic permission denied');
        }
      } catch (err) {
        console.log(err);
      }
    }
  };



useEffect(()=>{
const options: Options = {
  sampleRate: 16000,
  channels: 1,
  bitsPerSample: 16,
  fileName: 'novo.wav',
  fromMic: false,
  saveFile: false,
  audioEmitInterval: 1000
};

ScreenAudioRecorder.init(options);

ScreenAudioRecorder.on('data', data => {
  // your code, data = base64
});
requestPermission () 
},[])


function Record(){
  ScreenAudioRecorder.start();
  // asks permission and if confirmed starts recording
}


function stopRecord(){
 await AudioRecord.stop();
 // stop record, it is promise
}

return(...)
```


## Options 

| Name | Description | Default |
|------|-----------------------------------|-----------|
|sampleRate| Sample Rate in hz. | 44100 |
|channels| Channels, 1 = MONO, 2 = STEREO. | 1 |
|bitsPerSample| Bits per sample. | 16 |
|audioEmitInterval| Interval in miliseconds to receive audio base64 data to "on" event. | 0 |
|fileName| Output file name. (Don't forget ".wav") | audio.wav |
|fromMic| Record audio from microphone instead of device. For android before 10 even if the option is true, the audio will be captured from the microphone, because android doesn't support. | false |
|saveFile | The captured audio must be recorded. | false |




## Usage android >= 14

the 2 codes if used together must be separated, and not used together to avoid crashes with android 14 or ++
example
if(android >= 14){code2}else {code1}

```ts
import ScreenAudioRecorder, { Options } from 'react-native-recorder-audio-system ';



useEffect(()=>{


ScreenAudioRecorder.on('data', data => {
  // your code, data = base64
});

},[])


function Record(){
  ScreenAudioRecorder.start();
  // asks permission and if confirmed starts recording
}


function stopRecord(){
 await AudioRecord.stop();
 // stop record, it is promise
}

return(...)

```


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