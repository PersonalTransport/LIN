# LIN

## Introduction ##
LIN is a compiler (written in java) that will parse [LIN Node capability](http://www.cs-group.de/fileadmin/media/Documents/LIN_Specification_Package_2.2A.pdf#page=164) and [LIN description](http://www.cs-group.de/fileadmin/media/Documents/LIN_Specification_Package_2.2A.pdf#page=175) files and generate C source code that implements the LIN 2.2 spec for slave or master nodes.

## Download ##
- [Latest release (v0.2.1-beta)](https://github.com/PersonalTransport/LIN/releases/download/v0.2.1-beta/LIN-v0.2.1-beta.zip).
- Previous releases can be found on the GitHub [releases](https://github.com/PersonalTransport/LIN/releases) page.

## Install ##
##### Windows #####
 - Extract the downloaded zip file into the path "C:\Program Files\LIN-v0.2.1-beta"
  - The final folder structure should look something like this.
    * C:\Program Files\LIN-v0.2.1-beta
        * bin
            * LIN
            * LIN.bat
        * lib
            * LIN-v0.2.1-beta.jar
  - Then finally add "C:\Program Files\LIN-v0.2.1-beta\bin" to the environment PATH variable.
    - [Here](https://youtu.be/dU_ca27EGT8?t=98) is a short video on how to do this in this video he adds C:\Python27 just type C:\Program Files\LIN-v0.2.1-beta\bin instead. You should also make sure that Java is also on your PATH variable as well.

#### Unix ####
 - Extract the downloaded zip file where ever you feel fit.
  - I put it in /opt/LIN-v0.2.1-beta
 - Add that location to your PATH so you can run the LIN-v0.2.1-beta/bin/LIN bash script.

## Supported Targets ##
- PIC24FJXXGB00X family.
- Support for others should not be too difficult to add.

## Dependencies ##
- [Java 1.8](https://www.oracle.com/java/index.html) the compiler is written in Java 1.8.
- [antlr4](http://www.antlr.org/) is used to generated the parsers for [LIN Node capability](http://www.cs-group.de/fileadmin/media/Documents/LIN_Specification_Package_2.2A.pdf#page=164) and [LIN description](http://www.cs-group.de/fileadmin/media/Documents/LIN_Specification_Package_2.2A.pdf#page=175) files.
- [string template](http://www.stringtemplate.org/) (usually packaged with antlr4) is used as a template engine to generate the C drivers.
- [jcommander](http://jcommander.org/) used to make command line tools more user-friendly.


## Usage ##
```
The following options are required: -t, --target -i, --target-interface

Usage: LIN [options] [command] [command options]
  Options:
    -h, --help
       Show help this message.
       Default: false
  * -t, --target
       The target device.
  * -i, --target-interface
       The target device's interface.
    --version
       Display compiler version information.
       Default: false
  Commands:
    slave      Generate the slave C slave driver.
      Usage: slave [options] source files...
        Options:
          -o, --output
             Output directory.
             Default: gen
          -s, --slave
             name of slave node to export
             Default: <empty string>

    master      Generate the slave C master driver.
      Usage: master [options] source files...
        Options:
          -o, --output
             Output directory.
             Default: gen
```

## Examples ##
##### Generate a master node from a LIN Node capability file. #####
  - ```LIN -t PIC24FJ64GB002 -i UART1 master CEM.ncf```
  - This will generate two files gen/CEM.h and gen/CEM.c that targets the PIC24FJ64GB002 and uses the first UART module, you will include these files in your master node project.

</br>
##### Generate a slave node from a LIN Node capability file. #####
 - ```LIN -t PIC24FJ32GB002 -i UART1 slave LSM.ncf```
 - This will generate two files gen/LSM.h and gen/LSM.c that targets the PIC24FJ32GB002 and uses the first UART module, you will include these files in your slave node project.
