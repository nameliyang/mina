package com.ly.mina.quickstart;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class UIController {

    @FXML
    Button startBtn;
    @FXML
    Button disposeBtn;

    @FXML
    Button wakeupBtn;

    @FXML
    Button broadcastBtn;

    @FXML
    TextArea broadcastTextArea;


    @FXML
    ProgressBar progressBar;

    private static final int PORT = 9123;

    NioSocketAcceptor acceptor;

    @FXML
    public void startServer(ActionEvent e) {
        System.out.println("start server ....");
        acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain()
                .addLast("logger", new LoggingFilter());
        acceptor.getFilterChain().addLast(
                "codec",
                new ProtocolCodecFilter(new TextLineCodecFactory(
                        Charset.forName("UTF-8"))));
        acceptor.setHandler(new TimeServerHandler());

        acceptor.getSessionConfig().setReadBufferSize(2048);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,
                10);
        try {
            acceptor.bind(new InetSocketAddress(PORT));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("end start server ....");
    }

    @FXML
    public void disposeServer(ActionEvent event) {
        System.out.println("dispose btn ");
        acceptor.dispose();
    }

    @FXML
    public void wakeupBtn(ActionEvent event) {
        System.out.println("wake up btn ");
        acceptor.selector.wakeup();
    }

    @FXML
    public void sendBroadcast(ActionEvent event) throws InterruptedException {
        if (broadcastTextArea == null) {
            throw new IllegalStateException("broadcastBtn ²»ÄÜÎª¿Õ");
        }
        String msg = broadcastTextArea.getText();
        Set<WriteFuture> writeFutures = acceptor.broadcast(msg);
        final Iterator<WriteFuture> futureIterator = writeFutures.iterator();
        new Thread() {
            @Override
            public void run() {
                int writeCount = 0;
                int totalMsgs = writeFutures.size();
                for (; ; ) {
                    while (futureIterator.hasNext()) {

                        WriteFuture future = futureIterator.next();
                        if (future.isDone()) {
                            writeCount++;
                            final int finalWriteCount = writeCount;
                            Platform.runLater(() -> {
                                progressBar.setProgress(finalWriteCount / totalMsgs);
                            });
                            futureIterator.remove();
                        }
                    }
                }
            }
        }.start();

    }


}
