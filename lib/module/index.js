import { NativeModules, NativeEventEmitter } from 'react-native';
const AudioRecorder = NativeModules.ScreenAudioRecorder;
const EventEmitter = new NativeEventEmitter(AudioRecorder);
const eventsMap = {
  data: 'data'
};
const ScreenAudioRecorder = {
  init: options => AudioRecorder.init(options),
  start: () => AudioRecorder.start(),
  stop: () => AudioRecorder.stop(),
  info: ()=> AudioRecorder.info(),
  on: (event, callback) => {
    const nativeEvent = eventsMap[event];

    if (!nativeEvent) {
      throw new Error('Invalid event');
    }

    EventEmitter.removeAllListeners(nativeEvent);
    return EventEmitter.addListener(nativeEvent, callback);
  }
};
export default ScreenAudioRecorder;
//# sourceMappingURL=index.js.map