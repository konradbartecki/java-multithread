# Features

- Terminal UI using Jexer (please use `main()` in `TerminalInterfaceMain`)
- 2 thread pools, for requestors and executors
- Requestor will await (poll) for result
- Executor will callback Requestor on Executor's thread and set the result
- Then Requestor will read and consume result on it's own original thread

![Showcase](https://user-images.githubusercontent.com/3373490/113867852-4f462480-97af-11eb-8d76-81d7e56decf8.mp4)

