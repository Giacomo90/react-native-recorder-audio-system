"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _reactNative = require("react-native");

const AudioRecorder = _reactNative.NativeModules.ScreenAudioRecorder;
const EventEmitter = new _reactNative.NativeEventEmitter(AudioRecorder);
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

var _default = ScreenAudioRecorder;
exports.default = _default;
//# sourceMappingURL=index.js.map