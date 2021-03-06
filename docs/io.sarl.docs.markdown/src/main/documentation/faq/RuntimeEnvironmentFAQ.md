# FAQ related to the Runtime Environment 

[:Outline:]


## General Questions about Janus

### What is the SRE?

SRE stands for "SARL Runtime Environment". The SRE is an implementation of an agent platform, which is able to
run a SARL program. The official standard SRE supported by the SARL developers is the
[Janus platform](http://www.janusproject.io).


### What is Janus?

Janus is an open-source multi-agent platform fully implemented 
in Java [:compiler.level!]. Janus enables developers to quickly create 
web, enterprise and desktop agent-based applications.
[:Fact:]("[:compiler.level!]".shouldBeAtLeastJava)

__Janus is an agent execution platform not an agent-oriented language.__

It provides a comprehensive set of features to develop, run, display and monitor agent-based applications.

Janus-based applications can be distributed across a network. 
Janus could be used as an agent-oriented platform, an 
organizational platform, and/or a holonic platform. It 
also natively manages the concept of recursive agents and 
holons.

Official website: [www.janusproject.io](http://www.janusproject.io)


### Where can I ask my question?

If you cannot find an answer to your question in the FAQ, nor the reference documents, nor
the [existing SARL issues](https://github.com/sarl/sarl/issues),
you may ask the SARL developers on 
[the SARL forum](https://groups.google.com/forum/#!forum/sarl).


### Where can I found information on the release planning of Janus?

Janus is now part of the SARL project.
The release planning of SARL is detailed on the [milestones page](https://github.com/sarl/sarl/milestones)
on Github.

## Installation and Execution

### Is my operating system compatible with Janus?

The [Janus runtime platform](http://www.janusproject.io)
is a Java application. Every operating system which has 
a Java Virtual Machine with at least with the [:compiler.level!]
standard may be used to run Janus. 
[:Fact:]("[:compiler.level!]".shouldBeAtLeastJava)


### What is the version of the Java virtual machine to install?

Janus requires the JRE and the JDK [:compiler.level!] or higher to run and compile.
Note that if you plan to create Android applications, you may 
configure your JDK to produce 1.6 class files from [:compiler.level!] Java code,
depending of the current supported standard on Android platforms.
[:Fact:]("[:compiler.level!]".shouldBeAtLeastJava)


### How to launch an agent in Janus?

Three methods are available for launching one or more agents in the Janus platform:

* [From the command line](../gettingstarted/RunSARLAgentCLI.md).
* [Inside the Eclipse IDE](../gettingstarted/RunSARLAgentEclipse.md).
* [From a Java program](../gettingstarted/RunSARLAgentJava.md).


### Error: "The SRE is not standalone. It does not contain the Java dependencies."

This error occurs when there is no SARL Runtime Environment (SRE) installed on your
Eclipse environment, OR when the installed SRE is not compatible with the installed
version of the SARL tools, which are embedded in Eclipse.

For solving this problem, you must download the latest
[Janus platform](http://www.janusproject.io), and install it in your Eclipse
(Menu <code>Window&gt; Preferences&gt; SARL&gt; Installed SREs</code>).

<caution>If the latest stable version of Janus is not working, you should
download the latest development version.</caution>


### Error: "Incompatible SRE with SARL 1.1.1.1. Version must be lower than 0.0.0."

This error occurs when the SARL Runtime Environment (SRE) has a version lower than
the version of the SARL tools, which are embedded in the Eclipse IDE.
This difference of version may cause problems during the execution of your agents, since
the capacities' definitions may not be the same.

For solving this problem, you must download the version of the SARL Runtime Environment (SRE)
that is implementing the version of the SARL specification that you're using on Eclipse IDE.
For the Janus platform, the versions of the latest stable and development releases are displayed on
[this page](http://maven.janusproject.io/VERSION.txt).
For determining if the Janus platform implements the correct version of the SARL specification,
please read the explanation  on [how Janus version numbers are built](http://www.janusproject.io/#versionnumber).


### Error: "Agent class not found."

When the Janus platform cannot find the class file for the start-up agent, it
displays the error message `"Agent class not found"`.

For resolving this problem, you should check if:

* the class with the given name exists in the application's class path.
* the class name is given as the first command-line argument to your application.
* the class with the given name is a subtype of `Agent`.
[:Fact:](io.sarl.lang.core.Agent)


For showing the arguments given to Janus, you could launch Janus with the command line option:
`--cli`. This option stops Janus after displaying the command line arguments,
including the `--cli` option.


### Error: "Invalid byte 2 of 4-byte UTF-8 sequence."

When installing Janus as an SRE in the Eclipse interface, the plugin loads the Jar file of the
SRE with the default API.
The Jar archiver uses the default file encoding of the operating system.
On Linux and MacOS 10, it is almost UTF-8. On Windows, it is Latin1. And on MacOS (before 10),
it is Mac-Roman.

Unfortunately, the Janus Jar file is generated on a Linux operating system (UTF-8).
When the Java virtual machine tries to uncompress and use the content of the Jar, it
complains about an invalid charset format.

For solving this issue, you could launch your Eclipse with the command line option
`-Dfile.encoding=UTF-8`, which is forcing the Eclipse product to consider the
file as encoded with the UTF-8 charset.


## Runtime Behavior of Janus


### Are events received in the same order than when they are sent?

__No__.

There is no warranty on the receiving order of the events.
This is a particular implementation choice of the runtime
environment. For example, the
[Janus runtime environment](http://www.janusproject.io) executes
the event handlers in parallel. The real order of execution depends on
how the Java executor is running the handlers on the threads.



### How events are treated by the run-time environment?

When the event `e` is received by an agent the following algorithm is applied:
```
if "on Initialize" is currently running then
   add e to a buffer of events.
else if "on Destroy" is currently running then
   ignore the event.
else
   [:firefct]{fire(e)}
fi
```
The function [:firefct:] retrieves all the `on E` and runs them in parallel, and
there is a synchronization point after the running of all the `on E` if `E` is
`Initialize` or `Destroy` (for forcing synchronous execution of `on Initialize`
and `on Destroy`). At the end of the `on Initialize` (after synchronization point),
all the buffered events are fired.

Observe that if the event is fired from within the `on Initialize`, the same algorithm
is applied whatever the receiving agent.



### How the spawn function is run by the run-time environment?

Regarding `spawn()`, the function runs in two parts:

1. First, the spawn agent is created. This part is run in the same thread as the
   caller of spawn, so the spawn call blocks.
2. Once the spawn agent has been created, the initialization process runs
   within a separated thread from the spawner agent. So, the call `spawn()` is
   not locked. Then, the created thread runs all the initialization process,
   including the synchronous execution of `on Initialize` (see previous question).
   Consequently, the `on Initialize` of the spawn agent will not block the spawn caller.



### Must I configure the Janus kernels to be connected to other Janus kernels?

__No__.

Janus was designed to discover other kernels automatically.
By default, the different instances of the Janus platform
are connected together without any particular configuration.
The sole constraint is that the kernels must be on the
same local network.


## Contribute to Janus

### Where are the sources for Janus?

The sources for Janus are available inside the SARL project repository on
[Github](https://github.com/sarl/sarl/tree/master/sre/io.janusproject/).


### How can I find the current issues?

Janus Developers use [Github](https://github.com/sarl/sarl)
to manage bug tracking and project workflow. 
The issues are listed on [Github](https://github.com/sarl/sarl/issues). 


### How can I report a problem or a bug in Janus components?


You should submit your issue on 
[this page](https://github.com/sarl/sarl/issues/new).


[:Include:](../legal.inc)

