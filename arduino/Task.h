#ifndef _TASK_H
#define _TASK_H

// include Arduino basic header.
#include <Arduino.h>

class Task
{
  public:
    Task() {
      
    }
    
    Task(int type) {
      _type = type;
    }
    
    int getTaskType() {
      return _type;
    }

    int getFuncType() {
      return _taskFuncType;
    }

    int setFuncType(int taskFuncType) {
      _taskFuncType = taskFuncType;
    }

    int getValue() {
      return _value;
    }

    int setValue(int value) {
      _value = value;
    }
    
  private:
    int _type;
    int _taskFuncType;
    int _value = 0;
};
#endif
