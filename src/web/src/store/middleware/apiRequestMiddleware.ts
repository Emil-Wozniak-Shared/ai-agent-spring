import { createListenerMiddleware, isAsyncThunkAction } from '@reduxjs/toolkit';
import { updateLastActivity } from '../slices/appSlice';

export const apiRequestMiddleware = createListenerMiddleware();

// Track user activity on API requests
apiRequestMiddleware.startListening({
  matcher: isAsyncThunkAction,
  effect: async (action, listenerApi) => {
    // Update last activity timestamp on any API call
    listenerApi.dispatch(updateLastActivity());
  },
});

// Add token to requests automatically
apiRequestMiddleware.startListening({
  matcher: isAsyncThunkAction,
  effect: async (action, listenerApi) => {
    const state = listenerApi.getState() as any;
    const token = state.token?.token;

    if (token && action.meta?.arg) {
      // Intercept fetch requests to add Authorization header
      const originalFetch = window.fetch;
      window.fetch = async (input, init) => {
        const url = typeof input === 'string' ? input : input.url;

        if (url.includes('/api/') && !url.includes('/api/token')) {
          const headers = new Headers(init?.headers);
          headers.set('Authorization', `Bearer ${token}`);

          return originalFetch(input, {
            ...init,
            headers,
          });
        }

        return originalFetch(input, init);
      };
    }
  },
});