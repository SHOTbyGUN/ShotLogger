# ShotLogger
My ThreadSafe implementation of a Logger

# Disclaimer

USE WITH YOUR OWN RESPONSIBILITY!
No guarantees if this works or not.

# Getting started

```java

// Start the Logger
ShotLogger.getShotLogger().startBasic("/your/path/to/log/directory/log");

// Send a log message
Log.info("MyApplicationInit", this.getClass().getSimpleName(), "hello world!", null);

// Stop the Logger
ShotLogger.getShotLogger().close();

// Note that logger is not-daemon
// eg. it will block application from exiting if not closed

```

# Under the hood

I am bad at creating flowcharts, but this is the principle.

<img src="https://raw.githubusercontent.com/SHOTbyGUN/ShotLogger/master/other/crudeFlowChart.jpg">

<img src="https://raw.githubusercontent.com/SHOTbyGUN/ShotLogger/master/other/crudeLegend.jpg">
