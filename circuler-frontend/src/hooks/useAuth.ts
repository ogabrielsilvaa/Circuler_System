import { useMutation } from '@tanstack/react-query';
import { AxiosError } from 'axios';
import { useState } from 'react';
import { login, register } from '../services/auth.service';
import { useAuthStore } from '../stores/auth.store';
import { LoginFormValues, LoginFieldErrors, AuthUser, ApiErrorBody, RegisterFormValues, RegisterFieldErrors, RegisterRequest } from '../types/auth.types';
import { hasTwoWords, isValidEmail, isValidPassword, isValidCpf, stripCpf } from '../utils/validators';

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
      const user: AuthUser = { id: data.id, email: data.email, roles: data.roles };
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

export function useRegister() {
  const [fieldErrors, setFieldErrors] = useState<RegisterFieldErrors>({});

  const mutation = useMutation({
    mutationFn: (values: RegisterRequest) => register(values),
    onSuccess: () => setFieldErrors({}),
    onError: (error: unknown) => {
      if (error instanceof AxiosError && error.response) {
        const body = error.response.data as ApiErrorBody;
        const message = body?.message ?? 'Erro inesperado.';
        if (error.response.status === 422) {
          if (message.toLowerCase().includes('cpf')) {
            setFieldErrors({ cpf: 'Este CPF já está cadastrado.' });
            return;
          }
          if (message.toLowerCase().includes('e-mail') || message.toLowerCase().includes('email')) {
            setFieldErrors({ email: 'Este e-mail já está cadastrado.' });
            return;
          }
          setFieldErrors({ general: message });
          return;
        }
        setFieldErrors({ general: message });
        return;
      }
      setFieldErrors({ general: 'Não foi possível conectar ao servidor.' });
    },
  });

  function submitRegister(values: RegisterFormValues): void {
    const errors: RegisterFieldErrors = {};
    if (!hasTwoWords(values.name)) errors.name = 'Informe nome e sobrenome.';
    if (!isValidEmail(values.email)) errors.email = 'Informe um e-mail válido.';
    if (!isValidCpf(values.cpf)) errors.cpf = 'Informe um CPF válido (11 dígitos).';
    if (!isValidPassword(values.password)) {
      errors.password = 'Mínimo 8 caracteres, letra maiúscula, minúscula, número e símbolo.';
    }
    if (Object.keys(errors).length > 0) {
      setFieldErrors(errors);
      return;
    }
    setFieldErrors({});
    mutation.mutate({ ...values, cpf: stripCpf(values.cpf) });
  }

  return {
    submitRegister,
    isLoading: mutation.isPending,
    isSuccess: mutation.isSuccess,
    fieldErrors,
  };
}
