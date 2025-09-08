import { createListenerMiddleware, isAsyncThunkAction } from '@reduxjs/toolkit';
import { updateLastActivity } from '../slices/appSlice';
import Cookies from 'universal-cookie';

export const apiRequestMiddleware = createListenerMiddleware();

apiRequestMiddleware.startListening({
  matcher: isAsyncThunkAction,
  effect: async (action, listenerApi) => {
    listenerApi.dispatch(updateLastActivity());
  },
});

apiRequestMiddleware.startListening({
  matcher: isAsyncThunkAction,
  effect: async (action, listenerApi) => {
//     const cookies = new Cookies();
//     const token = cookies.get('X-TOKEN');
//     if (token) {
//       action.headers = {
//         ...action.headers,
//         'Authorization': `Bearer ${token}`
//       };
//     }
  },
});
