import { createListenerMiddleware } from '@reduxjs/toolkit';
import { createToken, setAuthorized } from '../slices/tokenSlice';
import { addNotification } from '../slices/appSlice';
import Cookies from 'universal-cookie';
import { TOKEN_NAME } from '../constants';

export const authMiddleware = createListenerMiddleware();


// Auto-clear expired tokens
authMiddleware.startListening({
  actionCreator: createToken.fulfilled,
  effect: async (action, listenerApi) => {
    const { expiresAt } = action.payload;

    if (expiresAt) {
      const cookies = new Cookies()
      const expiryTime = new Date(expiresAt).getTime();
      const now = Date.now();
      const timeUntilExpiry = expiryTime - now;

      if (timeUntilExpiry > 0) {
        setTimeout(() => {
          cookies.remove(TOKEN_NAME)
          listenerApi.dispatch(setAuthorized(false));
          listenerApi.dispatch(addNotification({
            message: 'Your session has expired. Please log in again.',
            type: 'warning'
          }));
        }, timeUntilExpiry);
      }
    }
  },
});
