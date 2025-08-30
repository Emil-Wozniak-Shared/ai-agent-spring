import {
  createSlice,
  createAsyncThunk,
  type PayloadAction,
} from "@reduxjs/toolkit";

export interface Notification {
  id: string;
  message: string;
  type: "success" | "error" | "info" | "warning";
  timestamp: number;
}

interface AppState {
  theme: "light" | "dark";
  sidebarOpen: boolean;
  notifications: Notification[];
  backendStatus: "connected" | "disconnected" | "checking";
  isOnline: boolean;
  lastActivity: number;
}

const initialState: AppState = {
  theme: "light",
  sidebarOpen: false,
  notifications: [],
  backendStatus: "disconnected",
  isOnline: navigator.onLine,
  lastActivity: Date.now(),
};

export const checkBackendConnection = createAsyncThunk(
  "app/checkBackendConnection",
  async (_, { rejectWithValue }) => {
    try {
      const response = await fetch("/api/health", {
        method: "GET",
        headers: { Accept: "application/json" },
      });
      if (response.ok) {
        return "connected";
      }
      return "disconnected";
    } catch (error) {
      return "disconnected";
    }
  },
);

const appSlice = createSlice({
  name: "app",
  initialState,
  reducers: {
    toggleTheme: (state) => {
      state.theme = state.theme === "light" ? "dark" : "light";
    },
    setTheme: (state, action: PayloadAction<"light" | "dark">) => {
      state.theme = action.payload;
    },
    toggleSidebar: (state) => {
      state.sidebarOpen = !state.sidebarOpen;
    },
    setSidebarOpen: (state, action: PayloadAction<boolean>) => {
      state.sidebarOpen = action.payload;
    },
    addNotification: (
      state,
      action: PayloadAction<Omit<Notification, "id" | "timestamp">>,
    ) => {
      const notification: Notification = {
        ...action.payload,
        id: `${Date.now()}-${Math.random().toString(36).substr(2, 9)}`,
        timestamp: Date.now(),
      };
      state.notifications.push(notification);

      // Keep only last 10 notifications
      if (state.notifications.length > 10) {
        state.notifications = state.notifications.slice(-10);
      }
    },
    removeNotification: (state, action: PayloadAction<string>) => {
      state.notifications = state.notifications.filter(
        (n) => n.id !== action.payload,
      );
    },
    clearNotifications: (state) => {
      state.notifications = [];
    },
    setOnlineStatus: (state, action: PayloadAction<boolean>) => {
      state.isOnline = action.payload;
    },
    updateLastActivity: (state) => {
      state.lastActivity = Date.now();
    },
    setBackendStatus: (
      state,
      action: PayloadAction<"connected" | "disconnected" | "checking">,
    ) => {
      state.backendStatus = action.payload;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(checkBackendConnection.pending, (state) => {
        state.backendStatus = "checking";
      })
      .addCase(checkBackendConnection.fulfilled, (state, action) => {
        state.backendStatus = action.payload as "connected" | "disconnected";
      })
      .addCase(checkBackendConnection.rejected, (state) => {
        state.backendStatus = "disconnected";
      });
  },
});

export const {
  toggleTheme,
  setTheme,
  toggleSidebar,
  setSidebarOpen,
  addNotification,
  removeNotification,
  clearNotifications,
  setOnlineStatus,
  updateLastActivity,
  setBackendStatus,
} = appSlice.actions;

export default appSlice.reducer;
