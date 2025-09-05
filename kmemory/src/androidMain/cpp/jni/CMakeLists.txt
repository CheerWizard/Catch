cmake_minimum_required(VERSION 3.22.1)
project(native_memory)

if (NOT CMAKE_SYSTEM_NAME STREQUAL "Android")
    if(NOT DEFINED ENV{JAVA_HOME})
        message(FATAL_ERROR "Please set JAVA_HOME to your JDK")
    endif()

    set(JAVA_INCLUDE_DIR "$ENV{JAVA_HOME}/include")
    if(CMAKE_SYSTEM_NAME STREQUAL "Linux")
        set(JAVA_INCLUDE_DIR_PLATFORM "$JAVA_INCLUDE_DIR/linux")
    elseif(CMAKE_SYSTEM_NAME STREQUAL "Darwin")
        set(JAVA_INCLUDE_DIR_PLATFORM "$JAVA_INCLUDE_DIR/darwin")
    elseif(CMAKE_SYSTEM_NAME STREQUAL "Windows")
        set(JAVA_INCLUDE_DIR_PLATFORM "$JAVA_INCLUDE_DIR/win32")
    else()
        message(FATAL_ERROR "Unsupported platform: ${CMAKE_SYSTEM_NAME}")
    endif()

    include_directories(${JAVA_INCLUDE_DIR} ${JAVA_INCLUDE_DIR_PLATFORM})
endif()

if (CMAKE_SYSTEM_NAME STREQUAL "Windows")
    set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/../../resources/jni/windows-x86_64)
elseif (CMAKE_SYSTEM_NAME STREQUAL "Linux")
    set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/../../resources/jni/linux-x86_64)
elseif (CMAKE_SYSTEM_NAME STREQUAL "Darwin")
    set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/../../resources/jni/macos-x86_64)
endif()

add_library(native_memory SHARED native_memory.cpp)

target_link_libraries(native_memory)