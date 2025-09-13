import React from "react";
import { useAppSelector, useAppDispatch } from "~/store/hooks";
import {
  fetchAllUsers,
  createUser,
  updateUser,
  type User,
  emptyUser,
} from "../store/slices/userSlice";
import { addNotification } from "~/store/slices/appSlice";
import UserCard from "~/components/user/UserCard";
import { Button } from "~/components/ui/button";
import CreateNewUser from "~/components/user/CreateUser";
import { Link } from "react-router-dom";

const Home: React.FC = () => {
  const dispatch = useAppDispatch();
  const { authorized } = useAppSelector((state) => state.token);
  const [newUser, setNewUser] = React.useState<User>(emptyUser);


  React.useEffect(() => {
    dispatch(fetchAllUsers());
  }, [dispatch]);

  return (
    <div className="page">
      <h1>Dashboard</h1>
      <CreateNewUser />
      {authorized
        ? <AuthorizedActions />
        : <Link to="/login"> {"Login"} </Link>
      }
    </div>
  );
};

const AuthorizedActions = () => {
  const [editingUser, setEditingUser] = React.useState<string | null>(null);
  const dispatch = useAppDispatch();
  const { users, loading, error } = useAppSelector((state) => state.users);

  const handleUpdateUser = async (email: string, userData: User) => {
    try {
      await dispatch(updateUser({ email, userData })).unwrap();
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
    <div>
      <h2 className="text-2xl">{"Users:"} {users.length}</h2>
      <button onClick={() => dispatch(fetchAllUsers())} disabled={loading}>
        {loading ? "Refreshing..." : "Refresh Users"}
      </button>

      {error && <div className="error-message">{error}</div>}
      <br />
      {users && users.length > 1 && (
        <div>
          {users.map((user) => (
            <UserCard
              key={user.email}
              user={user}
              isEditing={editingUser === user.email}
              onEdit={() => setEditingUser(user.email!!)}
              onSave={(userData) => handleUpdateUser(user.email!!, userData)}
              onCancel={() => setEditingUser(null)}
            />
          ))
          }
        </div>
      )}
      <br />
      {users && users.length === 1 && (
        <div>
          {users.map(user => (
            <Link to={`/profile/${user.name}`}>{"Profile"}</Link>
          ))}
        </div>
      )}
    </div>
  )
}

export default Home;
