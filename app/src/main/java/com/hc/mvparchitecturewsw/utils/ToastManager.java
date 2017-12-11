/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */

package com.hc.mvparchitecturewsw.utils;

import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup;

import java.util.LinkedList;

/**
 * 
 * @author Evgeny Shishkin
 * 
 */
class ToastManager extends Handler {

    private static final int MESSAGE_DISPLAY = 0xc2007;
    private static final int MESSAGE_ADD_VIEW = 0xc20074dd;
    private static final int MESSAGE_REMOVE = 0xc2007de1;

    private LinkedList<FsToast> msgQueue;

    private ToastManager() {
        msgQueue = new LinkedList<>();
    }


    private static class Holder {
        private static ToastManager toastManager = new ToastManager();
    }

    public static ToastManager getInstance() {
        return Holder.toastManager;
    }

    /**
     * Inserts a {@link FsToast} to be displayed.
     *
     * @param fsToast
     */
    void add(FsToast fsToast) {
        msgQueue.add(fsToast);
        displayMsg();
    }

    void addIgnoreDuplicate(FsToast fsToast){
        if (msgQueue.isEmpty() || !msgQueue.contains(fsToast)) {
            add(fsToast);
        }
    }

    /**
     * Removes all {@link FsToast} from the queue.
     */
    void clearMsg(FsToast fsToast) {
        msgQueue.remove(fsToast);
    }

    /**
     * Removes all {@link FsToast} from the queue.
     */
    void clearAllMsg() {
        if (msgQueue != null && !msgQueue.isEmpty()){
            final FsToast fsToast = msgQueue.peek();
            fsToast.handleHide();
        }
        if (msgQueue != null) {
            msgQueue.clear();
        }
        removeMessages(MESSAGE_DISPLAY);
        removeMessages(MESSAGE_ADD_VIEW);
        removeMessages(MESSAGE_REMOVE);
    }

    /**
     * Displays the next {@link FsToast} within the queue.
     */
    private void displayMsg() {
        if (msgQueue.isEmpty()) {
            return;
        }
        // First peek whether the FsToast is being displayed.
        final FsToast fsToast = msgQueue.peek();
        // If the activity is null we throw away the FsToast.
        if (fsToast.getContext() == null) {
            msgQueue.poll();
        }
        final Message msg;
        if (!fsToast.isShowing()) {
            // Display the FsToast
            msg = obtainMessage(MESSAGE_ADD_VIEW);
            msg.obj = fsToast;
            sendMessage(msg);
        } else {
            msg = obtainMessage(MESSAGE_DISPLAY);
//            sendMessageDelayed(msg, fsToast.getDuration()
//                    + fsToast.getInAnim().getDuration() + fsToast.getOutAnim().getDuration());
            sendMessageDelayed(msg, fsToast.getDuration());
        }
    }

    /**
     * Removes the {@link FsToast}'s view after it's display duration.
     *
     * @param fsToast The {@link FsToast} added to a {@link ViewGroup} and should be removed.s
     */
    private void removeMsg(final FsToast fsToast) {
        if (fsToast.getView().getParent() != null) {
            msgQueue.poll();
            fsToast.handleHide();
            Message msg = obtainMessage(MESSAGE_DISPLAY);
            sendMessage(msg);
        }
    }

    private void addMsgToView(FsToast fsToast) {
        fsToast.handleShow();
        final Message msg = obtainMessage(MESSAGE_REMOVE);
        msg.obj = fsToast;
//        sendMessageDelayed(msg, fsToast.getDuration()
//                + fsToast.getInAnim().getDuration() + fsToast.getOutAnim().getDuration());
        sendMessageDelayed(msg, fsToast.getDuration());
    }

    @Override
    public void handleMessage(Message msg) {
        final FsToast fsToast;
        switch (msg.what) {
            case MESSAGE_DISPLAY:
                displayMsg();
                break;
            case MESSAGE_ADD_VIEW:
                fsToast = (FsToast) msg.obj;
                addMsgToView(fsToast);
                break;
            case MESSAGE_REMOVE:
                fsToast = (FsToast) msg.obj;
                removeMsg(fsToast);
                break;
            default:
                super.handleMessage(msg);
                break;
        }
    }
}