# Features

- Terminal UI using Jexer (please use `main()` in `TerminalInterfaceMain`)
- 2 thread pools, for requestors and executors
- Requestor will await (poll) for result
- Executor will callback Requestor on Executor's thread and set the result
- Then Requestor will read and consume result on it's own original thread

![Preview](https://user-images.githubusercontent.com/3373490/113868648-32f6b780-97b0-11eb-8a23-6c369a28b062.gif)


