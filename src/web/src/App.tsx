import { useState } from "react";
import {
  createBrowserRouter,
  BrowserRouter as Router,
  RouterProvider,
  useRoutes,
} from "react-router-dom";
import Home from "./routes/Home";
import NotFound from "./routes/NotFound";
import AppLayout from "~/components/AppLayout";
import { Provider } from "react-redux";
import { store } from "~/store/index";
import "./app.css";
import Documents from "./routes/Documents";
import PubmedSearch from "./routes/PubmedSearch";
import Login from "./routes/Login";
import Profile from "./routes/Profile";
import { RouteErrorElement } from "./components/ErrorBoundary";
import { CookiesProvider } from 'react-cookie';

const router = createBrowserRouter([
  {
    element: <AppLayout />,
    errorElement: <RouteErrorElement />,
    children: [
      { path: "/", element: <Home /> },
      { path: "/documents", element: <Documents /> },
      { path: "/pubmed", element: <PubmedSearch /> },
      { path: "/login", element: <Login /> },
      { path: "/profile", element: <Profile /> },
      { path: "*", element: <NotFound /> },
    ],
  },
]);

const AppWrapper = () => (
    <CookiesProvider defaultSetOptions={{ path: '/' }}>
        <Provider store={store}>
            <RouterProvider router={router} />
        </Provider>
    </CookiesProvider>
);

export default AppWrapper;
