import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import AppWrapper from "./App";
import "./app.css";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <AppWrapper />
  </StrictMode>,
);
