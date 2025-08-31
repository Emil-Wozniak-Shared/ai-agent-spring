import React, { ReactNode } from "react";
import { ErrorBoundary } from "react-error-boundary";
import {
  useNavigate,
  useLocation,
  useRouteError,
  isRouteErrorResponse,
} from "react-router-dom";

interface ErrorFallbackProps {
  error: Error;
  resetErrorBoundary: () => void;
}

// Error fallback component for JavaScript errors
function ErrorFallback({ error, resetErrorBoundary }: ErrorFallbackProps) {
  const navigate = useNavigate();

  const handleGoHome = () => {
    navigate("/");
    resetErrorBoundary();
  };

  const handleGoBack = () => {
    window.history.back();
    resetErrorBoundary();
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <div className="max-w-md w-full bg-white rounded-lg shadow-lg p-6 text-center">
        <div className="mb-4">
          <div className="mx-auto w-16 h-16 bg-red-100 rounded-full flex items-center justify-center mb-4">
            <svg
              className="w-8 h-8 text-red-600"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L4.082 16.5c-.77.833.192 2.5 1.732 2.5z"
              />
            </svg>
          </div>
          <h1 className="text-xl font-semibold text-gray-900 mb-2">
            Oops! Something went wrong
          </h1>
          <p className="text-gray-600 mb-6">
            We encountered an unexpected error. Don't worry, it's not your
            fault.
          </p>
        </div>

        <div className="space-y-3">
          <button
            onClick={resetErrorBoundary}
            className="w-full bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 transition-colors"
          >
            Try Again
          </button>
          <button
            onClick={handleGoBack}
            className="w-full bg-gray-200 text-gray-800 py-2 px-4 rounded-md hover:bg-gray-300 transition-colors"
          >
            Go Back
          </button>
          <button
            onClick={handleGoHome}
            className="w-full bg-gray-200 text-gray-800 py-2 px-4 rounded-md hover:bg-gray-300 transition-colors"
          >
            Go Home
          </button>
        </div>

        {process.env.NODE_ENV === "development" && (
          <details className="mt-6 text-left">
            <summary className="cursor-pointer text-sm text-gray-500 hover:text-gray-700">
              Error Details (Development)
            </summary>
            <pre className="mt-2 text-xs bg-gray-100 p-2 rounded overflow-auto max-h-32">
              {error.message}
              {error.stack}
            </pre>
          </details>
        )}
      </div>
    </div>
  );
}

// Route error element for router-level errors (404s, loader errors, etc.)
export function RouteErrorElement() {
  const error = useRouteError();
  const navigate = useNavigate();

  let errorMessage = "An unexpected error occurred";
  let statusCode: number | undefined;

  if (isRouteErrorResponse(error)) {
    statusCode = error.status;
    errorMessage =
      error.statusText || error.data?.message || "Route error occurred";
  } else if (error instanceof Error) {
    errorMessage = error.message;
  }

  const handleGoHome = () => {
    navigate("/");
  };

  const handleGoBack = () => {
    window.history.back();
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <div className="max-w-md w-full bg-white rounded-lg shadow-lg p-6 text-center">
        <div className="mb-4">
          <div className="mx-auto w-16 h-16 bg-orange-100 rounded-full flex items-center justify-center mb-4">
            <svg
              className="w-8 h-8 text-orange-600"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
              />
            </svg>
          </div>
          <h1 className="text-xl font-semibold text-gray-900 mb-2">
            {statusCode ? `Error ${statusCode}` : "Route Error"}
          </h1>
          <p className="text-gray-600 mb-6">{errorMessage}</p>
        </div>

        <div className="space-y-3">
          <button
            onClick={handleGoBack}
            className="w-full bg-gray-200 text-gray-800 py-2 px-4 rounded-md hover:bg-gray-300 transition-colors"
          >
            Go Back
          </button>
          <button
            onClick={handleGoHome}
            className="w-full bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 transition-colors"
          >
            Go Home
          </button>
        </div>

        {process.env.NODE_ENV === "development" && error instanceof Error && (
          <details className="mt-6 text-left">
            <summary className="cursor-pointer text-sm text-gray-500 hover:text-gray-700">
              Error Details (Development)
            </summary>
            <pre className="mt-2 text-xs bg-gray-100 p-2 rounded overflow-auto max-h-32">
              {error.stack}
            </pre>
          </details>
        )}
      </div>
    </div>
  );
}

interface RouterErrorBoundaryProps {
  children: ReactNode;
  fallback?: React.ComponentType<ErrorFallbackProps>;
  onError?: (error: Error, errorInfo: { componentStack: string }) => void;
}

// Main ErrorBoundary wrapper component
export function RouterErrorBoundary({
  children,
  fallback: FallbackComponent = ErrorFallback,
  onError,
}: RouterErrorBoundaryProps) {
  const location = useLocation();

  const handleError = (error: Error, errorInfo: { componentStack: string }) => {
    console.error("Application Error:", error);
    console.error("Error Info:", errorInfo);

    // Optional error reporting
    if (onError) {
      onError(error, errorInfo);
    }
  };

  return (
    <ErrorBoundary
      FallbackComponent={FallbackComponent}
      onError={handleError}
      resetKeys={[location.pathname]} // Reset error boundary when route changes
    >
      {children}
    </ErrorBoundary>
  );
}

// Hook for manual error throwing (useful for testing)
export function useErrorHandler() {
  return (error: Error) => {
    throw error;
  };
}
