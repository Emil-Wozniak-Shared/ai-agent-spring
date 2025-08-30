import React from "react";
import { useAppSelector, useAppDispatch } from "~/store/hooks";
import {
  fetchAllUsers,
  createUser,
  updateUser,
} from "../store/slices/userSlice";
import { addNotification } from "~/store/slices/appSlice";

const Home: React.FC = () => {
  const dispatch = useAppDispatch();
  const { users, loading, error } = useAppSelector((state) => state.users);
  const [newUser, setNewUser] = React.useState({ name: "", email: "" });
  const [editingUser, setEditingUser] = React.useState<number | null>(null);

  React.useEffect(() => {
    dispatch(fetchAllUsers());
  }, [dispatch]);

  const handleCreateUser = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!newUser.name || !newUser.email) return;

    try {
      await dispatch(createUser(newUser)).unwrap();
      setNewUser({ name: "", email: "" });
      dispatch(
        addNotification({
          message: "User created successfully!",
          type: "success",
        }),
      );
    } catch (error) {
      dispatch(
        addNotification({
          message: "Failed to create user",
          type: "error",
        }),
      );
    }
  };

  const handleUpdateUser = async (
    id: number,
    userData: { name: string; email: string },
  ) => {
    try {
      await dispatch(updateUser({ id, userData })).unwrap();
      setEditingUser(null);
      dispatch(
        addNotification({
          message: "User updated successfully!",
          type: "success",
        }),
      );
    } catch (error) {
      dispatch(
        addNotification({
          message: "Failed to update user",
          type: "error",
        }),
      );
    }
  };

  return (
    <div className="page">
      <h1>Dashboard</h1>

      <div className="dashboard-section">
        <h2>Create New User</h2>
        <form onSubmit={handleCreateUser} className="user-form">
          <input
            type="text"
            placeholder="Name"
            value={newUser.name}
            onChange={(e) => setNewUser({ ...newUser, name: e.target.value })}
            required
          />
          <input
            type="email"
            placeholder="Email"
            value={newUser.email}
            onChange={(e) => setNewUser({ ...newUser, email: e.target.value })}
            required
          />
          <button type="submit" disabled={loading}>
            {loading ? "Creating..." : "Create User"}
          </button>
        </form>
      </div>

      <div className="dashboard-section">
        <h2>Users ({users.length})</h2>
        <button onClick={() => dispatch(fetchAllUsers())} disabled={loading}>
          {loading ? "Refreshing..." : "Refresh Users"}
        </button>

        {error && <div className="error-message">{error}</div>}

        {
          <div className="users-grid">
            {users.map((user) => (
              <UserCard
                key={user.id}
                user={user}
                isEditing={editingUser === user.id}
                onEdit={() => setEditingUser(user.id)}
                onSave={(userData) => handleUpdateUser(user.id, userData)}
                onCancel={() => setEditingUser(null)}
              />
            ))}
          </div>
        }
      </div>
    </div>
  );

  interface UserCardProps {
    user: { id: number; name: string; email: string };
    isEditing: boolean;
    onEdit: () => void;
    onSave: (userData: { name: string; email: string }) => void;
    onCancel: () => void;
  }

  const UserCard: React.FC<UserCardProps> = ({
    user,
    isEditing,
    onEdit,
    onSave,
    onCancel,
  }) => {
    const [editData, setEditData] = React.useState({
      name: user.name,
      email: user.email,
    });

    const handleSave = () => {
      onSave(editData);
    };

    if (isEditing) {
      return (
        <div className="user-card editing">
          <input
            value={editData.name}
            onChange={(e) => setEditData({ ...editData, name: e.target.value })}
          />
          <input
            value={editData.email}
            onChange={(e) =>
              setEditData({ ...editData, email: e.target.value })
            }
          />
          <div className="card-actions">
            <button onClick={handleSave}>Save</button>
            <button onClick={onCancel}>Cancel</button>
          </div>
        </div>
      );
    }

    return (
      <div className="user-card">
        <h3>{user.name}</h3>
        <p>{user.email}</p>
        <button onClick={onEdit}>Edit</button>
      </div>
    );
  };
};

export default Home;
