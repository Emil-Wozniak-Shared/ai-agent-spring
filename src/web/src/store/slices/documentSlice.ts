import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import {apiClient} from '../api'

export interface Document {
  id: string;
  text: string;
  metadata: object;
}

interface DocumentState {
  documents: Document[];
  searchResults: Document[];
  loading: boolean;
  error: string | null;
}

const initialState: DocumentState = {
  documents: [],
  searchResults: [],
  loading: false,
  error: null,
};

export const createManyDocuments = createAsyncThunk(
  "documents/createMany",
  async (
    documents: Omit<Document, "id" | "createdAt">[],
    { rejectWithValue },
  ) => {
    try {
      const response = await apiClient.request("/api/documents", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
        },
        body: JSON.stringify(documents),
      });
      if (!response.ok) throw new Error("Failed to create documents");
      return await response.json();
    } catch (error) {
      return rejectWithValue(
        error instanceof Error ? error.message : "Unknown error",
      );
    }
  },
);

export const searchDocuments = createAsyncThunk(
  "documents/search",
  async (
    searchQuery: { query: string; source: string },
    { rejectWithValue },
  ) => {
    try {
      const payload = {
        query: searchQuery.query,
        keywords: [
          {
            key: "source",
            value: searchQuery.source,
          },
        ],
      };
      const response = await apiClient.request("/api/documents/search", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
        },
        body: JSON.stringify(payload),
      });
      if (!response.ok) throw new Error("Search failed");
      return await response.json();
    } catch (error) {
      return rejectWithValue(
        error instanceof Error ? error.message : "Unknown error",
      );
    }
  },
);

const documentSlice = createSlice({
  name: "documents",
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
    clearSearchResults: (state) => {
      state.searchResults = [];
    },
  },
  extraReducers: (builder) => {
    builder
      // Create many documents
      .addCase(createManyDocuments.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(createManyDocuments.fulfilled, (state, action) => {
        state.loading = false;
        state.documents.push(...action.payload);
      })
      .addCase(createManyDocuments.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      // Search documents
      .addCase(searchDocuments.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(searchDocuments.fulfilled, (state, action) => {
        state.loading = false;
        state.searchResults = action.payload;
      })
      .addCase(searchDocuments.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });
  },
});

export const { clearError, clearSearchResults } = documentSlice.actions;
export default documentSlice.reducer;
