import { createListenerMiddleware } from '@reduxjs/toolkit';
import { setOnlineStatus, addNotification } from '../slices/appSlice';

export const networkMiddleware = createListenerMiddleware();

// Listen for network status changes
networkMiddleware.startListening({
  predicate: () => true,
  effect: async (action, listenerApi) => {
    const handleOnline = () => {
      listenerApi.dispatch(setOnlineStatus(true));
      listenerApi.dispatch(addNotification({
        message: 'Connection restored',
        type: 'success'
      }));
    };

    const handleOffline = () => {
      listenerApi.dispatch(setOnlineStatus(false));
      listenerApi.dispatch(addNotification({
        message: 'Connection lost',
        type: 'warning'
      }));
    };

    // Set up event listeners only once
    if (!window.__networkListenersAdded) {
      window.addEventListener('online', handleOnline);
      window.addEventListener('offline', handleOffline);
      window.__networkListenersAdded = true;
    }
  },
});