import { api } from './api';
import { LoginRequest, LoginResponse } from '../types/auth.types';

export async function login(credentials: LoginRequest): Promise<LoginResponse> {
  const response = await api.post<LoginResponse>('/api/auth/login', credentials);
  return response.data;
}
