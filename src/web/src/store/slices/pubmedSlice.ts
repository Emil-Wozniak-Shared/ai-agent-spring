import {
  createSlice,
  createAsyncThunk,
  type PayloadAction,
} from "@reduxjs/toolkit";

export interface PubmedArticle {
  id: string;
  title: string;
  authors: string[];
  abstract?: string;
  publishedDate: string;
  journal: string;
  doi?: string;
}

interface PubmedState {
  articles: PubmedArticle[];
  currentArticle: PubmedArticle | null;
  loading: boolean;
  error: string | null;
}

const initialState: PubmedState = {
  articles: [],
  currentArticle: null,
  loading: false,
  error: null,
};

export const searchPubmedArticles = createAsyncThunk(
  "pubmed/searchArticles",
  async (
    searchParams: { query: string; limit?: number },
    { rejectWithValue },
  ) => {
    try {
      const response = await fetch("/api/pubmed/search/articles", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(searchParams),
      });
      if (!response.ok) throw new Error("Pubmed search failed");
      return await response.json();
    } catch (error) {
      return rejectWithValue(
        error instanceof Error ? error.message : "Unknown error",
      );
    }
  },
);

export const searchPubmedArticlesByIds = createAsyncThunk(
  "pubmed/searchByIds",
  async (ids: string[], { rejectWithValue }) => {
    try {
      const response = await fetch(
        `/api/pubmed/search/articles/${ids.join(",")}`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({}),
        },
      );
      if (!response.ok) throw new Error("Pubmed search by IDs failed");
      return await response.json();
    } catch (error) {
      return rejectWithValue(
        error instanceof Error ? error.message : "Unknown error",
      );
    }
  },
);

export const fetchArticleAbstract = createAsyncThunk(
  "pubmed/fetchAbstract",
  async (articleId: string, { rejectWithValue }) => {
    try {
      const response = await fetch(
        `/api/pubmed/articles/${articleId}/abstract`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({}),
        },
      );
      if (!response.ok) throw new Error("Failed to fetch abstract");
      return await response.text();
    } catch (error) {
      return rejectWithValue(
        error instanceof Error ? error.message : "Unknown error",
      );
    }
  },
);

const pubmedSlice = createSlice({
  name: "pubmed",
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
    clearArticles: (state) => {
      state.articles = [];
    },
    setCurrentArticle: (state, action: PayloadAction<PubmedArticle>) => {
      state.currentArticle = action.payload;
    },
  },
  extraReducers: (builder) => {
    builder
      // Search articles
      .addCase(searchPubmedArticles.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(searchPubmedArticles.fulfilled, (state, action) => {
        state.loading = false;
        state.articles = action.payload;
      })
      .addCase(searchPubmedArticles.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      // Search by IDs
      .addCase(searchPubmedArticlesByIds.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(searchPubmedArticlesByIds.fulfilled, (state, action) => {
        state.loading = false;
        state.articles = action.payload;
      })
      .addCase(searchPubmedArticlesByIds.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      // Fetch abstract
      .addCase(fetchArticleAbstract.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchArticleAbstract.fulfilled, (state, action) => {
        state.loading = false;
        if (state.currentArticle) {
          state.currentArticle.abstract = action.payload;
        }
      })
      .addCase(fetchArticleAbstract.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });
  },
});

export const { clearError, clearArticles, setCurrentArticle } =
  pubmedSlice.actions;
export default pubmedSlice.reducer;
