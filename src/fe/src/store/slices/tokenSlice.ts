import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";

interface TokenState {
  token: string | null;
  loading: boolean;
  error: string | null;
  expiresAt: string | null;
}

const initialState: TokenState = {
  token: null,
  loading: false,
  error: null,
  expiresAt: null,
};

export const createToken = createAsyncThunk(
  "token/create",
  async (
    credentials: { username?: string; password?: string; [key: string]: any },
    { rejectWithValue },
  ) => {
    try {
      const response = await fetch("/api/token", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(credentials),
      });
      if (!response.ok) throw new Error("Authentication failed");
      return await response.json();
    } catch (error) {
      return rejectWithValue(
        error instanceof Error ? error.message : "Unknown error",
      );
    }
  },
);

const tokenSlice = createSlice({
  name: "token",
  initialState,
  reducers: {
    clearToken: (state) => {
      state.token = null;
      state.expiresAt = null;
      state.error = null;
    },
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
        state.token = action.payload.token;
        state.expiresAt = action.payload.expiresAt;
      })
      .addCase(createToken.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });
  },
});

export const { clearToken, clearError } = tokenSlice.actions;
export default tokenSlice.reducer;
