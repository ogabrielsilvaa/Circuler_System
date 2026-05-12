import { useMutation } from '@tanstack/react-query';
import { AxiosError } from 'axios';
import { useState } from 'react';
import { login } from '../services/auth.service';
import { useAuthStore } from '../stores/auth.store';
import { LoginFormValues, LoginFieldErrors, AuthUser, ApiErrorBody } from '../types/auth.types';

export function useAuth() {
  const setSession = useAuthStore((state) => state.setSession);
  const clearSession = useAuthStore((state) => state.clearSession);
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);
  const isHydrating = useAuthStore((state) => state.isHydrating);

  const [fieldErrors, setFieldErrors] = useState<LoginFieldErrors>({});

  const loginMutation = useMutation({
    mutationFn: (values: LoginFormValues) => login(values),
    onSuccess: async (data) => {
      setFieldErrors({});
      const user: AuthUser = { email: data.email, roles: data.roles };
      await setSession(data.token, user);
    },
    onError: (error: unknown) => {
      if (error instanceof AxiosError && error.response) {
        const status = error.response.status;
        const body = error.response.data as ApiErrorBody;
        const message = body?.message ?? 'Erro inesperado.';

        if (status === 401) {
          setFieldErrors({ general: 'E-mail ou senha incorretos.' });
          return;
        }

        if (status === 400) {
          const errors: LoginFieldErrors = {};
          if (message.toLowerCase().includes('email')) errors.email = 'O e-mail é obrigatório.';
          if (message.toLowerCase().includes('password') || message.toLowerCase().includes('senha')) {
            errors.password = 'A senha é obrigatória.';
          }
          if (!errors.email && !errors.password) {
            errors.general = message;
          }
          setFieldErrors(errors);
          return;
        }

        setFieldErrors({ general: message });
        return;
      }
      setFieldErrors({ general: 'Não foi possível conectar ao servidor.' });
    },
  });

  function submitLogin(values: LoginFormValues): void {
    setFieldErrors({});
    loginMutation.mutate(values);
  }

  return {
    submitLogin,
    isLoading: loginMutation.isPending,
    fieldErrors,
    setFieldErrors,
    clearSession,
    isAuthenticated,
    isHydrating,
  };
}
