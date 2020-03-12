import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'dart:io';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);
  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  MethodChannel methodChannel = MethodChannel("com.example.notification");

  bool isServiceRunning;

  @override
  void initState() {
    isServiceRunning = false;
    super.initState();
  }

  void startService() async {
    if (Platform.isAndroid) {
      String data = await methodChannel.invokeMethod("startMusic");
      print(data);
    }
    setState(() {
      isServiceRunning = true;
    });
  }

  void stopService() async {
    if (Platform.isAndroid) {
      String data = await methodChannel.invokeMethod("stopMusic");
      print(data);
    }
    setState(() {
      isServiceRunning = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Music Player Example"),
      ),
      body: Container(
        child: Center(
          child: MaterialButton(
            child: Text(isServiceRunning?"Stop":"Play"),
            color: Colors.white,
            onPressed: isServiceRunning?stopService:startService,
          )
        )
      ),
    );
  }
}
