package com.android.pacificist.jam;

import android.os.Build;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;

import java.lang.reflect.Field;

/**
 * Created by pacificist on 2019/3/26.
 */
public class MessageQueueProxy {

    private MessageQueue mMessageQueue;
    private Field mMessagesField;
    private boolean mSuccess = true;

    public MessageQueueProxy() {
        try {
            mMessagesField = MessageQueue.class.getDeclaredField("mMessages");
            mMessagesField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            mSuccess = false;
        } catch (SecurityException e) {
            mSuccess = false;
        }

        if (mSuccess) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mMessageQueue = Looper.getMainLooper().getQueue();
            } else {
                try {
                    Field queueField = Looper.class.getDeclaredField("mQueue");
                    queueField.setAccessible(true);
                    mMessageQueue = (MessageQueue) queueField.get(Looper.getMainLooper());
                } catch (IllegalAccessException e) {
                    mSuccess = false;
                } catch (IllegalArgumentException e) {
                    mSuccess = false;
                } catch (NoSuchFieldException e) {
                    mSuccess = false;
                } catch (SecurityException e) {
                    mSuccess = false;
                }
            }
        }
    }

    public Message getCurrentMessage() {
        Message message = null;
        if (mMessagesField != null && mMessageQueue != null) {
            try {
                message = (Message) mMessagesField.get(mMessageQueue);
            } catch (IllegalAccessException e) {
            } catch (IllegalArgumentException e) {
            }
        }
        return message;
    }
}
