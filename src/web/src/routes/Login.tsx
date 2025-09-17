import React from "react";
import { useAppSelector, useAppDispatch } from "~/store/hooks";
import {
  clearError,
  createToken,
  logout,
} from "../store/slices/tokenSlice";
import { addNotification } from "~/store/slices/appSlice";
import { Label } from "@radix-ui/react-label";
import { Input } from "~/components/ui/input";
import { Button } from "~/components/ui/button";
import { useCookies } from 'react-cookie'
import { TOKEN_NAME } from "~/store/constants";

const Login: React.FC = () => {
  const [cookies, setCookie, removeCookie] = useCookies([TOKEN_NAME]);
  const dispatch = useAppDispatch();
  const { authorized, loading, error } = useAppSelector((state) => state.token);
  const [credentials, setCredentials] = React.useState({
    login: "",
    password: "",
  });

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await dispatch(createToken(credentials)).unwrap();
      for (var pair of response.headers.entries()) {
          if (pair[0] === 'x-token') {
              setCookie(TOKEN_NAME, pair[1])
          }
      }

      dispatch(addNotification({ message: "Login successful!", type: "success" }));
      setCredentials({ login: "", password: "" });
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
    removeCookie(TOKEN_NAME)
    dispatch(logout());
    dispatch(addNotification({ message: "Logged out successfully", type: "info" }));
  };

  if (authorized) {
    return (
      <>
        <h1 className="text-4xl font-bold mb-4">Authentication</h1>
        <div className="my-4">
          <p>✅ You are logged in!</p>
          <p className="mb-4">
            {authorized ? "Authorized" : "Not authorized"}
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
          <Label htmlFor="login">Login:</Label>
          <Input
            id="login"
            type="text"
            value={credentials.login}
            onChange={(e) =>
              setCredentials({ ...credentials, login: e.target.value })
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
