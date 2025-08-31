import { createListenerMiddleware, isAnyOf } from '@reduxjs/toolkit';
import { apiClient } from '../../utils/api';
import { addNotification } from '../slices/appSlice';
import type { RootState, AppDispatch } from '../index';

// Create listener middleware for API handling
export const apiMiddleware = createListenerMiddleware();

// Generic API error handler
const handleApiError = (error: any, dispatch: AppDispatch) => {
  const message = error?.message || 'An error occurred';
  dispatch(addNotification({
    message,
    type: 'error'
  }));
};

// Listen for any rejected API action
apiMiddleware.startListening({
  matcher: isAnyOf(
    // Add your async thunks here that should trigger notifications
  ),
  effect: async (action, listenerApi) => {
    if (action.type.endsWith('/rejected')) {
      handleApiError(action.payload, listenerApi.dispatch);
    }
  },
});
