import React from "react";
import { Link, Outlet, useLocation } from "react-router-dom";
import { useAppSelector, useAppDispatch } from "~/store/hooks";
import { toggleTheme, checkBackendConnection } from "~/store/slices/appSlice";
import NotificationCenter from "~/components/NotificationCenter";
import Navbar from "./Navbar";
import { setAuthorized } from "../store/slices/tokenSlice";
import { useCookies } from "react-cookie";
import { TOKEN_NAME } from "~/store/constants";
import { fetchAllUsers } from "~/store/slices/userSlice";
import { findOrcid } from "~/store/slices/orcidSlice";

interface LayoutProps {}

const AppLayout: React.FC<LayoutProps> = () => {
  const [cookies, setCookie, removeCookie] = useCookies([TOKEN_NAME]);
  const location = useLocation();
  const dispatch = useAppDispatch();
  const { theme, backendStatus } = useAppSelector((state) => state.app);
  React.useEffect(() => {
    dispatch(checkBackendConnection());
    dispatch(setAuthorized(true));
    dispatch(fetchAllUsers());
    dispatch(findOrcid());
    const interval = setInterval(() => {
      dispatch(checkBackendConnection());
    }, 30000);

    return () => clearInterval(interval);
  }, [dispatch]);

  const isActive = (path: string) => {
    return location.pathname === path ? "nav-link active" : "nav-link";
  };

  const handleLogout = () => {
    removeCookie(TOKEN_NAME);
    dispatch(setAuthorized(false));
  };

  return (
    <div
      className={`app-layout ${theme} flex flex-col h-screen justify-between`}
    >
      <Navbar />
      <main className="container mx-auto max-w-[85rem] px-4 sm:px-6 lg:px-8 min-h-fit">
        <div className="container flex flex-col items-center justify-center">
          <Outlet />
        </div>
      </main>
      <NotificationCenter />
      <footer className="h-10">
        <p>&copy; 2025 My Gradle + React App</p>
      </footer>
    </div>
  );
};

export default AppLayout;
