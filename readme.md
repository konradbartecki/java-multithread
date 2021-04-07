# Features

- Terminal UI using Jexer (please use `main()` in `TerminalInterfaceMain`)
- 2 thread pools, for requestors and executors
- Requestor will await (poll) for result
- Executor will callback Requestor on Executor's thread and set the result
- Then Requestor will read and consume result on it's own original thread

![example.mp4](example.mp4)