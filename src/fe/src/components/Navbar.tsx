import { useState } from "react";
import {
  NavigationMenu,
  NavigationMenuItem,
  NavigationMenuList,
} from "~/components/ui/navigation-menu";
import { buttonVariants } from "./ui/button";
import { useAppDispatch, useAppSelector } from "~/store/hooks";
import { toggleTheme, checkBackendConnection } from "~/store/slices/appSlice";
import { Link } from "react-router-dom";

interface RouteProps {
  href: string;
  label: string;
}

const routeList: RouteProps[] = [
  {
    href: "/",
    label: "Home",
  },
  {
    href: "/documents",
    label: "Documents",
  },
  {
    href: "/pubmed",
    label: "PubMed",
  },
  {
    href: "/login",
    label: "Login",
  },
];

export default function Navbar() {
  const [isOpen, setIsOpen] = useState<boolean>(false);
  const dispatch = useAppDispatch();
  const { theme, backendStatus } = useAppSelector((state) => state.app);
  return (
    <header className="sticky border-b-[1px] top-0 z-40 w-full bg-white dark:border-b-slate-700 dark:bg-background">
      <NavigationMenu className="mx-auto">
        <NavigationMenuList className="container h-14 px-4 w-screen flex justify-between ">
          <div className="nav-brand">
            <Link to="/">Pubmed Agent</Link>
            <span className={`status-indicator ${backendStatus}`}>
              {backendStatus}
            </span>
          </div>
          <nav className="hidden md:flex gap-2">
            {routeList.map((route: RouteProps, i) => (
              <Link
                rel="noreferrer noopener"
                to={route.href}
                key={i}
                className={`text-[17px] ${buttonVariants({
                  variant: "ghost",
                })}`}
              >
                {route.label}
              </Link>
            ))}
            <li>
              <button
                onClick={() => dispatch(toggleTheme())}
                className="theme-toggle"
              >
                {theme === "light" ? "🌙" : "☀️"}
              </button>
            </li>
          </nav>
        </NavigationMenuList>
      </NavigationMenu>
    </header>
  );
}
