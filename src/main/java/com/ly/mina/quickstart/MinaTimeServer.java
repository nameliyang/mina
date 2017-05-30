package com.ly.mina.quickstart;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class MinaTimeServer extends Application {

    private static final int PORT = 9123;
    
    private static final int BUFFER_READ_SIZE = 4;
    
    NioSocketAcceptor acceptor;

    public static void main(String[] args) throws IOException {
    	
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (acceptor != null) {
                    acceptor.dispose();
                }
                Platform.exit();
            }
        });


        FlowPane flowPane = new FlowPane();
        flowPane.setVgap(10);
        flowPane.setHgap(10);
        flowPane.setPadding(new Insets(10));
        Button startBtn = new Button("startServer");
        Button wakeupSelector = new Button("wakeUpBtn");
        Button disposeBtn = new Button("disposeBtn");

        addStartEvent(startBtn);
        addDisposeEvent(disposeBtn);
        addWakupEvent(wakeupSelector);

        flowPane.getChildren().addAll(startBtn, disposeBtn, wakeupSelector);

        Scene scene = new Scene(flowPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addWakupEvent(Button wakeupSelector) {
        wakeupSelector.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("wake up btn ");
                acceptor.selector.wakeup();
            }
        });
    }

    private void addDisposeEvent(Button disposeBtn) {
        disposeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("dispose btn ");
                acceptor.dispose();
            }
        });
    }

    public void addStartEvent(Button startBtn) {
        startBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("start server ....");
                acceptor = new NioSocketAcceptor();
                acceptor.getFilterChain()
                        .addLast("logger", new LoggingFilter());
                acceptor.getFilterChain().addLast(
                        "codec",
                        new ProtocolCodecFilter(new TextLineCodecFactory(
                                Charset.forName("UTF-8"))));
                
                acceptor.setHandler(new TimeServerHandler());

                acceptor.getSessionConfig().setReadBufferSize(BUFFER_READ_SIZE);
                acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,
                        10);
                try {
                    acceptor.bind(new InetSocketAddress(PORT));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("end start server ....");
            }
        });
    }


}

