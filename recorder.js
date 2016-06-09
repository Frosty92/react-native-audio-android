var AudioRecorderManager = require('react-native').NativeModules.RNAudioRecorder;

function AudioRecorder() {
  this._isAudioRecording = false;
};

AudioRecorder.prototype.startAudioRecording = function(successCallback, errorCallback) {
    if (!this._isAudioRecording) {
        this._isAudioRecording = true;
        AudioRecorderManager.startAudioRecording((data) => successCallback(data),(error) => errorCallback(error));  
    } else {
        errorCallback('recording in progress');       
    }   
}
AudioRecorder.prototype.stopAudioRecording = function(callback) {
    if (this._isAudioRecording) {
        this._isAudioRecording = false;
       AudioRecorderManager.stopAudioRecording((result) => callback(result));
    }
} 
  
module.exports = AudioRecorder;

