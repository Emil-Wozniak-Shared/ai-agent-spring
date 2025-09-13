import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import Cookies from 'universal-cookie';

interface TokenState {
  authorized: boolean | null;
  loading: boolean;
  error: string | null;
  expiresAt: string | null;
}

const initialState: TokenState = {
  authorized: false,
  loading: false,
  error: null,
  expiresAt: null,
};


export const createToken = createAsyncThunk(
  "token/create",
  async (
    credentials: { login?: string; password?: string;[key: string]: any },
    { rejectWithValue },
  ) => {
    try {
      const response = await fetch("/api/token", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(credentials),
      });
      if (!response.ok) throw new Error("Authentication failed");
      const json = await response.json();
      return json;
    } catch (error) {
      return rejectWithValue(
        error instanceof Error ? error.message : "Unknown error",
      );
    }
  },
);

export const setAuthorized = createAsyncThunk(
  "token/set",
  async (state: boolean) => {
    try {
      return state;
    } catch (error) {
      console.error(error)
    }
  },
);

export const logout = createAsyncThunk(
  "logout",
  async () => {
    try {
      const response = await fetch("/api/logout", { method: "POST" });
      if (!response.ok) throw new Error("Authentication failed");
      return true
    } catch (error) {
      console.error(error)
    }
  },
);

const tokenSlice = createSlice({
  name: "token",
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(createToken.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(createToken.fulfilled, (state, action) => {
        state.loading = false;
        state.authorized = true;
        state.expiresAt = action.payload.expiresAt;
      })
      .addCase(createToken.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      .addCase(logout.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(logout.fulfilled, (state, action) => {
        state.loading = false;
        state.authorized = null;
        state.expiresAt = null;
      })
      .addCase(logout.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      .addCase(setAuthorized.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(setAuthorized.fulfilled, (state, action) => {
        state.loading = false;
        state.authorized = action.payload ?? false;
        state.expiresAt = null;
      })
      .addCase(setAuthorized.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
  },
});

export const { clearError } = tokenSlice.actions;
export default tokenSlice.reducer;
