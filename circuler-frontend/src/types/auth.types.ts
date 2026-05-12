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
