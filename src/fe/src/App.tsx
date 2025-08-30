import { useState } from "react";
import {
  createBrowserRouter,
  BrowserRouter as Router,
  RouterProvider,
  useRoutes,
} from "react-router-dom";
import Home from "./routes/home";
import Articles from "./routes/articles";
import Faq from "./routes/faq";
import NotFound from "./routes/NotFound";
import { Layout } from "~/components/Layout";
import "./app.css";

const router = createBrowserRouter([
  {
    element: <Layout />,
    children: [
      { path: "/", element: <Home /> },
      { path: "/articles", element: <Articles /> },
      { path: "/faq", element: <Faq /> },
      { path: "*", element: <NotFound /> },
    ],
  },
]);

const AppWrapper = () => <RouterProvider router={router} />;

export default AppWrapper;
