import React from "react";
import { createUser, emptyUser, type User } from "~/store/slices/userSlice";
import { Button } from "../ui/button";
import { useAppDispatch, useAppSelector } from "~/store/hooks";
import { addNotification } from "~/store/slices/appSlice";
import { Input } from "../ui/input";

const CreateNewUser = () => {
  const dispatch = useAppDispatch();
  const { loading } = useAppSelector((state) => state.users);
  const [newUser, setNewUser] = React.useState<User>(emptyUser);

  const handleCreateUser = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!newUser.name || !newUser.email) return;

    try {
      await dispatch(createUser(newUser)).unwrap();
      setNewUser(emptyUser);
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

  return (
    <div className="container">
      <h2>Create New User</h2>
      <form onSubmit={handleCreateUser} className="user-form">
        <Input
          type="text"
          placeholder="Name"
          value={newUser.name}
          onChange={(e) => setNewUser({ ...newUser, name: e.target.value })}
          required
        />
        <Input
          type="email"
          placeholder="Email"
          value={newUser.email}
          onChange={(e) => setNewUser({ ...newUser, email: e.target.value })}
          required
        />
        <Input
          id="password"
          type="password"
          placeholder="password"
          value={newUser.email}
          onChange={(e) => setNewUser({ ...newUser, email: e.target.value })}
          required
        />
        <Button type="submit" disabled={loading}>
          <span>{loading ? "Creating..." : "Create User"}</span>
        </Button>
      </form>
      <br />
    </div>
  );
};

export default CreateNewUser;
