# VENDING MACHINE CHANGE CALULATOR

This is a sample app for a change calculator in a vending machine. The intent is that based on the total of a purchase and the coins used to pay, calculate what coins should be used to pay for the purchase.

## Prerequisite
Required:

- java

Suggested:
- mvn

Optional
- docker

## Installation

This is a multi-module maven project. To build the modules for this project

On systems with mvn installed
```
mvn install
```

On Windows without mvn installed
```
.\mvnw.cmd install

```
On Linux without mvn installed
```
./mvnw install
```

## Useage
### To use the library in another application
Add the dependancy to your project:

**Maven**
```
    <dependency>
      <groupId>oracle.exercise</groupId>
      <artifactId>api</artifactId>
      <version>0.0.1-SNAPSHOT</version>
    </dependency>
```
**Gradle**
```
oracle.exercise:api:0.0.1-SNAPSHOT
```

### Starting the application
To use the test harness you can run the following

Windows example command
```
java -jar .\harness\target\harness-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

Linux example command
```
java -jar ./harness/target/harness-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

### Using Docker
To run this application using docker, build the application with the above command, then run the container using docker.

```
docker run -it vending-machine-harness:0.0.1-SNAPSHOT
```

### Using the test harness
**Setup**

The application will prompt you to enter values for the [float](https://en.wikipedia.org/wiki/Float_(money_supply)]) for a vending machine.

Each of the coins registered in the CoinsEnum will be listed in ascending value. The expected input is a whole number. If the input is invalid then the value will be rejected and the same coin value will be presented until a valid input is received.

*Example setup*
```
Please enter the number of each of the coins for the double
0.01: 100
0.02: 100
0.05: 100
0.10: 100
0.20: 100
0.50: 100
1.00: 100
2.00: 100
```

**Usage**

Enter the total value of the products being purchased. Then enter the value of coins used to pay.

*Example usage*
```
Enter the cost of product:
0.99
Enter the number of each coin you paid with:
0.01: 0
0.02: 0
0.05: 0
0.10: 0
0.20: 0
0.50: 0
1.00: 0
2.00: 1
Change to pay: 1.01
Change returned:
1.00: 1
0.01: 1
Total: 1.01
```

**Listing**

To list the contents of the spent coins hopper use the *hopper* command

```
Enter the cost of product:
hopper
These are the coins in the hopper:
0.01: 0
0.02: 0
0.05: 0
0.10: 1
0.20: 1
0.50: 1
1.00: 0
2.00: 0
```

To list the contents of the coins still in the float use the *float* command

```
Enter the cost of product:
float
These are the coins in the float:
0.01: 99
0.02: 100
0.05: 100
0.10: 100
0.20: 100
0.50: 100
1.00: 100
2.00: 100
```

**Exiting**

Ctrl-C will work or type *exit* when entering a cost

*Example Usage*
```
Enter the cost of product:
exit
Goodbye
```

## Assumptions
- Used Coins -
    Most common vending machines in use retain the spent coins in a hopper. These coins are not available for use in producing change for subsequent transaction.


- Decimal currancy - 
  No currancy symbols are in use in this application to keep it generic. The assumption is that the coinage availabe is updated in the CoinsEnum class for different denominations. For none decimal based currancies, the calculation engine will need to be replaced. 
  
- Pay with largest coins first - As this is a change machine, assume that we want to pay with the largest coins possible to be able to return the pennies when we start to run low.

- No Lombok - Assumed you wanted to see my code
