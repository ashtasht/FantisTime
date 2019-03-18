# FantisTime
Screen-time limiting software over multiple computers.

FantisTime is a tool used to limit screen time on computers, but it designed to work with multiple computers over the internet (it also supports diffrent operating systems).
_It uses an extrenal server, so you are not limited to a LAN._

## Client
### Configuration
FantisTime's client uses a json file for configurations. This file containes configurations only for the current PC. The default location for this file is `~/.config/fantistime.json` for Linux and Mac, or `...\Users\...\.fantistime.json` for Windows.
Here is a simple fantistime.json file:
```
{
    "serverIP":"127.0.0.1",
    "serverPort":3141,
    "checkInterval":3,
    "idleSensitivity":0.8,
    "checksPerReport":8
}
```
#### `serverIP`
The IP address of the server (`127.0.0.1` for localhost). 
#### `serverSocket`
The port the server is listening to. It should much the port in the server configurations file.
#### `checkInterval`
FantisTime checks idle by looking on the mouse location every few seconds and compare it. `checkInterval` is the amount of seconds between each check.
#### `idleSensitivity`
How much sensitive should FantisTime be to unintended mouse movements. The value have to be between 1 (the most sensitive) to 0 (idle all the time).
#### `checkPerReport`
How huch checks of the mouse location to do before deciding if idling or not.

## TODO
1. Make it more secure and harder to trick: add `password` to the local configurations file and move everything releated to measuring to the server configurations, send the password each time to the server (in a secure way) and get the configuration from it.

