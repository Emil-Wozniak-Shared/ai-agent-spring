import {
  createSlice,
  createAsyncThunk,
  type PayloadAction,
} from "@reduxjs/toolkit";
import { apiClient } from "../api";

export interface Orcid {
  email: string;
  id?: string | null;
}

interface OrcidState {
  orcid: Orcid;
  loading: boolean;
  error: string | null;
}

export const defaultOrcid: Orcid = {
  email: "",
  id: null,
};

const initialState: OrcidState = {
  orcid: defaultOrcid,
  loading: false,
  error: null,
};

export const updateOrcid = createAsyncThunk(
  "orcid/update",
  async (id: string, { rejectWithValue }) => {
    try {
      const payload = { id };
      const response = await apiClient.request(`/api/orcid/orcid/`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });
      if (response.status !== 204) throw new Error("Failed to update orcid");
      return payload;
    } catch (error) {
      return rejectWithValue(
        error instanceof Error ? error.message : "Unknown error",
      );
    }
  },
);

export const findOrcid = createAsyncThunk("orcid/find", async () => {
  try {
    const response = await apiClient.request(`/api/orcid`, { method: "GET" });
    if (response.status !== 200) {
      return defaultOrcid;
    }
    const json = await response.json();
    return json;
  } catch (error) {
    console.error(error);
  }
});

const orcidSlice = createSlice({
  name: "orcid",
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      // Find articles
      .addCase(findOrcid.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(findOrcid.fulfilled, (state, action) => {
        state.loading = false;
        state.orcid = { ...state.orcid, ...action.payload };
      })
      .addCase(findOrcid.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      // Search articles
      .addCase(updateOrcid.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(updateOrcid.fulfilled, (state, action) => {
        state.loading = false;
        state.orcid = { ...state.orcid, ...action.payload };
      })
      .addCase(updateOrcid.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });
  },
});

export const { clearError } = orcidSlice.actions;
export default orcidSlice.reducer;
