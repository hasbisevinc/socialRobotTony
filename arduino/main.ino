#include <SoftwareSerial.h>
#include <Scheduler.h>
#include <QueueList.h>
#include <AFMotor.h>
#include <Servo.h>
#include "Task.h"

/*
 * Digital pin 11: DC Motor #1 / Stepper #1 (activation/speed control)
 * Digital pin 3: DC Motor #2 / Stepper #1 (activation/speed control)
 * Digital pin 5: DC Motor #3 / Stepper #2 (activation/speed control)
 * Digital pin 6: DC Motor #4 / Stepper #2 (activation/speed control)
 * Digitals pin 9: Servo #1 control
 * Digital pin 10: Servo #2 control
 */

#define MOTOR_TASK 1
#define SERVO_TASK 2

#define LEFT_MOTOR_FORWARD 1
#define LEFT_MOTOR_BACKWARD 2
#define LEFT_MOTOR_STOP 3
#define RIGHT_MOTOR_FORWARD 4
#define RIGHT_MOTOR_BACKWARD 5
#define RIGHT_MOTOR_STOP 6
#define MOTOR_DELAY 7
#define LEFT_SERVO 8
#define RIGHT_SERVO 9
#define SERVO_DELAY 10

#define STRING_LEFT_MOTOR_FORWARD "LF"
#define STRING_LEFT_MOTOR_BACKWARD "LB"
#define STRING_LEFT_MOTOR_STOP "LS"
#define STRING_RIGHT_MOTOR_FORWARD "RF"
#define STRING_RIGHT_MOTOR_BACKWARD "RB"
#define STRING_RIGHT_MOTOR_STOP "RS"
#define STRING_MOTOR_DELAY "MD"
#define STRING_LEFT_SERVO "LA"
#define STRING_RIGHT_SERVO "RA"
#define STRING_SERVO_DELAY "SD"

#define BLUETOOTH_RX 6
#define BLUETOOTH_TX 5
#define LED 13
#define LEFT_SERVO 9
#define RIGHT_SERVO 10

SoftwareSerial bluetooth(BLUETOOTH_RX, BLUETOOTH_TX); // RX, TX

QueueList <Task> motorQueue;
QueueList <Task> servoQueue;
String btBuffer = "";

Servo leftServo;
Servo rightServo;
AF_DCMotor leftMotor(1);
AF_DCMotor rightMotor(2);

void setup() {
  Serial.begin(9600);
  bluetooth.begin(9600);
  leftServo.attach(LEFT_SERVO);
  rightServo.attach(RIGHT_SERVO);
  leftMotor.run(RELEASE);
  rightMotor.run(RELEASE);

  pinMode(LED, OUTPUT);

  Scheduler.startLoop(motorTask);
  Scheduler.startLoop(servoTask);
  Scheduler.startLoop(BluetoothTask);
  
  Serial.println("Goodnight moon!");
}

void motorTask()
{
  if (!motorQueue.isEmpty ()){
    Task t = motorQueue.pop();
    Serial.print("motor:");
    Serial.print("getTaskType:");
    Serial.print(t.getTaskType());
    Serial.print("getFuncType:");
    Serial.print(t.getFuncType());
    Serial.print("getValue:");
    Serial.println(t.getValue());

    if (t.getTaskType() != MOTOR_TASK) {
      Serial.print("it is not motor task");
    } else {
      if (t.getFuncType() == LEFT_MOTOR_FORWARD) {
        leftMotor.run(FORWARD);
        leftMotor.setSpeed(t.getValue());
      }
      else if (t.getFuncType() == LEFT_MOTOR_BACKWARD) {
        leftMotor.run(BACKWARD);
        leftMotor.setSpeed(t.getValue());
      }
      else if (t.getFuncType() == LEFT_MOTOR_STOP) {
        leftMotor.run(RELEASE);
      } else if (t.getFuncType() == RIGHT_MOTOR_FORWARD) {
        rightMotor.run(FORWARD);
        rightMotor.setSpeed(t.getValue());
      }
      else if (t.getFuncType() == RIGHT_MOTOR_BACKWARD) {
        rightMotor.run(BACKWARD);
        rightMotor.setSpeed(t.getValue());
      }
      else if (t.getFuncType() == RIGHT_MOTOR_STOP) {
        rightMotor.run(RELEASE);
      }
      else if (t.getFuncType() == MOTOR_DELAY) {
        delay(t.getValue());
      } else {
        Serial.print("motor task not found");
      }
    }
  }
  delay(1);
}

void servoTask()
{
  if (!servoQueue.isEmpty ()){
    Task t = servoQueue.pop();
    Serial.print("servo:");
    Serial.print("getTaskType:");
    Serial.print(t.getTaskType());
    Serial.print("getFuncType:");
    Serial.print(t.getFuncType());
    Serial.print("getValue:");
    Serial.println(t.getValue());
    if (t.getTaskType() != SERVO_TASK) {
      Serial.print("it is not servo task");
    } else {
      if (t.getValue() < 10) {
        Serial.print("servo value cannot be less than 10");
        return;
      }
      if (t.getFuncType() == LEFT_SERVO) {
        leftServo.write(t.getValue()); 
      }
      else if (t.getFuncType() == RIGHT_SERVO) {
        rightServo.write(t.getValue()); 
      }
      else if (t.getFuncType() == SERVO_DELAY) {
        delay(t.getValue());
      } else {
        Serial.print("servo task not found");
      }
    }
  }
  delay(1);
}

void BluetoothTask()
{
  if (bluetooth.available()) {
    char c = bluetooth.read();
    if (c == 0x0A) {
      Serial.print("tam mesaj:");
      Serial.println(btBuffer);
      createTask(btBuffer);
      btBuffer = "";
    } else {
      btBuffer.concat(c);
    }
  }
  delay(1);
}

void createTask(String str) {
  int taskType;
  int taskFuncType;
  String strType = str.substring(0,2);
  if (strType == STRING_LEFT_MOTOR_FORWARD){
    taskFuncType = LEFT_MOTOR_FORWARD;
    taskType = MOTOR_TASK;
  } else if (strType == STRING_LEFT_MOTOR_BACKWARD){
    taskFuncType = LEFT_MOTOR_BACKWARD;
    taskType = MOTOR_TASK;
  } else if (strType == STRING_LEFT_MOTOR_STOP){
    taskFuncType = LEFT_MOTOR_STOP;
    taskType = MOTOR_TASK;
  } else if (strType == STRING_RIGHT_MOTOR_FORWARD){
    taskFuncType = RIGHT_MOTOR_FORWARD;
    taskType = MOTOR_TASK;
  } else if (strType == STRING_RIGHT_MOTOR_BACKWARD){
    taskFuncType = RIGHT_MOTOR_BACKWARD;
    taskType = MOTOR_TASK;
  } else if (strType == STRING_RIGHT_MOTOR_STOP){
    taskFuncType = RIGHT_MOTOR_STOP;
    taskType = MOTOR_TASK;
  } else if (strType == STRING_MOTOR_DELAY){
    taskFuncType =MOTOR_DELAY;
    taskType = MOTOR_TASK;
  } else if (strType == STRING_LEFT_SERVO){
    taskFuncType = LEFT_SERVO;
    taskType = SERVO_TASK;
  } else if (strType == STRING_RIGHT_SERVO){
    taskFuncType = RIGHT_SERVO;
    taskType = SERVO_TASK;
  } else if (strType == STRING_SERVO_DELAY){
    taskFuncType = SERVO_DELAY;
    taskType = SERVO_TASK;
  }
  
  int value = str.substring(2).toInt();
  
  Task t(taskType);
  t.setFuncType(taskFuncType);
  t.setValue(value);
  if (taskType == MOTOR_TASK) {
    motorQueue.push(t);
  } else if (taskType == SERVO_TASK) {
    servoQueue.push(t);
  } else {
    Serial.println("taskType error");
  }
  
}

void loop() {
  delay(100);
}
