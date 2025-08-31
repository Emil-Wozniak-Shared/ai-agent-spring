import { type Middleware } from "@reduxjs/toolkit";

export const loggerMiddleware: Middleware =
  (store) => (next) => (action: any) => {
    if (process.env.NODE_ENV === "development") {
      console.group(`🔥 ${action.type}`);
      // console.log("Previous State:", store.getState());
      console.log("Action:", action);

      const result = next(action);

      // console.log("Next State:", store.getState());
      console.groupEnd();

      return result;
    }

    return next(action);
  };
