package com.ly.mina.quickstart;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TimeServerHandler extends IoHandlerAdapter {

    Logger logger = LoggerFactory.getLogger(TimeServerHandler.class);
    
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        logger.debug("session created ...");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        cause.printStackTrace();
    }

    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        String str = message.toString();
        if (str.trim().equalsIgnoreCase("quit")) {
            session.close();
            return;
        }
        session.write("reponse msg = --------------------------" + str.toUpperCase());
        logger.debug("Message received=|" + str + "|");

    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
        logger.debug("IDLE " + session.getIdleCount(status));
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        //do nothing
    }
}
