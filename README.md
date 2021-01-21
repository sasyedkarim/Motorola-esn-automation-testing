## Running tests

### Running with Maven

To execute tests from the command line:

    All tests:
        $ mvn clean test -DdriverType=chrome -DdriverWaitTime=60 -PCT
     
    Specific tags:
        $ mvn clean test -Dtags="@tag" -DdriverType=chrome -DdriverWaitTime=60 -PCT
        $ mvn clean test -Dtags=@SyedMSI -DdriverType=chrome -DdriverWaitTime=60 -Dsurefire.thread.count=1 -Dsurefire.fork.count=1 -Dparallel=none -DlogLevel=INFO
    Combinations:
        $ mvn clean test -Dtags="@tag and @anothertag" -DdriverType=chrome -DdriverWaitTime=60 -PCT

### Running and Debugging tests from IntelliJ

There are 2 ways to run/debug the tests in IntelliJ

**Run/Debug Configurations**

Create a new by selecting "Edit Configurations" then a new Maven config.

Enter the following values (examples):

Command line: clean test -Dtags="@tag" -DdriverType=chrome -DdriverWaitTime=60 -f pom.xml

Profiles (separated with space): CT

**Terminal**

First navigate to the folder you wish to run the tests from using the cd command.

For Cucumber test type in the terminal the following command :

     mvn test -Dtags="@tag" -DdriverType=chrome -DdriverWaitTime=60 -PCT

For tests within sme-int type the following command :

    mvn verify -PCD

**Profiles and Properties**

Each method on running tests have further parameters for using profiles and properties. Examples are:

-Pheadless-chrome - runs chrome in headless mode

-Dsurefire.thread.count=4 - runs 4 tests in parallel

For further values see the [pom.xml](pom.xml) at the top level and in a chosen lower module
  
[1]:http://chromedriver.chromium.org/downloads
[2]:https://github.com/mozilla/geckodriver/releases
[3]:https://www.seleniumhq.org/download/
