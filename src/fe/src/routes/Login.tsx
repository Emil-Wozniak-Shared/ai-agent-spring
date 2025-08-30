import React from "react";
import { useAppSelector, useAppDispatch } from "~/store/hooks";
import { createToken, clearError } from "../store/slices/tokenSlice";
import { addNotification } from "~/store/slices/appSlice";

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
      <div className="page">
        <h1>Authentication</h1>
        <div className="auth-section">
          <p className="success-message">✅ You are logged in!</p>
          <p>
            <strong>Token:</strong> {token.substring(0, 20)}...
          </p>
          <button onClick={handleLogout} className="logout-btn">
            Logout
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="page">
      <h1>Login</h1>

      <form onSubmit={handleLogin} className="login-form">
        <div className="form-group">
          <label htmlFor="username">Username:</label>
          <input
            id="username"
            type="text"
            value={credentials.username}
            onChange={(e) =>
              setCredentials({ ...credentials, username: e.target.value })
            }
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="password">Password:</label>
          <input
            id="password"
            type="password"
            value={credentials.password}
            onChange={(e) =>
              setCredentials({ ...credentials, password: e.target.value })
            }
            required
          />
        </div>

        <button type="submit" disabled={loading}>
          {loading ? "Logging in..." : "Login"}
        </button>

        {error && (
          <div className="error-message">
            {error}
            <button onClick={() => dispatch(clearError())}>×</button>
          </div>
        )}
      </form>
    </div>
  );
};

export default Login;
