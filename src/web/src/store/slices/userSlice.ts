import {
  createSlice,
  createAsyncThunk,
  type PayloadAction,
} from "@reduxjs/toolkit";
import { apiClient } from "../api";

export interface User {
  id: number | null;
  name: string;
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  active: boolean;
  createdAt: Date;
  updatedAt: Date;
  roles: string[];
  description?: string | null
}

export const emptyUser: User = {
  id: null,
  name: "",
  firstName: "",
  lastName: "",
  email: "",
  password: "",
  active: true,
  createdAt: new Date(),
  updatedAt: new Date(),
  roles: ["user"],
  description: null
} as User;

interface UserState {
  users: User[];
  currentUser: User | null;
  loading: boolean;
  error: string | null;
}

const initialState: UserState = {
  users: [],
  currentUser: null,
  loading: false,
  error: null,
};

export const fetchAllUsers = createAsyncThunk(
  "users/fetchAll",
  async (_, { rejectWithValue }) => {
    try {
      const response = await apiClient.request("/api/users");
      if (!response.ok) throw new Error("Failed to fetch users");
      const json = await response.json();
      return json.users;
    } catch (error) {
      console.error(error);
      return rejectWithValue(
        error instanceof Error ? error.message : "Unknown error",
      );
    }
  },
);

export const fetchUserById = createAsyncThunk(
  "users/fetchById",
  async (id: number, { rejectWithValue }) => {
    try {
      const response = await apiClient.request(`/api/users/${id}`);
      if (!response.ok) throw new Error("User not found");
      return await response.json();
    } catch (error) {
      return rejectWithValue(
        error instanceof Error ? error.message : "Unknown error",
      );
    }
  },
);

export const createUser = createAsyncThunk(
  "users/create",
  async (userData: Omit<User, "id">, { rejectWithValue }) => {
    try {
      const response = await apiClient.request("/api/users", {
        method: "POST",
        body: JSON.stringify(userData),
      });
      if (!response.ok) throw new Error("Failed to create user");
      return await response.json();
    } catch (error) {
      return rejectWithValue(
        error instanceof Error ? error.message : "Unknown error",
      );
    }
  },
);

export const updateUser = createAsyncThunk(
  "users/update",
  async (
    { email, userData }: { email: string; userData: Partial<User> },
    { rejectWithValue },
  ) => {
    try {
      const response = await apiClient.request(`/api/users/${email}`, {
        method: "PUT",
        body: JSON.stringify(userData),
      });
      if (!response.ok) throw new Error("Failed to update user");
      return await response.json();
    } catch (error) {
      return rejectWithValue(
        error instanceof Error ? error.message : "Unknown error",
      );
    }
  },
);

export const describeUser = createAsyncThunk(
  "users/describe",
  async (
  ) => {
    try {
      const response = await apiClient.request(`/api/ask/describe`, { method: "GET" });
    console.log('response thunk' , response)
      if (!response.ok) throw new Error("Failed to update user");
      return await response.json();
    } catch (error) {
     console.error(error);
    }
  },
);

const userSlice = createSlice({
  name: "users",
  initialState,
  reducers: {
    setCurrentUser: (state, action) => {
      state.currentUser = action.payload;
    },
    clearError: (state) => {
      state.error = null;
    },
    clearCurrentUser: (state) => {
      state.currentUser = null;
    },
  },
  extraReducers: (builder) => {
    builder
      // Fetch all users
      .addCase(fetchAllUsers.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchAllUsers.fulfilled, (state, action) => {
        state.loading = false;
        state.users = action.payload;
      })
      .addCase(fetchAllUsers.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      // Fetch user by ID
      .addCase(fetchUserById.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchUserById.fulfilled, (state, action) => {
        state.loading = false;
        state.currentUser = action.payload;
      })
      .addCase(fetchUserById.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      // Create user
      .addCase(createUser.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(createUser.fulfilled, (state, action) => {
        state.loading = false;
        state.users.push(action.payload);
      })
      .addCase(createUser.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      // describe user
      .addCase(describeUser.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(describeUser.fulfilled, (state, action) => {
        state.loading = false;
        const response = action.payload
        console.log('response', response)
        const index = state.users.findIndex(it => it.email === response.email)
        console.log('index', index)
        state.users[index].description = response.description;
      })
      .addCase(describeUser.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      // Update user
      .addCase(updateUser.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(updateUser.fulfilled, (state, action) => {
        state.loading = false;
        const index = state.users.findIndex(
          (user) => user.email === action.payload.email,
        );
        if (index !== -1) {
          state.users[index] = action.payload;
        }
        if (state.currentUser?.email === action.payload.email) {
          state.currentUser = action.payload;
        }
      })
      .addCase(updateUser.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });
  },
});

export const { clearError, clearCurrentUser, setCurrentUser } =
  userSlice.actions;
export default userSlice.reducer;
