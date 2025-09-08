import { configureStore } from "@reduxjs/toolkit";
import userSlice from "./slices/userSlice";
import documentSlice from "./slices/documentSlice";
import pubmedSlice from "./slices/pubmedSlice";
import tokenSlice from "./slices/tokenSlice";
import appSlice from "./slices/appSlice";
import orcidSlice from "./slices/orcidSlice";
import {
  apiMiddleware,
  authMiddleware,
  loggerMiddleware,
  networkMiddleware,
  apiRequestMiddleware,
} from "./middleware";

export const store = configureStore({
  reducer: {
    users: userSlice,
    documents: documentSlice,
    pubmed: pubmedSlice,
    token: tokenSlice,
    app: appSlice,
    orcid: orcidSlice
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: ["persist/PERSIST", "persist/REHYDRATE"],
      },
    })
      .prepend(
        apiMiddleware.middleware,
        authMiddleware.middleware,
        networkMiddleware.middleware,
        apiRequestMiddleware.middleware,
      )
      .concat(loggerMiddleware),
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
