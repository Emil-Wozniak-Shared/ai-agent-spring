import React from "react";
import { Link, Outlet, useLocation } from "react-router-dom";
import { useAppSelector, useAppDispatch } from "~/store/hooks";
import { toggleTheme, checkBackendConnection } from "~/store/slices/appSlice";
import { clearToken } from "~/store/slices/tokenSlice";
import NotificationCenter from "~/components/NotificationCenter";
import Navbar from "./Navbar";

interface LayoutProps {}

const AppLayout: React.FC<LayoutProps> = () => {
  const location = useLocation();
  const dispatch = useAppDispatch();
  const { theme, backendStatus } = useAppSelector((state) => state.app);
  const { token } = useAppSelector((state) => state.token);

  React.useEffect(() => {
    dispatch(checkBackendConnection());
    const interval = setInterval(() => {
      dispatch(checkBackendConnection());
    }, 30000);

    return () => clearInterval(interval);
  }, [dispatch]);

  const isActive = (path: string) => {
    return location.pathname === path ? "nav-link active" : "nav-link";
  };

  const handleLogout = () => {
    dispatch(clearToken());
  };

  return (
    <div
      className={`app-layout ${theme} flex flex-col h-screen justify-between`}
    >
      <Navbar />
      <main className="container mx-auto max-w-[85rem] px-4 sm:px-6 lg:px-8 min-h-fit">
        <Outlet />
      </main>
      <NotificationCenter />
      <footer className="h-10">
        <p>&copy; 2025 My Gradle + React App</p>
      </footer>
    </div>
  );
};

export default AppLayout;
