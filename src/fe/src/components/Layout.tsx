import type { FC, ReactNode } from "react";
import Navbar from "./Navbar";
import { Outlet } from "react-router-dom";

type Props = {
  children: React.ReactNode;
};

export const Layout: FC = () => (
  <main className="flex items-center justify-center pt-16 pb-4">
    <div className="flex-1 flex flex-col items-center gap-16 min-h-0">
      <Navbar />
      <div className="m-auto w-full space-y-6 px-4">
        <Outlet />
      </div>
    </div>
  </main>
);
