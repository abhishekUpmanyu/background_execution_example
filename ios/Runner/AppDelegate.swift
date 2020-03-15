import UIKit
import Flutter
import AVFoundation

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {

    var audioPlayer = AVAudioPlayer()

    override func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
        
        do {
            audioPlayer = try AVAudioPlayer(contentsOf:
                URL.init(fileURLWithPath: Bundle.main.path(forResource: "sample", ofType: "mp3")!))
            audioPlayer.prepareToPlay()
        }
        catch {
            print(error)
        }
        
        audioPlayer.numberOfLoops = -1
        
        let controller = window?.rootViewController as! FlutterViewController

        let channel = FlutterMethodChannel(name:"music", binaryMessenger:controller.binaryMessenger)
        channel.setMethodCallHandler({
            (call: FlutterMethodCall, result: FlutterResult) -> Void
            in
            guard call.method == "startMusic" || call.method == "stopMusic" else {
                result(FlutterMethodNotImplemented)
                return
            }
            if (call.method == "startMusic") {
                self.startMusic(result: result)
            }
            else if (call.method == "stopMusic") {
                self.stopMusic(result: result)
            }
        })

        GeneratedPluginRegistrant.register(with: self)
        return super.application(application, didFinishLaunchingWithOptions: launchOptions)
    }
    
    private func startMusic(result: FlutterResult) {
        audioPlayer.play()
        result(true)
    }
    
    private func stopMusic(result: FlutterResult) {
        if (audioPlayer.isPlaying) {
            audioPlayer.stop()
            audioPlayer.currentTime = 0
        }
        result(false)
    }
}
