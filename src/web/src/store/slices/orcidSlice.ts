import {
  createSlice,
  createAsyncThunk,
  type PayloadAction,
} from "@reduxjs/toolkit";
import {apiClient} from '../api'

interface Orcid {
  email: string
  id?: string | null
}

interface OrcidState {
  orcid: Orcid;
  loading: boolean;
  error: string | null;
}

const initialState: OrcidState = {
  orcid: { id: null },
  loading: false,
  error: null,
};

export const updateOrcid = createAsyncThunk(
  "orcid/Update",
  async (id: string, { rejectWithValue }) => {
    try {
      const payload = { id };
      console.log(payload)
      const response = await apiClient.request(
        `/api/orcid/orcid/`,
        {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        },
      );
      if (response.status !== 204) throw new Error("Failed to update orcid");
      return payload;
    } catch (error) {
      return rejectWithValue(
        error instanceof Error ? error.message : "Unknown error",
      );
    }
  },
);

const orcidSlice = createSlice({
  name: "orcid",
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    }
  },
  extraReducers: (builder) => {
    builder
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
      })
  },
});

export const { clearError } = orcidSlice.actions;
export default orcidSlice.reducer;
