import { createListenerMiddleware, isAnyOf } from '@reduxjs/toolkit';
import { apiClient } from '../api';
import { addNotification } from '../slices/appSlice';
import type { RootState, AppDispatch } from '../index';
import { logout } from '../slices/tokenSlice';

export const apiMiddleware = createListenerMiddleware();

const handleApiError = (error: any, dispatch: AppDispatch) => {
  const message = error?.message || 'An error occurred';
  dispatch(addNotification({
    message,
    type: 'error'
  }));
};

apiMiddleware.startListening({
  matcher: isAnyOf(
      logout,
    // Add your async thunks here that should trigger notifications
  ),
  effect: async (action, listenerApi) => {
    if (action.type.endsWith('/rejected')) {
      handleApiError(action.payload, listenerApi.dispatch);
    }
  },
});
