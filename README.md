# LIN

## Introduction ##
LIN is a compiler that will parse [LIN Node capability files](http://www.cs-group.de/fileadmin/media/Documents/LIN_Specification_Package_2.2A.pdf#page=164) and [LIN description files](http://www.cs-group.de/fileadmin/media/Documents/LIN_Specification_Package_2.2A.pdf#page=175) then generates the corresponding C driver to implement the LIN slave or master node.

## Download ##
- [Latest release (v0.1.1)](https://github.com/PersonalTransport/LIN/releases/download/v0.1.1-alpha/LIN-v0.1.1-alpha.jar).
- Previous releases can be found on the github releases [releases](https://github.com/PersonalTransport/LIN/releases) page.

## Supported Targets ##
- PIC24FJ64GB004 family.
- Support for others should not be too difficult to add.

## Dependencies ##
- [antlr4](http://www.antlr.org/) is used to generated the parsers for [LIN Node capability files](http://www.cs-group.de/fileadmin/media/Documents/LIN_Specification_Package_2.2A.pdf#page=164) and [LIN description files](http://www.cs-group.de/fileadmin/media/Documents/LIN_Specification_Package_2.2A.pdf#page=175) files.
- [stringtemplate](http://www.stringtemplate.org/) (usually packaged with antlr4) is used as a temlplate engine to generated the C drivers.
- [jcommander](http://jcommander.org/) used to make command line tools more user friendly.


## Usage ##
```
The following options are required: -i, --target-interface -t, --target 

Usage: LIN [options] [command] [command options]
  Options:
    -h, --help
       Show help this message.
       Default: false
  * -t, --target
       The target device.
  * -i, --target-interface
       The target device's interface.
  Commands:
    slave      Generate the slave C slave driver.
      Usage: slave [options] sources...
        Options:
        * -o, --output
             Output directory.
          -s, --slave
             name of slave node to export

    master      Generate the slave C master driver.
      Usage: master [options] sources...
        Options:
        * -o, --output
             Output directory.
```

## Examples ##
- Generate a master node from a LIN description file.
  - ```java -jar -t PIC24FJ64GB002 -i UART1 LIN.jar master -o gen CEM.ldf```
  - This will generate a master driver from the file in the current directory called CEM.ldf and output the driver source code in the subfolder gen.

- Generate a slave node from a LIN Node capability file.
 - ```java -jar -t PIC24FJ64GB002 -i UART1 LIN.jar slave -o gen LSM.ncf```
 - This will generate a slave driver from the file in the current directory called LSM.ncf and output the driver source code in the subfolder gen.

- Generate a slave node from a LIN description file (this is not a prefered method).
  - ```java -jar -t PIC24FJ64GB002 -i UART1 LIN.jar slave -o gen -s LSM CEM.ldf```
 - This will generate a slave driver for LSM from the file in the current directory called CEM.ldf and output the driver source code in the subfolder gen. 
