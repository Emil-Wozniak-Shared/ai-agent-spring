import React from "react";
import { useAppSelector, useAppDispatch } from "~/store/hooks";
import {
  createToken,
  clearError,
  clearToken,
} from "../store/slices/tokenSlice";
import { addNotification } from "~/store/slices/appSlice";
import { Label } from "@radix-ui/react-label";
import { Input } from "~/components/ui/input";
import { Button } from "~/components/ui/button";

const Login: React.FC = () => {
  const dispatch = useAppDispatch();
  const { token, loading, error } = useAppSelector((state) => state.token);
  const [credentials, setCredentials] = React.useState({
    username: "",
    password: "",
  });

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      await dispatch(createToken(credentials)).unwrap();
      dispatch(
        addNotification({
          message: "Login successful!",
          type: "success",
        }),
      );
      setCredentials({ username: "", password: "" });
    } catch (error) {
      dispatch(
        addNotification({
          message: "Login failed",
          type: "error",
        }),
      );
    }
  };

  const handleLogout = () => {
    dispatch(clearToken());
    dispatch(
      addNotification({
        message: "Logged out successfully",
        type: "info",
      }),
    );
  };

  if (token) {
    return (
      <>
        <h1 className="text-4xl font-bold mb-4">Authentication</h1>
        <div className="my-4">
          <p>✅ You are logged in!</p>
          <p className="mb-4">
            <strong>Token:</strong> {token.substring(0, 20)}...
          </p>
          <Button onClick={handleLogout}>Logout</Button>
        </div>
      </>
    );
  }

  return (
    <>
      <h1 className="text-4xl font-bold mb-4">Login</h1>
      <form
        onSubmit={handleLogin}
        className="shadow-md rounded px-8 pt-6 pb-8 mb-4"
      >
        <div className="mb-6">
          <Label htmlFor="username">Username:</Label>
          <Input
            id="username"
            type="text"
            value={credentials.username}
            onChange={(e) =>
              setCredentials({ ...credentials, username: e.target.value })
            }
            required
            className="shadow border rounded w-full py-2 px-3 text-gray-700 leading-tight shadow-outline"
          />
        </div>

        <div className="mb-6">
          <Label htmlFor="password">Password:</Label>
          <Input
            id="password"
            type="password"
            value={credentials.password}
            onChange={(e) =>
              setCredentials({ ...credentials, password: e.target.value })
            }
            required
            className="shadow border rounded w-full py-2 px-3 text-gray-700 leading-tight shadow-outline"
          />
        </div>

        <Button type="submit" disabled={loading} variant="outline">
          {loading ? "Logging in..." : "Login"}
        </Button>

        {error && (
          <div className="text-red-500">
            {error}
            <button onClick={() => dispatch(clearError())}>×</button>
          </div>
        )}
      </form>
    </>
  );
};

export default Login;
