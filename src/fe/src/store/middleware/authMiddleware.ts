import { createListenerMiddleware } from '@reduxjs/toolkit';
import { createToken, clearToken } from '../slices/tokenSlice';
import { addNotification } from '../slices/appSlice';

export const authMiddleware = createListenerMiddleware();

// Auto-clear expired tokens
authMiddleware.startListening({
  actionCreator: createToken.fulfilled,
  effect: async (action, listenerApi) => {
    const { expiresAt } = action.payload;

    if (expiresAt) {
      const expiryTime = new Date(expiresAt).getTime();
      const now = Date.now();
      const timeUntilExpiry = expiryTime - now;

      if (timeUntilExpiry > 0) {
        setTimeout(() => {
          listenerApi.dispatch(clearToken());
          listenerApi.dispatch(addNotification({
            message: 'Your session has expired. Please log in again.',
            type: 'warning'
          }));
        }, timeUntilExpiry);
      }
    }
  },
});