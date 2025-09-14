import { type Middleware } from "@reduxjs/toolkit";

export const loggerMiddleware: Middleware =
  (store) => (next) => (action: any) => {
    if (process.env.NODE_ENV === "development") {
      // console.log(`🔥 ${action.type} Action:`, action);
      const result = next(action);
      return result;
    }

    return next(action);
  };
