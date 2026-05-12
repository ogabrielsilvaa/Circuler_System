import { View, Text, ScrollView, KeyboardAvoidingView, Platform } from 'react-native';
import { useRouter, type Href } from 'expo-router';
import { useState } from 'react';
import { useAuth } from '../../../hooks/useAuth';
import { LoginFormValues } from '../../../types/auth.types';
import { Button } from '../../../components/Button';
import { Input } from '../../../components/Input';
import { LoginHeader } from '../-components/LoginHeader';

export default function Login() {
  const router = useRouter();
  const { submitLogin, isLoading, fieldErrors } = useAuth();

  const [form, setForm] = useState<LoginFormValues>({ email: '', password: '' });

  function handleChange(field: keyof LoginFormValues, value: string): void {
    setForm((prev) => ({ ...prev, [field]: value }));
  }

  function handleLogin(): void {
    submitLogin(form);
  }

  function handleRegister(): void {
    router.push('/(auth)/register' as unknown as Href);
  }

  return (
    <KeyboardAvoidingView
      className="flex-1 bg-emerald-800"
      behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
    >
      <ScrollView
        contentContainerClassName="flex-grow items-center justify-center px-6 py-12"
        keyboardShouldPersistTaps="handled"
      >
        <LoginHeader />

        <View className="w-full max-w-sm">
          <Input
            label="E-mail"
            value={form.email}
            onChangeText={(v) => handleChange('email', v)}
            error={fieldErrors.email}
            inputClassName="bg-emerald-700 text-white"
            placeholderTextColor="#6ee7b7"
            keyboardType="email-address"
            autoCapitalize="none"
            autoCorrect={false}
            editable={!isLoading}
            placeholder="seu@email.com"
          />

          <Input
            label="Senha"
            value={form.password}
            onChangeText={(v) => handleChange('password', v)}
            error={fieldErrors.password}
            inputClassName="bg-emerald-700 text-white"
            placeholderTextColor="#6ee7b7"
            secureTextEntry
            editable={!isLoading}
            placeholder="••••••••"
          />

          {fieldErrors.general ? (
            <Text className="text-red-400 text-sm text-center mb-4">
              {fieldErrors.general}
            </Text>
          ) : null}

          <Button
            label={isLoading ? 'Entrando...' : 'Entrar'}
            variant="primary"
            className="mb-3"
            onPress={handleLogin}
            disabled={isLoading}
          />

          <Button
            label="Cadastrar"
            variant="secondary"
            onPress={handleRegister}
            disabled={isLoading}
          />
        </View>
      </ScrollView>
    </KeyboardAvoidingView>
  );
}
