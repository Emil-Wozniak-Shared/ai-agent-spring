import { useState } from "react";
import {
  createBrowserRouter,
  BrowserRouter as Router,
  RouterProvider,
  useRoutes,
} from "react-router-dom";
import Home from "./routes/Home";
import NotFound from "./routes/NotFound";
import { Layout } from "~/components/Layout";
import AppLayout from "~/components/AppLayout";
import { Provider } from "react-redux";
import { store } from "~/store/index";
import "./app.css";
import Documents from "./routes/Documents";
import PubmedSearch from "./routes/PubmedSearch";
import Login from "./routes/Login";

const router = createBrowserRouter([
  {
    element: <AppLayout />,
    children: [
      { path: "/", element: <Home /> },
      { path: "/documents", element: <Documents /> },
      { path: "/pubmed", element: <PubmedSearch /> },
      { path: "/login", element: <Login /> },
      { path: "*", element: <NotFound /> },
    ],
  },
]);

const AppWrapper = () => (
  <Provider store={store}>
    <RouterProvider router={router} />
  </Provider>
);

export default AppWrapper;
