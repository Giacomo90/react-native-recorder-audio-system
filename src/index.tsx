import { NativeModules, NativeEventEmitter } from 'react-native';

export interface Options {
  /**
   * Sample rate in hz. Default = 44100
   */
  sampleRate?: number;
  /**
   * Channels, 1 = MONO, 2 = STEREO. Default = 1
   * - `1 | 2`
   */
  channels?: number;
  /**
   * Bits per sample. Default = 16
   * - `8 | 16`
   */
  bitsPerSample?: number;
  /**
   * Interval in miliseconds to receive audio base64 data to "on" event. Default = 0
   */
  audioEmitInterval?: number;
  /**
   * File name. Default = "audio.wav"
   */
  fileName?: string;
  /**
   * Record audio from microphone instead of device. Default = false
   */
  fromMic?: boolean;
  /**
   * Save recorded audio. Default = false
   */
  saveFile?: boolean;
}

type ScreenAudioRecorderType = {
  init: (options: Options) => void;
  start: () => void;
  stop: () => Promise<string>;
  info: ()=> string
  on: (event: 'data', callback: (data: string) => void) => void;
};

const AudioRecorder = NativeModules.ScreenAudioRecorder;
const EventEmitter = new NativeEventEmitter(AudioRecorder);
const eventsMap = {
  data: 'data',
};

const ScreenAudioRecorder: ScreenAudioRecorderType = {
  init: (options: Options) => AudioRecorder.init(options),
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
  },
   
};

export default ScreenAudioRecorder;
