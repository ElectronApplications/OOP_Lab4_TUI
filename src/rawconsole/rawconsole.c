#include <stdio.h>

#ifdef _WIN32
    #include <conio.h>
#else
    #include <termios.h>
    #include <unistd.h>
#endif

#include "rawconsole_RawConsole.h"

enum RawKeys {
    KEY_UNKNOWN = 0,
    KEY_ESC = 1,
    KEY_LEFT = 2,
    KEY_UP = 3,
    KEY_RIGHT = 4,
    KEY_DOWN = 5,
    KEY_ENTER = 6
};

JNIEXPORT jint JNICALL Java_rawconsole_RawConsole_getRawKeyId(JNIEnv *env, jclass this_class) {
    #ifdef _WIN32
        int input = getch();
        if(input == 224)
            input = getch();
        
        switch(input) {
            case 27: return KEY_ESC;
            case 75: return KEY_LEFT;
            case 72: return KEY_UP;
            case 77: return KEY_RIGHT;
            case 80: return KEY_DOWN;
            case 13: return KEY_ENTER;
            default: return KEY_UNKNOWN;
        }
    #else
        struct termios original_mode;
        struct termios new_mode;

        tcgetattr(0, &original_mode);
        new_mode = original_mode;
        new_mode.c_lflag &= ~(ECHO | ICANON);
        tcsetattr(0, TCSANOW, &new_mode);

        int input;
        read(fileno(stdin), &input, 4);

        tcsetattr(0, TCSANOW, &original_mode);

        switch(input) {
            case 27: return KEY_ESC;
            case 4479771: return KEY_LEFT;
            case 4283163: return KEY_UP;
            case 4414235: return KEY_RIGHT;
            case 4348699: return KEY_DOWN;
            case 10: return KEY_ENTER;
            default: return KEY_UNKNOWN;
        }
    #endif
}