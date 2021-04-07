package Presenting;

import Jobs.ExecutorJob;
import Jobs.FutureTaskOfSumming;
import Jobs.RequestorJob;
import Model.SummingRequestBuilder;
import Model.SummingResponse;
import jexer.*;
import jexer.event.TMenuEvent;
import jexer.menu.TMenu;
import jexer.menu.TMenuItem;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static jexer.TCommand.cmHelp;
import static jexer.TKeypress.kbF1;

public class TerminalInterfaceMain extends TApplication {

    private static PriorityBlockingQueue<FutureTaskOfSumming> requestQueue;
    private static PriorityBlockingQueue<SummingResponse> responseQueue;
    private static SummingRequestBuilder builder;

    private static ThreadPoolExecutor requestsPool;
    private static ThreadPoolExecutor executorPool;

    private static TList requestsList;
    private static TList responsesList;
    private static TList threadsList;

    private static ByteArrayOutputStream stream = new ByteArrayOutputStream();
    private static PrintStream printStream = new PrintStream(stream);

    private static TText text;
    private static TWindow textWindow;

    public TerminalInterfaceMain() throws Exception {
        super(BackendType.SWING);
        this.getBackend().setTitle("KBartecki-s21970-UTP5");

        // Create standard menus for Tool, File, and Window.
        TMenu threadMenu = addMenu("Threads");
        threadMenu.addItem(1000, "Restart pool");
        threadMenu.addItem(1001, "Stop");
        threadMenu.addItem(1004, "Start new requestor");
        threadMenu.addItem(1005, "Start new executor");
        TMenu logMenu = addMenu("Data");
        logMenu.addItem(1002, "Clear log");
        logMenu.addItem(1003, "Clear responses");

        var requestsWindows = new ListWindow(this,"Requests", 0, 0, this.getDesktop().getWidth() / 3, this.getDesktop().getHeight()-1);
        var responsesWindow = new ListWindow(this,"Responses", 0, 0, this.getDesktop().getWidth() / 3, this.getDesktop().getHeight()-1);
        var threadsWindow = new ListWindow(this,"Threads", 0, 0, this.getDesktop().getWidth() / 3, this.getDesktop().getHeight()-1);

        textWindow = this.addWindow("Log", 0,this.getDesktop().getHeight()/5*3,this.getDesktop().getWidth(),this.getDesktop().getHeight()/5*3);
        text = textWindow.addText("Log:",0,0, textWindow.getWidth(), textWindow.getHeight());

        requestsList = requestsWindows.listField;
        responsesList = responsesWindow.listField;
        threadsList = threadsWindow.listField;
    }

    @Override
    protected boolean onMenu(TMenuEvent menu) {
        if(menu.getId() == 1000)
        {
            StopThreadPools();
            CreateThreadPools();
        }
        if(menu.getId() == 1001)
        {
            StopThreadPools();
        }
        if(menu.getId() == 1002)
        {
            stream = new ByteArrayOutputStream();
            printStream = new PrintStream(stream);
            System.setOut(printStream);
        }
        if(menu.getId() == 1003)
        {
            responseQueue.clear();
        }
        if(menu.getId() == 1004)
        {
            requestsPool.submit(new RequestorJob(builder, requestQueue));
        }
        if(menu.getId() == 1005)
        {
            executorPool.submit(new ExecutorJob(requestQueue, responseQueue));
        }
        return super.onMenu(menu);
    }

    private static void StopThreadPools() {
        requestsPool.shutdownNow();
        executorPool.shutdownNow();
        CreateThreadPools();
    }

    private static void CreateThreadPools()
    {
        requestsPool = new ThreadPoolExecutor(
                32, 32,
                15L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>());

        executorPool = new ThreadPoolExecutor(
                32, 32,
                15L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>());
    }

    public static void main(String [] args) throws Exception {
        builder = new SummingRequestBuilder();
        System.setOut(printStream);

        requestQueue = new PriorityBlockingQueue<FutureTaskOfSumming>();
        responseQueue = new PriorityBlockingQueue<SummingResponse>();
        CreateThreadPools();

        requestsPool.submit(new RequestorJob(builder, requestQueue));
        requestsPool.submit(new RequestorJob(builder, requestQueue));
        requestsPool.submit(new RequestorJob(builder, requestQueue));
        requestsPool.submit(new RequestorJob(builder, requestQueue));

        executorPool.submit(new ExecutorJob(requestQueue, responseQueue));


        new Thread( () -> {
            try {
                new TerminalInterfaceMain().run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        new Thread( () -> {
            try {
                while(true)
                {
                    UpdateInterface();
                    Thread.sleep(1000/60);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void UpdateInterface() {
        if(TerminalInterfaceMain.requestsList != null)
        {
            List<String> requests = new ArrayList<>(TerminalInterfaceMain.requestQueue)
                    .stream()
                    .map(req -> req.getDisplayName())
                    .collect(Collectors.toList());
            TerminalInterfaceMain.requestsList.setList(requests);
        }
        if(TerminalInterfaceMain.responsesList != null)
        {
            List<String> responses = new ArrayList<>(TerminalInterfaceMain.responseQueue)
                    .stream()
                    .map(req -> req.getDisplayName())
                    .collect(Collectors.toList());
            TerminalInterfaceMain.responsesList.setList(responses);
        }
        if(TerminalInterfaceMain.threadsList != null)
        {
            var requestorThreads = Thread.getAllStackTraces().keySet()
                    .stream()
                    .filter(s -> s.getName().startsWith("pool"))
                    .map(s -> s.getName() + " " + s.getState().toString())
                    .collect(Collectors.toList());
            TerminalInterfaceMain.threadsList.setList(requestorThreads);
        }
        if(TerminalInterfaceMain.text != null)
        {
            TerminalInterfaceMain.text.setHeight(TerminalInterfaceMain.textWindow.getHeight());
            TerminalInterfaceMain.text.setWidth(TerminalInterfaceMain.textWindow.getWidth());
            TerminalInterfaceMain.text.setText(stream.toString());
            TerminalInterfaceMain.text.getVerticalScroller().setValue(TerminalInterfaceMain.text.getVerticalScroller().getBottomValue());
        }
    }
}
