import axios, { AxiosError } from 'axios';
import { ApiErrorBody } from '../types/auth.types';

const BASE_URL = process.env.EXPO_PUBLIC_API_URL ?? 'http://10.0.2.2:8080';

export const api = axios.create({
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
});

export let authToken: string | null = null;

export function setAuthToken(token: string | null): void {
  authToken = token;
}

let onUnauthorized: (() => void) | null = null;

export function setOnUnauthorized(fn: (() => void) | null): void {
  onUnauthorized = fn;
}

api.interceptors.request.use((config) => {
  if (authToken) {
    config.headers.Authorization = `Bearer ${authToken}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error: AxiosError) => {
    if (error.response?.status === 401 && onUnauthorized) {
      onUnauthorized();
    }
    return Promise.reject(error);
  }
);

export function extractApiErrorMessage(error: unknown): string {
  if (error instanceof AxiosError && error.response?.data) {
    const data = error.response.data as ApiErrorBody;
    if (typeof data.message === 'string') {
      return data.message;
    }
  }
  return 'Erro inesperado. Tente novamente.';
}
