import { api } from './api';
import { LoginRequest, LoginResponse, RegisterRequest, RegisterResponse } from '../types/auth.types';

export async function login(credentials: LoginRequest): Promise<LoginResponse> {
  const response = await api.post<LoginResponse>('/api/auth/login', credentials);
  return response.data;
}

export async function register(data: RegisterRequest): Promise<RegisterResponse> {
  const response = await api.post<RegisterResponse>('/api/users', data);
  return response.data;
}
