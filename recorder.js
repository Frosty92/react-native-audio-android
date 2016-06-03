
var AudioRecorderManager = require('react-native').NativeModules.RNAudioRecorder;

function AudioRecorder() {

    this._isAudioRecording = false;

    AudioRecorder.prototype.startAudioRecording = function() {
        console.log("AudioRecorderManager", AudioRecorderManager);
        if (!this._isAudioRecording) {
              this._isAudioRecording = true;
              return AudioRecorderManager.startAudioRecording();
        } else {
              return Promise.resolve("Audio is already recording");
        }   
    }
    AudioRecorder.prototype.stopAudioRecording = function() {
        if (this._isAudioRecording) {
            this._isAudioRecording = false;
            return AudioRecorderManager.stopAudioRecording();
        } else {
           return Promise.resolve("Not recording Audio");
        }    
    } 
}

module.exports = AudioRecorder;

