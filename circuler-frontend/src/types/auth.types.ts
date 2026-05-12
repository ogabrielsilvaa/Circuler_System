export type LoginRequest = {
  email: string;
  password: string;
};

export type LoginResponse = {
  token: string;
  email: string;
  roles: string[];
};

export type AuthUser = {
  email: string;
  roles: string[];
};

export type LoginFormValues = {
  email: string;
  password: string;
};

export type LoginFieldErrors = {
  email?: string;
  password?: string;
  general?: string;
};

export type ApiErrorBody = {
  timestamp: string;
  status: number;
  message: string;
};

export type RegisterRequest = {
  name: string;
  email: string;
  cpf: string;
  password: string;
};

export type RegisterFormValues = {
  name: string;
  email: string;
  cpf: string;
  password: string;
};

export type RegisterFieldErrors = {
  name?: string;
  email?: string;
  cpf?: string;
  password?: string;
  general?: string;
};

export type RegisterResponse = {
  id: number;
  name: string;
  email: string;
  cpf: string;
};
