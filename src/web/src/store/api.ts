
import Cookies from 'universal-cookie';


const API_BASE_URL = process.env.NODE_ENV === "production"
  ? "" // In production, served from same origin
  : "http://localhost:5173"; // In development, proxy handles forwarding to :8080

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

  async request<T>(
    endpoint: string,
    options: RequestInit = {},
  ): Promise<Response> {
    const cookies = new Cookies();
    const token = cookies.get('X-TOKEN');
    const url = `${this.baseUrl}${endpoint}`;
    const config: RequestInit = {
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        ...options.headers,
      },
      ...options,
    };
    if (token) {
      config.headers = {
        'Authorization': `Bearer ${token}`,
        ...config.headers,
      }
    }

    try {
      return await fetch(url, config) as Response;
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
  update: (id: number, userData: any) => apiClient.put(`/api/users/${id}`, userData),
};

export const documentApi = {
  createMany: (documents: any[]) => apiClient.post("/api/documents", documents),
  search: (searchQuery: any) => apiClient.post("/api/documents/search", searchQuery),
};

export const pubmedApi = {
  searchArticles: (searchParams: any) => apiClient.post("/api/pubmed/search/articles", searchParams),
  searchByIds: (ids: string[]) => apiClient.post(`/api/pubmed/search/articles/${ids.join(",")}`, {}),
  getAbstract: (articleId: string) => apiClient.post(`/api/pubmed/articles/${articleId}/abstract`, {}),
};

export const tokenApi = {
  create: (credentials: any) => apiClient.post("/api/token", credentials),
};

export const healthApi = {
  check: () => apiClient.get("/api/health"),
};
