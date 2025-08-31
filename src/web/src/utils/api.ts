// src/fe/src/utils/api.ts
const API_BASE_URL =
  process.env.NODE_ENV === "production"
    ? "" // In production, served from same origin
    : ""; // In development, proxy handles forwarding to :8080

export interface ApiResponse<T> {
  data: T;
  message?: string;
  status: number;
}

export class ApiClient {
  private baseUrl: string;

  constructor(baseUrl: string = API_BASE_URL) {
    this.baseUrl = baseUrl;
  }

  private async request<T>(
    endpoint: string,
    options: RequestInit = {},
  ): Promise<T> {
    const url = `${this.baseUrl}${endpoint}`;

    const config: RequestInit = {
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        ...options.headers,
      },
      ...options,
    };

    try {
      const response = await fetch(url, config);

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }

      const contentType = response.headers.get("content-type");
      if (contentType && contentType.includes("application/json")) {
        return await response.json();
      } else {
        return (await response.text()) as unknown as T;
      }
    } catch (error) {
      console.error(`API request failed: ${endpoint}`, error);
      throw error;
    }
  }

  // GET request
  async get<T>(endpoint: string): Promise<T> {
    return this.request<T>(endpoint, { method: "GET" });
  }

  // POST request
  async post<T>(endpoint: string, data?: any): Promise<T> {
    return this.request<T>(endpoint, {
      method: "POST",
      body: data ? JSON.stringify(data) : undefined,
    });
  }

  // PUT request
  async put<T>(endpoint: string, data?: any): Promise<T> {
    return this.request<T>(endpoint, {
      method: "PUT",
      body: data ? JSON.stringify(data) : undefined,
    });
  }

  // DELETE request
  async delete<T>(endpoint: string): Promise<T> {
    return this.request<T>(endpoint, { method: "DELETE" });
  }
}

// Create singleton instance
export const apiClient = new ApiClient();

// Specific API methods for your endpoints
export const userApi = {
  getAll: () => apiClient.get("/api/users"),
  getById: (id: number) => apiClient.get(`/api/users/${id}`),
  create: (userData: any) => apiClient.post("/api/users", userData),
  update: (id: number, userData: any) =>
    apiClient.put(`/api/users/${id}`, userData),
};

export const documentApi = {
  createMany: (documents: any[]) => apiClient.post("/api/documents", documents),
  search: (searchQuery: any) =>
    apiClient.post("/api/documents/search", searchQuery),
};

export const pubmedApi = {
  searchArticles: (searchParams: any) =>
    apiClient.post("/api/pubmed/search/articles", searchParams),
  searchByIds: (ids: string[]) =>
    apiClient.post(`/api/pubmed/search/articles/${ids.join(",")}`, {}),
  getAbstract: (articleId: string) =>
    apiClient.post(`/api/pubmed/articles/${articleId}/abstract`, {}),
};

export const tokenApi = {
  create: (credentials: any) => apiClient.post("/api/token", credentials),
};

export const healthApi = {
  check: () => apiClient.get("/api/health"),
};
