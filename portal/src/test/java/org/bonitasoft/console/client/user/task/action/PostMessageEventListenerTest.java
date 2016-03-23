package org.bonitasoft.console.client.user.task.action;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;

import com.google.gwtmockito.GwtMockitoTestRunner;

@RunWith(GwtMockitoTestRunner.class)
public class PostMessageEventListenerTest {

    @Spy
    private final CustomPostMessageEventListener customPostMessageEventListener = new CustomPostMessageEventListener();

    private class CustomPostMessageEventListener extends PostMessageEventListener {

        private String actionToWatch = null;

        @Override
        protected void onSuccess(final String caseId) {
            success(caseId);
        }

        public void success(final String caseId) {
        }

        @Override
        protected void onError(final String dataFromError, final int errorCode) {
            error(dataFromError, errorCode);
        }

        public void error(final String dataFromError, final int errorCode) {
        }

        @Override
        public String getActionToWatch() {
            return actionToWatch;
        }

        public void setActionToWatch(final String actionToWatch) {
            this.actionToWatch = actionToWatch;
        }
    }

    @Test
    public void onSuccessOrError_should_be_called_with_valid_json() throws Exception {
        final String jsonMessage = "{\"message\":\"error\",\"action\":\"\"}";
        customPostMessageEventListener.setActionToWatch("");

        customPostMessageEventListener.onMessageEvent(jsonMessage);

        verify(customPostMessageEventListener).error(anyString(), anyInt());
    }

    @Test
    public void onSuccessOrError_should_not_be_called_with_invalid_json() throws Exception {
        final String jsonMessage = "{\"message\":\"success\",action\":\"Start process\"}";
        customPostMessageEventListener.setActionToWatch("Start process");

        customPostMessageEventListener.onMessageEvent(jsonMessage);

        verify(customPostMessageEventListener, never()).success(anyString());
        verify(customPostMessageEventListener, never()).error(anyString(), anyInt());
    }

    @Test
    public void onSuccessOrError_should_not_be_called_with_action_not_watched() throws Exception {
        final String jsonMessage = "{\"message\":\"error\",\"action\":\"\"}";
        customPostMessageEventListener.setActionToWatch("Start process");

        customPostMessageEventListener.onMessageEvent(jsonMessage);

        verify(customPostMessageEventListener, never()).success(anyString());
        verify(customPostMessageEventListener, never()).error(anyString(), anyInt());
    }
}
